package gameBeans;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Graph {

	private Map<Integer, List<Integer>> map = new HashMap<>();

	// This function adds a new vertex to the graph
	public void addVertex(int s) {
		map.put(s, new LinkedList<>());
	}

	// This function adds the edge
	// between source to destination
	public void addEdge(int source, int destination) {
		if (!map.containsKey(source))
			addVertex(source);

		if (!map.containsKey(destination))
			addVertex(destination);

		map.get(source).add(destination);
	}

}
