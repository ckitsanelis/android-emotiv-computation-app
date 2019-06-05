package Part2.MQTT;


import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

class MQTTSubscriber implements MqttCallback {

    private final String topic              = "Server";
    private final String broker             = "tcp://localhost:1883";
    private final String clientID           = "JavaSubscriber";
    private final int qos = 2;
    private MqttClient mqttClient;
    private MemoryPersistence memoryPersistence;
    private MqttConnectOptions connectOptions;
    private PublisherRunnable publisherRunnable;


    public MQTTSubscriber(PublisherRunnable publisherRunnable) {
        this.publisherRunnable = publisherRunnable;
        connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(true);
        connectOptions.setConnectionTimeout(5);
        memoryPersistence = new MemoryPersistence();
        try {
            mqttClient = new MqttClient(broker,clientID,memoryPersistence);
            mqttClient.setCallback(this);
            mqttClient.connect(connectOptions);
            mqttClient.subscribe(topic,qos);
            System.out.println("Subscriber connected and subscribed successfully");
        } catch (MqttException e) {
            System.out.println("Unable to subscribe to topic");
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        if(mqttMessage.toString().startsWith("NEW:")) {
            // Add new topic to send messages to
            String user = mqttMessage.toString().substring(4);
            System.out.println("New Phone with id \"" + user + "\" connected to broker");
            publisherRunnable.addUser(user);
        }
        else if (mqttMessage.toString().startsWith("DEL:")) {
            // Delete topic to send messages to
            String user = mqttMessage.toString().substring(4);
            System.out.println("Phone with id \"" + user + "\" has disconnected");
            publisherRunnable.removeUser(user);
        }
        else {
            // Send time to publisher
            System.out.println("Changing publish frequency to " + mqttMessage.toString());
            publisherRunnable.changeSleep(Integer.parseInt(mqttMessage.toString()));
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    public void disconnect() {
        if(mqttClient.isConnected()) {
            try {
                mqttClient.unsubscribe(topic);
                mqttClient.disconnect();
            } catch (MqttException e) {
                System.out.println("Unable to disconnect from broker");
            }
        }
    }
}