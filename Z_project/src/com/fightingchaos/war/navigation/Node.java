package com.fightingchaos.war.navigation;

/**
 * A NavigationNode represents an entry in the NavigationHeap. It provides some
 * simple operators to classify it against other NavigationNodes when the heap
 * is sorted.
 * 
 * Portions Copyright (C) Greg Snook, 2000
 * 
 * @author TR
 * 
 */
public class Node implements Comparable<Node> {

	/**
	 * pointer to the cell in question
	 */
	public Cell cell = null;

	/**
	 * (g + h) in A* represents the cost of traveling through this cell
	 */
	public float cost = 0.0f;

	public Node() {
	}

	public Node(Cell c, float costs) {
		cell = c;
		cost = costs;
	}

	// public int compareTo(Node o1, Node o2) {
	// if(o1.cost < (o2.cost))
	// return -1;
	// else if((o1.cost > (o2.cost)))
	// return 1;
	// else
	// return 0;
	// }

	/**
	 * To compare two nodes, we compare the cost or `f' value, which is the sum
	 * of the g and h values defined by A*.
	 * 
	 * @param o
	 *            the Object to be compared.
	 * @return a negative integer, zero, or a positive integer as this object is
	 *         less than, equal to, or greater than the specified object.
	 */
	public int compareTo(Node o) {
		if (this.cost < (o.cost))
			return -1;
		else if ((this.cost > (o.cost)))
			return 1;
		else
			return 0;
	}
}
