package cs639.sax;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.io.*;
import java.util.*;


public class SaxParse extends DefaultHandler {

  private Stack<String> nodes,content,all;
  Writer w;
  String fileURL;
  public SaxParse(Writer w, String file){
	  this.w=w;
	  fileURL=file;
  }
  
  // Initialize the per-document data structures
  public void startDocument() throws SAXException {
    
    // The stack needs to be reinitialized for each document
    // because an exception might have interrupted parsing of a
    // previous document, leaving an unempty stack.
    nodes = new Stack<String>();
    content = new Stack<String>();
    all = new Stack<String>();
  }
 
  // Initialize the per-element data structures
  public void startElement(String namespaceURI, String localName,
   String qualifiedName, Attributes atts) {
  
	  all.push(localName);
	  if(localName.equalsIgnoreCase("classname")||localName.equalsIgnoreCase("name"))
		  	nodes.push(localName);
  }
  public void characters (char[] ch, int start, int length)
			throws SAXException,EmptyStackException{
	  String qname = "";
	  if(!nodes.isEmpty())
		  if(all.peek().equalsIgnoreCase("classname")||all.peek().equalsIgnoreCase("name")){
			  for(int i=start;i<start+length;i++)
				  qname = qname +ch[i];
			  content.push(qname);
		  }
  }
  
  public void endElement(String namespaceURI, String localName,
   String qualifiedName){
	  String s = "";
	  String del = "";
	  if(!nodes.isEmpty()){
		  if(all.peek().equalsIgnoreCase("name")){
			  for(int idx=0;idx<content.size();idx++){
				  s=s+del+content.get(idx);
				  del = ".";
			  }		  
			  try{
				  w.write("Method "+content.peek()+" : ");
				  w.write(s);
				  w.write("\n");
			  }catch(IOException e){
				  System.err.println(e.getCause());
			  }
			  nodes.pop();
			  content.pop();
		  }
		  if(all.peek().equalsIgnoreCase("jclass")){
			  nodes.pop();
			  content.pop();
		  }
	  }
	  all.pop();
  }

  // Flush and commit the per-document data structures
  public void endDocument() {  
    
  }
    

  public void parseFile() {
      
    try {
      XMLReader parser = XMLReaderFactory.createXMLReader();
      ContentHandler handler = new SaxParse(w,fileURL);
      parser.setContentHandler(handler);
      parser.parse(fileURL);
    }
    catch (Exception e) {
      System.err.println(e);
    }
  
  }   // end parseFile()
   
} // end class
