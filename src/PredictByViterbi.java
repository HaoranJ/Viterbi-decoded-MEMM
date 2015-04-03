import java.io.*;
import java.util.*;
import opennlp.model.GenericModelReader;
import opennlp.model.MaxentModel;

public class PredictByViterbi {

	public void predictViterbi(String modelFileName, String testFileName, String resultFileName) throws IOException {
		Scanner scan = new Scanner(new File(testFileName));
		PrintWriter writer = new PrintWriter(resultFileName);
		MaxentModel m = new GenericModelReader(new File(modelFileName)).getModel();

		double[][][] p = new double[10010][][];
		for (int i = 0; i < 10010; ++i)
			p[i] = new double[4][];
		double[][] f = new double[10010][];
		for (int i = 0; i < 10010; ++i)
			f[i] = new double[4];
		int[][] pre = new int[10010][];
		for (int i = 0; i < 10010; ++i)
			pre[i] = new int[4];
		String[] words = new String[10010];

		int t = 0;

		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if (line.trim().length() == 0) {
				// System.out.println("hello");
				f[1] = p[1][3];

				for (int i = 2; i <= t; ++i)
					for (int j = 0; j < 3; ++j) // current state
					{
						f[i][j] = 0;
						for (int k = 0; k < 3; ++k) // prior state
						{
							double q = f[i - 1][k] * p[i][k][j];
							if (q > f[i][j]) {
								f[i][j] = q;
								pre[i][j] = k;
							}
						}
					}

				double c = 0;
				int j = 0;
				for (int k = 0; k < 3; ++k)
					if (f[t][k] > c) {
						c = f[t][k];
						j = k;
					}
				for (int i = t; i > 0; --i) {
					if (j == 0)
						words[i] = words[i] + " I";
					if (j == 1)
						words[i] = words[i] + " O";
					if (j == 2)
						words[i] = words[i] + " B";
					j = pre[i][j];
				}

				for (int i = 1; i <= t; ++i) {
					writer.write(words[i]);
					writer.write("\n");
				}

				writer.write("\n");
				t = 0;
				continue;
			}

			++t;

			String[] features = new String[7];

			String[] tokens = line.split(" ");
			for (int i = 0; i < 6; ++i)
				features[i] = tokens[i];

			words[t] = features[0].substring(8);

			features[6] = "preState=&";
			p[t][3] = m.eval(features);

			features[6] = "preState=I";
			p[t][0] = m.eval(features);

			features[6] = "preState=O";
			p[t][1] = m.eval(features);

			features[6] = "preState=B";
			p[t][2] = m.eval(features);
		}
		scan.close();
		writer.close();
	}

}
