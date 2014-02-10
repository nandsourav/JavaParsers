package cs639.presentation;

import cs639.jsource.*;
import java.io.*;

public class JavaToXML {

	/**
	 * Get the short file name. eg. "examples.sort.SortEx.java" -->
	 * "SortEx.java"
	 * 
	 * @param completeName
	 * @return short name
	 */
	public static String getShortName(String completeName) {
		String shortName = completeName;
		int index = -1;
		if ((index = completeName.lastIndexOf(".")) != -1)
			shortName = completeName.substring(index + 1);
		return shortName;
	}

	private static void printUsage() {
		System.out.println("usage: java JavaToXML [-v] [-s] className\n"
				+ "\t[-v] validate the source file (using .dtd file)\n"
				+ "\t[-v -s] validate the source file using XML schema");
	}

	public static void main(String[] args) {

		if (args.length < 1) {
			printUsage();
			System.exit(0);
		}
		try {
			String className = null;
			boolean validate = false;
			boolean schemaValidate = false;
			for (int i = 0; i < args.length; i++) {
				String arg = args[i];
				if (arg.startsWith("-")) {
					String option = arg.substring(1);
					if (option.equalsIgnoreCase("v")) {
						validate = true;
					} else if (option.equalsIgnoreCase("s")) {
						schemaValidate = true;
					}
				} else {
					className = args[i];
				}
			}
			if (className == null) {
				printUsage();
				System.exit(0);
			}
			// eoneil note: we are risking name clashes by putting all
			// these .xml's in the same directory (should fix)
			String outFileName = getShortName(className) + ".xml";
			// Get UTF-8-encoded output--
			OutputStreamWriter writer0 = new OutputStreamWriter(
					new FileOutputStream(outFileName), "UTF-8");
			Writer writer = new BufferedWriter(writer0);
			// create all the XML--
			JavaSource source = new JavaSource(className);
			try {
				writer.write(source.toXML(validate, schemaValidate)
						.toString());  // convert StringBuilder to String
			} catch (ClassNotFoundException e) {
				System.err.println(e);
			}
			writer.close();
			if (validate) {
				String[] arg = null;
				if (schemaValidate)
					arg = new String[] { "-v", "-s", outFileName };
				else
					arg = new String[] { "-v", outFileName };
				// call Counter to validate the generated xml file
				cs639.validation.Counter.main(arg);
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
