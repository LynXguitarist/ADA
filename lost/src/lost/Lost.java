package lost;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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
	private List<Pair<Integer, Integer>> edges;
	private char[][] map;
	private Map<Pair<Integer, Integer>, Integer> magicalWheels; // ED for MW

	private int row, col;
	private int numVertices;
	private int rJ, cJ, rK, cK;
	private int exit_x, exit_y;

	public Lost(int row, int col, int numberMW) {
		graph = new HashMap<>(row * col);
		edges = new ArrayList<>(row * col);
		
		map = new char[row][col];
		magicalWheels = new HashMap<>(numberMW);

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

		results[0] = processJohn();
		results[1] = processKate();

		return results;
	}

	public void processMap(String[] map, int col) {
		int row = 0;
		for (String line : map) {
			for (int i = 0; i < col; i++) {
				char c = line.charAt(i);
				this.map[row][i] = c;
				// saves the position of the exit 
				if(c == EXIT) {
					exit_x = row;
					exit_y = i;
				}
				// if not Obstacle, saves vertices
				if(c != OBSTACLE) {
					graph.put(Pair.of(row, i), numVertices);
					// saving edges
					if(i > 0) {
						edges.add(Pair.of(numVertices, numVertices-1));
						// VALE A PENA GUARDAR AS DUAS LIGACOES?
					}
					if(row > 0) {
						edges.add(Pair.of(numVertices, graph.get(Pair.of(i, row - 1))));
						// VALE A PENA GUARDAR AS DUAS LIGACOES?
					}
					numVertices++;
				}
			}
			row++;
		}
	}

	public void savesMagicalWheel(int r, int c, int t) {
		magicalWheels.put(Pair.of(r, c), t);
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
	// Probably use BellmanFord
	
	private int processJohn() {
		Queue<Integer> via = new ArrayDeque<>(numVertices);
		
		int[] distances = new int[numVertices];
		for(int i = 0; i < row; i++) {
			distances[i] = Integer.MAX_VALUE;
		}
		int currVertice = graph.get(Pair.of(rJ, cJ));
		
		distances[currVertice]= 0;
		via.add(currVertice);
		
		boolean changes = false;
		for(int i = 1; i < row * col; i++) {
			changes = updateLengths(distances, via);
			if(!changes)
				break;
		}
		// Negative-weight cycles 
		if(changes && updateLengths(distances, via)) {
			// Lost in time
		}
		
		return distances[graph.get(Pair.of(exit_x, exit_y))];
	}

	private int processKate() {

		return 0;
	}
	
	private boolean updateLengths(int[]distances, Queue<Integer> via) {
		boolean changes = false;
		int current = via.poll();
		// Tests all edges with x, y position-> via
		for(int i = 0; i < 1; i++) { // dummy for
			if(distances[0] < Integer.MAX_VALUE) {
				distances[0] = distances[current] + 1; // depends if water or bla bla
				changes = true;
			}
		}
		
		return changes;
	}
}
