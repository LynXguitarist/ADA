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

	private Map<Pair<Integer, Integer>, Integer> vertices; // vertices of the graph
	private List<Edge> johnEdges; // John's edges
	private List<Edge> kateEdges; // Kate's edges
	private char[][] map; // island
	private Map<Integer, Integer> magicalWheels; // ED for MW -> (char ref, numVertice)

	private int row, col; // size of the island
	private int numVertices;// number of vertices for Kate and John
	private int rJ, cJ, rK, cK; // coordinates where John and Kate start
	private int exit_x, exit_y; // coordinates of the exit

	public Lost(int row, int col, int numberMW) {
		vertices = new HashMap<>(row * col);

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

	/**
	 * Processes the algorithm for John and Kate
	 * 
	 * @return results
	 */
	public int[] processResult() {
		int[] results = new int[2];

		results[0] = bellmanFordJohn();
		results[1] = bellmanFordKate();

		return results;
	}

	/**
	 * Saves a magical wheel
	 * 
	 * @param index
	 * @param r
	 * @param c
	 * @param t
	 */
	public void savesMagicalWheel(int index, int r, int c, int t) {
		int vertex = magicalWheels.get(index); // vertex of the MW

		int jumpVertex = vertices.get(Pair.of(r, c)); // vertex of the jump
		// creates an edge between the MW and the jump vertex
		johnEdges.add(new Edge(vertex, jumpVertex, t));
	}

	/**
	 * Processes the graph, using the island(matrix of chars)
	 * 
	 * @param map
	 * @param col
	 */
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
		processVertices();
	}

	/**
	 * Saves positions of John and Kate
	 * 
	 * @param rJ
	 * @param cJ
	 * @param rK
	 * @param cK
	 */
	public void J_K_Pos(int rJ, int cJ, int rK, int cK) {
		this.rJ = rJ;
		this.cJ = cJ;
		this.rK = rK;
		this.cK = cK;
	}

	// ------------------------------Private_Methods---------------------------//

	/**
	 * Runs the bellmanFord algorithm for John
	 * 
	 * @return shortest path(time)
	 */
	private int bellmanFordJohn() {
		int x = rJ;
		int y = cJ;

		int[] via = new int[numVertices];

		int[] length = new int[numVertices];
		for (int i = 0; i < numVertices; i++)
			length[i] = Integer.MAX_VALUE;

		int origin = vertices.get(Pair.of(x, y));

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

		return length[vertices.get(Pair.of(exit_x, exit_y))];
	}

	/**
	 * Runs the bellmanFord algorithm for Kate
	 * 
	 * @return shortest path(time)
	 */
	private int bellmanFordKate() {
		int x = rK;
		int y = cK;

		int[] via = new int[numVertices];

		int[] length = new int[numVertices];
		for (int i = 0; i < numVertices; i++)
			length[i] = Integer.MAX_VALUE;

		int origin = vertices.get(Pair.of(x, y));

		length[origin] = 0;
		via[origin] = origin;

		boolean changes = false;
		for (int i = 1; i < numVertices; i++) {
			changes = updateLengths(kateEdges, length, via);
			if (!changes)
				break;
		}

		return length[vertices.get(Pair.of(exit_x, exit_y))];
	}

	private boolean updateLengths(List<Edge> edges, int[] len, int[] via) {
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

	// ----------------------------Process_vertices-------------------------------//

	/**
	 * Processes the island(matrix of chars) and creates the vertices of the graph
	 * and the edges
	 */
	private void processVertices() {
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.col; j++) {
				char c = this.map[i][j];
				// if not Obstacle, saves vertices
				if (c != OBSTACLE) {
					boolean wasVertexAdd = false;

					if (vertices.putIfAbsent(Pair.of(i, j), numVertices) == null)
						wasVertexAdd = true;
					// Not WATER
					if (c != WATER)
						addEdgesJohn(i, j, c);

					addEdgesKate(i, j, c);
					if (wasVertexAdd)
						numVertices++;
				}
			}
		}
	}

	/**
	 * Adds John's edges
	 * 
	 * @param i
	 * @param j
	 * @param c
	 */
	private void addEdgesJohn(int i, int j, char c) {
		int currentVertex = vertices.get(Pair.of(i, j));
		// Add horizontal edge
		if (j > 0) { // can add edge with previous vertex
			char previousChar = this.map[i][j - 1];
			// GRASS or MW
			if (previousChar != WATER && previousChar != OBSTACLE) {
				int previousVertex = vertices.get(Pair.of(i, j - 1));

				if (previousChar != EXIT)
					johnEdges.add(new Edge(previousVertex, currentVertex, getWeightByTile(previousChar)));

				if (c != EXIT)
					johnEdges.add(new Edge(currentVertex, previousVertex, getWeightByTile(c)));
			}

		}
		// Add vertical edge
		if (i > 0) { // can add edge with upper vertex
			char upperChar = this.map[i - 1][j];

			if (upperChar != WATER && upperChar != OBSTACLE) {
				int upperVertex = vertices.get(Pair.of(i - 1, j));

				if (upperChar != EXIT)
					johnEdges.add(new Edge(upperVertex, currentVertex, getWeightByTile(upperChar)));

				if (c != EXIT)
					johnEdges.add(new Edge(currentVertex, upperVertex, getWeightByTile(c)));
			}

		}
		// Is Magical Wheel
		if (c != GRASS && c != EXIT) {
			int index = (c - '0') - 1;
			magicalWheels.put(index, numVertices);
		}
	}

	/**
	 * Adds Kate's edges
	 * 
	 * @param i
	 * @param j
	 * @param c
	 */
	private void addEdgesKate(int i, int j, char c) {
		int currentVertex = vertices.get(Pair.of(i, j));
		// Add horizontal edge
		if (j > 0) { // can add edge with previous vertex
			char previousChar = this.map[i][j - 1];

			// GRASS or MW
			if (previousChar != OBSTACLE) {
				int previousVertex = vertices.get(Pair.of(i, j - 1));

				if (previousChar != EXIT)
					kateEdges.add(new Edge(previousVertex, currentVertex, getWeightByTile(previousChar)));

				if (c != EXIT)
					kateEdges.add(new Edge(currentVertex, previousVertex, getWeightByTile(c)));
			}
		}
		// Add vertical edge
		if (i > 0) { // can add edge with upper vertex
			char upperChar = this.map[i - 1][j];

			if (upperChar != OBSTACLE) {
				int upperVertex = vertices.get(Pair.of(i - 1, j));

				if (upperChar != EXIT)
					kateEdges.add(new Edge(upperVertex, currentVertex, getWeightByTile(upperChar)));

				if (c != EXIT)
					kateEdges.add(new Edge(currentVertex, upperVertex, getWeightByTile(c)));
			}
		}
	}

	/**
	 * returns the weight of leaving a tile
	 * 
	 * @param tile
	 * @return weight
	 */
	private int getWeightByTile(char tile) {
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
