package algorithm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import parser.Counter;
import parser.Parser;
import parser.Sentence;
import parser.Tag;
import parser.Word;

public class CrossValidation {
	// will do cross validation for a chosen algorithm 
	Counter counter;
	
	public CrossValidation(Counter counter) {
		this.counter = counter; 
	}
	
	// takes the directory in and parses all the files. 
	public List<Sentence> parseFiles(File dir) {
		File directories[] = dir.listFiles();
		List<Sentence> allSentences = new ArrayList<Sentence>();
		Parser parser = new Parser();
		for (File directory : directories) {
			File files[] = directory.listFiles();
			for (File file:files) {			// for each file in each directory, it parses the sentences into the required format.
				String fileName = file.getAbsolutePath();;
				List<Sentence> sentences = parser.parseSentences(fileName); 
				allSentences.addAll(sentences);
			}
		}
		return allSentences;
		
	}
	
	//algorithm = Viterbi or Bayes, and type = 0 for having unknown words and 1 for no unknown words. 
	public double crossValidation(File directory, Algorithm algorithm, int type) {
		List<Sentence> allSentences = parseFiles(directory); //parse all the files;
		int n = allSentences.size();
		System.out.println("Size of data - "+ n );
		int part = n/10;
		System.out.println(" part size " + part); 
		int k = 0; 
		double[] accuracyArray = new double[10];
		for (int i=0; i<10; i++) { 
			//cross validation, getting the testing sublist (10th part) and keeping the rest for training.
			List<Sentence> testing = allSentences.subList(k, k + part);			
			List<Sentence> training = new ArrayList<Sentence>();
			training.addAll(allSentences);
			training.removeAll(testing);
			counter.reset(); //new training data, new counter
			counter.addPart(training);
			if (type == 1) counter.addPartTest(testing); // if no unknown words, we add them in, but ignore the bigrams
			int correct = 0;
			int total = 0;
			for (Sentence sentence:testing) {
				Tag[] result = algorithm.applyToSentence(sentence);				
				List<Word> words = sentence.getWords();
				total += words.size();
				for (int j=0; j<words.size(); j++) {
					if (result[j]==words.get(j).getTag()) correct+=1;	//if prediction is right, correct increments
				}
			}
			double accuracy = (double) correct/(double) total; // getting the accuracy
			System.out.println ("Testing "+ i +" accuracy "+ accuracy);
			accuracyArray[i]=accuracy;	
			k = k + part;
		}
		double sum = 0;
		for (int p=0; p<10; p++){
			sum+=accuracyArray[p];			
		}
		return sum/10;	//getting the average accuracy	
	}
	
	public double applyAlgorithm(File directory, Algorithm algorithm, int type){ //applies the chosen algorithm using cross validation; 
		double averageAccuracy = crossValidation(directory, algorithm, type);
		return averageAccuracy;
		
	}
	
	
	

}
