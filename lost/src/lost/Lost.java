package lost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lost {

	private static final char GRASS = 'G';
	private static final char OBSTACLE = 'O';
	private static final char WATER = 'W';
	private static final char EXIT = 'X';

	public static final int LOST_IN_TIME = Integer.MIN_VALUE;
	public static final int UNREACHABLE = Integer.MAX_VALUE;

	public static final String LOST_IN_TIME_S = "Lost in Time";
	public static final String UNREACHABLE_S = "Unreachable";

	private Map<Pair<Integer, Integer>, Integer> graph;
	private List<Edge> johnEdges;
	private List<Edge> kateEdges;
	private char[][] map;
	private Map<Integer, Integer> magicalWheels; // ED for MW -> numVertice - time

	private int row, col;
	private int numVertices;// number of vertices for Kate and John
	private int rJ, cJ, rK, cK;
	private int exit_x, exit_y;

	public Lost(int row, int col, int numberMW) {
		graph = new HashMap<>(row * col);

		johnEdges = new ArrayList<>(row * col * 4);
		kateEdges = new ArrayList<>(row * col * 4);
		magicalWheels = new HashMap<>(numberMW);

		map = new char[row][col];

		this.row = row;
		this.col = col;
		this.numVertices = 0;

		this.exit_x = 0;
		this.exit_y = 0;

		this.rJ = 0;
		this.cJ = 0;
		this.rK = 0;
		this.cK = 0;
	}

	public int[] processResult() {
		int[] results = new int[2];

		results[0] = bellmanFordJohn();
		results[1] = bellmanFordKate();

		return results;
	}

	public void savesMagicalWheel(int r, int c, int t) {
		// add to magicalWheels
		magicalWheels.put(numVertices, t);
		// add to graph
		graph.put(Pair.of(r, c), numVertices++);
	}

	public void processMap(String[] map, int col) {
		int row = 0;
		for (String line : map) {
			for (int i = 0; i < col; i++) {
				char c = line.charAt(i);
				this.map[row][i] = c;
				// saves the position of the exit
				if (c == EXIT) {
					exit_x = row;
					exit_y = i;
				}
			}
			row++;
		}
		processGraph();
	}

	public void J_K_Pos(int rJ, int cJ, int rK, int cK) {
		this.rJ = rJ;
		this.cJ = cJ;
		this.rK = rK;
		this.cK = cK;
	}

	// ------------------------------Private_Methods---------------------------//

	// Lost in time -> negative cycle
	// Unreachable -> there is no connection to the exit

	private int bellmanFordJohn() {
		int x = rJ;
		int y = cJ;

		int[] via = new int[numVertices];

		int[] length = new int[numVertices];
		for (int i = 0; i < numVertices; i++)
			length[i] = Integer.MAX_VALUE;

		int origin = graph.get(Pair.of(x, y));

		length[origin] = 0;
		via[origin] = origin;

		boolean changes = false;
		for (int i = 1; i < numVertices; i++) {
				changes = updateLengths(johnEdges, length, via);
			if (!changes)
				break;
		}
		// Negative-weight cycles
		if (changes && updateLengths(johnEdges, length, via))
			return Integer.MIN_VALUE;
		
		return length[graph.get(Pair.of(exit_x, exit_y))];
	}
	
	private int bellmanFordKate() {
		int x = rK;
		int y = cK;

		int[] via = new int[numVertices];

		int[] length = new int[numVertices];
		for (int i = 0; i < numVertices; i++)
			length[i] = Integer.MAX_VALUE;

		int origin = graph.get(Pair.of(x, y));

		length[origin] = 0;
		via[origin] = origin;

		boolean changes = false;
		for (int i = 1; i < numVertices; i++) {
				changes = updateLengths(kateEdges, length, via);
			if (!changes)
				break;
		}
		
		return length[graph.get(Pair.of(exit_x, exit_y))];
	}

	private boolean updateLengths(List<Edge>edges, int[] len, int[] via) {
		boolean changes = false;
		for (Edge edge : edges) {
			int tail = edge.getV1();
			int head = edge.getV2();
			if (len[tail] < Integer.MAX_VALUE) {
				int newLen = len[tail] + edge.getWeight();
				if (newLen < len[head]) {
					len[head] = newLen;
					via[head] = tail;
					changes = true;
				}
			}
		}
		return changes;
	}

	private void processGraph() {
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.col; j++) {
				char c = this.map[i][j];
				// if not Obstacle, saves vertices
				if (c != OBSTACLE) {
					boolean wasVertexAdd = false;
					if (graph.putIfAbsent(Pair.of(i, j), numVertices) == null)
						wasVertexAdd = true;
					// Not WATER
					if (c != WATER)
						addEdgesJohn(i, j, c);

					addEdgesKate(i, j, c);
					if(wasVertexAdd)
						numVertices++;
				}
			}
		}
	}

	private void addEdgesJohn(int i, int j, char c) {
		// Add horizontal edge
		if (j > 0) { // can add edge with previous vertex
			char previousChar = this.map[i][j - 1];
			// GRASS or MW
			if (previousChar != WATER && previousChar != OBSTACLE) {
				johnEdges.add(new Edge(numVertices - 1, numVertices, getWeightByTile(previousChar, i, j)));
			}
		}
		// Add vertical edge
		if (i > 0) { // can add edge with upper vertex
			char upperChar = this.map[i - 1][j];
			if (upperChar != WATER && upperChar != OBSTACLE) {
				int vertex = graph.get(Pair.of(i - 1, j));
				johnEdges.add(new Edge(vertex, numVertices, getWeightByTile(upperChar, i, j)));
			}

		}
		// Is Magical Wheel, connects the position of the MW to the traveled position
		if (c != GRASS && c != EXIT) {
			int vertex = (c - '0') - 1; // position traveled using MW
			johnEdges.add(new Edge(numVertices, vertex, magicalWheels.get(vertex)));
		}
	}

	private void addEdgesKate(int i, int j, char c) {
		// Add horizontal edge
		if (j > 0) { // can add edge with previous vertex
			char previousChar = this.map[i][j - 1];
			// GRASS or MW
			if (previousChar != OBSTACLE) {
				kateEdges.add(new Edge(numVertices - 1, numVertices, getWeightByTile(previousChar, i, j)));
			}
		}
		// Add vertical edge
		if (i > 0) { // can add edge with upper vertex
			char upperChar = this.map[i - 1][j];
			if (upperChar != OBSTACLE) {
				int vertex = graph.get(Pair.of(i - 1, j));
				kateEdges.add(new Edge(vertex, numVertices, getWeightByTile(upperChar, i, j)));
			}
		}
	}

	private int getWeightByTile(char tile, int x, int y) {
		int weight = 0;
		switch (tile) {
		case GRASS:
			weight = 1;
			break;
		case WATER:
			weight = 2;
			break;
		default: // is Magical Wheel or Exit
			weight = 1;
			break;
		}
		return weight;
	}
}
