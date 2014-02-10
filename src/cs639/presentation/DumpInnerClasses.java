package cs639.presentation;

import java.lang.reflect.*;

public class DumpInnerClasses {

	public static void main(String[] args) {
		try {
			Class<?> c = Class.forName(args[0]);
			Class<?> m[] = c.getDeclaredClasses();

			for (int i = 0; i < m.length; i++) {
				if (!Modifier.toString(m[i].getModifiers()).contains("private")) {
					System.out.println("class " + m[i].getName()
							+ ", Modifiers: "
							+ Modifier.toString(m[i].getModifiers()));
					
				}
			}
		} catch (Throwable e) {
			System.err.println(e);
		}
	}
}
