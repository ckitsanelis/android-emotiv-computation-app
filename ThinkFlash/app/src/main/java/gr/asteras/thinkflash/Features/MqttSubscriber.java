package gr.asteras.thinkflash.Features;


import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.os.Handler;

import gr.asteras.thinkflash.Features.Runnable.PublisherRunnable;

public class MqttSubscriber implements MqttCallback{

    private final String id;
    private final String topic = "Phones";
    private final int qos = 2;
    private String connectTo;
    private MqttClient mqttClient = null;
    private MemoryPersistence memoryPersistence;
    private MqttConnectOptions connectOptions;
    private Handler uiHandler;


    public MqttSubscriber(String server, Handler handler, String id) {
        connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(true);
        connectOptions.setConnectionTimeout(5);
        memoryPersistence = new MemoryPersistence();
        connectTo = server;
        this.id = "S" + id;
        uiHandler = handler;
        initClient();
    }

    private void initClient() {
        Log.i("Connecting to",connectTo);
        try {
            mqttClient = new MqttClient(connectTo,id,memoryPersistence);
            mqttClient.setCallback(this);
            mqttClient.connect(connectOptions);
            subscribe();
            Log.i("Connected","to" + connectTo);
        } catch (MqttException e) {
            uiHandler.sendEmptyMessage(4);
            Log.i("InitConnectError",e.getMessage());
        }
    }


    private void subscribe() {
        if(mqttClient == null || !mqttClient.isConnected())
            initClient();
        else {
            try {
                mqttClient.subscribe(topic, qos);
                String newConnection = "NEW:" + id.substring(1);
                PublisherRunnable publisherRunnable = new PublisherRunnable(connectTo,newConnection,id);
                Thread publisherThread = new Thread(publisherRunnable);
                publisherThread.start();
                uiHandler.sendEmptyMessage(5);
            } catch (Exception e) {
                //uiHandler.sendEmptyMessage(4);
                Log.i("ClientSubscribeError", e.getMessage());
            }
        }
    }

    public void unsubscribe() {
        if(mqttClient != null && mqttClient.isConnected()) {
            try {
                String killConnection = "DEL:" + id.substring(1);
                PublisherRunnable publisherRunnable = new PublisherRunnable(connectTo,killConnection,id);
                Thread publisherThread = new Thread(publisherRunnable);
                publisherThread.start();
                mqttClient.unsubscribe(topic);
                mqttClient.disconnect();
                mqttClient.close();
                mqttClient = null;
                uiHandler.sendEmptyMessage(7);
            } catch (Exception e) {
                Log.i("ClientUnsubErr", e.getMessage());
            }
        }
    }


    @Override
    public void connectionLost(Throwable cause) {
        uiHandler.sendEmptyMessage(6);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // Part 2 Implementation
        if(String.valueOf(message).equals("NEED USER")) {
            //send message with name
            String newConnection = "NEW:" + id.substring(1);
            PublisherRunnable publisherRunnable = new PublisherRunnable(connectTo,newConnection,id);
            Thread publisherThread = new Thread(publisherRunnable);
            publisherThread.start();
        }
        else {
            String[] splitString = String.valueOf(message).split("\\s+");
            if (splitString[2].equals("Opened")) {
                uiHandler.sendEmptyMessage(0);
            } else if (splitString[2].equals("Closed")) {
                uiHandler.sendEmptyMessage(3);
            }
        }

        // Part 1 Implementation
//        if(splitString[1].equals("ON")) {
//            if (splitString[3].equals("ON"))
//                uiHandler.sendEmptyMessage(0);
//            else
//                uiHandler.sendEmptyMessage(1);
//        }
//        else if(splitString[1].equals("OFF")){
//            if (splitString[3].equals("ON"))
//                uiHandler.sendEmptyMessage(2);
//            else
//                uiHandler.sendEmptyMessage(3);
//        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
