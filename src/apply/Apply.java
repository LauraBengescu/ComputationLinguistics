package apply;

import parser.*;

import java.io.File;

import algorithm.Algorithm;
import algorithm.CrossValidation;
import algorithm.NaiveBayesAlgorithm;
import algorithm.ViterbiAlgorithm;

public class Apply {
	//main class, applies both algorithms and gets the results 
	
	public static void main(String[] args) {
		File directory = new File("C:/Users/Laura/Desktop/WSJ-2-12");
		Counter counter = new Counter();
		CrossValidation c = new CrossValidation(counter);
		Algorithm vAlgorithm = new ViterbiAlgorithm(counter);
		Algorithm nbAlgorithm = new NaiveBayesAlgorithm(counter);		
		//accuracy having unknown words
		double averageAccuracyUNK = c.applyAlgorithm(directory, vAlgorithm, 0);
		//accuracy with no unknown words, but unknown bigrams
		double averageAccuracy = c.applyAlgorithm(directory, vAlgorithm, 1);
		//using Naive Bayes 
		double averageAccuracyNB = c.applyAlgorithm(directory, nbAlgorithm, 0);
		System.out.println("Viterbi accuracy - " + averageAccuracy);
		System.out.println("Viterbi accuracy - with unknown words " + averageAccuracyUNK);
		System.out.println("Naive Bayes accuracy - " + averageAccuracyNB);
	}

}
