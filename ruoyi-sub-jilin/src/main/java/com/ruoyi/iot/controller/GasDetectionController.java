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
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Scheduled(cron = "0 */1 * * * *")
    public void pushSwzk() {
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
        dataMap.put("device_code", "5414A7750BBF");
        dataMap.put("status", "在线");
        Object so2 = ((List<Map<String, Object>>) map.get("so2")).get(0).get("value");
        Object co2 = ((List<Map<String, Object>>) map.get("co2")).get(0).get("value");
        Object co = ((List<Map<String, Object>>) map.get("co")).get(0).get("value");
        Object h2s = ((List<Map<String, Object>>) map.get("h2s")).get(0).get("value");
        Object nh3 = ((List<Map<String, Object>>) map.get("nh3")).get(0).get("value");

        dataMap.put("so2", new BigDecimal(so2.toString()).multiply(new BigDecimal(0.0001)).setScale(4, RoundingMode.HALF_UP));
        dataMap.put("no2", 0);
        dataMap.put("co", new BigDecimal(co.toString()).multiply(new BigDecimal(0.0001)).setScale(4, RoundingMode.HALF_UP));
        dataMap.put("co2", new BigDecimal(co2.toString()).multiply(new BigDecimal(0.0001)).setScale(4, RoundingMode.HALF_UP));
        dataMap.put("o2", ((List<Map<String, Object>>)map.get("oxygen")).get(0).get("value"));
        dataMap.put("ch4",  new BigDecimal(h2s.toString()).multiply(new BigDecimal(0.0001)).setScale(4, RoundingMode.HALF_UP));
        dataMap.put("h2s", ((List<Map<String, Object>>)map.get("phosphine")).get(0).get("value"));
        //dataMap.put("tvoc", "1");
        dataMap.put("nh3", new BigDecimal(nh3.toString()).multiply(new BigDecimal(0.0001)).setScale(4, RoundingMode.HALF_UP));
        dataMap.put("dust", ((List<Map<String, Object>>)map.get("dust")).get(0).get("value"));
        dataMap.put("date_time", now);
        dataMap.put("push_time", now);
        dataMap.put("other", "");
        dataMap.put("portal_id", "1751847977770553345");
        // 创建values的List并添加valueMap
        List<Map<String, Object>> valuesList = new ArrayList<>();
        valuesList.add(dataMap);

        // 创建根Map
        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("values", valuesList);
        hdyHttpUtils.pushIOT(rootMap,"832944e052d04cccbb3e215e8a3e037f");
    }

}
