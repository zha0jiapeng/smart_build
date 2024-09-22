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

import java.text.SimpleDateFormat;
import java.util.*;

public class MqttSubscriber {

    private static final String MQTT_SERVER = "tcp://101.200.216.234:6879";  // MQTT 服务器地址
    private static final String MQTT_TOPIC = "24090502540001";  // 要订阅的 Topic
    private static final String MQTT_USERNAME = "username";  // 用户名
    private static final String MQTT_PASSWORD = "password";  // 密码

    public static void main(String[] args) {
        System.out.println(new Date());
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
        Map<Integer, String> idValueMap = new HashMap<>();
        boolean isConfirm = false;
        try {
            System.out.println("初始数据：" + json);
            JSONObject jsonObject = JSON.parseObject(json);

            if (jsonObject.containsKey("data")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < dataArray.size(); i++) {
                    JSONObject obj = dataArray.getJSONObject(i);
                    String timestamp = obj.getString("tp");
                    idValueMap.put(-1, timestamp);
                    JSONArray pointsArray = obj.getJSONArray("point");
                    for (int j = 0; j < pointsArray.size(); j++) {
                        JSONObject pointObj = pointsArray.getJSONObject(j);
                        int id = pointObj.getIntValue("id");
                        String value = pointObj.getString("val");
                        if (id == 36){
                            isConfirm = true;
                        }
                        idValueMap.put(id, value);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to parse message: " + e.getMessage());
        }
        if (isConfirm){
//            pushIOT(idValueMap);
        }
    }

    public static void pushIOT(Map<Integer, String> idValueMap) {
        Map<String, Object> valueMap = new HashMap<>();
        //门户ID  String
        valueMap.put("portal_id", "");
        //标段ID  String
        valueMap.put("sub_project_id", "");
        //设备编号  String 0
        valueMap.put("device_code", idValueMap.get(0));
        //设备工作状态  String
        valueMap.put("work_status", "正常");
        //电箱类型  String
        valueMap.put("electric_box_type", "");
        //父级电箱  String
        valueMap.put("electric_box_parent", "");
        //正向无功总电能  Decimal 30
        valueMap.put("total_p_r_e", idValueMap.get(30));
        //反向无功总电能  Decimal 31
        valueMap.put("total_p_r_r_e", idValueMap.get(31));
        //正向有功总电量  Decimal 28
        valueMap.put("total_p_a_e", idValueMap.get(28));
        //反向有功总电量  Decimal 29
        valueMap.put("total_p_r_a_e", idValueMap.get(29));
        //ABC三项电压  Decimal 1+2+3
        String a_b_c_voltage = calculateAndFormatSum(idValueMap, 1, 2, 3);
        valueMap.put("a_b_c_voltage", a_b_c_voltage);
        //ABC三项电流  Decimal (7+8+9)/3
        String a_b_c_currentString = calculateAndFormatSum(idValueMap, 1, 2, 3);
//        valueMap.put("a_b_c_current", a_b_c_current);
        //A相电流  Decimal 7
        valueMap.put("a_electricity", idValueMap.get(7));
        //B相电流  Decimal 8
        valueMap.put("b_blectricity", idValueMap.get(8));
        //C相电流  Decimal 9
        valueMap.put("c_electricity", idValueMap.get(9));
        //A相电压  Decimal 1
        valueMap.put("a_voltage", idValueMap.get(1));
        //B相电压  Decimal 2
        valueMap.put("b_voltage", idValueMap.get(2));
        //C相电压  Decimal 3
        valueMap.put("c_voltage", idValueMap.get(3));
        //用电负载功率  Decimal
        valueMap.put("box_power", "");
        //线缆A温度  Decimal 36
        valueMap.put("line_a_temperature", idValueMap.get(36));
        //线缆B温度  Decimal 37
        valueMap.put("line_b_temperature", idValueMap.get(37));
        //线缆C温度  Decimal 38
        valueMap.put("line_c_temperature", idValueMap.get(38));
        //线缆N温度  Decimal 39
        valueMap.put("line_n_temperature", idValueMap.get(39));
        //总瞬时功率  Decimal
        valueMap.put("totalI_a_p", "");
        //跳闸  String
        valueMap.put("switch_off", "");
        //合闸  String
        valueMap.put("switch_on", "");
        //推送时间  Date tp（时间戳）
        String timestampStr = idValueMap.get(-1);
        try {
            // 将时间戳字符串解析为长整型
            long timestamp = Long.parseLong(timestampStr);
            // 创建 Date 对象
            Date date = new Date(timestamp);
            // 定义日期时间格式化器
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 格式化 Date 为字符串
            timestampStr = formatter.format(date);
        } catch (NumberFormatException e) {
            System.out.println("Invalid timestamp format: " + e.getMessage());
        }
        valueMap.put("push_time", timestampStr);


        //其他  String
        valueMap.put("other", "");
        //总用电量  String
        valueMap.put("total_wattage", "");
        //预警类型（设备传输：漏电...  String
        valueMap.put("alarmCode", "");
        //数据类型(0、正常 1、报警)  String
        valueMap.put("data_type", "0");

        List<Map<String, Object>> values = new ArrayList<>();
        values.add(valueMap);
        Map<String, List<Map<String, Object>>> param = new HashMap<>();
        param.put("values", values);

//        hdyHttpUtils.pushIOT(param, "bbe55ec4-fc7b-4cd1-a704-1f07964b82d6");
    }


    private static String calculateAndFormatSum(Map<Integer, String> map, Integer... keys) {
        double sum = 0.0;

        for (Integer key : keys) {
            String value = map.get(key);
            if (value != null) {
                try {
                    sum += Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing value for key " + key + ": " + value);
                }
            }
        }
        // 保留两位小数
        return String.format("%.2f", sum);
    }
}