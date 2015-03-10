package computingAlgorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import algorithm.Algorithm;
import parser.Counter;
import parser.Parser;
import parser.Sentence;
import parser.Tag;
import parser.ViterbiAlgorithm;
import parser.Word;

public class CrossValidation {
	
	Counter counter;
	
	public CrossValidation(Counter counter) {
		this.counter = counter; 
	}
	
	public List<Sentence> parseFiles(File dir) {
		File directories[] = dir.listFiles();
		List<Sentence> allSentences = new ArrayList<Sentence>();
		Parser parser = new Parser();
		for (File directory : directories) {
			File files[] = directory.listFiles();
			for (File file:files) {				
				String fileName = file.getAbsolutePath();;
				List<Sentence> sentences = parser.parseSentences(fileName); 
				allSentences.addAll(sentences);
			}
		}
		return allSentences;
		
	}
	
	
	public float crossValidation(File directory, Algorithm algorithm) {
		List<Sentence> allSentences = parseFiles(directory);
		int n = allSentences.size();
		int part = n/10;
		int k = 0; 
		float[] accuracyArray = new float[10];
		for (int i=0; i<10; i++) {
			List<Sentence> testing = allSentences.subList(k, k + part);			
			List<Sentence> training = new ArrayList<Sentence>();
			training.addAll(allSentences);
			training.removeAll(testing);
			counter.reset();
			counter.addPart(training);
			//System.out.println("KeySet" +  counter.wordTagCount.keySet().toString());
			int correct = 0;
			int total = 0;
			for (Sentence sentence:testing) {
				Tag[] result = algorithm.applyToSentence(sentence);				
				List<Word> words = sentence.getWords();				
				total += words.size();
				for (int j=0; j<words.size(); j++) {
				//	System.out.println(result[j]);
				//	System.out.println(words.get(j).getTag());
					if (result[j]==words.get(j).getTag()) correct+=1;					
				}
				
			}
			float accuracy = (float) correct/(float) total;
			//System.out.println ("Testing "+i+" accuracy "+ accuracy);
			accuracyArray[i]=accuracy;	
			k = k + part;
		}
		float sum = 0;
		for (int p=0; p<10; p++){
			sum+=accuracyArray[p];			
		}
		return sum/10;		
	}
	
	public float applyAlgorithm(File directory, Algorithm algorithm){
		float averageAccuracy = crossValidation(directory, algorithm);
		return averageAccuracy;
		
	}
	
	
	

}
