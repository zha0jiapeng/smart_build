package com.ruoyi.iot.controller;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.iot.utils.HdyHttpUtils;
import com.ruoyi.iot.utils.TuhuguancheUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/carLocation")
public class CarLocationController {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    HdyHttpUtils hdyHttpUtils;

    @RequestMapping("/location")
    public Map carLocation() {
        String carLocationStr = (String) redisTemplate.opsForValue().get("carLocation");
        if (carLocationStr == null) {
            return TuhuguancheUtil.getDeviceLocation();
        } else {
            return JSON.parseObject(carLocationStr, Map.class);
        }
    }

    @GetMapping("/pushHdy")
    private void pushHdy() {
        Map deviceLocation = TuhuguancheUtil.getDeviceLocation();
        redisTemplate.opsForValue().set("carLocation", JSON.toJSONString(deviceLocation));
        List<Map<String, Object>> values = new ArrayList<>();
        JSONArray objects = (JSONArray) deviceLocation.get("result");
        for (Object iobj : objects) {
            JSONObject itemMap = (JSONObject) iobj;
            Map<String, Object> valueMap = new HashMap<>();
            //门户ID
            String portalId = "1751847977770553345";
            valueMap.put("portal_id", portalId);
            //标段ID
            String subProjectId = "1801194524869922817";
            valueMap.put("sub_project_id", subProjectId);
            //设备标识
            valueMap.put("device_code", itemMap.get("imei"));
            //设备Id
            valueMap.put("device_id", itemMap.get("imei"));
            //设备在线状态
            valueMap.put("work_status", itemMap.get("status"));
            //开机状态
            valueMap.put("power_on_status", "1");
            //标签X坐标
            valueMap.put("position_x", itemMap.get("lng"));
            //标签Y坐标
            valueMap.put("position_y", itemMap.get("lat"));
            //标签Z坐标
            valueMap.put("position_z", 0);
            //数据产生时间
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedNow = now.format(formatter);
            valueMap.put("data_time", formattedNow);
            //推送时间
            valueMap.put("push_time", formattedNow);
            //其他
            valueMap.put("other", "");
            values.add(valueMap);
        }

        Map<String, List<Map<String, Object>>> param = new HashMap<>();
        param.put("values", values);

        hdyHttpUtils.pushIOT(param, "5df811d1-d051-4bf5-bd7b-88967194b30a");
    }
}
