package legionellosis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Legionellosis {

	private Map<Integer, Integer> walks;
	private List<Integer>[] edges; // locations' connections
	
	private int[] locationsWeight; // array with weight of every location

	private int numberOfLocations; // saves the number of locations
	private int biggestWeight; // saves the biggest weight recorded

	public Legionellosis(int numberOfLocations) {
		walks = new HashMap<>();
		this.createDT(numberOfLocations);

		this.locationsWeight = new int[numberOfLocations];

		this.numberOfLocations = numberOfLocations;
		this.biggestWeight = 0;
	}

	public void addEdge(int l1, int l2) {
		edges[l1].add(l2);
		edges[l2].add(l1);
	}

	/**
	 * Processes the movement done by a sick person
	 * 
	 * @param homeLocation
	 * @param distance
	 */
	public void addMovement(int homeLocation, int distance) {
		walks.put(homeLocation, distance);
	}

	public List<Integer> getPerilousLocations() {
		// processes the movement done by sick people
		processMovement();
		
		List<Integer> locations = new LinkedList<>();

		int size = 0;
		for (int i = 0; i < numberOfLocations; i++) {
			if (locationsWeight[i] == biggestWeight) {
				locations.add(i + 1);
				size++;
			}
		}
		
		if (size == numberOfLocations) // If there is no perilous location, the line has
			return new LinkedList<>();

		return locations;
	}
	
	//------------------------------------Private_Methods-------------------------------------//

	private void processMovement() {
		// Pesquisa por bfs, dfs ou outro

		// Por cada residente, adiciona 1 so uma vez a cada posicao
		
		// Provavelmente usar um depth-limit-search
	}
	
	@SuppressWarnings("unchecked")
	private void createDT(int length) {
		this.edges = new List[length];
		for (int i = 0; i < edges.length; i++) {
			edges[i] = new ArrayList<>();
		}
	}

}
