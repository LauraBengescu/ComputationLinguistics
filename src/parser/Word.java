package parser;

public class Word {
	private Tag tag;
	private String word;
	
	public Word(String word, Tag tag){
		this.tag = tag;
		this.word = word;
	}
	
	public String getWord(){
		return word;
	}
	
	public Tag getTag(){
		return tag;
	}
	
	public void setTag(Tag tag){
		this.tag = tag;
	}

}
