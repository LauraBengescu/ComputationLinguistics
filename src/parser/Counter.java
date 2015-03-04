package parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Counter {
	public HashMap<String, HashMap<Tag,Integer>> wordTagCount;
	public HashMap<Tag, Integer> tagCounter;
	public HashMap<Tag, HashMap<Tag, Integer>> tagRelationships;
	public Parser parser;
	
	public Counter() {
		wordTagCount = new HashMap<String,HashMap<Tag,Integer>>();
		tagCounter = new HashMap<Tag,Integer>();
		tagRelationships = new HashMap<Tag,HashMap<Tag,Integer>>();
		parser = new Parser();
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
		}
		
		
	}

	public void addWordTag(Word word) {
		if (wordTagCount.containsKey(word.getWord())) {
			HashMap<Tag, Integer> tagList = wordTagCount.get(word);
			if (tagList.containsKey(word.getTag()))
				tagList.replace(word.getTag(),tagList.get(word.getTag())+1);
			else
				tagList.put(word.getTag(), 1);
		}
		else {
			HashMap<Tag, Integer> tagList = new HashMap<Tag,Integer>();
			tagList.put(word.getTag(), 1);
			wordTagCount.put(word.getWord(), tagList);
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
			HashMap<Tag, Integer> tagList = tagRelationships.get(tag2);
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
		return wordTagCount.get(word).get(tag)/tagCounter.get(tag);
	}
	
	//get probability that tag2 follows tag1
	public float doTRProbability(Tag tag1, Tag tag2) {
		//get the list of tags that follow tag1
		HashMap<Tag,Integer> tagList = tagRelationships.get(tag1);
		//find tag2
		Integer count = tagList.get(tag2);
		if (count != null) return count/tagCounter.get(tag1);	
		else return 0; //deal with this;
	}

}
