package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

public class GraphWrapper {

	public Graph<String,String> graph;
	//This is the constructor of GraphWrapper. 
	// It initializes the instance field with a new empty 
	//instance of your Graph ADT. 
	public GraphWrapper() {
		graph = new Graph<String, String>();
	}
	
	//Adds a node represented by the string nodeData to your graph. 
	//If an identical node already exists in the graph, the output 
	//of addNode is not defined, that is, it is left at your discretion. 
	public void addNode(String nodeData) {
		this.graph.add_node(nodeData);
	}

	/**Creates an edge from parentNode to childNode with label edgeLabel 
	 in your graph. If either of the nodes does not exist in the graph, 
	the output of this command is not defined. If an identical edge 
	(same parent, child, and label) already exists, the output of this 
	command is not defined either, as it is left at your discretion whether 
	to allow identical edges in your implementation. */
	public void addEdge(String parentNode, String childNode, String edgeLabel) {
		this.graph.add_edge(parentNode,childNode,edgeLabel);
	}

	//This operation has no effect on your graph. It returns an iterator which returns 
	//the nodes in lexicographical (alphabetical) order. 
	public Iterator<String> listNodes(){
		ArrayList<String> dummy_list = new ArrayList<String>();
		if (graph.num_nodes() == 0) {
			return dummy_list.iterator(); 
		}else {
			for (Entry<String, TreeSet<Edge<? extends String, ? extends String>>> entry : graph.iterator()) {
				String tmp = entry.getKey();
				dummy_list.add(tmp);
			}
			List<String> dummy2 = dummy_list.subList(0,dummy_list.size());
			Collections.sort(dummy2);
			return dummy2.iterator();
		}
	}

	/**This operation has no effect on your graph. It returns iterator which 
	returns the list of childNode(edgeLabel) in lexicographical (alphabetical) 
	order by node name and secondarily by edge label. childNode(edgeLabel) means 
	there is an edge with label edgeLabel from parentNode to childNode. If there 
	are multiple edges from parentNode to some childNode, there should be separate 
	entry for each edge. If there is a reflexive edge, parentNode(edgeLabel) should 
	be in the list. 
	*/
	public Iterator<String> listChildren(String parentNode){
		TreeSet<Edge<? extends String, ? extends String>> e = graph.graph.get(parentNode);
		ArrayList<String> list = new ArrayList<String>();
		if (e.size() == 0) {
			return list.iterator();
		}else {
			for(Edge<? extends String, ? extends String> tmp : e) {
				list.add(tmp.get_end() + "(" + tmp.get_label() + ")");
			}		
			List<String> dummy = list.subList(0,list.size());
			Collections.sort(dummy);
			return dummy.iterator();
		}
	}
	

}