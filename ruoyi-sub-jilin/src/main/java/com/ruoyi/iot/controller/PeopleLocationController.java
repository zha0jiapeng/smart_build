package com.ruoyi.iot.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.iot.utils.SwzkHttpUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/peopleLocation")
public class PeopleLocationController {

    @Resource
    SwzkHttpUtils swzkHttpUtils;

//    @PostMapping("/inTunnelLocation")
//    public Map<String,Object> peopleLocation(@RequestBody Map request){
//        Map<String,Object> parse = new HashMap();
//        if(!request.containsKey("map_id")){
//            parse.put("code",500);
//            parse.put("msg","map_id必传");
//            return parse;
//        }
//        HttpResponse execute = HttpRequest.post("http://192.168.1.200:9501/push/list")
//                .body(JSON.toJSONString(request), "application/json")
//                .execute();
//        String body = execute.body();
//        return JSONObject.parseObject(body, Map.class);
//    }


    @PostMapping("/outTunnelLocation")
    public Map<String,Object> outPeopleLocation(@RequestBody Map request){
        Map<String,Object> parse = new HashMap();
        if(!request.containsKey("map_id")){
            parse.put("code",500);
            parse.put("msg","map_id必传");
            return parse;
        }
        HttpResponse execute = HttpRequest.post("http://10.1.3.207:9501/push/list")
                .body(JSON.toJSONString(request), "application/json")
                .execute();
        String body = execute.body();
        return JSONObject.parseObject(body, Map.class);
    }




  //  @Scheduled(cron = "0 */5 * * * *")
    private void pushSwzkIn() {
        String now = DateUtil.now();
        HttpResponse execute = HttpRequest.post("http://10.1.3.207:9501/push/list")
                .body(JSON.toJSONString(new Object()), "application/json")
                .execute();
        String body = execute.body();
        Map parse = JSONObject.parseObject(body, Map.class);
        List<Map<String, Object>> datee = (List<Map<String, Object>>) parse.get("data");
        // 创建主数据结构
        Map<String, Object> data = new HashMap<>();
        data.put("deviceType", "2001000040");
        data.put("SN", "renyuandingwei1");
        data.put("dataType", "200300015");
        data.put("workAreaCode", "YJBH-SSZGX_GQ-08");
        data.put("workSurface", "YJBH-SSZGX_JGB2_BD-SG-205_ZYM-BT-ZB-08");
        data.put("tunnCode", "100001");
        data.put("deviceName", "人员定位基站");
        List<Map<String, Object>> valuesList = new ArrayList<>();
        for (Map<String, Object> itemMap : datee) {
            // 创建 values 列表

            Map<String, Object> valuesObj = new HashMap<>();
            valuesObj.put("reportTs", DateUtil.current());

            // 创建 profile 对象
            Map<String, Object> profileObj = new HashMap<>();
            profileObj.put("appType", "life");
            profileObj.put("modelId", "200017");
            profileObj.put("poiCode", "w0907005");
            profileObj.put("deviceType", "2001000040");

            // 创建 properties 对象
            Map<String, Object> propertiesObj = new HashMap<>();
            //propertiesObj.put("ringCode", "");
            //propertiesObj.put("icCode", "");
            //propertiesObj.put("heartRate", "");
            propertiesObj.put("electric", itemMap.get("bat"));
            propertiesObj.put("time", now);
            propertiesObj.put("sos", "0");
            propertiesObj.put("type", "01");
            propertiesObj.put("stationX", 0);
            propertiesObj.put("stationY", 0);
            propertiesObj.put("stationZ", 0);
            propertiesObj.put("humanX", 0);
            propertiesObj.put("humanY", 0);
            propertiesObj.put("humanZ", 0);
            propertiesObj.put("stationDistance", new BigDecimal(2939).add(new BigDecimal(itemMap.get("result_x").toString())));
            propertiesObj.put("holeDistance", 0);
            propertiesObj.put("idCardNumber", ((Map<String, Object>) itemMap.get("user_info")).get("number"));


            propertiesObj.put("name", ((Map<String, Object>) itemMap.get("user_info")).get("user_name"));
            propertiesObj.put("locateMode", "GPS");

            // 将 profile 和 properties 对象放入 values 对象中
            valuesObj.put("profile", profileObj);
            valuesObj.put("properties", propertiesObj);
            // 将 values 对象放入 values 列表中
            valuesList.add(valuesObj);

        }
        data.put("values", valuesList);
        swzkHttpUtils.pushIOT(data);
    }


   // @Scheduled(cron = "0 */5 * * * *")
    private void pushSwzkOut() {
        HttpResponse execute = HttpRequest.post("http://10.1.3.207:9501:9501/push/list")
                .body(JSON.toJSONString(new Object()), "application/json")
                .execute();

        String body = execute.body();
        Map parse = JSONObject.parseObject(body, Map.class);
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("portalId", "1751847977770553345");
        bodyMap.put("subProjectId", "1763492186013306882");
        bodyMap.put("deviceNumber", parse.get("tid").toString());
        bodyMap.put("tabWorkStatus", "在线");
        bodyMap.put("positionX", parse.get("res").toString());
        bodyMap.put("positionY", "somePositionY");
        bodyMap.put("positionZ", "somePositionZ");
        bodyMap.put("power", "somePower");
        bodyMap.put("tabStatus", parse.get("speed").toString());
        bodyMap.put("sosStatus", "");
        bodyMap.put("positionTime",  parse.get("time").toString());
        bodyMap.put("localStation","");
        bodyMap.put("deviceCode", "jizhan1");
        bodyMap.put("InOrOut", "洞外");
        bodyMap.put("pushTime", DateUtil.now());
        bodyMap.put("other", "");

        // 创建values部分
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put("body", bodyMap);
        valuesMap.put("facturer", "人员定位基站");
        valuesMap.put("push_time",DateUtil.now());

        // 创建最外层的Map
        Map<String, Object> mainMap = new HashMap<>();
        mainMap.put("values", new Map[] { valuesMap });


    }


}
