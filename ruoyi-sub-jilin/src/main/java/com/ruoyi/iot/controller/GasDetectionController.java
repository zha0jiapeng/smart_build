package com.ruoyi.iot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;

import com.ruoyi.iot.enums.IndexType;
import com.ruoyi.iot.utils.HdyHttpUtils;
import com.ruoyi.iot.utils.Modbus4jReadUtil;
import com.ruoyi.iot.utils.ModbusTcpMaster;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/gasDetection")
public class GasDetectionController {

    @Resource
    HdyHttpUtils hdyHttpUtils;
    @Autowired
    RedisCache redisCache;
    @RequestMapping("/listByThingsBoard")
    public Map getGasGasDetection(){
        Object thingsboardToken = redisCache.getCacheObject("thingsboard_token");
        if(thingsboardToken==null) {
            String url = "10.1.3.201:8080/api/auth/login";
            Map<String, Object> map = new HashMap();
            map.put("username", "1939291579@qq.com");
            map.put("password", "zhao521a.");
            HttpResponse execute = HttpRequest.post(url).body(JSON.toJSONString(map), "application/json").execute();
            JSONObject jsonObject = JSON.parseObject(execute.body());
            Object token = jsonObject.get("token");
            redisCache.setCacheObject("thingsboard_token",token,2, TimeUnit.HOURS);
        }
        String url = "http://10.1.3.201:8080/api/plugins/telemetry/DEVICE/8e018740-4b26-11ef-8d02-a5729e1018f3/values/timeseries";
        HttpResponse execute = HttpRequest.get(url).bearerAuth(thingsboardToken.toString()).execute();
        String body = execute.body();
        return JSON.parseObject(body,Map.class);
    }

    @Scheduled(cron = "0 */10 * * * *")
    private void pushSwzk() {
        String now = DateUtil.now();
        Object thingsboardToken = redisCache.getCacheObject("thingsboard_token");
        if(thingsboardToken==null) {
            String url = "10.1.3.201:8080/api/auth/login";
            Map<String, Object> map = new HashMap();
            map.put("username", "1939291579@qq.com");
            map.put("password", "zhao521a.");
            HttpResponse execute = HttpRequest.post(url).body(JSON.toJSONString(map), "application/json").execute();
            JSONObject jsonObject = JSON.parseObject(execute.body());
            Object token = jsonObject.get("token");
            redisCache.setCacheObject("thingsboard_token",token,2, TimeUnit.HOURS);
        }
        String url = "http://10.1.3.201:8080/api/plugins/telemetry/DEVICE/8e018740-4b26-11ef-8d02-a5729e1018f3/values/timeseries";
        HttpResponse execute = HttpRequest.get(url).bearerAuth(thingsboardToken.toString()).execute();
        String body = execute.body();
        Map map = JSON.parseObject(body, Map.class);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("deviceCode", "5414A7750BBF");
        dataMap.put("workStatus", "在线");
        dataMap.put("status", "在线");
        dataMap.put("so2", ((List<Map<String, Object>>)map.get("sulfur_dioxide")).get(0).get("value"));
        dataMap.put("no2", ((List<Map<String, Object>>)map.get("nitrogen_dioxide")).get(0).get("value"));
        dataMap.put("co", ((List<Map<String, Object>>)map.get("carbon_monoxide")).get(0).get("value"));
        dataMap.put("co2", ((List<Map<String, Object>>)map.get("carbon_dioxide")).get(0).get("value"));
        dataMap.put("o2", ((List<Map<String, Object>>)map.get("oxygen")).get(0).get("value"));
        dataMap.put("ch4", ((List<Map<String, Object>>)map.get("methane")).get(0).get("value"));
        dataMap.put("h2s", ((List<Map<String, Object>>)map.get("phosphine")).get(0).get("value"));
        //dataMap.put("tvoc", "1");
        dataMap.put("nh3", ((List<Map<String, Object>>)map.get("ammonia")).get(0).get("value"));
        dataMap.put("dust", ((List<Map<String, Object>>)map.get("dust")).get(0).get("value"));
        dataMap.put("dataTime", now);
        dataMap.put("pushTime", now);
        dataMap.put("other", "");

        // 创建value的Map
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("body", dataMap);
        valueMap.put("facturer", "深圳市蓝川科技有限公司/洞内气体检测仪");
        valueMap.put("push_time", now);

        // 创建values的List并添加valueMap
        List<Map<String, Object>> valuesList = new ArrayList<>();
        valuesList.add(valueMap);

        // 创建根Map
        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("values", valuesList);
        hdyHttpUtils.pushIOT(rootMap);
    }

}
