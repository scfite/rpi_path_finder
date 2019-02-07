package campus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import graph.Edge;
import graph.Graph;

import java.util.TreeMap;
import java.util.TreeSet;

// The model data type to represent the RPI campus
public class CampusModel {
	// Graph data structure with Edge type double, and node type Node
	public Graph<Double, Node> graph;
	//arrays to hold parsed data to be turned into a graph
	ArrayList<Node> nodes = new ArrayList<Node>();
	ArrayList<Node> buildings = new ArrayList<Node>();
	ArrayList<Edge<String,String>> edges = new ArrayList<Edge<String, String>>();
	
	/**
	 * @param a , b the two nodes to find the distance between 
	 * @return the distance between the two nodes
	 */
	private Double node_dist(Node a, Node b) {
		Double distx = Double.parseDouble(b.get_x()) - Double.parseDouble(a.get_x());
		Double disty = Double.parseDouble(b.get_y()) - Double.parseDouble(a.get_y());
		//square each distance
		distx *= distx;
		disty *= disty;
		Double d = Math.sqrt(distx + disty);
		return d;
	}
	
	/**
	 * @param id the id for the node we are looking for
	 * @param name the name of the node we are looking for
	 * @return the node that matches the given information if the node
	 * 			exists, null otherwise
	 */
	public Node which_node(String id, String name) {
	
		for (Node n : nodes) {
			//if the given id is null
			if(id == null || id.equals("")) {
				if(n.get_name().equals(name)) {
					return n;
				}
			//if there is a valid id given
			}else {
				if (n.get_id().equals(id)) {
					return n;
				}
			}
		}
		//Determine what to return if given info is null or empty
		if(id == null && name != null) {
			return new Node(name,null,null,null);
		}else if (id != null && name == null) {
			return new Node(null,id,null,null);
		}else if (id == null && name == null) {
			return null;
		}
		else if (id.equals("")) {
			return new Node(name,null,null,null);
		}else if (name.equals("")) {
			return new Node(null,id,null,null);
		}
		//return null node if not found
		return null; 
	}
	
