import opennlp.maxent.*;
import opennlp.maxent.io.*;
import opennlp.model.EventStream;
import java.io.*;

public class TrainingModel {
	public void train(String dataFileName, String modelFileName) {
		try {
			FileReader datafr = new FileReader(new File(dataFileName));
			EventStream es = new BasicEventStream(new PlainTextByLineDataStream(datafr));
			GISModel model = GIS.trainModel(es, 100, 4);
			File outputFile = new File(modelFileName);
			GISModelWriter writer = new SuffixSensitiveGISModelWriter(model, outputFile);
			writer.persist();
		} catch (Exception e) {
			System.out.print("Unable to create model due to exception: ");
			System.out.println(e);
		}
	}

}
