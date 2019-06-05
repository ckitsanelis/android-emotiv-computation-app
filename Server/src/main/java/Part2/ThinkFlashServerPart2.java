package Part2;


import Part2.KNN.Classifier;
import Part2.KNN.Vectors.TestFeatureVector;
import Part2.KNN.Vectors.TrainFeatureVector;
import Part2.MQTT.PublisherRunnable;
import Part2.MQTT.SubscriberRunnable;
import Part2.Parser.TestParser;
import Part2.Parser.TrainParser;

import java.util.ArrayList;
import java.util.List;

public class ThinkFlashServerPart2 {

    public static void main(String[] args) {

        final int duration    = 3000;
        final int bufferCheck = 5000;

        // Get k
        int k = 10;
        if (args.length > 0) {
            if (args[0].equals("-k")) {
                try {
                    k = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Value provided wasn't a valid integer");
                    return;
                }
            }
        }

        // Initiate buffer
        Buffer bufferList = new Buffer();

        // Create threads for MQTT operations
        PublisherRunnable publisher = new PublisherRunnable(duration,bufferList);
        Thread MqttPublisher = new Thread(publisher);
        MqttPublisher.start();
        SubscriberRunnable subscriber = new SubscriberRunnable(publisher);
        Thread MqttSubscriber = new Thread(subscriber);
        MqttSubscriber.start();

        // Read csv files and start classifier
        List<TrainFeatureVector> trainVectors = new ArrayList<>();
        List<TestFeatureVector> testVectors   = new ArrayList<>();
        trainVectors = TrainParser.readTrainFile();
        testVectors  = TestParser.readTestFiles();
        Classifier.classify(k,trainVectors,testVectors,bufferList);


        // When Buffer is empty, order all threads to kill
        while(!bufferList.isEmpty()) {
            try {
                System.out.println("Main thread sleeping for " + bufferCheck);
                Thread.sleep(bufferCheck);
            } catch (InterruptedException e) {
                System.out.println("Main thread was interrupted!");
                bufferList.empty();
            }
        }
        System.out.println("Switching off");
        publisher.terminate();
        subscriber.terminate();
        try {
            MqttSubscriber.join();
            MqttPublisher.join();
        } catch (InterruptedException e) {
            System.out.println("Unable to join threads");
        }
        System.out.println("Killing main");
    }
}
