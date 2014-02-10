package cs639.jsource;

import java.lang.reflect.*;
import java.util.ArrayList;

public class JavaSource {
	private String sourceName;
	
	public JavaSource(String sourceName) {
		this.sourceName = sourceName;
	}
	
	public StringBuilder toXML(boolean validate,
			boolean schemaValidate) throws ClassNotFoundException {

		StringBuilder sb = new StringBuilder(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

		// depend on different flags to set the corresponded tag
		// eoneil: alternatively, use DOCTYPE either way and use -schema flag of
		// Counter to associate
		// an XML schema with XML when needed for validation
		if (validate) {
			if (schemaValidate) {
				sb
						.append("<source xsi:noNamespaceSchemaLocation=\"javaSource.xsd\" "
								+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
			} else {
				sb.append("<!DOCTYPE source SYSTEM \"javaSource.dtd\">\n");
				sb.append("<source>\n");
			}
		} else {
			sb.append("<source xmlns=\"http://schemas.cs.umb.edu/jsource/\">\n");
		}

		Class<?> c = Class.forName(sourceName);
		// TODO by eoneil:we should check that this class is public, to ensure
		// that its filename is the same
		sb.append(indent(1) + "<sourceName>\n");
		sb.append(indent(2) + c.getSimpleName() + ".java\n");
		sb.append(indent(1) + "</sourceName>\n");
		sb.append(classToXML(c, 1));
		sb.append("</source>\n");
		return sb;
	}

	/**
	 * Render the java class into XML format.
	 * 
	 * @param currClass
	 *            the class to be rendered
	 * @param depth
	 * @return the StringBuilder which contains the corresponded part of XML
	 */
	private StringBuilder classToXML(Class<?> currClass, int depth) {

		StringBuilder sb = indent(depth).append("<jclass>\n");
		sb.append(indent(depth + 1)).append("<className>").append(
				currClass.getName()).append("</className>\n");

		sb.append(indent(depth + 1)).append("<visibility>");
		int mod = currClass.getModifiers();
		if (Modifier.isPublic(mod))
			sb.append("public");
		else if (Modifier.isProtected(mod))
			sb.append("protected");
		else if (Modifier.isPrivate(mod))
			sb.append("private");
		else
			sb.append("package");
		sb.append("</visibility>\n");

		Class<?>[] inner = currClass.getDeclaredClasses();

		sb.append(indent(depth + 1)).append("<innerclasses>\n");

		for (int i = 0; i < inner.length; i++)
			if (Modifier.isPublic(inner[i].getModifiers()))
				sb.append(classToXML(inner[i], depth + 2));
		sb.append(indent(depth + 1)).append("</innerclasses>\n");
		sb.append(indent(depth + 1)).append("<methods>\n");

		Method[] meth = currClass.getDeclaredMethods();
		ArrayList<Method> staticMeth = new ArrayList<Method>();
		ArrayList<Method> nonStaticMeth = new ArrayList<Method>();

		for (int i = 0; i < meth.length; i++) {
			if (Modifier.isStatic(meth[i].getModifiers()))
				staticMeth.add(meth[i]);
			else
				nonStaticMeth.add(meth[i]);
		}

		sb.append(indent(depth + 2)).append("<static>\n");
		for (Method m : staticMeth)
			sb.append(JavaMethod.methodToXML(m, depth + 3));

		sb.append(indent(depth + 2)).append("</static>\n");

		sb.append(indent(depth + 2)).append("<nonstatic>\n");

		for (Method m : nonStaticMeth)
			sb.append(JavaMethod.methodToXML(m, depth + 3));
		sb.append(indent(depth + 2)).append("</nonstatic>\n");

		sb.append(indent(depth + 1)).append("</methods>\n");
		sb.append(indent(depth)).append("</jclass>\n");
		return sb;
	}

	/**
	 * utility method to generate s blanks
	 * 
	 * @param s
	 *            the number of blanks to be generate
	 * @return the StringBuilder that contains the blank
	 */
	public static StringBuilder indent(int s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s; i++)
			sb.append("  ");
		return sb;
	}

}