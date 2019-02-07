package graph;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/** The Graph class represents a number of nodes with any number of directed edges, excluding duplicates,
 *  to other nodes within the graph. No part of the node can be null. 
 */
public class Graph<E extends Comparable<E>,N extends Comparable<N>> {
	
	private int num_nodes;
	private int num_edges;
	
	//graph structure is a tree map with the node type N as the key, and a tree set 
	// , which holds edges to all of the children, as the value
	public TreeMap<N,TreeSet<Edge<? extends E,? extends N>>> graph = 
			new TreeMap<N,TreeSet<Edge<? extends E, ? extends N>>>();

	/**
	 *  ABSTRACT FUNCTION:
	 *    A directed graph is NaG (not a graph) if num_nodes < 1 or if there
	 * is an edge that points to a node that doesn't exist. 
	 * A directed graph is a collection of nodes, commonly represented as strings 
	 * (but can be a node class), that may have edges connecting
	 * them to other nodes in any order. A directed graph may have between 0 and 
	 * n^2 edges with n being the number of nodes, because we are not allowing duplicate edges. 
	 *
	 * REPRESENTATION INVARIANT for each Graph G:
	 * num_nodes != null && num_edges != null &&
	 * num_nodes >= 0 && num_edges >= 0 &&
	 * for (i : G) { 
	 *	TreeSet<Edge> e = i.getChildren();
	 * for (x : e){
	 *   (G.containsKey(x.get_start()) != False) &&
	 *   (G.containsKey(x.get_end()) != False) &&
	 *  }
	 * }
	 */
	
	//CONSTRUCTORS
	/**
	 * @effects constructs a new null graph
	 */
	public Graph() {
		this.num_nodes = 0;
		this.num_edges = 0;
		checkRep();
	}

	/**
	 * Input files for graph are described in *parser.java
	 * @param n an array list of nodes in the graph
	 * @param e an array list of edges in the graph
	 * @requires every edge does not connect to a node that is not in the graph
	 * 		if n is empty, e must be as well
	 * @effects Constructs a new Graph with the given lists of nodes and edges.
	 * 		If n and e are empty, construct an empty graph.
	 */
	public Graph(ArrayList<? extends N> n, ArrayList<Edge<? extends E, ? extends N>> e) {
		//if given no nodes, create an empty graph
		if (n.size() <= 0) {
			this.num_nodes = 0;
			this.num_edges = 0;
			checkRep();
		}
		else {
			// Add nodes to graph
			for (N tmp : n) {
				TreeSet<Edge<? extends E, ? extends N>> tmp_edgeset = 
						new TreeSet<Edge<? extends E, ? extends N>>();
				try {
					graph.put(tmp, tmp_edgeset);
				}catch (NullPointerException npe) {
					System.out.println("A node in the graph was initialized with an empty set");
				}
			}
			// Add edges to graph
			for(Edge<? extends E, ? extends N> tmp : e) {
				try {
				//add edge to the origin node's list of children
				graph.get(tmp.get_start()).add(tmp);
				} catch (NullPointerException npe) {
					System.out.println("Tried to add null element to graph");
				}
			}
			this.num_nodes = graph.size();
			this.num_edges = graph.values().size(); 
			checkRep();
		}
	}
	
	//ACCESSORS
	/**
	 * @return the number of nodes in the graph
	 */
	public int num_nodes() {
		return this.num_nodes;
	}

	/**
	 * @return the number of edges in the graph
	 */
	public int num_edges() {
		return this.num_edges;
	}
	
