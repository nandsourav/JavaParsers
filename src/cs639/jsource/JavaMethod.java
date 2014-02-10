package cs639.jsource;
import java.lang.reflect.*;

public class JavaMethod{
	/**
	 * Render the method to XML format
	 * @param m the method
	 * @param depth
	 * @return the StringBuilder that contains the corresponded part of XML
	 */
	public static StringBuilder methodToXML(Method m, int depth){
		StringBuilder sb = JavaSource.indent(depth).append("<method>\n");
		sb.append(JavaSource.indent(depth+1)).append("<name>"+m.getName()+"</name>\n");
		sb.append(JavaSource.indent(depth+1)).append("<visibility>");
		int mod = m.getModifiers();
		if(Modifier.isPublic(mod))
			sb.append("public");
		else if(Modifier.isPrivate(mod))
			sb.append("private");
		else if(Modifier.isProtected(mod))
			sb.append("protected");
		else
			sb.append("package");
		sb.append("</visibility>\n");
		sb.append(JavaSource.indent(depth+1)).append("<params>\n");
		
		for(Type t : m.getGenericParameterTypes())
			sb.append(paramToXML(t,depth+2));
		sb.append(JavaSource.indent(depth+1)).append("</params>\n");
		sb.append(JavaSource.indent(depth)).append("</method>\n");
		return sb;
	}
	/**
	 * Render the param to XML format
	 * @param param the param
	 * @param depth
	 * @return the StringBuilder which contains the corresponded part of XML
	 */
	private static StringBuilder paramToXML(Type param, int depth){
		StringBuilder sb = JavaSource.indent(depth).append("<param>\n");
		sb.append(JavaSource.indent(depth+1)).append("<paramType>");
		StringBuilder sb1 = new StringBuilder();
		typeString(sb1, param);
		//typeString0(sb1, param);
		String strParam = sb1.toString();
		strParam = strParam.replaceAll("<", "&lt;");
		strParam = strParam.replaceAll(">", "&gt;");
		sb.append(strParam);
		sb.append("</paramType>\n");
		sb.append(JavaSource.indent(depth)).append("</param>\n");
		return sb;
	}
	
	// The simple experimentally determined method
	// to create a type string like Set<Grid$Spot> or String[]
	// or int[][] or Set<int[]>
	private static void typeString0(StringBuilder sb, Type param) {
		// Although in general it's not great to use toString to
		// get information about an object, but this is a tricky situation
		// we do what we have to do....
		// This may need work, but handles given examples
		String strParam = param.toString();
		strParam = strParam.replace("class ", "");
		strParam = strParam.replace("interface ", "");
		int count = 0;
		// count and remove brackets before [L--
		while (strParam.contains("[[L")) { // multi-dim array type 
			strParam = strParam.replaceFirst("\\[", "");  //needs regex
			count++; // count dimension of array
		}
		// here with at most [L
		if (strParam.contains("[L")) {
			strParam = strParam.replace("[L","");
			count++;
		}
		String brackets = "";
		for (int i=0; i< count; i++) {
			brackets += "[]";  //add []'s for dimensions
		}
		strParam = strParam.replace(";", brackets);
		sb.append(strParam);
	}
	
	
	// More serious attempt at representing Java types
	// A Java type can be represented fully by a Class object
	// (this includes arrays and primitive types, i.e.,
	// all the types known to the JVM)
	// unless it is a parametrized object type (non-wildcarded) or
	// a wildcard parametized type or type variable or ...
	// These generics-related cases are handled by the
	// various sub-interfaces of Type: ParametrizedType,
	// WildcardType, etc, incl. the mysterious GenericArrayType.
	// If it's a parametrized type, it has type parameters
	// which themselves may be parametrized.
	// Thus we need recursion to run through all of them
	// Similarly, an array type may have components themselves
	// arrays.
	// Note this doesn't handle wildcard and GenericArrayType types 
	// completely, instead, just uses their toString representation
	// For a related useful method, see getClass(Type), to get the underlying
	// class for a type, at http://www.artima.com/weblogs/viewpost.jsp?thread=208860
	private static void typeString(StringBuilder sb, Type param) {
		if (param instanceof ParameterizedType) {
			assert(!(param instanceof Class));
			ParameterizedType pType = (ParameterizedType) param;
			typeString(sb, pType.getRawType());
			Type[] parameters = ((ParameterizedType) param)
					.getActualTypeArguments();
			sb.append("<");
			for (int i = 0; i < parameters.length; i++) {
				typeString(sb, parameters[i]);
				if (i < parameters.length - 1)
					sb.append(",");
			}
			sb.append(">");
		} else if (param instanceof Class) { // this includes interfaces
			Class<?> c = (Class<?>) param;   // and primitive types
			if (c.isArray()) { // array type 
				Type t = c.getComponentType();  // can also be an array
				typeString(sb, t);
				sb.append("[]");
			} else sb.append(c.getName());
		} else if (param instanceof WildcardType) {	
			sb.append(param); // wildcard types, should explore further
		} else if (param instanceof GenericArrayType){   
			sb.append("X").append(param); // mysterious case
		} else if (param instanceof TypeVariable<?>) {
			sb.append(((TypeVariable<?>)param).getName()); 
		} else assert(false);  // shouldn't get here
	}
	/**Unit test
	 * @param args
	 */
	public static void main(String[] args){
		try {
			java.util.Date[][] x = new java.util.Date[2][2];
			Class<?> c0 = x.getClass();
			StringBuilder sb0 = new StringBuilder();
			System.out.println("toString: "+ c0);
			typeString0(sb0, c0);
			System.out.println("2d array type: "+ sb0);
			StringBuilder sb1 = new StringBuilder();
			java.util.Set<Integer> ints = new java.util.HashSet<Integer>();
			typeString(sb1, ints.getClass());
			System.out.println("HashSet<Integer> type: "+ sb1);
			
			Class<?> c = Class.forName("examples.sort.SortEx");//"java.util.Set");
			Method m[] = c.getDeclaredMethods();
			
			for (int i = 0; i < m.length; i++) {
				System.out.println(methodToXML(m[i], 0));
				
				for(Type t : m[i].getGenericParameterTypes()) {
					StringBuilder sb = new StringBuilder();	
					typeString(sb, t);
					System.out.println("type: "+ sb);
				}
			}
			
		
		} catch (Throwable e) {
			System.err.println(e);
		}
	}
}