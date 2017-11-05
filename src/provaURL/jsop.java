package provaURL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.*;

public class jsop {
	public static void main(String[] args) throws Exception {
		String html = "<html><head><title>First parse</title></head>"
		  + "<body><p>Parsed HTML into a doc.</p></body></html>";
		Document doc = Jsoup.parse(html);
		System.out.println(doc);
	}
}
