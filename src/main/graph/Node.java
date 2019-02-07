package graph;

import java.util.TreeSet;

/** A Node is a string that represents whatever data the user decides to input
 *
 */
public class Node implements Comparable<Node>{

	private String data;

	// Abstract  Function:
	// A Node n is invalid if n.get_data() == null. 
	// A Node is a string that represents the value or data of this given node
	//
	// Representation Invariant for every Node n:
	//  n.get_data() != null
	
	/**
	 * @param s the string of the data for this node
	 * @effects constructs a new Node with data value s
	 */
	public Node(String s) {
		this.data = s;
		checkRep();
	}	
	
	/**
	 * @return the data of the node
	 */
	public String get_data() {
		return this.data;
	}
	
	
	private void checkRep() {
		if (this.data == null) {
			throw new RuntimeException("A nodes data cannot be null");
		}
	}

	@Override
	public int compareTo(Node arg0) {
		return this.data.compareTo(arg0.get_data());
	}
	
}
