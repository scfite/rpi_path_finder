package campus;

// Node to represent buildings and intersections at RPI
// If Node is an intersection, name will be ""  
public class Node implements Comparable<Node> {

	private String name;
	private String id;
	private String x;
	private String y;
	private boolean intersection;
	
	// CONSTRUCTOR
	/**
	 * @param n node name, id the id of node, x horizontal pixel coordinate
	 * 		   y vertical pixel coordinate
	 * @requires id, x, y != null
	 */
	public Node(String n, String id, String x, String y) {
		this.name = n;
		this.id = id;
		this.x = x;
		this.y = y;
		if (n == null) {
			this.intersection = false;
		// If given a blank name we can assume this is an intersection
		} else if(n.equals("")) {
			this.intersection = true;
		} else {
			this.intersection = false;
		}
	}
	
	// ACCESSORS
	/**
	 * @return true if this node is an intersection, false otherwise 
	 */
	public boolean is_intersection() {
		return this.intersection;
	}

	/**
	 * @return the name of this node
	 */
	public String get_name() {
		return this.name;
	}
	
	/**
	 * @return the id of this node
	 */
	public String get_id() {
		return this.id;
	}
	
	/**
	 * @return the formatted id
	 */
	public String get_id_string() {
		return this.id.toString();
	}
	
	/**
	 * @return the x coordinate of this node
	 */
	public String get_x() {
		return this.x;
	}
	
	/**
	 * @return the y coordinate of this node
	 */
	public String get_y() {
		return this.y;
	}
	
	// Function to compare nodes to each other
	@Override
	public int compareTo(Node o) {
		try {
			if (o == null || o.get_id() == null || o.get_name() == null) {
				return 1;
			}else if (this.get_name().equals(o.get_name())) {
				return this.id.compareTo(o.get_id());
			}else {
				return this.name.compareTo(o.get_name());
			}
		}catch(NullPointerException n) {
			return -1;
		}
		
	}

}
