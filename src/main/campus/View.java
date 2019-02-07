package campus;

import java.util.ArrayList;
import java.util.Collections;

// Class to control how data is displayed to user
public class View {
	
	public void list_buildings(ArrayList<Node> nodes) {
		Collections.sort(nodes);
		for (Node n : nodes) {
			System.out.println(n.get_name() + "," + n.get_id());
		}
	}
	
	public void list_commands() {
		System.out.println("-------COMMANDS-------");
		System.out.println("b : Lists all buildings");
		System.out.println("r : Prompts user for the ids or names of buildings and prints shortest path between them");
		System.out.println("q : Quits the program");
		System.out.println("m : Prints a menu of all commands");
		System.out.println("----------------------");
	}
	
	public void prompt_for_buildings(String num) {
		System.out.print(num + " building id/name, followed by Enter: ");
	}

	public void unknown() {
		System.out.print("Unknown option\n");
	}


}
