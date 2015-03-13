package naiveBayes;

import java.util.Hashtable;

import parser.Tag;
import parser.Word;

public class NBFeatures {
	private Hashtable<Tag, Integer> numericTagInstances;  //numerals
	private Hashtable<Tag, Integer> capitalisedTagInstances; //given a tag, how many words of that tag are capitalised
	private Hashtable<Tag, Integer> allCapsTagInstances; //given a tag, how many words of that tag are all caps
	private Hashtable<Tag, Integer> endsInSTagInstances; //given a tag, how many words of that tag end in s 
	private Hashtable<Tag, Integer> tagInstances;     // how many times each tag has appeared;
	private Hashtable<String, Integer> wordInstances;  //how many times each word has appeared 
	private Integer wordsTotal = 0;
	
	public NBFeatures(){
		this.initialise();
	}
	
	public void initialise() {
		 this.numericTagInstances = new Hashtable<Tag, Integer>();
		 this.capitalisedTagInstances = new Hashtable<Tag, Integer>();		
		 this.allCapsTagInstances = new Hashtable<Tag, Integer>();	
		 this.endsInSTagInstances = new Hashtable<Tag, Integer>();
		 this.tagInstances = new Hashtable<Tag, Integer>();	
		 this.wordInstances = new Hashtable<String, Integer>();	
		 this.wordsTotal = 0; 
		 Tag[] tags = Tag.values();
		 for (int i =0 ; i<tags.length; i++) {
			 increment(tagInstances,tags[i]);
		 }
	}
	
	// add the word in depending on what feature it has (0/1) 
	public void add(Word word){
		wordsTotal++;
		Tag tag = word.getTag();
		increment(tagInstances, tag);
		String string = word.getWord();
		if(isNumeric(string)){
			increment(numericTagInstances, tag);
		}
		if(isCapitalised(string)){
			increment(capitalisedTagInstances, tag);
		}
		if(isAllCaps(string)){
			increment(allCapsTagInstances, tag);
		}
		if(endsInS(string)){
			increment(endsInSTagInstances, tag);
		}
		Integer wordInstance = wordInstances.get(string);
		if(wordInstance == null) { 
			wordInstance = 0; 
		}
		wordInstances.put(string, ++wordInstance);
	}

	private boolean endsInS(String string) {
		int n = string.length() - 1;
		return string.charAt(n) == 's' || string.charAt(n) == 'S';
	}

	private boolean isAllCaps(String string) {
		boolean isAllCaps = true;
		int pos = 0;
		while(isAllCaps && pos < string.length()){
			isAllCaps = Character.isUpperCase(string.charAt(pos));
			pos++;
		}
		return isAllCaps;
	}

	private boolean isCapitalised(String string) {
		return Character.isUpperCase(string.charAt(0)) && !isAllCaps(string); 
	}

	private boolean isNumeric(String string) {
		int pos = 0;
		while(pos < string.length()){
			if(Character.isDigit(string.charAt(pos))){
				return true;
			}
			pos++;
		}
		return false;
	}

	private void increment(Hashtable<Tag, Integer> hashTable, Tag tag) {
		Integer instances = hashTable.get(tag);
		if(instances == null){
			instances = 0;
		}
		instances += 1;
		hashTable.put(tag, instances);		
	}

	private float numericTagProbability(Tag tag, boolean isNumeric){		
		Integer numericInst = numericTagInstances.get(tag);
		return getProbability(numericInst, isNumeric, tag);		
	}
	
	private float getProbability(Integer instances, boolean needToInvert, Tag tag) {
		if(instances == null){
			instances = 1;
		}
		int tagInstance = tagInstances.get(tag);
		float prob = (float) instances / (float) tagInstance;
		if(!needToInvert){
			prob = 1 - prob;
		}
		return prob;
	}

	private float capitalisedTagProbability(Tag tag, boolean isCapitalised){
		Integer capitalisedInst = capitalisedTagInstances.get(tag);
		return getProbability(capitalisedInst, isCapitalised, tag);
	}
	
	private float allCapsTagProbability(Tag tag, boolean isAllCaps){
		Integer allCapsInst = allCapsTagInstances.get(tag);
		return getProbability(allCapsInst, isAllCaps, tag);
	}
	
	private float endsInSTagProbability(Tag tag, boolean endsInS){
		Integer endsInSInst = endsInSTagInstances.get(tag);
		return getProbability(endsInSInst, endsInS, tag);
	}
		
	// gets the probability of a word, given a tag, using naive Bayes to compute the total probability 
	public float getTagProbability(String word, Tag tag){
		float numericProb, capitalisedProb, allCapsProb, endsInSProb;
		numericProb = numericTagProbability(tag, isNumeric(word));
		capitalisedProb = capitalisedTagProbability(tag, isCapitalised(word));
		allCapsProb = allCapsTagProbability(tag, isAllCaps(word));
		endsInSProb = endsInSTagProbability(tag, endsInS(word));
		float tagProb = (float) tagInstances.get(tag) / (float) wordsTotal;
		float wordProb = wordProbability(word);
		return (float) Math.log(tagProb * numericProb * capitalisedProb * allCapsProb * endsInSProb / wordProb);
	}

	private float wordProbability(String word) {
		Integer instances = wordInstances.get(word);
		if(instances == null) instances = 1;
		return (float) ((float) instances / (float) wordsTotal);
	}
}