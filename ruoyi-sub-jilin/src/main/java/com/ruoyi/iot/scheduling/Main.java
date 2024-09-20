package com.ruoyi.iot.scheduling;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.iot.bean.DataPublisher;
import com.ruoyi.iot.bean.MQTTConnector;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        // 初始化 MQTT 连接
        MQTTConnector connector = new MQTTConnector();
        connector.connect();

        // 创建数据发布者
        DataPublisher publisher = new DataPublisher(connector);

        // 模拟数据上报
        JSONObject data = new JSONObject();
        data.put("tp", System.currentTimeMillis());

        JSONArray points = new JSONArray();
        points.add(createPoint(0, "12210271220040"));
        points.add(createPoint(1, 0));
        points.add(createPoint(2, 0));
        points.add(createPoint(3, 0));

        JSONObject dataWrapper = new JSONObject();
        dataWrapper.put("point", points);

        JSONArray dataArray = new JSONArray();
        dataArray.add(dataWrapper);

        JSONObject payloadObj = new JSONObject();
        payloadObj.put("data", dataArray);

        String payload = payloadObj.toJSONString();

        // 发布数据
        publisher.publishData("acrel", "meter", "0000", payload);

        // 断开连接
        connector.disconnect();
    }

    private static JSONObject createPoint(int id, Object value) {
        JSONObject point = new JSONObject();
        point.put("id", id);
        point.put("val", value);
        return point;
    }
}