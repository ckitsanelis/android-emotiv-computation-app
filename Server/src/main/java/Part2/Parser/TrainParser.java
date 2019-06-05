package Part2.Parser;

import Part2.KNN.Vectors.TrainFeatureVector;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrainParser {

    public static List<TrainFeatureVector> readTrainFile() {

        String csvFilePath = "Files/TrainingSet/Training Set.csv";
        List<TrainFeatureVector> retList = new ArrayList<>();

        FileReader fileReader = null;

        CSVParser csvParser = null;

        try {
            System.out.println("Preparing CSV Parser object.");

            /* Creating file reader */
            fileReader = new FileReader(csvFilePath);

            CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("LabelClassName", "Entropy_AF3" ,"Entropy_F7",
                    "Entropy_F3","Entropy_FC5","Entropy_T7","Entropy_P7","Entropy_O1","Entropy_O2","Entropy_P8","Entropy_T8",
                    "Entropy_FC6", "Entropy_F4","Entropy_F8","Entropy_AF4");

            /* Creating a CSV printer */
            csvParser = new CSVParser(fileReader, csvFormat);

            List<CSVRecord> rowList = csvParser.getRecords();

            System.out.println("Loop through the records");

            int label = -1;

            for (int i = 1; i < rowList.size(); i++) {
                CSVRecord row = rowList.get(i);
                String Label = row.get("LabelClassName");
                if (Label.equals("EyesOpened"))
                    label = 1;
                else
                    label = 0;
                double Entropy_AF3 = Double.parseDouble(row.get("Entropy_AF3"));
                double Entropy_F7 = Double.parseDouble(row.get("Entropy_F7"));
                double Entropy_F3 = Double.parseDouble(row.get("Entropy_F3"));
                double Entropy_FC5 = Double.parseDouble(row.get("Entropy_FC5"));
                double Entropy_T7 = Double.parseDouble(row.get("Entropy_T7"));
                double Entropy_P7 = Double.parseDouble(row.get("Entropy_P7"));
                double Entropy_O1 = Double.parseDouble(row.get("Entropy_O1"));
                double Entropy_O2 = Double.parseDouble(row.get("Entropy_O2"));
                double Entropy_P8 = Double.parseDouble(row.get("Entropy_P8"));
                double Entropy_T8 = Double.parseDouble(row.get("Entropy_T8"));
                double Entropy_FC6 = Double.parseDouble(row.get("Entropy_FC6"));
                double Entropy_F4 = Double.parseDouble(row.get("Entropy_F4"));
                double Entropy_F8 = Double.parseDouble(row.get("Entropy_F8"));
                double Entropy_AF4 = Double.parseDouble(row.get("Entropy_AF4"));

                List<Double> attributes = new ArrayList<>();
                attributes.add(Entropy_AF3);
                attributes.add(Entropy_F7);
                attributes.add(Entropy_F3);
                attributes.add(Entropy_FC5);
                attributes.add(Entropy_T7);
                attributes.add(Entropy_P7);
                attributes.add(Entropy_O1);
                attributes.add(Entropy_O2);
                attributes.add(Entropy_P8);
                attributes.add(Entropy_T8);
                attributes.add(Entropy_FC6);
                attributes.add(Entropy_F4);
                attributes.add(Entropy_F8);
                attributes.add(Entropy_AF4);

                TrainFeatureVector record = new TrainFeatureVector();
                record.setAttributes(attributes);
                record.setLabel(label);
                retList.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
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
                System.out.println(ex.getMessage());
            }
        }

        return retList;
    }
}
