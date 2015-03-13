package algorithm;
import parser.*; 

public class NaiveBayesAlgorithm implements Algorithm {
	// implements Naive Bayes algorithm
	private Counter counter; 
	
	public NaiveBayesAlgorithm(Counter counter) {
		this.counter = counter;
	}
	
	
	@Override
	public Tag[] applyToSentence(Sentence sentence) {
		Tag[] tags = Tag.values();
		Tag[] result = new Tag[sentence.getWords().size()];
		for(int i = 0; i < sentence.getWords().size(); i++){
			double maxProb = Double.NEGATIVE_INFINITY;
			String word = sentence.getWords().get(i).getWord();
			for(int j = 0; j < tags.length; j++){
				double tagProb = counter.tagProbability(tags[j], word); // gets the probability using the counter and updates the result if necessary 
				if(tagProb > maxProb){
					maxProb = tagProb;
					result[i] = tags[j];
				}
				
			}
		}
		return result;
	}

	
	
}
