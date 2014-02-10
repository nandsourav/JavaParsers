// Grid.java, for problem problem 2 of hw5
// Modified by Paul Kim
// Cs310- hw5
// Grid game problem (Weiss pg. 301)
// A given grid of cells has some cells filled with spots.
// Two spots belong to a group if they share a common edge.
// Compute the size of a group starting from a certain cell.
import java.io.*;
import java.util.*;

public class Grid {
	public static class Spot {
		private int row;

		private int col;

		public Spot(int i, int j) {
			row = i;
			col = j;
		}

		public boolean equals(Object rhs) {
			if (rhs == null || getClass() != rhs.getClass())
				return false;
			Spot other = (Spot) rhs;
			return other.row == row && other.col == col ? true : false;
		}

		public int hashCode() {
			Integer ii = new Integer(row);
			Integer jj = new Integer(col);
			return ii.hashCode() + jj.hashCode();
		}
	}

	public Grid(boolean[][] ingrid) {
		grid = ingrid;
	}

	// examine cell, add it to spots if occupied, then
	// examine its neighbors
	// done if cell empty or already in spots or outside area
	private void growGroup(int i, int j, Set<Spot> spots) {
		Spot temp = new Spot(i, j);
		if (i < 0 || i >= N || j < 0 || j >= N)
			return;
		if ((grid[i][j] == false) || spots.contains(temp))
			return;
		if (grid[i][j] == true)
			spots.add(temp);

		growGroup(i - 1, j, spots);
		growGroup(i, j - 1, spots);
		growGroup(i + 1, j, spots);
		growGroup(i, j + 1, spots);
	}

	public int sizeOfGroup(int i, int j) {
		Set<Spot> spots = new HashSet<Spot>();
		growGroup(i, j, spots);
		return spots.size();
	}

	public static void main(String[] args) {
		boolean[][] gridData = { { false, true, false }, { false, true, true },
				{ true, true, false } };
		// Note group starting from (2,1) is size 5 (all the trues)
		Grid mygrid = new Grid(gridData);
		System.out.println("Size of Group is: " + mygrid.sizeOfGroup(2, 1)
				+ "\n");
	}

	public final int N = 3; // for little case below

	private boolean[][] grid = null;
}
