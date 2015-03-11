package apply;

import parser.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import naiveBayes.NaiveBayes;
import algorithm.Algorithm;
import parser.*;
import computingAlgorithms.CrossValidation;

public class Apply {

	
	public static void main(String[] args) {
		File directory = new File("C:/Users/Laura/Desktop/WSJ-2-12");
		Counter counter = new Counter();
		CrossValidation c = new CrossValidation(counter);
		Algorithm algorithm = new ViterbiAlgorithm(counter);
		Algorithm nbAlgorithm = new NaiveBayes(counter);
		
		double averageAccuracy = c.applyAlgorithm(directory, algorithm);
		System.out.println(averageAccuracy);		
		
	}

}
