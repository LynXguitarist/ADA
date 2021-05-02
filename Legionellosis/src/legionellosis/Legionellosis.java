package legionellosis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

public class Legionellosis {

	private Map<Integer, Integer> walks; // Contains the interviews done to sick people
	private List<Integer>[] edges; // Locations' connections

	private int[] locationsWeight; // Array with weight of every location

	private int numberOfLocations; // Saves the number of locations
	private int biggestWeight; // Saves the biggest weight recorded

	public Legionellosis(int numberOfLocations) {
		this.createDT(numberOfLocations);

		this.locationsWeight = new int[numberOfLocations];

		this.numberOfLocations = numberOfLocations;
		this.biggestWeight = 0;
	}

	/**
	 * Initializes the map that contains the interviews
	 * 
	 * @param sickPeople
	 */
	public void updateSickPeople(int sickPeople) {
		this.walks = new HashMap<>(sickPeople);
	}

	public void addEdge(int l1, int l2) {
		edges[l1 - 1].add(l2 - 1);
		edges[l2 - 1].add(l1 - 1);
	}

	/**
	 * Processes the movement done by a sick person
	 * 
	 * @param homeLocation
	 * @param distance
	 */
	public void addMovement(int homeLocation, int distance) {
		walks.put(homeLocation - 1, distance);
	}

	public List<Integer> getPerilousLocations() {
		// Processes the movement done by sick people
		for (Entry<Integer, Integer> entry : walks.entrySet())
			processMovement(entry.getKey() - 1, entry.getValue());

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

	// ------------------------------------Private_Methods-------------------------------------//

	private void processMovement(int source, int limit) {
		// Stack that saves the locations visited
		Stack<Integer> nodeStack = new Stack<>();

		// Array that saves the distances to the source
		int[] distances = new int[numberOfLocations];

		// Current location
		int current = source;
		locationsWeight[current] += 1;

		nodeStack.add(source);

		while (!nodeStack.isEmpty()) {
			current = nodeStack.pop();

			if (distances[current] <= limit) {
				for (int neighbor : this.edges[current]) {
					if (distances[neighbor] == 0 && neighbor != source) {
						distances[neighbor] = distances[current] + 1;
						nodeStack.add(neighbor);

						locationsWeight[neighbor] += 1;
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void createDT(int length) {
		this.edges = new List[length];
		for (int i = 0; i < edges.length; i++) {
			edges[i] = new ArrayList<>();
		}
	}

}