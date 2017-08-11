package provaURL;

import java.net.*;
import java.nio.CharBuffer;
import java.util.StringTokenizer;
import java.io.*;

public class prova {
    public static void main(String[] args) throws Exception {
    	//int lenMax=800;
    	int n=0;
    	String inputLine;
    	int nplat=0;
    	int num = 0;
    	Technique[] p = new Technique[200];
    	
    	for (n=1001 ; n<=1003; n++) {
		    String a = null;
			
			URL cg = new URL("https://attack.mitre.org/wiki/Technique/T"+n);
		    BufferedReader in = new BufferedReader(
		    new InputStreamReader(cg.openStream()));
			
		    
		    /*String casa="casa";
			System.out.println(casa.charAt(3));
			int[] n = new int[30];*/
			/*char[] prima= new char[401]; 
			char[] seconda= new char[401]; */
			
			
		    
		    while ((inputLine = in.readLine()) != null) {
		    	/*if (inputLine.length()>lenMax+70000){
		    		for (int i=0; i<lenMax/2; i++) {
		    			prima[i]=inputLine.charAt(i);
		    		}
		    		for (int i=0; i<lenMax/2; i++) {	
		    			seconda[i]=inputLine.charAt(i+lenMax/2);
		    		}
		    		String s1 = CharBuffer.wrap(prima).toString();
		    		String s2 = CharBuffer.wrap(prima).toString();
		
		    		a+=s1+"\r\n";
		    		a+=s2+"\r\n";
		    	}
		    	else {*/
		    		a+=inputLine+"\r\n";

		    		System.out.println(inputLine);
		    	//}
		    }
		    
		    try{    	       
		    	FileOutputStream prova = new FileOutputStream("prova.txt");
		    	PrintStream scrivi = new PrintStream(prova);
		    	scrivi.println(a);           
		    }	    
		    catch (IOException e) {            
		    	System.out.println("Errore: " + e);
		    	System.exit(1);         
		    }
		    

		    
		    boolean cerca = false;
		    String sottostringa="Platform";
		    int max = a.length() - sottostringa.length();
		    int i;
		    // ricerchiamo la sottostringa ciclando il contenuto di quest'ultima
		    test:
		    for (i = 0; i <= max; i++) {
		      int lung = sottostringa.length();
		      int j = i;
		      int k = 0;
		      while (lung-- != 0) {
		        if (a.charAt(j++) != sottostringa.charAt(k++)) {
		          continue test;
		        }
		      }
		 	
		      cerca = true;
		      break test;
		    }
		    
		    System.out.println(cerca ? "Trovata" : "Non Trovata");
		
		    String code= new String ("</td></tr>");
		    String s3=null;
		    String span=new String("span class");
		    // stampiamo l'output sulla base dell'esito della ricerca		
		    System.out.println("i: "+i);
		    	int j=0;
		    	char[] aaaa= new char[10]; 
		    	for(j=0; j<1000;j++) {
					aaaa[0]=a.charAt(i+20+j);
					aaaa[1]=a.charAt(i+21+j);
					aaaa[2]=a.charAt(i+22+j);
					aaaa[3]=a.charAt(i+23+j);
					aaaa[4]=a.charAt(i+24+j);
					aaaa[5]=a.charAt(i+25+j);
					aaaa[6]=a.charAt(i+26+j);
					aaaa[7]=a.charAt(i+27+j);
					aaaa[8]=a.charAt(i+28+j);
					aaaa[9]=a.charAt(i+29+j);
		        	s3 = CharBuffer.wrap(aaaa).toString();
		        	if(span.equals(s3)) {
		              nplat++;	              
		        	}
		        	
		    		if(code.equals(s3)) {
		    			num=i+20+j;
		        		System.out.println(s3);
		        		System.out.println(num);
		        		break;
		        	}
		    	}
				System.out.println(nplat);     	
				cerca = false;
		        String sottostringa1="<title>";
		        int max1 = a.length() - sottostringa.length();
		        // ricerchiamo la sottostringa ciclando il contenuto di quest'ultima
		        test:
		        for (i = 0; i <= max1; i++) {
		          int lung = sottostringa1.length();
		          j = i;
		          int k = 0;
		          while (lung-- != 0) {
		            if (a.charAt(j++) != sottostringa1.charAt(k++)) {
		              continue test;
		            }
		          }
		     	
		          cerca = true;
		          break test;
		        }
		        System.out.println(cerca ? "Trovata" : "Non Trovata");
		        String st1 = null;
		    	char[] ricercaName= new char[100];
		    	for (int m=0; m<100; m++) {
		    		ricercaName[m]=a.charAt(i+sottostringa1.length()+m);
		    	}
		    	st1 = CharBuffer.wrap(ricercaName).toString();
		    	StringTokenizer st= new StringTokenizer(st1,"-");
		        String tokenNAME = st.nextToken();
		        
		    	p[n-1001] = new Technique(n,tokenNAME);
		    	p[n-1001].addPlatform("aaa");
		    	p[n-1001].addPlatform("aaa");

		    	System.out.println(p[n-1001].getID());
		    	System.out.println(p[n-1001].getName());
		    	
		
		        in.close();               
		}
    	
    	System.out.println(p[2].getPlatform(0));
    	System.out.println(p[2].getPlatforms());

    }
}
