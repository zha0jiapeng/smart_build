package com.ruoyi.iot.listen;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson2.JSON;

import com.alibaba.fastjson2.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.ruoyi.iot.bean.ExcelBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
@Slf4j
public class DemoDataListener implements ReadListener<ExcelBean> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<ExcelBean> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    // private DemoDAO demoDAO;

//    public DemoDataListener() {
//        // 这里是demo，所以随便new一个。实际使用如果到了spring,请使用下面的有参构造函数
//        demoDAO = new DemoDAO();
//    }

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param demoDAO
     */
//    public DemoDataListener(DemoDAO demoDAO) {
//        this.demoDAO = demoDAO;
//    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(ExcelBean data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        //tuisong

        Map<String,List<ExcelBean>> map = new HashMap<>();
        ArtemisConfig config = new ArtemisConfig();
        config.setHost("10.1.3.2"); // 代理API网关nginx服务器ip端口
        config.setAppKey("29632148");  // 秘钥appkey
        config.setAppSecret("7k0RVVHqdynytJBhPfz8");// 秘钥appSecret
        Map<String, String> paramMap = new HashMap<String, String>();// post请求Form表单参数
        paramMap.put("pageNo", "1");
        paramMap.put("pageSize", "100");
        final String getApi = "/artemis/api/resource/v2/person/personList";
        String body = JSON.toJSON(paramMap).toString();
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getApi);
            }
        };
        String httpResponse = ArtemisHttpUtil.doPostStringArtemis( path, body, null, null, "application/json");
        JSONObject jsonObject = JSONObject.parseObject(httpResponse);
        Map<String, Object> data = (Map) jsonObject.get("data");
        List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("list");
        for (Map<String, Object> item : list) {
            for (ExcelBean excelBean : cachedDataList) {
                System.out.println(excelBean.getId_card());
                if(excelBean.getId_card().equals(item.get("certificateNo").toString())) {
                    List<Map<String, String>> photos = (List<Map<String, String>>) item.get("personPhoto");
                    for (Map<String, String> photo : photos) {
                        String picUri = photo.get("picUri");
                        excelBean.setFace_image("http://10.1.3.2" + picUri);
                    }
                    break;
                }
            }
        }
        for (ExcelBean excelBean : cachedDataList) {
            excelBean.setTm_leader("");
            if(StringUtils.isNotBlank(excelBean.getEntry_time()))
                excelBean.setEntry_time(excelBean.getEntry_time().replaceAll("\\.","\\-")+" 00:00:00");
            else
                excelBean.setEntry_time("");
            if(StringUtils.isNotBlank(excelBean.getExit_time()))
                excelBean.setExit_time(excelBean.getExit_time().replaceAll("\\.","\\-")+" 00:00:00");
            else
                excelBean.setExit_time("");
            if(StringUtils.isNotBlank(excelBean.getParty_member_flag()))
                excelBean.setParty_member_flag(excelBean.getParty_member_flag().equals("是")?"1":"0");

            excelBean.setPush_time(DateUtil.now());
            if(StringUtils.isNotBlank(excelBean.getId_card_period()))
                excelBean.setId_card_period(excelBean.getId_card_period().replaceAll("\\.","\\-"));
        }
        map.put("values",cachedDataList);
        System.out.println("............."+JSON.toJSONString(map));


        String url = "http://10.0.100.23:18080/sdata/rest/dataservice/rest/standard/74d072c8-7fbf-40fd-b6ea-236226c90d01";

        HttpResponse execute = HttpRequest.put(url).body(JSON.toJSONString(map),"application/json").execute();
        String body1 = execute.body();
        System.out.println(JSON.toJSONString(body1));
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.now());
    }

}
