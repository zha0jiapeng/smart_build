package com.ruoyi.iot.scheduling;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MqttSubscriber {

    private static final String MQTT_SERVER = "tcp://your-mqtt-server.com:1883";  // MQTT 服务器地址
    private static final String MQTT_TOPIC = "platform/acrel/meter/json-v2/analog/0000";  // 要订阅的 Topic
    private static final String MQTT_USERNAME = "your-username";  // 用户名
    private static final String MQTT_PASSWORD = "your-password";  // 密码

    public static void main(String[] args) {
        try {
            // 创建 MQTT 客户端
            MqttClient client = new MqttClient(MQTT_SERVER, MqttClient.generateClientId());

            // 设置回调函数
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String payload = new String(message.getPayload());
                    System.out.println("Message arrived:\nTopic: " + topic + "\nMessage: " + payload);

                    // 解析 JSON 数据
                    parseMessage(payload);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used here
                }
            });

            // 设置 MQTT 连接选项
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(MQTT_USERNAME);
            options.setPassword(MQTT_PASSWORD.toCharArray());
            options.setCleanSession(true);
            options.setKeepAliveInterval(60);  // 设置 keep-alive 时间

            // 连接到 MQTT 服务器
            client.connect(options);
            System.out.println("Connected to MQTT server.");

            // 订阅 Topic
            client.subscribe(MQTT_TOPIC, 1);  // 设置 QoS 为 1
            System.out.println("Subscribed to topic: " + MQTT_TOPIC);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private static void parseMessage(String json) {
        try {
            System.out.println("初始数据：" + json);
            JSONObject jsonObject = JSON.parseObject(json);

            if (jsonObject.containsKey("data")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < dataArray.size(); i++) {
                    JSONObject obj = dataArray.getJSONObject(i);
                    String timestamp = obj.getString("tp");

                    JSONArray pointsArray = obj.getJSONArray("point");
                    for (int j = 0; j < pointsArray.size(); j++) {
                        JSONObject pointObj = pointsArray.getJSONObject(j);
                        int id = pointObj.getIntValue("id");
                        String value = pointObj.getString("val");
                        System.out.println("ID: " + id + ", Value: " + value + ", Timestamp: " + timestamp);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to parse message: " + e.getMessage());
        }
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