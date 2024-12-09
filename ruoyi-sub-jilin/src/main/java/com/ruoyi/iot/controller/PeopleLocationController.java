package com.ruoyi.iot.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.iot.utils.HdyHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;


@RestController
@Slf4j
@RequestMapping("/peopleLocation")
public class PeopleLocationController {

    @Resource
    HdyHttpUtils hdyHttpUtils;


    @PostMapping("/outTunnelLocation")
    public JSONObject outPeopleLocation(@RequestBody Map request) {
        //Map<String,Object> parse = new HashMap();
        HttpResponse execute = HttpRequest.post("http://10.1.3.207:9501/push/list")
                .body(JSON.toJSONString(new Object()), "application/json")
                .execute();
        String body = execute.body();
        log.info("body :{}", body);
        JSONObject jsonObject = JSONObject.parseObject(body);
        if (jsonObject != null) {
            JSONArray data = jsonObject.getJSONArray("data");
            for (Object datum : data) {
                JSONObject item = (JSONObject) datum;
                double[] doubles = XYToCoordinates(Double.parseDouble(item.get("result_x").toString()), Double.parseDouble(item.get("result_y").toString()));
                item.put("result_wgs84_x", doubles[0]);
                item.put("result_wgs84_y", doubles[1]);
            }

        }
        return jsonObject;
    }

    List<String> validTids = Arrays.asList("11250", "10867", "10985", "11076", "11585");

    @Scheduled(cron = "0 */1 * * * *")
    private void pushSwzkOut() {
        Map<String, Object> request = new HashMap();
        HttpResponse execute = HttpRequest.post("http://10.1.3.207:9501/push/list")
                .body(JSON.toJSONString(request), "application/json")
                .execute();
        String body = execute.body();
        Map parse = JSONObject.parseObject(body, Map.class);
        List<Map<String, Object>> datee = (List<Map<String, Object>>) parse.get("data");
        String now = DateUtil.now();
        if (datee == null) {
            return;
        }
        for (Map<String, Object> map : datee) {
            // 创建存储设备数据的 Map
            Map<String, Object> deviceData = new HashMap<>();
            String tid = map.get("tid").toString();
            //判定为车辆
            if (validTids.contains(tid)) {
                String deviceLocationJson = JSON.toJSONString(map);
                // 使用 HttpUtil 发送 GET 请求
                System.out.println("开始调用车辆定位：" + deviceLocationJson);
                String result = HttpUtil.get("http://10.1.3.204:8097/carLocation/pushHdy?data=" + deviceLocationJson);
                continue;
            }
            deviceData.put("device_id", map.get("tid"));
            deviceData.put("sub_project_id", "1801194524869922817");
            deviceData.put("device_code", "3009f9b0bb24");
            deviceData.put("work_status", "1");
            deviceData.put("power_on_status", "1");
            double[] doubles = XYToCoordinates(Double.parseDouble(map.get("result_x").toString()), Double.parseDouble(map.get("result_y").toString()));
            deviceData.put("position_x", doubles[0]);
            deviceData.put("position_y", doubles[1]);
            deviceData.put("position_z", map.get("result_z"));
            deviceData.put("sos_status", "0");
            deviceData.put("distance", 0.0);
            deviceData.put("data_time", DateUtil.date(Long.parseLong(map.get("time").toString()) * 1000).toString("yyyy-MM-dd HH:mm:ss"));
            deviceData.put("push_time", now);
            deviceData.put("other", "");

            // 将设备数据放入 List 中
            List<Map<String, Object>> values = new ArrayList<>();
            values.add(deviceData);

            // 创建总的 Map 并将 List 放入其中
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("values", values);
            hdyHttpUtils.pushIOT(resultMap, "199a5516-f4c3-45d0-ac7b-e30d73ffa595");
        }

    }


    public static double[] XYToCoordinates(double resultX, double resultY) {
        double[] center = {14002825, -5164160};  // 中心坐标

        // x 坐标计算
        double x = ((resultX * 1.34 + center[0]) / 20037508.342789) * 180;

        // y 坐标计算
        double y = ((resultY * 1.34 + center[1]) / 20037508.342789) * 180;
        y = (-180 / Math.PI) * (2 * Math.atan(Math.exp((y * Math.PI) / 180)) - Math.PI / 2);

        // 返回转换后的 x, y 坐标
        return new double[]{x, y};
    }


}
