// program to sort numbers from an array of ints
// example of use of sort from Java library */
// Example program to sort an array of simple objects by one field */
package examples.sort;

import java.util.Arrays;
import java.util.Comparator;

public class SortEx {

    public static class Pair {
	public Pair(String s1, int i1) { s = s1; i = i1; }
	public String GetS() { return s; }
	public int getI() { return i; }
	public String toString() { return "(" + s + ", " + i + ")"; }

	private String s;
	private int i;
	public static class Foo {
	    public void hi() { System.out.println("hi");}
	}
    };

    // superfluous  method to call JDK sort
    // to give us an example of a <> type for a parameter
    public static void sortPairs(Pair[] a, Comparator<Pair> comp)
    {
	Arrays.sort(a, comp);
    }
    public static void main(String [] args)
    {
	Pair[] pairs = new Pair[4];
	int i;

	pairs[0] = new Pair("A", 10);
	pairs[1] = new Pair("B", 5);
	pairs[2] = new Pair("C", 100);
	pairs[3] = new Pair("D", -1);
	/* sort the pairs by their integer i fields */
	sortPairs(pairs, new OrderByI());
	/* print them out-- */
	for (i=0;i<pairs.length;i++)
	    System.out.print(" " + pairs[i]);
	System.out.println();
    }
    public static class OrderByI implements Comparator<Pair>
    {
	public int compare( Pair p1, Pair p2 )
	{
	    return p1.getI() - p2.getI();
	}
    }
}


    
