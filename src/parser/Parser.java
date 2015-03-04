package parser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class Parser {
	public Sentence parseSentence(String sentence){
		int i = 0;
		Sentence result = new Sentence();
		char currentChar = '~';
		String currentWord = "";
		String currentTag = "";
		boolean readingTag = false; // is true iff we are currently reading a tag for a word
		//this is true immediately after seeing a '/' character until we see a space
		
		while(i < sentence.length()){
			currentChar = sentence.charAt(i++);
			if(!readingTag){
				if(currentChar == '/'){
					readingTag = true;
				}
				else if(currentChar != '[' && currentChar != ' ' && currentChar != ']'){
					currentWord += currentChar;
				}
			}
			else{
				if(currentChar == ' '){
					Tag tag = getTag(currentTag);
					readingTag = false;
					result.addWord(new Word(currentWord, tag));
					currentWord = "";
				}
				else{
					currentTag += currentChar;
				}
			}
		}
		return result;
	}
	
	private Tag getTag(String tagString) {
		Tag result = Tag.CC;
		switch(tagString){
			case "CC":
				result = Tag.CC;
			case "CD":
				result = Tag.CD;
			case "DT":
				result = Tag.DT;
			case "EX":
				result = Tag.EX;
			case "FW":
				result = Tag.FW;
			case "IN":
				result = Tag.IN;
			case "JJ":
				result = Tag.JJ;
			case "JJR": 
				result = Tag.JJR;
			case "JJS":
				result = Tag.JJS;
			case "LS":
				result = Tag.LS;
			case "MD":
				result = Tag.MD;
			case "NN":
				result = Tag.NN;
			case "NNS":
				result = Tag.NNS;
			case "NNP":
				result = Tag.NNP;
			case "NNPS":
				result = Tag.NNPS;
			case "PDT":
				result = Tag.PDT;
			case "POS":
				result = Tag.POS;
			case "PRP":
				result = Tag.PRP;
			case "PRP$":
				result = Tag.PRP$;
			case "RB":
				result = Tag.RB;
			case "RBR":
				result = Tag.RBR;
			case "RBS":
				result = Tag.RBS;
			case "RP":
				result = Tag.RP;
			case "SYM":
				result = Tag.SYM;
			case "TO":
				result = Tag.TO;
			case "UH":
				result = Tag.UH;
			case "VB":
				result = Tag.VB;
			case "VBD":
				result = Tag.VBD;
			case "VBG":
				result = Tag.VBG;
			case "VBN":
				result = Tag.VBP;
			case "VBZ":
				result = Tag.VBZ;
			case "WDT":
				result = Tag.WDT;
			case "WP":
				result = Tag.WP;
			case "WP$":
				result = Tag.WP$;
			case "WRB":
				result = Tag.WRB;
			default:
				result = Tag.PUNCT;
		}
		return result;
	}

	public List<String> splitSentences(String fileName) throws IOException{

		LinkedList<String> results = new LinkedList<String>();
		BufferedReader istream = new BufferedReader(new FileReader(fileName));
		try {
			String current = "";
			String line = "";
            while( (line = istream.readLine()) != null) {
            	if(line == "./. "){
            		results.add(current);
            	}
            	else if(line != "======================================"){
            		current += line;
            	}	
            }
		} catch (FileNotFoundException e) {	
			e.printStackTrace();
		}
		istream.close();
		return results;
	}
	
	public List<Sentence> parseSentences(String fileName){
		List<Sentence> results = new LinkedList<Sentence>();
		List<String> sentenceStrings = new LinkedList<String>();
		try {
			sentenceStrings = splitSentences(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(String sentenceString : sentenceStrings){
			results.add(parseSentence(sentenceString));
		}
		return results;		
	}
}
