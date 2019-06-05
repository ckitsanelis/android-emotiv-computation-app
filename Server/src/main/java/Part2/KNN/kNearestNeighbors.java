package Part2.KNN;


import Part2.KNN.Vectors.TestFeatureVector;
import Part2.KNN.Vectors.TrainFeatureVector;

import java.util.ArrayList;
import java.util.List;

public class kNearestNeighbors {

    public List<TrainFeatureVector> findNearest(int k, List<TrainFeatureVector> trainVectors, TestFeatureVector testVector) {
        List<TrainFeatureVector> neighbours = new ArrayList<>();
        for (int i = 0 ; i < trainVectors.size() ; i++) {
            TrainFeatureVector tempTrain = trainVectors.get(i);
            double tempDistance = EuclideanDistance.getDistance(testVector.getAttributes(),tempTrain.getAttributes());
            tempTrain.setDistance(tempDistance);
            if (i < k) {
                neighbours.add(i,tempTrain);
                continue;
            }

            double biggestDistance = neighbours.get(0).getDistance();
            int farthest = 0;
            for (int j = 1 ; j < k ; j++) {
                if (neighbours.get(j).getDistance() > biggestDistance) {
                    farthest = j;
                    biggestDistance = neighbours.get(j).getDistance();
                }
            }
            if (tempTrain.getDistance() < neighbours.get(farthest).getDistance()) {
                neighbours.remove(farthest);
                neighbours.add(farthest, tempTrain);
            }
        }
        return neighbours;
    }

    public int predictLabel(List<TrainFeatureVector> oldNeighbours, int k) {
        int counter0 = 0, counter1 = 0;
        double weight0 = 0, weight1 = 0;
        List<Integer> label0Array = new ArrayList<>();
        List<Integer> label1Array = new ArrayList<>();
        List<Double> weights = new ArrayList<>();
        for (int i = 0 ; i < k ; i++) {
            weights.add(0.0);
        }
        //Give bigger weight to closest neighbours
        List<TrainFeatureVector> neighbours = SortList(oldNeighbours);
        //Track counters and item placements of each label
        for (int i = 0 ; i < k ; i++) {
            if(neighbours.get(i).getLabel() == 0) {
                counter0++;
                label0Array.add(i);
            }
            else {
                counter1++;
                label1Array.add(i);
            }
        }
        System.out.println("Counter 0 : " + counter0);
        System.out.println(label0Array.toString());
        System.out.println("Counter 1 : " + counter1);
        System.out.println(label1Array.toString());
        if (counter0 == k) {
            System.out.println("PREDICTION : Eyes Closed");
            return 0;
        }
        if (counter1 == k) {
            System.out.println("PREDICTION : Eyes Opened");
            return 1;
        }
        //Get item placements for each label, calculate individual weights and add them to corresponding spot in second list
//        System.out.println("Weights for label 0");
        while (!label0Array.isEmpty()) {
            int val = label0Array.get(0);
            label0Array.remove(0);
            weight0 = weight0 + (1 / neighbours.get(val).getDistance());
            weights.remove(val);
            weights.add(val,weight0);
//            System.out.println("Weight " + val + " = " + weight0);
        }
//        System.out.println("Weights for label 1");
        while (!label1Array.isEmpty()) {
            int val = label1Array.get(0);
            label1Array.remove(0);
            weight1 = weight1 + (1 / neighbours.get(val).getDistance());
            weights.remove(val);
            weights.add(val,weight1);
//            System.out.println("Weight " + val + " = " + weight1);
        }
        //Find neighbor with the biggest (label counter * individual weight)
        double largest = -1;
        int spot = -1;
        for (int i = 0 ; i < weights.size() ; i++) {
            if (neighbours.get(i).getLabel() == 0) {
                if (counter0 * weights.get(i) > largest) {
                    spot = i;
                    largest = counter0 * weights.get(i);
                }
            }
            else {
                if (counter1 * weights.get(i) > largest) {
                    spot = i;
                    largest = counter1 * weights.get(i);
                }
            }
        }
        System.out.println("Largest = " + largest + "at spot : " + spot);
        System.out.println("PREDICTION : " + transformLabel(neighbours.get(spot).getLabel()));
        return neighbours.get(spot).getLabel();
    }

    private String transformLabel(int label) {
        if (label == 0)
            return "Eyes Closed";
        else
            return "Eyes Opened";

    }

    private List<TrainFeatureVector> SortList(List<TrainFeatureVector> oldList) {
        if (oldList.size() < 2)
            return oldList;
        List<TrainFeatureVector> newList = new ArrayList<>();
        while (!oldList.isEmpty()) {
            int spot = 0;
            double smallest = oldList.get(spot).getDistance();
            for (int i = 1 ; i < oldList.size() ; i++) {
                if (oldList.get(i).getDistance() < smallest) {
                    smallest = oldList.get(i).getDistance();
                    spot = i;
                }
            }
            newList.add(oldList.get(spot));
            oldList.remove(spot);
        }
        return newList;
    }
}
