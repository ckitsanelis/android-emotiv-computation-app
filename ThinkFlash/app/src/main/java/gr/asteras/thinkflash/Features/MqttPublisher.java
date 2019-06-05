package gr.asteras.thinkflash.Features;


import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublisher{

    private final String id;
    private final String topic = "Server";
    private final int qos = 2;
    private String connectTo;
    private MqttClient mqttClient = null;
    private MemoryPersistence memoryPersistence;
    private MqttConnectOptions connectOptions;


    public MqttPublisher(String server, String id) {
        connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(true);
        memoryPersistence = new MemoryPersistence();
        connectTo = server;
        this.id = "P" + id;
        initClient();
    }

    private void initClient() {
        Log.i("Connecting to",connectTo);
        try {
            mqttClient = new MqttClient(connectTo,id,memoryPersistence);
            mqttClient.connect(connectOptions);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if(mqttClient == null) {
            initClient();
        }
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(qos);
            mqttClient.publish(topic, mqttMessage);
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
