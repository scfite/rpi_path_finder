package campus;


import java.util.ArrayList;

import graph.Edge;

import java.io.*;

// Class to parse the nodes and edges input files 
public class CampusParser {
	
	/**
	 * @param filename the file to read the nodes from
	 * @param nodes an array to add the nodes to
	 * @requires delimiter for data is ','
	 * @modifies the arraylist of nodes 
	 * @effects the arraylist "nodes" : all nodes from the file are in nodes 
	 * @throws IOException
	 */
	public ArrayList<Node> readNodeData(String filename, ArrayList<Node> nodes) 
		throws IOException {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = null;
			while ((line = reader.readLine()) != null) {
				int i = line.indexOf(",");
				if (i == -1)  {
            	 throw new IOException("File "+filename+" not a CSV file. Or can't find"
            	 		+ "index of delimeter");
				}
				//FOUR ATTRIBUTES COLLECTED BELOW
				//FIRST : NAME
				String name = line.substring(0,i);

				//SECOND : ID 
				String line2 = line.substring(i+1, line.length());
				int i2 = line2.indexOf(",");
				String id = line2.substring(0, i2);

				//THIRD : X COORDINATE 
				String line3 = line2.substring(i2+1, line2.length());
				int i3 = line3.indexOf(",");
				String x = line3.substring(0,i3);

				//FOURTH : Y COORDINATE 
				String line4 = line3.substring(i3+1, line3.length());
				int i4 = line4.length();
				String y = line4.substring(0,i4);

				//ADD NODE TO TREESET
				Node tmp = new Node(name,id,x,y);
				nodes.add(tmp);
			}
			return nodes;
		}
	
	/**
	 * @param filename
	 * @param edges
	 * @requires two node ids per line are given to represent the edge,
	 * 			  	the edges in the file are meant to be undirected
	 * @modifies the arraylist of edges
	 * @effects the arraylist "edges" : it reflects an undirected graph given by 
	 * 			a list of edges 
	 * @throws IOException
	 */
	public ArrayList<Edge<String, String>> 
	readEdgeData(String filename, ArrayList<Edge<String,String>> edges) 
		throws IOException {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = null;
			while ((line = reader.readLine()) != null) {
				int i = line.indexOf(",");
				if (i == -1)  {
				 throw new IOException("File "+filename+" not a CSV file. Or can't find"
				 		+ "index of delimeter");
				}
				// Get first Node id
				String id1 = line.substring(0, i);
				
				// Get second Node id
				String line2 = line.substring(i+1, line.length());
				String id2 = line2.substring(0, line2.length());
				// Create a new edge for the two ids found
				Edge<String, String> tmp = new Edge<String, String>(id1, id2, "");
				// UNDIRECTED: Add tmp AND the reverse
				// DIRECTED: Add tmp  ONLY
				edges.add(tmp);
				edges.add(tmp.reverse());
			}
			return edges;	
	}
	
}