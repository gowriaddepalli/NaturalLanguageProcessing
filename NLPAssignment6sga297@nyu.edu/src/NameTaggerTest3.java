import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class NameTaggerTest3 {

	String previousWord;
    String previousPosTag;
    String previousChunk;
    String previousNameTag;
    
    String currentWord;
    String currentPosTag;
    String currentChunk;
    String currentNameTag;
    String currentCapital;
    String currentWordConstant;

    
    String nextWord;
    String nextPosTag;
    String nextChunk;
    String nextNameTag;
    String nextCapital;
    String nextWordConstant;

    
    public static void main(String[] args) throws IOException {
        File file = new File("FeatureBuilderTest3");
        FileOutputStream fos = new FileOutputStream(file);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        NameTaggerTest3 t = new NameTaggerTest3();
        t.initialisevalue();
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        String line;
        while ((line = br.readLine()) != null) {
        	if(line.equals("-DOCSTART-\t-X-\tO\tO")) {
                bw.write(line);
                bw.newLine();
            }else if(line.length() == 0){
            	if(t.previousPosTag =="" && t.previousChunk == ""){
            		bw.write(line);
                    bw.newLine();
                    t.initialisevalue();
            	}else{
            		t.nextPosTag = "";
                    t.nextChunk = "";
                    bw.write(t.writeToFile());
                    bw.newLine();
                    bw.write(line);
                    bw.newLine();
                    t.initialisevalue();
            	}
            }else{
            	if(t.previousPosTag == "" && t.previousChunk == "") {
                    String[] currentLineSplitValues = line.split("\t");
                    t.currentWord = currentLineSplitValues[0];
                    t.WordConstructGeneration(t.currentWord,true);
                    t.wordIsCapital(t.currentWord, true);
                    t.currentPosTag = currentLineSplitValues[1];
                    t.currentChunk = currentLineSplitValues[2];
                    line = br.readLine();
                    if (line != null) {
                        if (line.length() != 0) {
                            String[] nextLineSpiltValues = line.split("\t");
                            t.nextWord = nextLineSpiltValues[0];
                            t.WordConstructGeneration(t.nextWord,false);
                            t.wordIsCapital(t.nextWord,false);
                            t.nextPosTag = nextLineSpiltValues[1];
                            t.nextChunk = nextLineSpiltValues[2];
                        }
                        String output = t.writeToFile();
                        bw.write(output);
                        t.currentWordConstant ="";
                        bw.newLine();
                        if (line.length() == 0) {
                            bw.write(line);
                            bw.newLine();
                            t.initialisevalue();
                        } else {
                            t.setValues();
                        }
                    } else {
                        break;
                    }
                }else {
                    String[] nextLineSpiltValues = line.split("\t");
                    t.nextWord = nextLineSpiltValues[0];
                    t.WordConstructGeneration(t.nextWord,false);
                    t.wordIsCapital(t.nextWord,false);
                    t.nextPosTag = nextLineSpiltValues[1];
                    t.nextChunk = nextLineSpiltValues[2];
                    String output = t.writeToFile();
                    bw.write(output);
                    bw.newLine();
                    t.setValues();
                }
            }
        }
        bw.close();
    }
    
    public void initialisevalue(){
    	previousWord = "";
        previousPosTag = "";
        previousChunk = "";
        previousNameTag = "@@";
        currentWord = "";
        currentPosTag = "";
        currentChunk = "";
        currentCapital = "";
        currentWordConstant = "";
        nextWord = "";
        nextPosTag  = "";
        nextChunk = "";
        nextCapital = "";
        nextWordConstant = "";
    }
    
    
    public String writeToFile(){
        return   currentWord + "\t" 
                +  "previousWord=" + previousWord +"\t" 
                + "previousPosTag=" + previousPosTag + "\t" 
          	  +  "previousNameTag=" + "@@" + "\t" 
          	  +  "previousChunk=" + previousChunk + "\t" 
                + "currentPosTag=" + currentPosTag + "\t" 
                + "currentCapital=" + currentCapital + "\t" 
                + "currentWordConstant=" + currentWordConstant + "\t"
                + "nextCapital=" + nextCapital + "\t" 
          + "nextWordConstant=" + nextWordConstant + "\t"
          	  + "nextPosTag=" + nextPosTag + "\t" 
          	  + "nextWord=" + nextWord +"\t";
    }
    
    public void setValues() {
    	previousWord = currentWord;
    	previousPosTag = currentPosTag;
    	previousChunk = currentChunk;
    	currentWord = nextWord;
    	currentPosTag = nextPosTag;
    	currentChunk = nextChunk;
    	currentCapital = nextCapital;
    	currentWordConstant = nextWordConstant;
    	nextWordConstant ="";
        
    }
    
    public void wordIsCapital(String word,boolean val) {
        if (Character.isUpperCase(word.charAt(0)) && val) {
        	currentCapital = "C";
        } else if(Character.isUpperCase(word.charAt(0)) && !val){
            nextCapital = "C";
        }else if(!Character.isUpperCase(word.charAt(0)) && val){
        	currentCapital = "N";
        }else{
        	nextCapital = "N";
        }
    }
    

    
    public void WordConstructGeneration(String word,boolean val) {
        for (int i = 0; i < word.length(); i++) {
            if(Character.isLetter(word.charAt(i))  && val) {
                if (Character.isUpperCase(word.charAt(i))) {
                	currentWordConstant += "L";
                } else {
                	currentWordConstant += "l";
                }
            } else if (Character.isDigit(word.charAt(i)) &&  val) {
            	currentWordConstant += "N";
            } else if (!Character.isLetter(word.charAt(i)) && !Character.isDigit(word.charAt(i)) &&  val){
            	currentWordConstant +=  word.charAt(i);
            }else if(Character.isLetter(word.charAt(i))  && !val){
            	if (Character.isUpperCase(word.charAt(i))) {
            		nextWordConstant += "L";
                } else {
                	nextWordConstant += "l";
                }
            }else if(Character.isDigit(word.charAt(i)) &&  !val){
            	nextWordConstant += "N";
            }else{
            	nextWordConstant +=  word.charAt(i);
            }
        }
    }
    
    
}
