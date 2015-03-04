package parser;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ViterbiAlgorithm {

	Counter counter;
	float[][] score;
	int[][] backpointer;
	  //sort out relationships between sentence parsing and counter hashtables;
	
	File directory = new File("C:/Users/Laura/Desktop/WSJ-2-12");
	public ViterbiAlgorithm(){
		counter = new Counter();
	}
	

	public Tag[] applyToSentence(String sentence) {		
		Sentence parsedSentence = counter.parser.parseSentence(sentence);
		List<Word> words = parsedSentence.getWords();		
		int n = words.size();
		Tag[] tags = Tag.values();
		int m = tags.length;
		score = new float[m][n];
		backpointer = new int[m][n];
		for (int i=0; i<m; i++) {
			//log prob;
			score[i][0] = counter.doWTProbability(words.get(0).getWord(), tags[i]) * counter.doTRProbability(Tag.START,tags[i]); //deal with probability method;
		}
		for (int j=1; j<n; j++) {
			for (int i=0; i<m; j++) {
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

	
	
}

