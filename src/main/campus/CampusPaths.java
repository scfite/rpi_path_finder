package campus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import graph.Edge;

// Controls the program
public class CampusPaths {
	CampusModel model = new CampusModel();
	View view = new View();

	//check if a given string is numeric
	/**
	 * @param strNum a string
	 * @return true if the string represents a number, false if parseDouble(strNum) 
	 * throws a number format exception
	 */
	public static boolean isNumeric(String strNum) {
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
	
	// Gets the direction needed to walk from node 1 to node 2
	/**
	 * @param n1 the start node
	 * @param n2 the end node
	 * @return a string with the direction from n1 to n2 according to the specs provided in README
	 */
	public static String walk_direction(Node n1, Node n2) {
		Double n1x = Double.parseDouble(n1.get_x());
		Double n1y = Double.parseDouble(n1.get_y());
		Double n2x = Double.parseDouble(n2.get_x());
		Double n2y = Double.parseDouble(n2.get_y());

		Double angle = 
				(Double) Math.toDegrees(Math.atan2(Math.abs(n2y - n1y), Math.abs(n2x - n1x)));
		//Angles defined on map of RPI
		if (67.5 > angle && angle >= 22.5) {
			return "NorthWest";
		}else if (112.5 > angle  && angle >= 67.5) {
			return "North";
		} else if(157.5 > angle && angle >= 112.5) {
			return "SouthEast";
		} else if(202.5 > angle && angle >= 157.5) {
			return "South";
		} else if(247.5 > angle && angle >= 202.5) {
			return "SouthWest";
		} else if(292.5 > angle && angle >= 247.5) {
			return "West";
		} else if(337.5 > angle && angle >= 292.5) {
			return "NorthEast";
		} else {
			return "East";
		}
	}
	
	//formats the correct output
	/**
	 * @param node1 Starting node
	 * @param node2 destination node
	 * @param path array of the path to be printed
	 * @param valid1, valid2, booleans telling us if node1 and node2 are valid
	 * @return s, the string formatted to correctly display to shortest path between 
	 * 	 		node1 and node2
	 */
	public static void format_path_print(Node node1, boolean valid1, Node node2, 
			boolean valid2, ArrayList<Edge<Double, Node>> path) {
		String s = new String();
		Double total_dist= 0.0;
		if(!valid1) {
			if (node1 == null) {
			}
			//If node is intersection print id
			else if (node1.get_name() == null || node1.is_intersection()) {
				s += "Unknown building: [" + node1.get_id() + "]\n";
			//If node is building print name
			}else {
				s += "Unknown building: [" + node1.get_name() + "]\n";
			}
		}
		if(!valid2) {
			if (node2 == null) {
			}
			// If node2 is not a copy of node1
			else if(node1 != null && node1.compareTo(node2) != 0) {
				//If node is intersection print id
				if(node2.get_name() == null || node2.is_intersection()) {
					s += "Unknown building: [" + node2.get_id() + "]\n";
				//If node is building print name
				}else {
					s += "Unknown building: [" + node2.get_name() + "]\n";
				}
			}
		}
		//If both nodes are valid
		else if(valid1 && valid2) {
			s += "Path from " + node1.get_name() + " to " + node2.get_name() + ":\n";
			if (path == null) {
				s += "There is no path from " + node1.get_name() + " to " + node2.get_name() + ".\n";
			}else {
				for (Edge<Double, Node> e : path) {
					total_dist += e.get_label();
					String direction = walk_direction(e.get_start(), e.get_end());
					if (e.get_end().is_intersection()) {
						s += "     Walk " + direction +" to (Intersection " + e.get_end().get_id();
					}else {
						s += "     Walk " + direction +" to (" + e.get_end().get_name();
					}
					s += ")\n";
				}
			s += "Total distance: " + String.format("%.3f", total_dist) + " pixel units.\n";
			}
		}
		System.out.print(s);
	}

	
	/**
	 * @param nodes_file the file that stores what nodes are to be added to the graph
	 * @param edges_file the file that stores what edges are to be added to the graph
	 */
	public void initialize(String nodes_file, String edges_file) {
		model.createNewGraph(nodes_file, edges_file);
	}

	
	//Program driver
	public static void main(String[] args) {
		CampusPaths controller = new CampusPaths();
		//INSERT DATA FILES HERE
		controller.initialize("data/RPI_map_data_Nodes.csv", "data/RPI_map_data_Edges.csv");
		// Input command
		String input;	
		BufferedReader reader = 
				new BufferedReader(new InputStreamReader(System.in));
		try {
			// Continuously prompt user to command until quit command
			controller.view.list_commands();
			while ((input = reader.readLine()) != null) {
				if (input.equals("b")) {
					controller.view.list_buildings(controller.model.buildings);
				//Prompt for path between buildings
				}else if (input.equals("r")) {
					Node n1, n2;
					controller.view.prompt_for_buildings("First");
					String first = "";
					first = reader.readLine();
					System.out.println();
					controller.view.prompt_for_buildings("Second");
					String second = "";
					second = reader.readLine();
					System.out.println();
					if(isNumeric(first)) {
						n1 = controller.model.which_node(first, "");
					}else {
						n1 = controller.model.which_node("", first);
					}
					if(isNumeric(second)) {
						n2 = controller.model.which_node(second, "");
					}else {
						n2 = controller.model.which_node("", second);
					}
					controller.model.findPath(n1, n2);
				//List available commands
				}else if (input.equals("m")) {
					controller.view.list_commands();
				// Quit
				}else if (input.equals("q")) {
					return;
				} else {
					controller.view.unknown();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
}