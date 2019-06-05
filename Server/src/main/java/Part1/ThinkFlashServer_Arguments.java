package Part1;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.ThreadLocalRandom;

public class ThinkFlashServer_Arguments {

    public static void main(String[] args) {

        String topic            = "ServerToPhone";
        String music_content    = "Uninitialized message";
        String flash_content    = "Uninitialized message";
        String content          = "Uninitialized message";
        String broker           = "tcp://localhost:1883";                                     /* Mqtt broker address */
        String clientID         = "Server";                                                   /* Optimally, client's address */
        int qos = 2;

        int number_of_messages;
        int time_interval;

        MemoryPersistence persistence = new MemoryPersistence();                              /* Memory Persistence */

        if (args.length > 0 && args.length < 5) {
            try {
                number_of_messages = Integer.parseInt(args[1]);
                time_interval = Integer.parseInt(args[3]);
                time_interval = time_interval * 1000;

                for (int i = 0; i < number_of_messages; i++) {

                    System.out.println("\n>>> Generating a new signal...");
                    int music_signal = ThreadLocalRandom.current().nextInt(0, 2);       /* Random 0/1 generator */
                    int flash_signal = ThreadLocalRandom.current().nextInt(0, 2);       /* Random 0/1 generator */
                    System.out.println(">>>Music Signal generated: " + music_signal);                 /* Printing the generated signal */
                    System.out.println(">>>Flash Signal generated: " + flash_signal);

                    if (music_signal == 1){
                        music_content = "MUSIC ON";
                    } else music_content = "MUSIC OFF";

                    if (flash_signal == 1) {
                        flash_content = "FLASH ON";
                    } else flash_content = "FLASH OFF";

                    System.out.println(">>> "+ music_content);
                    System.out.println(">>> "+ flash_content);
                    content = music_content + " " + flash_content;

                    try {
                        MqttClient mqttClient = new MqttClient(broker, clientID, persistence);
                        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
                        mqttConnectOptions.setCleanSession(true);
                        System.out.println(">>> Connecting to broker: " + broker);
                        mqttClient.connect(mqttConnectOptions);                                       /* Connecting to broker */
                        System.out.println(">>> Connected");
                        System.out.println(">>> Publishing message: " + content);
                        MqttMessage mqttMessage = new MqttMessage(content.getBytes());                /* MqttMessage instance */
                        mqttMessage.setQos(qos);                                                      /* Setting the QoS */
                        mqttClient.publish(topic, mqttMessage);                                       /* Publish the message */
                        System.out.println(">>> Message published");
                        mqttClient.disconnect();                                                      /* Disconnect broker */
                        System.out.println(">>> Disconnected");

                        try {
                            System.out.println(">>> Going to sleep for " + time_interval/1000 + " second(s)");
                            Thread.sleep(time_interval);                                              /* Broadcast Interval */
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } catch (MqttException e) {
                        System.out.println(">>> Reason: " + e.getReasonCode());
                        System.out.println(">>> Message: " + e.getMessage());
                        System.out.println(">>> Localized Message: " + e.getLocalizedMessage());
                        System.out.println(">>> Cause: " + e.getCause());
                        System.out.println(">>> Exception: " + e);
                        e.printStackTrace();
                    }

                }
                System.out.println(">>> Exiting now...");
                System.exit(0);

            }catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + " must be an integer.");
                System.exit(1);
            }
        }

        else {
            System.out.println("Usage: java Server -n [number of messages] -t [time interval in seconds]");
            System.exit(2);
        }

    }
}