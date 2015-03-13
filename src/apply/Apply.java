package apply;

import parser.*;

import java.io.File;

import algorithm.Algorithm;
import algorithm.CrossValidation;
import algorithm.NaiveBayesAlgorithm;
import algorithm.ViterbiAlgorithm;

public class Apply {

	
	public static void main(String[] args) {
		File directory = new File("C:/Users/Laura/Desktop/WSJ-2-12");
		Counter counter = new Counter();
		CrossValidation c = new CrossValidation(counter);
		Algorithm vAlgorithm = new ViterbiAlgorithm(counter);
		Algorithm nbAlgorithm = new NaiveBayesAlgorithm(counter);		
		double averageAccuracy = c.applyAlgorithm(directory, vAlgorithm);
		//double averageAccuracyNB = c.applyAlgorithm(directory, nbAlgorithm);
		System.out.println("Viterbi accuracy - " + averageAccuracy);
		//System.out.println("Naive Bayes accuracy - " + averageAccuracyNB);
	}

}
