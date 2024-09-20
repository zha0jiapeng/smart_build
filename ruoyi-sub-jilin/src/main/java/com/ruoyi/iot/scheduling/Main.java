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


    public void pushIOT() {
        Map<String, Object> valueMap = new HashMap<>();
        //门户ID  String
        valueMap.put("portal_id", "");
        //标段ID  String
        valueMap.put("sub_project_id", "");
        //设备编号  String
        valueMap.put("device_code", "");
        //设备工作状态  String
        valueMap.put("work_status", "");
        //电箱类型  String
        valueMap.put("electric_box_type", "");
        //父级电箱  String
        valueMap.put("electric_box_parent", "");
        //正向无功总电能  Decimal
        valueMap.put("total_p_r_e", "");
        //反向无功总电能  Decimal
        valueMap.put("total_p_r_r_e", "");
        //正向有功总电量  Decimal
        valueMap.put("total_p_a_e", "");
        //反向有功总电量  Decimal
        valueMap.put("total_p_r_a_e", "");
        //ABC三项电压  Decimal
        valueMap.put("a_b_c_voltage", "");
        //ABC三项电流  Decimal
        valueMap.put("a_b_c_current", "");
        //A相电流  Decimal
        valueMap.put("a_electricity", "");
        //B相电流  Decimal
        valueMap.put("b_blectricity", "");
        //C相电流  Decimal
        valueMap.put("c_electricity", "");
        //A相电压  Decimal
        valueMap.put("a_voltage", "");
        //B相电压  Decimal
        valueMap.put("b_voltage", "");
        //C相电压  Decimal
        valueMap.put("c_voltage", "");
        //用电负载功率  Decimal
        valueMap.put("box_power", "");
        //线缆A温度  Decimal
        valueMap.put("line_a_temperature", "");
        //线缆B温度  Decimal
        valueMap.put("line_b_temperature", "");
        //线缆C温度  Decimal
        valueMap.put("line_c_temperature", "");
        //线缆N温度  Decimal
        valueMap.put("line_n_temperature", "");
        //总瞬时功率  Decimal
        valueMap.put("totalI_a_p", "");
        //跳闸  String
        valueMap.put("switch_off", "");
        //合闸  String
        valueMap.put("switch_on", "");
        //推送时间  Date
        valueMap.put("push_time", "");
        //其他  String
        valueMap.put("other", "");
        //总用电量  String
        valueMap.put("total_wattage", "");
        //预警类型（设备传输：漏电...  String
        valueMap.put("alarmCode", "");
        //数据类型(0、正常 1、报警)  String
        valueMap.put("data_type", "");

        List<Map<String, Object>> values = new ArrayList<>();
        values.add(valueMap);
        Map<String, List<Map<String, Object>>> param = new HashMap<>();
        param.put("values", values);

//        hdyHttpUtils.pushIOT(param, "bbe55ec4-fc7b-4cd1-a704-1f07964b82d6");
    }
}