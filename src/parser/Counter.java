package parser;

import java.util.HashMap;
import java.util.List;

import naiveBayes.NBFeatures;

public class Counter {
	public HashMap<String, HashMap<Tag,Integer>> wordTagCount;
	public HashMap<Tag, Integer> tagCounter;
	public HashMap<Tag, HashMap<Tag, Integer>> tagRelationships;
	public NBFeatures naiveBayesFeatures;
	
	
	public Counter() {
		reset();
	}
	
	public void reset() {
		wordTagCount = new HashMap<String,HashMap<Tag,Integer>>();
		tagCounter = new HashMap<Tag,Integer>();
		tagRelationships = new HashMap<Tag,HashMap<Tag,Integer>>();
		naiveBayesFeatures = new NBFeatures();
	}
		
	public void addPart(List<Sentence> sentences) {
		for (Sentence sentence:sentences) {
			addInCounter(sentence);
		}
	}
	
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
	
	public void addTag(Tag tag) {
		if (tagCounter.containsKey(tag))
			 tagCounter.replace(tag, tagCounter.get(tag)+1);
		else
			tagCounter.put(tag, 1);
	}
	
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
	public float doWTProbability(String word, Tag tag) {
		float count = 0;
		float tagCount = 0;
		HashMap<Tag,Integer> tagList = wordTagCount.get(word);	
		float result = (float) 0.0001;
		if (tagList!=null) {
			if (tagList.containsKey(tag)){
				count = (float) tagList.get(tag);
				tagCount = (float) tagCounter.get(tag);		
				//System.out.println("count - " + count);		
				//System.out.println("tagcount - " + tagCount);
				result = (count+1)/(tagCount+1);
			}
		}
		int m = Tag.values().length;
		return (float) ((count+1)/(tagCount+m));
		//System.out.println("result " + result ); 
		//return result;
	}
	
	//get probability that tag2 follows tag1
	public float doTRProbability(Tag tag1, Tag tag2) {
		float trCount = 0;
		float tagCount = 0;
		HashMap<Tag,Integer> tagList = tagRelationships.get(tag1);
		if (tagList!=null) {
			if (tagList.containsKey(tag2)) {
				trCount = (float) tagList.get(tag2);
				tagCount = (float) tagCounter.get(tag1);	
			}
		}
		return (float) (trCount+1)/(tagCount+Tag.values().length);
	}
	
	
	//for Naive Bayes
	
	public float tagProbability(Tag tag, String word){		
		return (float) (naiveBayesFeatures.getTagProbability(word, tag) * doWTProbability(word,tag));
	}

	
	
	
	
	
	
	

}
