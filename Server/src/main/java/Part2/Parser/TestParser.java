package Part2.Parser;


import Part2.KNN.Entropy;
import Part2.KNN.Vectors.TestFeatureVector;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestParser {

    public static List<TestFeatureVector> readTestFiles() {
        File dir = new File("Files/FinalData");
        List<TestFeatureVector> testList = new ArrayList<>();

        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {

                System.out.println("Reading file: " + file);
                TestFeatureVector testVector = getTestVector(file);
                // Make sure Emotiv had a good connection or the file wasn't empty
                if(!testVector.getAttributes().get(0).equals(0.0)) {
                    testList.add(testVector);
                }
            }
        }
        else {
            System.out.println("There are no files in the folder");
            System.exit(0);
        }

        return testList;
    }

    private static TestFeatureVector getTestVector(File csvFilePath) {

        FileReader fileReader = null;

        CSVParser csvParser = null;

        TestFeatureVector TestFeatureVector = new TestFeatureVector();

        try {
            fileReader = new FileReader(csvFilePath);

            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader("AF3" ,"F7",
                    "F3","FC5","T7","P7","O1","O2","P8","T8",
                    "FC6", "F4","F8","AF4","CQ_AF3" ,"CQ_F7",
                    "CQ_F3","CQ_FC5","CQ_T7","CQ_P7","CQ_O1","CQ_O2","CQ_P8","CQ_T8",
                    "CQ_FC6", "CQ_F4","CQ_F8","CQ_AF4","Marker").withSkipHeaderRecord(true).parse(fileReader);


            List<String> AF3_list = new ArrayList<>();
            List<String> F7_list = new ArrayList<>();
            List<String> F3_list = new ArrayList<>();
            List<String> FC5_list = new ArrayList<>();
            List<String> T7_list = new ArrayList<>();
            List<String> P7_list = new ArrayList<>();
            List<String> O1_list = new ArrayList<>();
            List<String> O2_list = new ArrayList<>();
            List<String> P8_list = new ArrayList<>();
            List<String> T8_list = new ArrayList<>();
            List<String> FC6_list = new ArrayList<>();
            List<String> F4_list = new ArrayList<>();
            List<String> F8_list = new ArrayList<>();
            List<String> AF4_list = new ArrayList<>();

            boolean first = true;
            for (CSVRecord record : records) {
                if(!first) {
                    if (!goodConnection(record))
                        continue;
                }
                else {
                    first = false;
                    if (!goodConnectionAndEvent(record))
                        continue;
                }

                String AF3 = record.get("AF3");
                String F7 = record.get("F7");
                String F3 = record.get("F3");
                String FC5 = record.get("FC5");
                String T7 = record.get("T7");
                String P7 = record.get("P7");
                String O1 = record.get("O1");
                String O2 = record.get("O2");
                String P8 = record.get("P8");
                String T8 = record.get("T8");
                String FC6 = record.get("FC6");
                String F4 = record.get("F4");
                String F8 = record.get("F8");
                String AF4 = record.get("AF4");

                AF3_list.add(AF3);
                F7_list.add(F7);
                F3_list.add(F3);
                FC5_list.add(FC5);
                T7_list.add(T7);
                P7_list.add(P7);
                O1_list.add(O1);
                O2_list.add(O2);
                P8_list.add(P8);
                T8_list.add(T8);
                FC6_list.add(FC6);
                F4_list.add(F4);
                F8_list.add(F8);
                AF4_list.add(AF4);
            }

            double[] AF3_values = new double[AF3_list.size()];
            double[] F7_values = new double[F7_list.size()];
            double[] F3_values = new double[F3_list.size()];
            double[] FC5_values = new double[FC5_list.size()];
            double[] T7_values = new double[T7_list.size()];
            double[] P7_values = new double[P7_list.size()];
            double[] O1_values = new double[O1_list.size()];
            double[] O2_values = new double[O2_list.size()];
            double[] P8_values = new double[P8_list.size()];
            double[] T8_values = new double[T8_list.size()];
            double[] FC6_values = new double[FC6_list.size()];
            double[] F4_values = new double[F4_list.size()];
            double[] F8_values = new double[F8_list.size()];
            double[] AF4_values = new double[AF4_list.size()];

            for (int i = 0; i < AF3_list.size(); i++) {
                AF3_values[i] = Double.parseDouble(AF3_list.get(i));
                F7_values[i] = Double.parseDouble(F7_list.get(i));
                F3_values[i] = Double.parseDouble(F3_list.get(i));
                FC5_values[i] = Double.parseDouble(FC5_list.get(i));
                T7_values[i] = Double.parseDouble(T7_list.get(i));
                P7_values[i] = Double.parseDouble(P7_list.get(i));
                O1_values[i] = Double.parseDouble(O1_list.get(i));
                O2_values[i] = Double.parseDouble(O2_list.get(i));
                P8_values[i] = Double.parseDouble(P8_list.get(i));
                T8_values[i] = Double.parseDouble(T8_list.get(i));
                FC6_values[i] = Double.parseDouble(FC6_list.get(i));
                F4_values[i] = Double.parseDouble(F4_list.get(i));
                F8_values[i] = Double.parseDouble(F8_list.get(i));
                AF4_values[i] = Double.parseDouble(AF4_list.get(i));
            }

            List<Double> entropy_attributes = new ArrayList<>();
            entropy_attributes.add(Entropy.calculateEntropy(AF3_values));
            entropy_attributes.add(Entropy.calculateEntropy(F7_values));
            entropy_attributes.add(Entropy.calculateEntropy(F3_values));
            entropy_attributes.add(Entropy.calculateEntropy(FC5_values));
            entropy_attributes.add(Entropy.calculateEntropy(T7_values));
            entropy_attributes.add(Entropy.calculateEntropy(P7_values));
            entropy_attributes.add(Entropy.calculateEntropy(O1_values));
            entropy_attributes.add(Entropy.calculateEntropy(O2_values));
            entropy_attributes.add(Entropy.calculateEntropy(P8_values));
            entropy_attributes.add(Entropy.calculateEntropy(T8_values));
            entropy_attributes.add(Entropy.calculateEntropy(FC6_values));
            entropy_attributes.add(Entropy.calculateEntropy(F4_values));
            entropy_attributes.add(Entropy.calculateEntropy(F8_values));
            entropy_attributes.add(Entropy.calculateEntropy(AF4_values));


            TestFeatureVector.setAttributes(entropy_attributes);

            String filename = csvFilePath.getPath();
            if (filename.contains("Opened"))
                TestFeatureVector.setLabel(1);
            else
                TestFeatureVector.setLabel(0);

        } catch (IOException e) {
            e.printStackTrace();
            e.getMessage();
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
                if (csvParser != null) {
                    csvParser.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                ex.getMessage();
            }
        }
        return TestFeatureVector;
    }

    private static Boolean goodConnection(CSVRecord record) {
        return ((Double.parseDouble(record.get("CQ_AF3")) > 1) && (Double.parseDouble(record.get("CQ_F7")) > 1) &&
                (Double.parseDouble(record.get("CQ_F3")) > 1) && (Double.parseDouble(record.get("CQ_FC5")) > 1) &&
                (Double.parseDouble(record.get("CQ_T7")) > 1) && (Double.parseDouble(record.get("CQ_P7")) > 1) &&
                (Double.parseDouble(record.get("CQ_O1")) > 1) && (Double.parseDouble(record.get("CQ_O2")) > 1) &&
                (Double.parseDouble(record.get("CQ_P8")) > 1) && (Double.parseDouble(record.get("CQ_T8")) > 1) &&
                (Double.parseDouble(record.get("CQ_FC6")) > 1) && (Double.parseDouble(record.get("CQ_F4")) > 1) &&
                (Double.parseDouble(record.get("CQ_F8")) > 1) && (Double.parseDouble(record.get("CQ_AF4")) > 1));
    }

    private static Boolean goodConnectionAndEvent(CSVRecord record) {
        return ((Double.parseDouble(record.get("CQ_AF3")) > 1) && (Double.parseDouble(record.get("CQ_F7")) > 1) &&
                (Double.parseDouble(record.get("CQ_F3")) > 1) && (Double.parseDouble(record.get("CQ_FC5")) > 1) &&
                (Double.parseDouble(record.get("CQ_T7")) > 1) && (Double.parseDouble(record.get("CQ_P7")) > 1) &&
                (Double.parseDouble(record.get("CQ_O1")) > 1) && (Double.parseDouble(record.get("CQ_O2")) > 1) &&
                (Double.parseDouble(record.get("CQ_P8")) > 1) && (Double.parseDouble(record.get("CQ_T8")) > 1) &&
                (Double.parseDouble(record.get("CQ_FC6")) > 1) && (Double.parseDouble(record.get("CQ_F4")) > 1) &&
                (Double.parseDouble(record.get("CQ_F8")) > 1) && (Double.parseDouble(record.get("CQ_AF4")) > 1) &&
                (Double.parseDouble(record.get("Marker")) > 0));
    }
}
