package naiveBayes;


import java.util.List;

//import main.Tagger;

import parser.*;
import algorithm.*; 

public class NaiveBayes implements Algorithm {
	
	private Counter counter; 
	
	public NaiveBayes(Counter counter) {
		this.counter = counter;
	}
	
	
	@Override
	public Tag[] applyToSentence(Sentence sentence) {
		Tag[] tags = Tag.values();
		Tag[] result = new Tag[sentence.getWords().size()];
		for(int i = 0; i < sentence.getWords().size(); i++){
			double maxProb = 0.0;
			String word = sentence.getWords().get(i).getWord();
			for(int j = 0; j < tags.length; j++){
				double tagProb = counter.tagProbability(tags[j], word);
				if(tagProb > maxProb){
					maxProb = tagProb;
					result[i] = tags[j];
				}
				
			}
		}
		return result;
	}

	
	
}
