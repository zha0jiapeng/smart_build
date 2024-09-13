package com.ruoyi.iot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.redis.RedisCache;

import com.ruoyi.iot.domain.Device;
import com.ruoyi.iot.enums.IndexType;
import com.ruoyi.iot.service.IDeviceService;
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
    @Autowired
    IDeviceService deviceService;
    @RequestMapping("/list")
    public Map list(){
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
        List<Device> list = deviceService.list(new LambdaQueryWrapper<Device>().eq(Device::getDeviceType,"GASDETECTOR").eq(Device::getYn,1));
        for (Device device : list) {
            ModbusMaster master = new ModbusTcpMaster().getSlave(device.getDeviceIp(), device.getDevicePort());
            Number temp = Modbus4jReadUtil.readHoldingRegister(master, 1, 0, DataType.TWO_BYTE_INT_UNSIGNED, "温度");
            Number humi = Modbus4jReadUtil.readHoldingRegister(master, 1, 1, DataType.TWO_BYTE_INT_UNSIGNED, "湿度");
            Number dust = Modbus4jReadUtil.readHoldingRegister(master, 1, 5, DataType.TWO_BYTE_INT_UNSIGNED, "粉尘");
            Number o2 = Modbus4jReadUtil.readHoldingRegister(master, 1, 10, DataType.TWO_BYTE_INT_UNSIGNED, "氧气");
            Number ch4 = Modbus4jReadUtil.readHoldingRegister(master, 1, 11, DataType.TWO_BYTE_INT_UNSIGNED, "甲烷");
            Number co = Modbus4jReadUtil.readHoldingRegister(master, 1, 12, DataType.TWO_BYTE_INT_UNSIGNED, "一氧化碳");
            Number h2s = Modbus4jReadUtil.readHoldingRegister(master, 1, 13, DataType.TWO_BYTE_INT_UNSIGNED, "硫化氢");
            Number co2 = Modbus4jReadUtil.readHoldingRegister(master, 1, 14, DataType.TWO_BYTE_INT_UNSIGNED, "二氧化碳");
            Number so2 = Modbus4jReadUtil.readHoldingRegister(master, 1, 15, DataType.TWO_BYTE_INT_UNSIGNED, "二氧化硫");
            Number nh3 = Modbus4jReadUtil.readHoldingRegister(master, 1, 16, DataType.TWO_BYTE_INT_UNSIGNED, "氨气");
            Number no2 = Modbus4jReadUtil.readHoldingRegister(master, 1, 21, DataType.TWO_BYTE_INT_UNSIGNED, "二氧化氮");

            master.destroy();
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("device_code", "5414A7750BBF");
            dataMap.put("status", "在线");
            dataMap.put("so2", new BigDecimal(so2.toString()).multiply(new BigDecimal(0.0001)).setScale(4, RoundingMode.HALF_UP));
            dataMap.put("no2", no2);
            dataMap.put("co", new BigDecimal(co.toString()).multiply(new BigDecimal(0.0001)).setScale(4, RoundingMode.HALF_UP));
            dataMap.put("co2", new BigDecimal(co2.toString()).multiply(new BigDecimal(0.0001)).setScale(4, RoundingMode.HALF_UP));
            dataMap.put("o2", o2);
            dataMap.put("ch4",  new BigDecimal(ch4.toString()).multiply(new BigDecimal(0.0001)).setScale(4, RoundingMode.HALF_UP));
            dataMap.put("h2s", h2s);
            //dataMap.put("tvoc", "1");
            dataMap.put("nh3", new BigDecimal(nh3.toString()).multiply(new BigDecimal(0.0001)).setScale(4, RoundingMode.HALF_UP));
            dataMap.put("dust", dust);
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

}
