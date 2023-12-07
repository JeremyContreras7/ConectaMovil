package com.Conectamovil;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttManager {

    private MqttClient mqttClient;

    public MqttManager(String broker, String clientId) {
        try {
            mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void connectAndSubscribe(String topic) {
        try {
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // Set the username and password if required by your HiveMQ setup
            // connOpts.setUserName("your_username");
            // connOpts.setPassword("your_password".toCharArray());

            System.out.println("Connecting to broker: " + mqttClient.getServerURI());
            mqttClient.connect(connOpts);
            System.out.println("Connected");

            // Subscribe to the specified topic
            mqttClient.subscribe(topic);

            // Set up callback for incoming messages
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    System.out.println("Received message from " + topic + ": " + new String(message.getPayload()));
                    // Aquí puedes procesar el mensaje recibido según tus necesidades
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used in this example
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(String topic, String message) {
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttClient.publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
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
}
