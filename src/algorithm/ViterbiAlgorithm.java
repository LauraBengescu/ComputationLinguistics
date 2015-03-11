package algorithm;
import java.util.List;

import parser.Counter;
import parser.Sentence;
import parser.Tag;
import parser.Word;

public class ViterbiAlgorithm implements Algorithm{

	Counter counter;
	double[][] score;
	int[][] backpointer;
	
	public ViterbiAlgorithm(Counter counter) {
		this.counter = counter;
	}

	public Tag[] applyToSentence(Sentence sentence) {		
		List<Word> words = sentence.getWords();	
		//System.out.println("New sentence");
		int n = words.size();
		Tag[] tags = Tag.values();
		int m = tags.length;
		score = new double[m][n];
		backpointer = new int[m][n];
		for (int i=0; i<m; i++) {
			score[i][0] = counter.doWTProbability(words.get(0).getWord(), tags[i]) * counter.doTRProbability(Tag.START,tags[i]); 			
		}
		int c = 0;
		for (int j=1; j<n; j++) {
			for (int i=0; i<m; i++) {
				int max = 0;
				double prob = 0;
				double newProb = 0;
				for (int k=0; k<m; k++) {
					newProb = score[k][j-1]*counter.doTRProbability(tags[k],tags[i])*counter.doWTProbability(words.get(j).getWord(),tags[i]);
					if (newProb>prob) {
						prob=newProb;
						max = k;
					}					    
						
				}
				score[i][j]=prob;				
				//if (prob == 0) System.out.println(i + " " + j + " " + score[i][j]);
				backpointer[i][j]=max;		
			}
		}
		//System.out.println(c);
				
		double bestLastTagProb = 0;
		int index = 0;
		for (int i=0; i<m; i++) {
			double newValue = score[i][n-1];
			if (newValue>bestLastTagProb) {
				bestLastTagProb=newValue;
				index = i;
			}
			
		}
		int[] result = new int[n];
		result[n-1]=index;
		for (int j = n-2; j>=0; j--) {
			result[j] = backpointer[result[j+1]][j+1];
		}
		Tag[] tagResults = new Tag[n];
		for (int i=0; i<n; i++) {
			tagResults[i]=tags[result[i]];			
		}
		return tagResults;
	
	}

	
}

