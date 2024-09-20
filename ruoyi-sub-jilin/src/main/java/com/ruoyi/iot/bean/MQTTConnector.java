package com.ruoyi.iot.bean;

import com.alibaba.fastjson2.JSON;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTConnector {

    private String broker = "tcp://your-broker-url:1883"; // 替换为实际的 broker 地址
    private String clientId = "clientId"; // 替换为实际的 clientId
    private String username = "your-username"; // 替换为实际的用户名
    private String password = "your-password"; // 替换为实际的密码
    private MqttClient mqttClient;

    public void connect() {
        try {
            mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());

            // 设置连接选项
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(60); // 根据文档，KeepAlive 可以设置为 1min~5min

            // 建立连接
            System.out.println("Connecting to broker: " + broker);
            mqttClient.connect(connOpts);
            System.out.println("Connected");

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
                System.out.println("Disconnected");
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }
}