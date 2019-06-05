package Part2.KNN.Vectors;


import java.util.ArrayList;
import java.util.List;

public class FeatureVector {

    private int label;
    private List<Double> attributes;

    public FeatureVector() {
        this.attributes = new ArrayList<>();
    }

    public void setLabel(int i) {
        label = i;
    }

    public int getLabel() {
        return label;
    }

    public void setAttributes(List<Double> attributes) {
        this.attributes = attributes;
    }

    public List<Double> getAttributes() {
        return attributes;
    }

    public void attrString() {
        System.out.print("Attributes:");
        List<Double> list = getAttributes();
        for(int k = 0 ; k < list.size() ; k++)
            System.out.print(list.get(k) + ",");
        System.out.println();
    }
}