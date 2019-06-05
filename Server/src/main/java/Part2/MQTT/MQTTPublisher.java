package Part2.MQTT;


import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.List;

public class MQTTPublisher {

    private final String topic              = "Phones";
    private final String broker             = "tcp://localhost:1883";
    private final String clientID           = "JavaPublisher";
    private final int qos = 2;
    private MqttClient mqttClient;
    private MemoryPersistence memoryPersistence;
    private MqttConnectOptions connectOptions;

    public MQTTPublisher() {
        connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(true);
        connectOptions.setConnectionTimeout(5);
        memoryPersistence = new MemoryPersistence();
        connect();
    }

    public void sendMessage(String message) {
            try {
                MqttMessage mqttMessage = new MqttMessage(message.getBytes());
                mqttMessage.setQos(qos);
                mqttClient.publish(topic, mqttMessage);
                System.out.println("Sent message: " + message);
            } catch (MqttException e) {
                System.out.println("Unable to send message to broker");
            }
    }

    public void connect() {
        if(mqttClient == null) {
            try {
                mqttClient = new MqttClient(broker, clientID, memoryPersistence);
            } catch (MqttException e) {
                System.out.println("Unable to create client");
            }
        }
        if(!mqttClient.isConnected()) {
            try {
                mqttClient.connect(connectOptions);
                System.out.println("Publisher connected successfully");
            } catch (MqttException e) {
                System.out.println("Unable to connect to broker");
            }
        }
    }

    public void disconnect() {
        if(mqttClient.isConnected()) {
            try {
                mqttClient.disconnect();
            } catch (MqttException e) {
                System.out.println("Unable to disconnect from broker");
            }
        }
    }
}
