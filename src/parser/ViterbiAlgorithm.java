package parser;
import java.util.List;

import algorithm.Algorithm;

public class ViterbiAlgorithm implements Algorithm{

	Counter counter;
	float[][] score;
	int[][] backpointer;
	  //sort out relationships between sentence parsing and counter hashtables;
	
	public ViterbiAlgorithm(Counter counter) {
		this.counter = counter;
	}

	public Tag[] applyToSentence(Sentence sentence) {		
		List<Word> words = sentence.getWords();	
		//System.out.println("New sentence");
		//for (int i=0; i<words.size(); i++) {
		//	System.out.print(words.get(i).getWord() + " ");
		//}
		//System.out.println("\n");
		//words.remove(0);
		int n = words.size();
		Tag[] tags = Tag.values();
		int m = tags.length;
		score = new float[m][n];
		backpointer = new int[m][n];
		for (int i=0; i<m; i++) {
			score[i][0] = counter.doWTProbability(words.get(1).getWord(), tags[i]) * counter.doTRProbability(Tag.START,tags[i]); //deal with probability method;
			//System.out.println(i + " " + counter.doWTProbability(words.get(1).getWord(), tags[i]));
			//System.out.println(i + " " + counter.doTRProbability(Tag.START,tags[i]));
		}
		
		for (int j=1; j<n; j++) {
			for (int i=1; i<m; i++) {
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
				//System.out.println(score[i][j]);
				backpointer[i][j]=max;		
			}
		}
		
		//for (int i=0; i<m; i++) {
		//	for (int j=0; j<n; j++) {
		//		System.out.print(i+ " " + j +  " " + score[i][j] + " ");
		//	}
		//}
		
		
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
		System.out.println("");
		for (int i=0; i<tagResults.length; i++) {
		//	System.out.println(tagResults[i].toString());
		}
		
		return tagResults;
	
	}

	
	
	

	
}

