package provaURL;


import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.*;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

public class tesi {
	static Statement stu = null;
	static Connection conn = null;
	static int numExample = 0;
    public static void main(String[] args) throws Exception {
    	int n=0;
    	int i=0;
    	String inputLine; 
    	String matrix=null;
    	Technique[] Tecnica = new Technique[200];
    	URL main = new URL("https://attack.mitre.org/wiki/Main_Page");
	    BufferedReader inmain = new BufferedReader(new InputStreamReader(main.openStream()));
	    while ((inputLine = inmain.readLine()) != null) {	
	    	matrix+=inputLine+"\r\n";
	    }
    	for (n=1160; n<1170; n++) {
		    String a = null;
			
			URL cg = new URL("https://attack.mitre.org/wiki/Technique/T"+n);
		    BufferedReader in = new BufferedReader(new InputStreamReader(cg.openStream()));
		    
		    Document doc = Jsoup.connect("https://attack.mitre.org/wiki/Technique/T"+n).get();		//utilizzo la libreria jsop per trovare il titolo
			String title = doc.title();//il titolo trovato contiene anche un - enterpriese che ho bisogno di rimuovere
			int lenTitle= title.length();			
			char cTitle[]=new char[lenTitle-13];
			for (i=0;i<lenTitle-13;i++) {
				cTitle[i]=title.charAt(i);			
			}
			String finalTitle = CharBuffer.wrap(cTitle).toString();
			
			
		    while ((inputLine = in.readLine()) != null) {
		    		a+=inputLine+"\r\n";
		    		//System.out.println(inputLine);//togli
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
	    	Tecnica[n-1001] = new Technique(n,finalTitle); 			//creo gli oggetti tecniche con id e titolo
	    	String CAPEC="CAPEC";
	    	String CAPECSQL1="";
	    	String CAPECSQL2="";
			Element content = doc.getElementById("content");
			Elements links = content.getElementsByTag("a");
			for (Element link : links) {		
			  //System.out.print(link+"\naaaaaaa");
			  String linkHref = link.attr("href");//prende la parte finale del link    
			  String linkText = link.text(); //prende quello che c'è nel link dopo i :
			  //System.out.println(linkHref);
			  //System.out.println(linkText);//posso fare un controllo su tutti questi e trovare l'unico che fa CAPEC...
			  boolean cerca = false;
			    int max = linkText.length() - CAPEC.length();
			    // ricerchiamo la sottostringa ciclando il contenuto di quest'ultima
			    test:
			    for (i = 0; i <= max; i++) {
			      int lung = CAPEC.length();
			      int j = i;
			      int k = 0;
			      while (lung-- != 0) {
			    	
			        if (linkText.charAt(j++) != CAPEC.charAt(k++)) {
			          continue test;
			        }
			      }
			 	
			      cerca = true;
			      break test;
			    }
			    if(cerca==true) {
			    CAPECSQL2="', '"+linkText;
		    	System.out.println(linkText);
		    	CAPECSQL1=" ,CAPECID";
			    }
			}
	    	String NetSQL1="";String SupSQL1="";
	    	String NetSQL2="";String SupSQL2="";
	    	ArrayList<String> RequiresNetwork=ricercaMultipla1(a,"Requires Network");
	    	for (i = 0; i < RequiresNetwork.size(); i++) {
		    	System.out.println(RequiresNetwork.get(i));
		    	NetSQL1=",RequiresNetwork";
		    	NetSQL2="', '"+RequiresNetwork.get(0);
		    }	
	    	ArrayList<String> SupportsRemote=ricercaMultipla(a,"Supports Remote</th><td>");
	    	for (i = 0; i < SupportsRemote.size(); i++) {
		    	System.out.println(SupportsRemote.get(i));
		    	SupSQL1=",SupportsRemote";
		    	StringTokenizer st = new StringTokenizer(SupportsRemote.get(0));
		    	SupSQL2="', '"+st.nextToken();
		    }
	    	
	    	QuerySQLGEN("Insert into technique(Codice, Nome"+NetSQL1+SupSQL1+CAPECSQL1+") Values ("+n+",'"+finalTitle+NetSQL2+SupSQL2+CAPECSQL2+"')");
	    	
	    	String parTactic=new String ("Tactic</th><td>"); 
		    ArrayList<String> vettoreTattichePerTech = ricercaMultipla(a,parTactic);
		    for (i = 0; i < vettoreTattichePerTech.size(); i++) {
		    	System.out.println(vettoreTattichePerTech.get(i));
		    	Tecnica[n-1001].addTactic(vettoreTattichePerTech.get(i));
		    	StringTokenizer st = new StringTokenizer(vettoreTattichePerTech.get(i));	 
		    	String tattica="";
		    	while(st.hasMoreTokens()) {
			    	tattica+=st.nextToken()+" ";
		    	}
		    	System.out.println(tattica);
		    	QuerySQL("tactic",tattica);
		    	QuerySQL2("`mydb`.`att&ckmatrix`",Integer.toString(n),tattica);
		    }
		    String parPermission=new String ("Permissions Required</th><td>");
		    ArrayList<String> PermissionRequired = ricercaMultipla(a,parPermission);
		    for (i = 0; i < PermissionRequired.size(); i++) {
		    	System.out.println(PermissionRequired.get(i));
		    	Tecnica[n-1001].addPermission(PermissionRequired.get(i));
		    	QuerySQL("PermissionRequired",PermissionRequired.get(i));
		    	QuerySQL2("permissionrequired_has_technique",PermissionRequired.get(i),Integer.toString(n));
		    }
		   
		    String parPermission1=new String ("Effective Permissions</th><td>");
		    ArrayList<String> EffectedPermission= ricercaMultipla(a,parPermission1);
		    for (i = 0; i < EffectedPermission.size(); i++) {
		    	System.out.println(EffectedPermission.get(i));
		    	Tecnica[n-1001].addEffPermission(EffectedPermission.get(i));
		    	QuerySQL("EffectedPermissionType",EffectedPermission.get(i));
		    	QuerySQL2("EffectedPermissionType_has_Technique",EffectedPermission.get(i),Integer.toString(n));
		    }
		    String parPlatform=new String ("Platform");
		    ArrayList<String> Platforms = ricercaMultipla1(a,parPlatform);
		    for (i = 0; i < Platforms.size(); i++) {
		    	System.out.println(Platforms.get(i));
		        Tecnica[n-1001].addPlatform(Platforms.get(i));
		        QuerySQL("Platform",Platforms.get(i));
		    	QuerySQL2("technique_has_platform",Integer.toString(n),Platforms.get(i));		        
		    }
		    String parDataSources=new String ("Data Sources");
		    ArrayList<String> DataSources = ricercaMultipla1(a,parDataSources);
		    for (i = 0; i < DataSources.size(); i++) {
		    	System.out.println(DataSources.get(i));
		    	QuerySQL("DataSources",DataSources.get(i));
		    	QuerySQL2("technique_has_DataSources",Integer.toString(n),DataSources.get(i));	
		    }
		    String parDefenseBypassed=new String ("Defense Bypassed");
		    ArrayList<String> DefenseBypassed = ricercaMultipla1(a,parDefenseBypassed);
		    for (i = 0; i < DefenseBypassed.size(); i++) {
		    	System.out.println(DefenseBypassed.get(i));
		    	QuerySQL("DefenseBypassed",DefenseBypassed.get(i));
		    	QuerySQL2("DefenseBypassed_has_technique",DefenseBypassed.get(i),Integer.toString(n));	 
		    }
		    String parContributors=new String ("Contributors"); 
		    ArrayList<String> Contributors = ricercaMultipla1(a,parContributors);
		    for (i = 0; i < Contributors.size(); i++) {
		    	System.out.println(Contributors.get(i));
		    	QuerySQL("Contributors",Contributors.get(i));
		    	QuerySQL2("Contributors_has_technique",Contributors.get(i),Integer.toString(n));	 
		    }
		    /*String parSupportsRemote=new String ("Supports Remote</th><td>"); //devo ancora metterlo nel DB HAHAHAHAH; si ma dovrebbe essere un boolean
		    ArrayList<String> SupportsRemote = ricercaMultipla(a,parSupportsRemote);
		    for (i = 0; i < SupportsRemote.size(); i++) {
		    	System.out.println(SupportsRemote.get(i));
		    	QuerySQL("SupportsRemote",SupportsRemote.get(i));
		    	QuerySQL2("technique_has_SupportsRemote",Integer.toString(n),SupportsRemote.get(i));	
		    }*/
		    String parSystemRequirements=new String ("System Requirements</th><td>\r\n" + //questo pure...
		    		"<p>");//non mi da quelli che sono link 
		    ArrayList<String> SystemRequirements = ricercaMultipla(a,parSystemRequirements);
		    for (i = 0; i < SystemRequirements.size(); i++) {
		    	System.out.println(SystemRequirements.get(i));
		    	QuerySQL("SystemRequirements",SystemRequirements.get(i));
		    	QuerySQL2("technique_has_SystemRequirements",Integer.toString(n),SystemRequirements.get(i));
		    }
		    try {
			    Example[] EX = new Example[20];
			    EX=ricercaEsempi(a);
			    for(int q=0;q<numExample;q++) {
			    	QuerySQLGEN("Insert into Example values('"+EX[q].getName()+"','"+EX[q].getType()+"','"+EX[q].getID()+"')");
			    	QuerySQLGEN("Insert into technique_has_example values('"+n+"','"+EX[q].getName()+"')");
			    }
			    numExample=0;
		    }catch(Exception e) {}
		    in.close();           	        		       	       
	   	}
    }
	public static ArrayList<String> ricerca (String gen, String par) {
    	int i=0;
    	ArrayList<String> nomeCaratteristica = new ArrayList<String>();
    	boolean cerca = false;
	    for(int l=0;l<gen.length()-par.length();l++) {
		    test:
		    for (i = l; i <= (gen.length()-par.length()); i++) {
		      int lung = par.length();
		      int j = i;
		      int k = 0;
		      while (lung-- != 0) {
		        if (gen.charAt(j++) != par.charAt(k++)) {
		          continue test;
		        }
		      }
		      cerca = true;
		      break test;
		    }
		    String st1 = null;
			char[] ricerca= new char[100];
			for (int m=0; m<100; m++) {
				if (i+par.length()+m < gen.length())
					ricerca[m]=gen.charAt(i+par.length()+m);
			}
			st1 = CharBuffer.wrap(ricerca).toString();
			StringTokenizer st= new StringTokenizer(st1,"\"");
			nomeCaratteristica.add(st.nextToken());
			l=i;
		}
		return nomeCaratteristica;
    }
    public static ArrayList<String> ricercaMultipla1(String gen,String par){
    	ArrayList<String> nomeCaratteristica = new ArrayList<String>();
    	boolean cerca = false;
	    int max = gen.length() - par.length();
	    int i=0;
	    // ricerchiamo la sottostringa ciclando il contenuto di quest'ultima
	    test:
	    for (i = 0; i <= max; i++) {
	      int lung = par.length();
	      int j = i;
	      int k = 0;
	      while (lung-- != 0) {
	    	
	        if (gen.charAt(j++) != par.charAt(k++)) {
	          continue test;
	        }
	      }
	 	
	      cerca = true;
	      break test;
	    }
	    if(cerca==true) {
		    //System.out.println(cerca ? "Trovata" : "Non Trovata"); togli
		    String s3=null;
		    String span=new String("span class");
		    // stampiamo l'output sulla base dell'esito della ricerca		
	    
	    	int j=0;
	    	char[] ricercaP= new char[10]; 
	    	String out=new String ("</span></t");
	    	for(j=0; j<700;j++) {
	    		for(int m=0; m<10; m++) {
		    		ricercaP[m]=gen.charAt(i+m+j);
	    		}
	    		
	    		s3= CharBuffer.wrap(ricercaP).toString();
	    		if(out.equals(s3)) {
	    			break;
	    		}
	        	if(span.equals(s3)) {
			    	char[] ricercaPl= new char[59]; 
	        		for(int m=20; m<79; m++) {
			    		ricercaPl[m-20]=gen.charAt(i+m+j);
		    		}
	        		s3= CharBuffer.wrap(ricercaPl).toString();    
	        		StringTokenizer st2= new StringTokenizer(s3,"<");
			        String name = st2.nextToken();		
			        nomeCaratteristica.add(name);
	        	}
	    	}
	   	}	

		return nomeCaratteristica;
    }
    public static ArrayList<String> ricercaMultipla (String gen,String par) {
    	int i=0;
    	ArrayList<String> nomeCaratteristica = new ArrayList<String>();
    	String par1=new String (", ");
    	boolean cerca = false;
	    
		test:
	    for (i = 0; i <= (gen.length()-par.length()); i++) {
	      int lung = par.length();
	      int j = i;
	      int k = 0;
	      while (lung-- != 0) {
	        if (gen.charAt(j++) != par.charAt(k++)) {
	          continue test;
	        }
	      }
	      cerca = true;
	      break test;
	    }
	
	    //System.out.println(cerca ? "Trovata" : "Non Trovata");
    	if(cerca==true) {
		    String s3=null;
		   	char[] ricerca = new char[100];
		   	for(int m=0; m<100; m++) {
		   		ricerca[m]=gen.charAt(i+par.length()+m);//i+par... mi da l'indice della prima lettere dalla prima tattica
			}
		   	s3= CharBuffer.wrap(ricerca).toString();
		
		   	StringTokenizer st= new StringTokenizer(s3,"<");
		   	StringTokenizer st1= new StringTokenizer(st.nextToken(),",");
		    String prima = st1.nextToken();
			nomeCaratteristica.add(prima);
			for(int k=0; k<100; k++) {
				char[] ricercapar=new char[2];
				for(int m=0; m<2; m++) {
			   		ricercapar[m]=gen.charAt(i+par.length()+m+k);//
		   		}
				String a= CharBuffer.wrap(ricercapar).toString();
				if(a.equals(par1)) {
					for(int m=2; m<30; m++) {
				   		ricerca[m-2]=gen.charAt(i+par.length()+m+k);
					}
					s3= CharBuffer.wrap(ricerca).toString();
			   		StringTokenizer st0= new StringTokenizer(s3,",");
			        String add = st0.nextToken();
			   		StringTokenizer st2= new StringTokenizer(add,"<");
			        add = st2.nextToken();
					nomeCaratteristica.add(add);
				}
			}
    	}
	return nomeCaratteristica;
    }
    public static Example[] ricercaEsempi (String gen){
    	ArrayList<String> Esempi = new ArrayList<String>();
    	Example[] EX = new Example[20];
    	char[] newhtml = new char [1000];
    	String par = new String ("\"Examples\"");
    	String par1 = new String("\"Mitigation\"");
    	String par2 = new String("\"Detection\"");
    	String html=null;
    	int i=0; //futura posizione di Examples
    	boolean cerca=false;
    	test:
    	    for (i = 0; i <= (gen.length()-par.length()); i++) {
    	      int lung = par.length();
    	      int j = i;
    	      int k = 0;
    	      while (lung-- != 0) {
    	        if (gen.charAt(j++) != par.charAt(k++)) {
    	          continue test;
    	        }
    	      }
    	      cerca = true;
    	      break test;
    	    }
    	cerca=false;
    	int p=0; //futura posizione di Mitigation
    	test:
    	    for (p = 0; p <= (gen.length()-par1.length()); p++) {
    	      int lung = par1.length();
    	      int j = p;
    	      int k = 0;
    	      while (lung-- != 0) {
    	        if (gen.charAt(j++) != par1.charAt(k++)) {
    	          continue test;
    	        }
    	      }
    	      cerca = true;
    	      break test;
    	    }
    	if(!cerca) {   		
        	test:
        	    for (p = 0; p <= (gen.length()-par2.length()); p++) {
        	      int lung = par2.length();
        	      int j = p;
        	      int k = 0;
        	      while (lung-- != 0) {
        	        if (gen.charAt(j++) != par2.charAt(k++)) {
        	          continue test;
        	        }
        	      }
        	      cerca = true;
        	      break test;
        	    }
    	}
    	/*if(cerca==true){
    		int q=i;
    		for(int n=0; n<1000;n++) {
	    		newhtml[n]=gen.charAt(n+q+par.length());
	        	html= CharBuffer.wrap(newhtml).toString();
    		test:
        	    for (i = 0; i <= (html.length()-par1.length()); i++) {
        	      int lung = par1.length();
        	      int j = i;
        	      int k = 0;
        	      while (lung-- != 0) {
        	        if (html.charAt(j++) != par1.charAt(k++)) {
        	          continue test;
        	        }
        	      }
        	      cerca = false;
        	      break test;
        	    } 
	        if(cerca==false) break;
    		}
    	}*/
    	if((p-i)<7000) { //devo inserire questo controllo, se la stringa da analizzare è troppo grande jsoup ha problemi
    	html = gen.substring(i,p);
	    	System.out.println(html);
	    	Document doc = Jsoup.parse(html);
			Elements links = doc.getElementsByTag("a");
			for (Element link : links) {		//tra i link mi escono gli esempi, mi puo essere utile 
				  //System.out.print(link);
				  String linkHref = link.attr("href");//prende la parte finale del link     (mi potrebbe essere utile solo per dire le tecniche ma per il resto no..)
				  String linkText = link.text(); //prende quello che c'è nel link dopo i :
				  //System.out.println(linkHref);//String ok = linkHref.replace("/", ok); togli
				  //System.out.println(linkText); togli
				  try {
					    Integer.parseInt(linkText);
				  }catch(Exception e){
					  Esempi.add(linkText);
					  //System.out.println("Ho aggiunto "+linkText); togli
					  String IDExample = linkHref.substring(linkHref.lastIndexOf('/')+1);
					  //System.out.println(IDExample); togli
					  String type = linkHref.substring(linkHref.indexOf("/")+6);
					  StringTokenizer ExampleToken= new StringTokenizer(type,"/");
					  String TypeExample = ExampleToken.nextToken();
					  //System.out.println(TypeExample); togli
					  EX[numExample] = new Example(IDExample,linkText,TypeExample);
					  numExample++;
				 }
			}
    	}
		p=0;
		i=0;
		return EX;
    }
    static void QuerySQLGEN (String Query) throws ClassNotFoundException {
      	 try {
      		 	String user = "root";
      	    	String pwd = "root";
   		    	Class.forName("com.mysql.jdbc.Driver");
   		    	conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false",user,pwd);			    
   		    	stu = conn.createStatement();
   		    	String sql = Query;
   		    	System.out.println(sql);
   		    	stu.execute(sql);
   	    	} catch (SQLException ex) {
   		    }
      }
    static void QuerySQL (String Table, String var) throws ClassNotFoundException {
    	try {
		 	String user = "root";
	    	String pwd = "root";
	    	Class.forName("com.mysql.jdbc.Driver");
	    	conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false",user,pwd);			    
	    	stu = conn.createStatement();
	    	String sql = "Insert into "+Table+" values('"+var+"')";
	    	System.out.println(sql);
	    	stu.execute(sql);
    	}catch (SQLException ex) {
    	}
    }
	static void QuerySQL2 (String Table, String var, String var2) throws ClassNotFoundException {
	 	try {
		 	String user = "root";
	    	String pwd = "root";
	    	Class.forName("com.mysql.jdbc.Driver");
	    	conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false",user,pwd);			    
	    	stu = conn.createStatement();
	    	String sql = "Insert into "+Table+" values('"+var+"','"+var2+"')";
	    	System.out.println(sql);
	    	stu.execute(sql);
	    } catch (SQLException ex) {
	    }
	}
}
