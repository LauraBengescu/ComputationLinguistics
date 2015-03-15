package parser;

import java.util.HashMap;
import java.util.List;

import naiveBayes.NBFeatures;

public class Counter {
	// storing the statistics and counts for the words, tags, and bigrams and calculate the probabilities 
	
	public HashMap<String, HashMap<Tag,Integer>> wordTagCount;  // Each word has a list of tags that it is associated with and the counts of each association  
	public HashMap<Tag, Integer> tagCounter; //how many times each tag appears 
	public HashMap<Tag, HashMap<Tag, Integer>> tagRelationships; // how many times each sequence of tags appears 
	public NBFeatures naiveBayesFeatures; // for naive bayes 
	
	
	public Counter() {
		reset();
	}
	
	public void reset() {
		wordTagCount = new HashMap<String,HashMap<Tag,Integer>>();
		tagCounter = new HashMap<Tag,Integer>();
		tagRelationships = new HashMap<Tag,HashMap<Tag,Integer>>();
		naiveBayesFeatures = new NBFeatures();
	}
	
	// goes through all the sentences and adds them in the hashtables
	public void addPart(List<Sentence> sentences) {
		for (Sentence sentence:sentences) {
			addInCounter(sentence);
		}
	}
	
	//adds the words and the bigrams in counter 
	public void addInCounter(Sentence sentence) {
		List<Word> words = sentence.getWords();
		int size = words.size();
		for (int i=0; i<size; i++) {
			addWordTag(words.get(i));
			addTag(words.get(i).getTag());
			if (i!=size-1) addTagRelationship(words.get(i).getTag(), words.get(i+1).getTag());
			naiveBayesFeatures.add(words.get(i));
		}
		
	}
	
	//specifically for adding all words when doing cross validation 
	public void addPartTest(List<Sentence> sentences) {
		for (Sentence sentence:sentences) {
			addInCounterTest(sentence);
		}
	}

    //adds all words in counter, but not the bigrams.	
	private void addInCounterTest(Sentence sentence) {
		List<Word> words = sentence.getWords();
		int size = words.size();
		for (int i=0; i<size; i++) {
			addWordTag(words.get(i));
			addTag(words.get(i).getTag());
			naiveBayesFeatures.add(words.get(i));
		}
	}

	//adds the word and corresponding tag to the right hashtable
	public void addWordTag(Word word) {
		String sWord = word.getWord();
		Tag tag = word.getTag();
		if (wordTagCount.containsKey(sWord)) {
			HashMap<Tag, Integer> tagList = wordTagCount.get(sWord);
			
			if (tagList.containsKey(tag))
				tagList.replace(tag,tagList.get(tag)+1);
			else
				tagList.put(tag, 1);
		}
		else {
			HashMap<Tag, Integer> tagList = new HashMap<Tag,Integer>();
			tagList.put(tag, 1);
			wordTagCount.put(sWord, tagList);
		}
		
	}
	
	//add the tag and increment the number of its appearances 
	public void addTag(Tag tag) {
		if (tagCounter.containsKey(tag))
			 tagCounter.replace(tag, tagCounter.get(tag)+1);
		else
			tagCounter.put(tag, 1);
	}
	
	//add tag sequence, tag1 tag2 
	public void addTagRelationship(Tag tag1, Tag tag2) {
		if (tagRelationships.containsKey(tag1)) {
			HashMap<Tag, Integer> tagList = tagRelationships.get(tag1);
			if (tagList.containsKey(tag2))
				tagList.replace(tag2,tagList.get(tag2)+1);
			else
				tagList.put(tag2, 1);
		}
		else {
			HashMap<Tag, Integer> tagList = new HashMap<Tag,Integer>();
			tagList.put(tag2, 1);
			tagRelationships.put(tag1, tagList);
		}
			
	}
	
	//get probability that a word is of a specific tag
	public double doWTProbability(String word, Tag tag) {
		double count = 0;
		double tagCount = 0;
		HashMap<Tag,Integer> tagList = wordTagCount.get(word);	
		double result = 0;
		if (tagList!=null) {
			if (tagList.containsKey(tag)){
				count = (double) tagList.get(tag);
				tagCount = (double) tagCounter.get(tag);					
			}
		}
		int m = wordTagCount.keySet().size();
		result = (double) (Math.log((count+0.001)/(tagCount+0.001*m))); //smoothing if the word is not in 
		return result;
	}
	
	//get probability that tag2 follows tag1
	public double doTRProbability(Tag tag1, Tag tag2) {
		double trCount = 0;
		double tagCount = 0;
		HashMap<Tag,Integer> tagList = tagRelationships.get(tag1);
		if (tagList!=null) {
			if (tagList.containsKey(tag2)) {
				trCount = (double) tagList.get(tag2);
				tagCount = (double) tagCounter.get(tag1);	
			}
		}
		return (double) (Math.log((trCount+0.001)/(tagCount+0.001*Tag.values().length))); // smoothing if the word is not in 
	}
	
	//for Naive Bayes, probability of the tag and word 	
	public double tagProbability(Tag tag, String word){		
		return (double) (naiveBayesFeatures.getTagProbability(word, tag) + doWTProbability(word,tag));
	}

	
	
	
	
	
	
	
	

}