	/**
	 * @param n, the node to look for in graph
	 * @return true if node was found in graph, false otherwise
	 */
	public boolean has_node(N n) throws NullPointerException{
		if(n == null)  {
			return false;
		}
		if(graph.containsKey(n)) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * @param data the data of the node that we want
	 * @return  the data if it exists 
	 * @throws RuntimeException if the node is not in the graph
	 */
	public N get_node(N data) {
		if (graph.containsKey(data)) {
			return data; 
		}
		else {
			throw new RuntimeException("node is not in the graph");
		}
	}

	/**
    * @param node a, b the two nodes from which to find the edge. l the label 
				of the edge.
    * @return the Edge(a,b,l) if it exists.
    * @throws RuntimeException if the edge is not in the graph
    */
    public Edge<? extends E,? extends N> get_edge(N a, N b, E l) {
    	Edge<? extends E,? extends N> tmp_edge = new Edge<E, N>(a,b,l);
    	if (graph.get(a).contains(new Edge<E, N>(a,b,l))) {
    		return tmp_edge;
		 }else {
			 throw new RuntimeException("the edge is not in the graph");
		 }
	}
	 
    /**
	 * @param n, the node for which to get the children of
	 * @return the tree set that contains the children of node n, or 
	 * 			null if the node doesn't exist
	 */
    public TreeSet<Edge<? extends E, ? extends N>> get_children(N n){
		return graph.get(n);
	}

	//MODIFIERS
	/**
	 * @param nodeData, the node of type N to be added to the graph
	 * @effects adds a new node with an empty set of children to graph if nodeData
	 * 			 is valid, else none
	 * @return true if the node was added, false if the node already exists or 
	 *      	if the node is null
	 */
	public boolean add_node(N nodeData) {
		if (graph.containsKey(nodeData)) {
			return false;
		}else if (nodeData == null){
			return false;
		}else {
			//add empty set of children for node
			TreeSet<Edge<? extends E, ? extends N>> tmp = 
					new TreeSet<Edge<? extends E, ? extends N>>();
			graph.put(nodeData, tmp);
			num_nodes++;
			return true;
		}
	}

	/**
	* @param a , b the nodes to connect with this edge. l the label for this edge
	* @requires a and b are currently in the graph, l != null
	* @effects if edge is valid, our graph data structure TreeMap<Node,TreeSet<Edge>> 
	* 			has an edge added for the proper node
	* @return true if the edge was added, false if (a node in the edge doesn't exist or 
	* 			a, b, or l are null)
	*/
	public boolean add_edge(N a, N b, E l) {
		if (a == null || b == null || l == null) {
			return false;
		}
		if (graph.containsKey(a) && graph.containsKey(b)) {
			if(!graph.get(a).add(new Edge<E, N>(a,b,l))) {
				throw new RuntimeException("The edge was unable to be added");
			}
			num_edges++;
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * @param a the start node, b the end node, l the label
	 * @return true if the graph has the edge, false otherwise
	 */
	public boolean has_edge(N a, N b, E l) {
		if(graph.get(a) == null) {
			return false;
		}
		if(graph.get(a).contains(new Edge<E, N>(a,b,l))) {
			return true;
		}
		return false;
	}
	
	/**
	 * @return an iterator over the graph data type
	 */
	public Set<Map.Entry<N, TreeSet<Edge<? extends E, ? extends N>>>> iterator(){
		return graph.entrySet();
	}

	// Helper function. Prints the graph.
	public void print_graph() {
		System.out.println("------------------------");
		for(Map.Entry<N, TreeSet<Edge<? extends E, ? extends N>>> entry : graph.entrySet()) {
			System.out.printf("Node: %s", entry.getKey());
			System.out.print("\nChildren: ");
			for(Edge<? extends E, ? extends N> e : entry.getValue()) {
				System.out.printf("___ %s", e.get_end());
				System.out.printf("  %f", e.get_label());
			}
			System.out.println();
		}
		System.out.println("------------------------");
	}

    
	//Checks that the representation invariant holds (if any).
	private void checkRep() throws RuntimeException {
		if (num_nodes < 0 || num_edges < 0) {
			throw new RuntimeException("Cant have a negative number of nodes or edges");
		}
		//if we have an empty graph do nothing
		if (num_nodes == 0 && num_edges == 0 && graph.isEmpty()) {
		}
		if (num_nodes != graph.size()) {
			throw new RuntimeException("Number of nodes is incorrect");
		}
		else {
			//iterate over entire graph
			for(Map.Entry<N, TreeSet<Edge<? extends E, ? extends N>>> entry : graph.entrySet()) {
				N tmp_node = entry.getKey();
				//iterate over children of node we are on
				for(Edge<? extends E, ? extends N> e : entry.getValue()) {
					// if the start node or the end node are not in graph, then an edge had an invalid node
					if (!graph.containsKey(e.get_start()) || !graph.containsKey(e.get_end()) ) {
						throw new RuntimeException("A node that did not exist was found in an edge");
					}
					if (e.get_start() != tmp_node) {
						throw new RuntimeException("A node had an edge originating from it that was pointing in the wrong direction");
					}
				}
			}
		}
		
	}
	
}
