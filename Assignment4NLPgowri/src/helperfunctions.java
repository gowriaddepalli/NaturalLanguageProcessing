import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;

public class helperfunctions {

	public static void main (String[] args) throws IOException {
		if (args.length != 2) {
		    System.err.println("the parser takes two arguments");
		    System.exit(1);
	}
		//if(Integer.parseInt(args[2]) > 5 || Integer.parseInt(args[2])  < 1){
		//	args[2] = 0;
		//}
		String parserfilename = args[0];
		File trainFile = new File(parserfilename);
		List<String> train = Files.readAllLines(trainFile.toPath(), StandardCharsets.UTF_8);
		String testfilename = args[1];
		File testFile = new File(testfilename);
		List<String> test = Files.readAllLines(testFile.toPath(), StandardCharsets.UTF_8);
		Map<Integer,String> tokens = new HashMap<Integer, String>();
		Map<Integer,String> states = new HashMap<Integer, String>();
		List<String> tokensstream = new ArrayList<>();
		List<String> statestream = new ArrayList<>();
		 
		states.put(0, "start");		
		tokens.put(0,"thisisunknownwordfiller");
		int tokencount = 1;
		int statecount = 1;
		for (int i = 0; i < train.size(); i++) {
		    String trainLine = train.get(i).trim();
		    if (trainLine.equals("")) {
		    	continue;
		    }
		    String[] trainvalues = trainLine.split("\t");
		    if (trainvalues.length != 2) {
			System.err.println ("format error at line " + i + ":" + trainvalues);
			System.exit(1);
		    }
		    String trainToken = trainvalues[0];
		    String trainPos = trainvalues[1];
		    if(!tokens.containsValue(trainToken)){
		    	tokens.put(tokencount,trainToken);
		    //	System.out.println(trainToken + " "  + tokencount);
		    	tokencount++;
		    }
		    if(!states.containsValue(trainPos)){
		    	states.put(statecount,trainPos);
		    	statecount++;
		    }
		    tokensstream.add(trainToken);
		    statestream.add(trainPos);
		}
		int[] statenumber = new int[states.size()];
		int statecounter = 0;
		for (Integer state: states.keySet()){
			statenumber[statecounter] = state;
			statecounter++;
		}
	//	System.out.println(statenumber.length);
	//	System.out.println(tokens.size());
	//	System.out.println(states.size());
		Map<String, Integer> tokenfrequency = new HashMap<>();
		Map<String, Integer> statefrequency = new HashMap<>();
		
		
		for (String word : tokensstream) { 
			tokenfrequency.compute(word, (k, v) -> v == null ? 1 : v + 1); 
		}
		
		for (String state : statestream) { 
			statefrequency.compute(state, (k, v) -> v == null ? 1 : v + 1); 
		}
		
		Map<String, Integer> sortedtokenfrequency = tokenfrequency.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new));
		Map<String, Integer> sortedstatefrequency = statefrequency.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new)); 
		//System.out.println(sortedtokenfrequency.toString());
		//System.out.println(sortedstatefrequency.get("NN"));
		//System.out.println(sortedstatefrequency.toString());
		
		double emissionmatrix [][] = new double[states.size()][tokens.size()];
		double transitionmatrix[][] = new double[states.size()][states.size()];
		
		String startindicator  = "start";
		for (int i = 0; i < train.size(); i++) {
		    String trainLine = train.get(i).trim();
		    if (trainLine.equals("")) {
		    	startindicator = "start";
		    	continue;
		    }else{
		    String[] trainvalues = trainLine.split("\t");
		    if (trainvalues.length != 2) {
			System.err.println ("format error at line " + i + ":" + trainvalues);
			System.exit(1);
		    }
		    String trainToken = trainvalues[0];
		    String trainPos = trainvalues[1];
		    int indexofstartindicator  = keyof(states,startindicator);
		    int indexoftrainPos = keyof(states,trainPos);
		  //  System.out.println(indexofstartindicator);
		  //  System.out.println(indexoftrainPos);
		 //   System.out.println(startindicator);
		 //   System.out.println(trainPos);
		    transitionmatrix[indexofstartindicator][indexoftrainPos]++;
		    startindicator = trainPos;
		    }
		}
		
		transitionmatrix = normalisematrix(transitionmatrix);

		
		for (int i = 0; i < train.size(); i++) {
		    String trainLine = train.get(i).trim();
		    if (trainLine.equals("")) {
		    	continue;
		    }else{
		    String[] trainvalues = trainLine.split("\t");
		    if (trainvalues.length != 2) {
			System.err.println ("format error at line " + i + ":" + trainvalues);
			System.exit(1);
		    }
		    String trainToken = trainvalues[0];
		    String trainPos = trainvalues[1];
		    int indexoftrainPos  = keyof(states,trainPos);
		    int indexoftrainToken = keyof(tokens,trainToken);
		    emissionmatrix[indexoftrainPos][indexoftrainToken]++;
		    }
		}
		for (int j = 0; j < emissionmatrix[0].length; j++) {
			emissionmatrix[0][j] = 1.0;
		}  
		emissionmatrix = normalisematrix(emissionmatrix);
		emissionmatrix[keyof(states,"NN")][0] = 1.0;
		List<String> wordsinasentence = new ArrayList<String>();
		PrintWriter writer = new PrintWriter("WSJ_24.pos", "UTF-8");
		    for (int j = 0; j < test.size(); j++) {
			    String testLine = test.get(j).trim();
			    if (!testLine.equals("")) {
			    	wordsinasentence.add(testLine);
			    	continue;
			    }else{
			    	int testvals[] = new int[wordsinasentence.size()];
					for(int i = 0; i < wordsinasentence.size(); i++){
						if(tokens.containsValue(wordsinasentence.get(i))){
							testvals[i] = keyof(tokens, wordsinasentence.get(i));
						}else{
							testvals[i] = 0;
						}
					}
					int[] path = ViterbiAlgorithm.viterbi(transitionmatrix, emissionmatrix, statenumber, testvals);
					for(int i = 0; i< path.length; i++){
						int statekey  = path[i]; 
						writer.println(wordsinasentence.get(i) + "\t" + states.get(statekey));
					}
					writer.println();
					wordsinasentence = new ArrayList<String>();
			    }
			}
		    writer.close();
		}
		
		
	

	private static int keyof(Map<Integer, String> states, String postagval) {
		// TODO Auto-generated method stub
		for (Entry<Integer, String> entry : states.entrySet()) {
			if (entry.getValue().equals(postagval)) {
				return entry.getKey();
			}
		}
		return -1;
	}
	
	public static double[][] normalisematrix(double[][] matrix){
		double[] sumofrows = new double[matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				sumofrows[i] += matrix[i][j];
			} 
		}

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = (double)(matrix[i][j]/sumofrows[i]);
			}
		}
		return matrix;
		
	    }

}
