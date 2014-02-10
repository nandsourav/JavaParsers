package cs639.presentation;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import cs639.xpath.*;
public class JSourceClient1 {

    public static void main(String[] args) {
    	String outFileName = args[1] + "_methods1.txt";
		// Get UTF-8-encoded output--
		OutputStreamWriter writer0;
		Writer writer;
		try {
			writer0 = new OutputStreamWriter(
					new FileOutputStream(outFileName), "UTF-8");
			writer = new BufferedWriter(writer0);
	    	XPathTestIgnoreNS.parseXML(args[0],writer);
		} 
	     catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
}
