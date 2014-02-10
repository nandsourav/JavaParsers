// Little XPath processor
// This program creates and runs the parser (DOM) with the 
// namespace-unaware flag, (like sax.Counter with -N)
// Then we don't have to provide a namespace context to the XPath query
// when the xml file itself has a namepace 
// Usage: java TextXPathIgnoreNS foo.xml 
package cs639.xpath;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import java.io.Writer;
import javax.xml.parsers.*;

public class XPathTestIgnoreNS {
  
    public static void parseXML(String path, Writer writer) {
	try {
		
		String xpathString = "//method/name";

		// These steps are from xml.apache.org/xalan-j/xpath_aps.html
		// 1. Instantiate an XPathFactory.
		XPathFactory factory = XPathFactory.newInstance();
  
		// 2. Use the XPathFactory to create a new XPath object
		XPath xpath = factory.newXPath();

		// 3. Compile an XPath string into an XPathExpression
		XPathExpression expression = xpath.compile(xpathString);
  
		// Case of suppressing NS processing: need to
		// talk to parser (DOM) directly: create it first--
		DocumentBuilderFactory domFactory = 
					DocumentBuilderFactory.newInstance();
		// When obtained this way, the domFactory is namespace-unaware
		// to start, so the following is not needed:
		// domFactory.setNamespaceAware(false); // tell parser: no NS

		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document document = builder.parse( new InputSource(path));
  
		// 4a. Evaluate the XPath expression on an input document
		// However, this prints at most one selected node

		//  4b. Evaluate the XPath expression for mult. nodes
		NodeList nodes = 
		    (NodeList)expression.evaluate(document,
						  XPathConstants.NODESET);
		for (int i=0;i<nodes.getLength();i++){
			writer.write("Method " + nodes.item(i).getTextContent() + " : " 
							       + findPath(nodes.item(i)));
			writer.write("\n");
	    }
		writer.close();
	} catch (Exception e) {
	    System.out.println("exception: " + e + e.getCause());
	}
    }
    private static String findPath(Node node) {
	String path = node.getTextContent();
    node = node.getParentNode();
	do {
	    switch(node.getNodeType()) {
	    case Node.ELEMENT_NODE:
	    case Node.TEXT_NODE:
	    	Node flag = node;
	    	do{
	    		if(flag.getNodeName().equalsIgnoreCase("classname"))
	    			path = flag.getTextContent() + "." + path;
	    		flag = flag.getPreviousSibling();
	    	}while(flag!=null);
	    	node = node.getParentNode();
	    	break;	    
	    } 
	}while (node.getNodeType() == Node.ELEMENT_NODE);
	return path;
    }
}
  
