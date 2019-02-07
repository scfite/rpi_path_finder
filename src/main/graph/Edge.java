package graph;

/** An edge represents a directed connection from node a to node b. Each edge has a label.
 */
public class Edge<E extends Comparable<E>, N extends Comparable<N>> implements Comparable<Edge<E,N>> {
	
	private N a; // Origin node
	private N b; // Destination node
	private E label;
	
	// Abstract Function:
	// An edge is invalid if label == null or if node a or b is not in graph g. An edge in a directed graph is
	// a directed path between two nodes. The node from which the edge originates
	// is called the parent node, and the node that is the destination of the edge is 
	// called a child of the node from which the edge originated.
	//
	// Representation Invariant for every edge e in graph g:
	// e.get_label() != null &&
	// e.get_start() != null &&
	// e.get_end() != null 
	
	/**
	 * @param a2 the parent node, b the child node, l the label for the edge
	 * @requires graph.get_node(a) == true && graph.get_node(b) == true && l != null
	 * @effects creates a new edge from node a to node b with label l
	 */
	public Edge(N a2, N b2, E l) {
		//if the label is valid
		if (l != null) {
			this.a = a2;
			this.b = b2;
			this.label = l;
			checkRep();
		} else {
			throw new RuntimeException("Label cannot be null");
		}
	}

	/**
	 * @param e, the edge to be copied
	 * @requires graph.get_node(e.get_start()) == true && graph.get_node(e.get_end()) == true && e.get_label != null
	 * @effects creates a new edge from node e.get_start() to node e.get_end() with label e.get_label
	 */	
	public Edge(Edge<? extends E,? extends N> e) {
		//if the label is valid
		if (e.get_label() != null) {
			this.a = e.get_start();
			this.b = e.get_end();
			this.label = e.get_label();
			checkRep();
		} else {
			throw new RuntimeException("Label cannot be null or empty");
		} 
	}
	
	//ACCESSORS
	/**
	 * @return the node a from which this edge originates from 
	 */
	public N get_start() {
		return a;
	}
	
	/**
	 * @return the node b from which this edge ends
	 */
	public N get_end() {
		return b;
	}
	
	/**
	 * @return the label for this edge
	 */
	public E get_label() {
		return label;
	}
	
	/**
	 * @param l the new label for this edge 
	 * @modifies the label variable of this edge
	 */
	public void set_label(E l) {
		this.label = l;
	}
	
	/**
	 * @return A new edge with the start and end node reversed 
	 * used for implementing undirected graphs
	 */
	public Edge<E, N> reverse(){
		return new Edge<E,N>(this.get_end(),this.get_start(),this.get_label());
	}

	//checks if the representation invariant holds
	// throws a RuntimeException if it is violated
	private void checkRep() {
		if (this.a == null || this.b == null || this.label == null) {
			throw new RuntimeException("Nothing in edge can be null");
		}
	}
	
	/**
	 * @return a formatted string representing this edge
	 */
	public String print_edge() {
		String tmp = this.a+"-->"+this.b+" : "+this.label+"\n";
		return tmp;
	}
		
	// function to compare edges against each other
	@Override
	public int compareTo(Edge<E,N> arg0) {
		if ( this.a.equals(arg0.a) && this.b.equals(arg0.b) ) {
			return this.label.compareTo(arg0.label);
		} else if ( this.b.equals(arg0.a) ) {
			return this.b.compareTo(arg0.b);
		} else{
			return this.b.compareTo(arg0.a);
		}
	}	
}
