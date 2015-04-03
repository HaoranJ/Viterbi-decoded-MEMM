import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class TestDataProcessing {
	private Scanner reader;
	private PrintWriter writer;
	private ArrayList<ArrayList<String>> a;

	public TestDataProcessing() {
		a = new ArrayList<ArrayList<String>>();
	}

	public void readFile(String pr, String pw, String delimiter) throws Exception {
		writer = new PrintWriter(pw);
		reader = new Scanner(new File(pr));
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
						writer.print(a.get(i).get(1) + "+" + a.get(i + 1).get(1) + "\n");
					else
						writer.print(a.get(i).get(1) + "+" + "# \n");
				}
				writer.println();
				a.clear();
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
		System.out.println("Test data processed, the output file is "+pw);
	}
}