	//creates a new undirected graph by passing the files to 'CampusParser' 
	/**
	 * @param nodes_file the file from which to read the nodes from
	 * @param edges_file the file from which to read the edges from
	 * @effects creates a new graph object
	 */
	public void createNewGraph(String nodes_file, String edges_file) {
		try {
			CampusParser parser = new CampusParser();
			ArrayList<Edge<? extends Double, ? extends Node>> formatted_edges =
					new ArrayList<Edge<? extends Double, ? extends Node>>();
			//read the files into our two array lists
			parser.readNodeData(nodes_file, nodes);
			parser.readEdgeData(edges_file, edges);
			//format our edges from pairs of id's to pairs of nodes
			for (Edge<String, String> e : edges) {
				//get the node for the given id
				Node start = which_node(e.get_start(), "");
				Node end = which_node(e.get_end(), "");
				Edge<Double, Node> tmp = 
				 new Edge<Double, Node>(start, end, node_dist(start, end));
				//add the edge that contains all node data and not just ids
				formatted_edges.add(tmp);
			}
			graph = new Graph<Double, Node>(nodes, formatted_edges);
			//create an array to only hold buildings
			for (Node n : nodes) {
				if(!n.is_intersection()) {
					buildings.add(n);
				}
			}
			Collections.sort(buildings);
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
	/**
	 * @param path, the list of edges we wish to make a deep copy of
	 * @return a deep copy of the given ArrayList<Edge>
	 */
	public ArrayList<Edge<Double, Node>> copy_path(ArrayList<Edge<Double, Node>> path){
		ArrayList<Edge<Double, Node>> copy = new ArrayList<Edge<Double, Node>>();
		for(Edge<Double, Node> e : path) {
			copy.add(new Edge<Double, Node>(e));
		}
		return copy;
	}

	// function to find the node with the smallest distance
	/**
	 * @param map , the map of nodes and their distance from the source node
	 * @param shortest_path, the map to hold what nodes have already been visited
	 * @return the neighboring node with the shortest distance from source that has 
	 *			 not already been visited
	 */
	public Node minDistance(TreeMap<Node, Double> map, 
			TreeMap<Node,Boolean> shortest_path) {
		Node min_node = null;
		Double min = Double.POSITIVE_INFINITY;
		for(Entry<Node, Double> node : map.entrySet()) {
			//If we haven't visited the node and it's distance from source is 
			//	less than our min
			if(shortest_path.get(node.getKey()) == false && 
						map.get(node.getKey()) <= min) {
				min = map.get(node.getKey());
				min_node = node.getKey();
			}
		}
		return min_node;
	}

	/**
	 * @param node1 the starting node
	 * @param node2 the destination node
	 * @return a string formatted to correctly display the shortest path 
	 * 			from node1 to node2
	 */
	public void findPath(Node node1, Node node2) {
		//bool to indicate whether an invalid node has been found
		boolean invalid = false;
		//bools to determine whether a node is unknown or not
		boolean valid1 = true; 
		boolean valid2 = true;
		//if one or more nodes are not in known buildings 
		if(!buildings.contains(node1) || !buildings.contains(node2))  {
			invalid = true;
			if(!buildings.contains(node1)) {
				valid1 = false;
			}
			if(node2 == null) {
				valid2 = false;
			}
			// If node2 isn't in list of buildings and node2 != node1
			else if(!buildings.contains(node2) && node2.compareTo(node1) != 0) {
				valid2 = false;
			}
			
			ArrayList<Edge<Double, Node>> npath =
					new ArrayList<Edge<Double, Node>>();
			// Print the invalid path in correct form
			CampusPaths.format_path_print(node1, valid1, node2, valid2, npath);
			return;
		}else {
			//map for distances from start to node in map
			TreeMap<Node, Double> Dist = new TreeMap<Node, Double>();
			//map to represent a Queue for distances from start to node in map
			TreeMap<Node, Double> DistQ = new TreeMap<Node, Double>();
			//map to represent what nodes have been visited
			TreeMap<Node, Boolean> visitMap = new TreeMap<Node, Boolean>();
			//initialize all distances to infinity
			for(Map.Entry<Node, TreeSet<Edge<? extends Double,? extends Node>>> entry 
					: graph.iterator()) {
				Dist.put(entry.getKey(),Double.POSITIVE_INFINITY);
				visitMap.put(entry.getKey(), false);
				for(Edge<? extends Double, ? extends Node> e : entry.getValue()) {
					Dist.put(e.get_end(),Double.POSITIVE_INFINITY);
					visitMap.put(e.get_end(), false);
				}
			}
			//Starting Node has distance 0
			Dist.replace(node1, 0.0);
			//copy distance values into another map that represents our queue
			DistQ.putAll(Dist);
			// Map to hold the paths of nodes
			TreeMap<Node, ArrayList<Edge<Double, Node>>> P = 
					new TreeMap<Node ,ArrayList<Edge<Double, Node>>>();
			//tmp empty path
			ArrayList<Edge<Double, Node>> tmp = new ArrayList<Edge<Double, Node>>();
			P.put(node1,tmp);
			//Iterator over entire graph
			for(Map.Entry<Node, TreeSet<Edge<? extends Double,? extends Node>>> entry : 
					graph.iterator()) {
				//pick the vertex with smallest distance from source, from Dist map, that 
				//  has not been processed yet
				Node u = minDistance(Dist,visitMap);
				//BASE CASE: if we have reached the destination node
				if(u.equals(node2)) {
					CampusPaths.format_path_print(node1, true, node2, true, P.get(u));
					return;
				}
				//mark the picked node as processed
				visitMap.replace(u, true);
				//update the distance values of the adjacent vertices of the picked vertex
				for(Edge<? extends Double, ? extends Node> e : graph.get_children(u)) {
					//if we haven't visited it yet, if it is not infinity, and it is less 
					//  than the previous vertex plus weight of e
					if(!visitMap.get(e.get_end()) &&
							Dist.get(u) != Double.POSITIVE_INFINITY &&
							Dist.get(u)+e.get_label() < Dist.get(e.get_end())) {
						//update the distance value
						Dist.replace(e.get_end(), Dist.get(u)+e.get_label());
						//copy path from u
						ArrayList<Edge<Double, Node>> new_path = copy_path(P.get(u));
						//add e to the old path from u
						new_path.add(new Edge<Double, Node>(e));
						//add the updated path to the current node
						P.put(e.get_end(),new_path);
					}
				}
				
			}
			// More printing for invalid input
			if(P.get(node2) == null || P.get(node2) == null && !invalid) {
				CampusPaths.format_path_print(node1, true, node2, false, P.get(node2));
				return;
			}
			return;
		}
	
	}
}
