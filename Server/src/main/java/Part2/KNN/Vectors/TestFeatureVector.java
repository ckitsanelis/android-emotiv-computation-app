package Part2.KNN.Vectors;


public class TestFeatureVector extends FeatureVector {

    private int predictionLabel;

    public TestFeatureVector() {
        super();
    }

    public void setPrediction (int i) {
        predictionLabel = i;
    }

    public int getPrediction(){
        return predictionLabel;
    }
}

