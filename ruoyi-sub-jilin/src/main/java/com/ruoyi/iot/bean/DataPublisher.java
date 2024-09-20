package com.ruoyi.iot.bean;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class DataPublisher {

    private MQTTConnector connector;

    public DataPublisher(MQTTConnector connector) {
        this.connector = connector;
    }

    public void publishData(String manufacturerId, String deviceType, String groupId, String payload) {
        try {
            String topic = String.format("platform/%s/%s/json-v2/analog/%s", manufacturerId, deviceType, groupId);
            System.out.println("Publishing to topic: " + topic);

            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(1); // 文档中指定 QoS 为 1

            connector.getMqttClient().publish(topic, message);
            System.out.println("Message published");

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}