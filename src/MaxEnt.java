import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.*;
import java.text.*;

import javax.swing.text.html.HTMLDocument.HTMLReader.TagAction;

public class MaxEnt {
	public static void main(String[] args) throws Exception {
        // Training data processing
		String pathRead = "training.txt";
		String pathWrite = "trainingProcessed.txt";
		TrainingDataProcessing t = new TrainingDataProcessing();
		t.readFile(pathRead, pathWrite, " ");
		
        // Test data processing
		TestDataProcessing test = new TestDataProcessing();
		String testFile = "test.txt";
		String testProcessedFile = "testProcessed.txt";
		test.readFile(testFile, testProcessedFile," ");
		System.out.println();
		
        // Train a model
		System.out.println("Training model as following");
		String dataFileName = "trainingProcessed.txt";
		String modelFileName = "Model.txt";
		TrainingModel trainModel = new TrainingModel();
		trainModel.train(dataFileName, modelFileName);
		System.out.println("Training model created, the output file is Model.txt");
		
        // predict by the model
		String resultFileName = "result.txt";
		PredictByModel predict = new PredictByModel();
		predict.predictBasedOnModel(modelFileName, testProcessedFile, resultFileName);
		System.out.println();
		System.out.println("Based on the training model, the predict results for the test data is result.txt");
		
		// predict using Viterbi Search
		String resultViterbi = "resultViterbi.txt";
		PredictByViterbi viterbi = new PredictByViterbi();
		viterbi.predictViterbi(modelFileName, testProcessedFile, resultViterbi);
		
        // evaluation for the simple deterministic decoding
		System.out.println();
		System.out.println("Evaluate the results of the simple deterministic decode. ");
		MaxEnt maxEnt = new MaxEnt();
		maxEnt.tagAccuracy(resultFileName, testFile);
		maxEnt.precisionAndRecall(resultFileName, testFile);
		
		// evaluation for the Viterbi decoding
		System.out.println();
		System.out.println("Evaluate the results of the Viterbi decoding. ");
		maxEnt.tagAccuracy(resultViterbi, testFile);
		maxEnt.precisionAndRecall(resultViterbi, testFile);
	}

	public void tagAccuracy(String resultFile, String correctFile) throws FileNotFoundException {
		Scanner sc1 = new Scanner(new File(resultFile));
		Scanner sc2 = new Scanner(new File(correctFile));
		int totalTags = 0, correctTags = 0;

		while (sc1.hasNextLine() && sc2.hasNextLine()) {
			String line1 = sc1.nextLine();
			String line2 = sc2.nextLine();
			if (line1.trim().length() > 0 && line2.trim().length() > 0) {
				String[] t1 = line1.split(" ");
				String[] t2 = line2.split(" ");
				totalTags++;
				if (t1[t1.length - 1].equals(t2[t2.length - 1]))
					correctTags++;
			}
		}
		sc1.close();
		sc2.close();
		System.out.println("Total tokens = " + totalTags);
		System.out.println("Tokens with correct tag = " + correctTags);
		DecimalFormat df = new DecimalFormat("#.###");
		System.out.println("Tag accuracy = " + df.format((double) correctTags / (double) totalTags));
	}

	public void precisionAndRecall(String resultFile, String correctFile) throws FileNotFoundException {
		int count = 0;
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> test = new ArrayList<String>();
		ArrayList<ChunkRange> checkBag = new ArrayList<ChunkRange>();
		int response = 0, key = 0, correct = 0;
		Scanner sc1 = new Scanner(new File(resultFile));
		Scanner sc2 = new Scanner(new File(correctFile));
		while (sc1.hasNextLine() && sc2.hasNextLine()) {
			String line1 = sc1.nextLine();
			String line2 = sc2.nextLine();
			if (line1.trim().length() == 0) {
				int len = result.size();
				int i = 0;
				while (i < len) {
					if (test.get(i).equals("O")) {
						i++;
						continue;
					}
					if (test.get(i).equals("I") || test.get(i).equals("B")) {
						int j = i + 1;
						while ( j < len && test.get(j).equals("I") ) {
							j++;
						}
						ChunkRange chunk = new ChunkRange(i, j - 1);
						checkBag.add(chunk);
						key++;
						//System.out.println(chunk);
						i = j;
					}
				}

				int x = 0;
				while (x < len) {
					if (result.get(x).equals("O")) {
						x++;
						continue;
					}
					if (result.get(x).equals("I") || result.get(x).equals("B")) {
						int j = x + 1;
						while (j < len && result.get(j).equals("I") ) {
							j++;
						}
						ChunkRange chunk = new ChunkRange(x, j - 1);
						
						//checkBag.contains doesn't work
						for(ChunkRange c : checkBag){
							if(c.equals(chunk)){
								correct++;
								break;		
							}
						}
						response++;
						x = j;
					}
				}
				result.clear();
				test.clear();
				checkBag.clear();
				continue;
			}
			result.add(getTag(line1));
			test.add(getTag(line2));
			
		}
		sc1.close();
		sc2.close();
		DecimalFormat df = new DecimalFormat("#.###");
		double precision = (double) correct / (double) response;
		System.out.println("Precision = " + df.format(precision));
		double recall = (double) correct / (double) key;
		System.out.println("Recall = "+ df.format(recall));
		double F = (double) (2 * precision * recall / (precision + recall));
		System.out.println("F-measure = " + df.format(F));
	}

	private String getTag(String line) {
		String[] tokens = line.split(" ");
		String tag = tokens[tokens.length - 1];
		return tag;
	}

}
