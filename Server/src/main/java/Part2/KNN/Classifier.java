package Part2.KNN;

import Part2.Buffer;
import Part2.KNN.Vectors.TestFeatureVector;
import Part2.KNN.Vectors.TrainFeatureVector;

import java.io.PrintWriter;
import java.util.List;


public class Classifier {

    public static void classify(int k, List<TrainFeatureVector> trainList, List<TestFeatureVector> testList, Buffer buffer) {

        int success = 0, wrong = 0;
        String fileName = "Files/Output/Classifier_Stats.txt";
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileName, "UTF-8");
            writer.println("LABEL\t\t\tPREDICTION\t\tRESULT\n");
        } catch (Exception e) {
            System.out.println("Couldn't open file");
        }
        kNearestNeighbors knn = new kNearestNeighbors();
        for (int i = 0 ; i < testList.size() ; i++ ) {
            testList.get(i).setPrediction(knn.predictLabel(knn.findNearest(k,trainList,testList.get(i)),k));
            String label, prediction, result;

            if (testList.get(i).getLabel() == 1)
                label = "Eyes Opened";
            else
                label = "Eyes Closed";
            System.out.println("CORRECT : " + label);

            if (testList.get(i).getPrediction() == 1)
                prediction = "Eyes Opened";
            else
                prediction = "Eyes Closed";

            if (testList.get(i).getLabel() == testList.get(i).getPrediction()) {
                success++;
                result = "Success";
            }
            else {
                wrong++;
                result = "Wrong";
            }
            System.out.println(result + "\n");
            buffer.push("Execute " + prediction + result);
            if (writer != null)
                writer.println(label + "\t\t" + prediction + "\t\t" + result);

            // Add new vector to train data
            TrainFeatureVector newTrain = new TrainFeatureVector();
            newTrain.setAttributes(testList.get(i).getAttributes());
            newTrain.setLabel(testList.get(i).getLabel());
            trainList.add(newTrain);
        }

        System.out.println("OVERALL CLASSIFIER RESULTS");
        System.out.println("SUCCESS : " + success);
        System.out.println("WRONG : " + wrong);
        double ratio = (double) success / (double) (success + wrong);
        System.out.println("SUCCESS RATIO : " + ratio);

        if (writer != null) {
            writer.println("\n\nOVERALL CLASSIFIER RESULTS");
            writer.println("SUCCESS : " + success);
            writer.println("WRONG : " + wrong);
            writer.println("SUCCESS RATIO : " + ratio);
            writer.close();
        }
    }
}
