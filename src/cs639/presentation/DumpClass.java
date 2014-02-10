package cs639.presentation;

import java.lang.reflect.*;

public class DumpClass {

    public static void main(String[] args) {
		
	try {
	    Class<?> c = Class.forName(args[0]);
	    dumpClass(c, 0);
	} catch (Throwable e) {
	    System.err.println(e);
	}
    }
    private static void dumpClass(Class<?> currClass, int depth){
			
	Class<?> m[] = currClass.getDeclaredClasses();
	Method meth[] = currClass.getDeclaredMethods();
		
	indent(depth);	
	System.out.println("class " + currClass.getName() + ", Modifiers: " + Modifier.toString( currClass.getModifiers()));
	for(int i = 0; i < m.length; i++){
	    if (!Modifier.toString(m[i].getModifiers()).contains("private")) {
		dumpClass(m[i], depth + 1);
	    }			
	}
	for(int j = 0; j < meth.length; j++){
	    indent(depth + 1);
	    System.out.println(meth[j].toGenericString());
	}
		
    }
    private static void indent(int s){
	for(int i = 0; i < s; i++)
	    System.out.print(" ");
    }

}
