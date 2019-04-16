import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class NameTaggerTest2 {

	String pword;
    String ppostag;
    String pchunk;
    String pnametag;
    
    String cword;
    String cpostag;
    String cchunk;
    String cnametag;
    boolean allcapital;
    String cwordliteralstring;
    boolean calldigit;
    
    String nword;
    String npostag;
    String nchunk;
    String nnametag;
    boolean nallcapital;
    String nwordliteralstring;
    boolean nalldigit;

    
    
    public static void main(String[] args) throws IOException {
        File filetoread = new File("FeatureBuilderTestDataset2");
        FileOutputStream fileoutputStream = new FileOutputStream(filetoread);
        BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(fileoutputStream));
        NameTaggerTest2 traindata = new NameTaggerTest2();
        traindata.datavalueinitialise();
        BufferedReader bufferedreader = new BufferedReader(new FileReader(args[0]));
        String linevalue;
        while ((linevalue = bufferedreader.readLine()) != null) {
        	if(linevalue.equals("-DOCSTART-\t-X-\tO\tO")) {
        		bwriter.write(linevalue);
        		bwriter.newLine();
            }else if(linevalue.length() == 0){
            	if(traindata.ppostag =="" && traindata.pchunk == ""){
            		bwriter.write(linevalue);
            		bwriter.newLine();
            		traindata.datavalueinitialise();
            	}else{
            		traindata.npostag = "";
            		traindata.nchunk = "";
            		bwriter.write(traindata.writefile());
            		bwriter.newLine();
            		bwriter.write(linevalue);
            		bwriter.newLine();
                    traindata.datavalueinitialise();
            	}
            }else{
            	if(traindata.ppostag == "" && traindata.pchunk == "") {
                    String[] currentvalues = linevalue.split("\t");
                    traindata.cword = currentvalues[0];
                    traindata.wordliteral(traindata.cword,true);
                    traindata.allwordCapital(traindata.cword, true);
                    traindata.alldigit(traindata.cword, true);
                    traindata.cpostag = currentvalues[1];
                    traindata.cchunk = currentvalues[2];
                    linevalue = bufferedreader.readLine();
                    if (linevalue != null) {
                        if (linevalue.length() != 0) {
                            String[] nextvalues = linevalue.split("\t");
                            traindata.nword = nextvalues[0];
                            traindata.wordliteral(traindata.nword,false);
                            traindata.allwordCapital(traindata.nword,false);
                            traindata.alldigit(traindata.nword, false);
                            traindata.npostag = nextvalues[1];
                            traindata.nchunk = nextvalues[2];
                        }
                        String outputval = traindata.writefile();
                        bwriter.write(outputval);
                        traindata.cwordliteralstring ="";
                        bwriter.newLine();
                        if (linevalue.length() == 0) {
                        	bwriter.write(linevalue);
                        	bwriter.newLine();
                        	traindata.datavalueinitialise();
                        } else {
                        	traindata.setValues();
                        }
                    } else {
                        break;
                    }
                }else {
                    String[] nextvalues = linevalue.split("\t");
                    traindata.nword = nextvalues[0];
                    traindata.wordliteral(traindata.nword,false);
                   traindata.allwordCapital(traindata.nword,false);
                    traindata.alldigit(traindata.nword,false);
                    traindata.npostag = nextvalues[1];
                   traindata.nchunk = nextvalues[2];
                    String output = traindata.writefile();
                    bwriter.write(output);
                    bwriter.newLine();
                    traindata.setValues();
                }
            }
        }
        bwriter.close();
    }
    
    private String writefile() {
		// TODO Auto-generated method stub
    	 return   cword + "\t" 
         +  "pword=" + pword +"\t" 
         + "ppostag=" + ppostag + "\t" 
   	  +  "pnametag=" + "@@" + "\t" 
   	  +  "pchunk=" + pchunk + "\t" 
         + "cpostag=" + cpostag + "\t" 
         + "cchunk=" + cchunk + "\t" 
         + "cwordliteralstring=" + cwordliteralstring + "\t"
         + "calldigit=" + calldigit + "\t"
   	  + "npostag=" + npostag + "\t" 
   	  +  "nchunk=" + nchunk + "\t" 
         + "nwordliteralstring=" + nwordliteralstring + "\t"
         + "nalldigit=" + nalldigit + "\t"
         + "nextNameTag=" + nnametag + "\t"
   	  + "nextWord=" + nword +"\t" 
         + cnametag;
	}

	private void datavalueinitialise() {
		// TODO Auto-generated method stub
		
        pword = "";
		ppostag = "";
		pchunk = "";
		pnametag = "@@";
		
		cword = "";
        cpostag = "";
        cchunk = "";
        cnametag = "";
        nword = "";
        npostag  = "";
        allcapital = true;
        cwordliteralstring = "";
        calldigit = true;
     
        nword = "";
        npostag  = "";
        nchunk  = "";
        nnametag  = "";
        nallcapital = true;
        nwordliteralstring = "";
        nalldigit = true;
	}


    
 
    
    public void setValues() {
    	pword = cword;
    	ppostag = cpostag;
    	pchunk = cchunk;
    	cword = nword;
    	cpostag = npostag;
    	pnametag = cnametag;
    	cnametag = nnametag;
    	cchunk = nchunk;
    	allcapital = nallcapital;
    	cwordliteralstring = nwordliteralstring;
    	calldigit = nalldigit;
    	nwordliteralstring ="";
    	nalldigit  = true;
        
    }
    
    public void alldigit(String cword2, boolean b) {
  		// TODO Auto-generated method stub
  		String cd = "";
  		String nd = "";
      	for (int i = 0; i < cword2.length(); i++) {
      	 if (Character.isDigit(cword2.charAt(i)) && b) {
      		 cd += "D";
           } else if(Character.isDigit(cword2.charAt(i)) && !b){
          	 nd += "D";
           }else if(!Character.isDigit(cword2.charAt(i)) && b){
          	 cd += "N";
           }else{
          	 nd += "N";
           }
      	 
      	 if(cd.contains("N") && b){
      		 calldigit = false;
   		}
   		
   		if(nd.contains("N") && b){
   			nalldigit = false;
   		}
      	}
  		
  	}

  	public void allwordCapital(String cword2, boolean b) {
  		String ac = "";
  		String nc = "";
  		for (int i = 0; i < cword2.length(); i++) {
  		 if (Character.isUpperCase(cword2.charAt(0)) && b) {
  			 ac += "C";
  	        } else if(Character.isUpperCase(cword2.charAt(0)) && !b){
  	        	nc += "C";
  	        }else if(!Character.isUpperCase(cword2.charAt(0)) && b){
  	        	ac += "N";
  	        }else{
  	        	nc += "N";
  	        }
  		}
  		
  		if(ac.contains("N") && b){
  			allcapital = false;
  		}
  		
  		if(nc.contains("N") && b){
  			nallcapital = false;
  		}
  		
  	}

  	public void wordliteral(String cword2, boolean b) {
  		// TODO Auto-generated method stub
  		for (int i = 0; i < cword2.length(); i++) {
              if(Character.isLetter(cword2.charAt(i))  && b) {
                  if (Character.isUpperCase(cword2.charAt(i))) {
                  	cwordliteralstring += "U";
                  } else {
                  	cwordliteralstring += "u";
                  }
              } else if (Character.isDigit(cword2.charAt(i)) &&  b) {
              	cwordliteralstring += "d";
              } else if (!Character.isLetter(cword2.charAt(i)) && !Character.isDigit(cword2.charAt(i)) &&  b){
              	cwordliteralstring +=  cword2.charAt(i);
              }else if(Character.isLetter(cword2.charAt(i))  && !b){
              	if (Character.isUpperCase(cword2.charAt(i))) {
              		nwordliteralstring += "U";
                  } else {
                  	nwordliteralstring += "u";
                  }
              }else if(Character.isDigit(cword2.charAt(i)) &&  !b){
              	nwordliteralstring += "d";
              }else{
              	nwordliteralstring +=  cword2.charAt(i);
              }
          }
  		
  	}
    
    
    
}
