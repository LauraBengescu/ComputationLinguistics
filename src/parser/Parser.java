package parser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class Parser {
	//parses the sentences using the string sentences from the files. 
	
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
				if(currentChar == ' '){		// means we've reached the next word so see if tag is of recognizable value 		
					try {
					Tag tag = Tag.valueOf(currentTag);					
					readingTag = false;
					result.addWord(new Word(currentWord, tag)); //add word to the sentence		
					currentWord = "";
					currentTag = "";
					}
					catch (IllegalArgumentException e) { 
						// if the tag is not in the list of tags, then this wasn't a right word-tag so start over
						readingTag = false;
						currentWord = "";
						currentTag = "";
						
					}
				}
				else{
					currentTag += currentChar;
				}
			}
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
            	if(line.equals("./. ")) {
            		results.add(current);
            		current = "";
            	}
            	else if(!line.equals("======================================")){ 
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
