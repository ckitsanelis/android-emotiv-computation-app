package Part2.KNN;


import java.util.List;

public class EuclideanDistance {

    public static double getDistance(List<Double> A, List<Double> B) {
        double total = 0;
        for(int i = 0; i < A.size(); i++) {
            total += Math.pow(A.get(i) - B.get(i),2);
        }
        return Math.sqrt(total);
    }
}
