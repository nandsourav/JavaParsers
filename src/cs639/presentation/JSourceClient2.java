package cs639.presentation;

import cs639.sax.*;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class JSourceClient2 {


  public static void main(String[] args) {
      
    try {

		String outFileName = args[1] + "_methods2.txt";
		OutputStreamWriter writer0 = new OutputStreamWriter(
				new FileOutputStream(outFileName), "UTF-8");
		Writer writer = new BufferedWriter(writer0);
		new SaxParse(writer,args[0]).parseFile();
		writer.close();
    }
    catch (Exception e) {
      System.err.println(e);
    }
  
  }   
   
} 
