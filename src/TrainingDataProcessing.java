import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;

public class TrainingDataProcessing {
	private Scanner reader;
	private PrintWriter writer;
	private ArrayList<ArrayList<String>> a;

	public TrainingDataProcessing() {
		a = new ArrayList<ArrayList<String>>();
	}

	public void readFile(String pr, String pw, String delimiter) throws Exception {
		writer = new PrintWriter(pw);
		reader = new Scanner(Paths.get(pr));
		String line = "";
		String delimiterRevised = "\\s*" + delimiter + "\\s*";

		while (reader.hasNextLine()) {
			line = reader.nextLine();
			if (line.trim().length() == 0) {
				int len = a.size();
				for (int i = 0; i < len; i++) {
					// curWord
					writer.print("curWord=" + a.get(i).get(0) + " ");
					// curTag
					writer.print("curTag=" + a.get(i).get(1) + " ");
					// preTag
					writer.print("preTag=");
					if (i > 0)
						writer.print(a.get(i - 1).get(1) + " ");
					else
						writer.print("& ");

					// postTag
					writer.print("postTag=");
					if (i < len - 1)
						writer.print(a.get(i + 1).get(1) + " ");
					else
						writer.print("# ");

					// previous and current word features conjunction
					writer.print("preConj=");
					if (i > 0)
						writer.print(a.get(i - 1).get(1) + "+" + a.get(i).get(1) + " ");
					else
						writer.print("&" + "+" + a.get(i).get(1) + " ");

					// following feature conjunction
					writer.print("postConj=");
					if (i < len - 1)
						writer.print(a.get(i).get(1) + "+" + a.get(i + 1).get(1) + " ");
					else
						writer.print(a.get(i).get(1) + "+" + "# ");

					// previous state BIO tag
					writer.print("preState=");
					if (i > 0)
						writer.print(a.get(i - 1).get(2) + " ");
					else
						writer.print("& ");
					// current BIO tag
					writer.print(a.get(i).get(2) + "\n");
				}
				a.clear();
				writer.println();
			} else {
				String[] tokens = line.split(delimiterRevised);
				ArrayList<String> list = new ArrayList<String>();
				for (int i = 0; i < tokens.length; i++) {
					list.add(tokens[i]);
					// System.out.println(list.get(i));
				}
				a.add(list);

			}
		}
		reader.close();
		writer.close();
		System.out.println("Training data processed, the output file is "+pw);
	}
}
