package parser;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViterbiAlgorithm {

	Counter counter;
	float[][] score;
	int[][] backpointer;
	  //sort out relationships between sentence parsing and counter hashtables;
	
	
	public float applyViterbiAlgorithm(File directory){
		float averageAccuracy = crossValidation(directory);
		return averageAccuracy;
		
	}
	

	public Tag[] applyToSentence(Sentence sentence) {		
		List<Word> words = sentence.getWords();		
		int n = words.size();
		Tag[] tags = Tag.values();
		int m = tags.length;
		score = new float[m][n];
		backpointer = new int[m][n];
		for (int i=0; i<m; i++) {
			score[i][0] = counter.doWTProbability(words.get(0).getWord(), tags[i]) * counter.doTRProbability(Tag.START,tags[i]); //deal with probability method;
			//System.out.println(score[i][0]);
		}
		
		for (int j=1; j<n; j++) {
			for (int i=0; i<m; i++) {
				int max = 0;
				float prob = 0;
				for (int k=0; k<m; k++) {
					float newProb = score[k][j-1]*counter.doTRProbability(tags[i],tags[k])*counter.doWTProbability(words.get(j).getWord(),tags[i]);
					if (newProb>prob) {
						prob=newProb;
						max = k;
					}					    
						
				}
				score[i][j]=prob;
				System.out.println(score[i][j]);
				backpointer[i][j]=max;		
			}
		}
		
		float bestLastTagProb = 0;
		int index = 0;
		for (int i=0; i<m; i++) {
			float newValue = score[i][n-1];
			if (newValue>bestLastTagProb) {
				bestLastTagProb=newValue;
				index = i;
			}
						
		}
		int[] result = new int[n];
		result[n-1]=index;
		for (int j = n-2; j>0; j--) {
			result[j] = backpointer[result[j+1]][j+1];
		}
		Tag[] tagResults = new Tag[n];
		for (int i=0; i<n; i++) {
			tagResults[i]=tags[result[i]];			
		}
		
		return tagResults;
	
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
	
	
	public float crossValidation(File directory) {
		
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
			counter = new Counter();
			counter.addPart(training);
			//System.out.println(counter.tagCounter.values());
			int correct = 0;
			int total = 0;
			for (Sentence sentence:testing) {
				Tag[] result = applyToSentence(sentence);				
				List<Word> words = sentence.getWords();				
				total += words.size();
				for (int j=0; j<words.size(); j++) {
					//System.out.println(result[j]);
					//System.out.println(words.get(j).getTag());
					if (result[j]==words.get(j).getTag()) correct+=1;					
				}				
			}
			float accuracy = correct/total;
			accuracyArray[i]=accuracy;	
			k = k + part;
		}
		float sum = 0;
		for (int p=0; p<10; p++){
			sum+=accuracyArray[p];			
		}
		return sum/10;		
	}
	
	
	public static void main(String[] args) {
		File directory = new File("C:/Users/Laura/Desktop/WSJ-2-12");
		ViterbiAlgorithm va = new ViterbiAlgorithm();
		float averageAccuracy = va.applyViterbiAlgorithm(directory);
		System.out.println(averageAccuracy);		
		
	}

	
	
}

