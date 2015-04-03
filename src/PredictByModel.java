import java.io.*;
import java.util.*;
import opennlp.model.GenericModelReader;
import opennlp.model.MaxentModel;

public class PredictByModel {

	public void predictBasedOnModel(String modelFileName, String testFileName,
			String resultFileName) throws IOException {
		Scanner scan = new Scanner(new File(testFileName));
		PrintWriter writer = new PrintWriter(resultFileName);
		MaxentModel m = new GenericModelReader(new File(modelFileName)).getModel();
		String[] features = new String[7];
		String preState = "&";
		String delimiter = "\\s*" + " " + "\\s*";

		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if (line.trim().length() > 0) {
				String[] tokens = line.split(delimiter);
				features[6] = "preState=" + preState;
				for (int i = 0; i < 6; ++i) {
					features[i] = tokens[i];
				}
				preState = m.getBestOutcome(m.eval(features));
				writer.print(features[0].substring(8) + " " + preState + "\n");
			} else {
				preState = "&";
				writer.println();
			}
		}
		scan.close();
		writer.close();
	}
}
