package parser;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Sentence implements Serializable {
	private static final long serialVersionUID = 1L;
	List<Word> words;
	
	public Sentence(){
		words = new LinkedList<Word>();
		words.add(new Word(" ", Tag.START));
	}
	
	public void addWord(Word word){
		words.add(word);
		
	}
	
	public List<Word> getWords() {
		return words;
	}
	
	

}
