package lost;

import java.util.HashMap;
import java.util.Map;

public class Lost {

	private static final char GRASS = 'G';
	private static final char OBSTACLE = 'O';
	private static final char WATER = 'W';

	public static final int LOST_IN_TIME = Integer.MIN_VALUE;
	public static final int UNREACHABLE = Integer.MAX_VALUE;
	public static final String LOST_IN_TIME_S = "Lost in Time";
	public static final String UNREACHABLE_S = "Unreachable";

	private char[][] map;
	private Map<Pair<Integer, Integer>, Integer> magicalWheels; // ED for MW

	private int rJ, cJ, rK, cK;

	public Lost(int row, int col, int numberMW) {
		map = new char[row][col];
		magicalWheels = new HashMap<>(numberMW);

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

	public void processLine(String[] map, int col) {
		int row = 0;
		for (String line : map) {
			for (int i = 0; i < col; i++) {
				char c = line.charAt(i);
				this.map[row][i] = c;
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

		return 0;
	}

	private int processKate() {

		return 0;
	}
}
