package com.Conectamovil;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class SimpleMqttChat {

    private static final String BROKER = "tcp://broker.hivemq.com:1883";
    private static final String CLIENT_ID = "JavaClient";

    public static void main(String[] args) {
        new SimpleMqttChat().start();
    }

    public void start() {
        try {
            MqttClient mqttClient = new MqttClient(BROKER, CLIENT_ID, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            System.out.println("Connecting to broker: " + BROKER);
            mqttClient.connect(connOpts);
            System.out.println("Connected");

            // Subscribe to the chat topic
            String chatTopic = "chat/topic";
            mqttClient.subscribe(chatTopic);

            // Set up callback for incoming messages
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    System.out.println("Received message from " + topic + ": " + new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used in this example
                }
            });

            // Simulate sending a message
            String message = "Hello, HiveMQ!";
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttClient.publish(chatTopic, mqttMessage);

            // Wait for a while to receive messages
            Thread.sleep(5000);

            // Disconnect
            mqttClient.disconnect();
            System.out.println("Disconnected");
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
