import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import lost.Lost;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		// Number of test cases
		int N = Integer.parseInt(in.readLine());
		for (int i = 0; i < N; i++) {
			// Rows, Columns and Magical wheels
			String[] R_C_M = in.readLine().split(" ");
			int R = Integer.parseInt(R_C_M[0]);
			int C = Integer.parseInt(R_C_M[1]);
			int M = Integer.parseInt(R_C_M[2]);

			Lost lost = new Lost(R, C, M);
			// Populates the map
			String[] map = new String[R];
			for (int j = 0; j < R; j++) {
				String line = in.readLine();
				map[j] = line;
			}
			lost.processLine(map, C);

			// Position of each Magical wheel
			for (int j = 0; j < M; j++) {
				String[] dst = in.readLine().split(" ");
				int r = Integer.parseInt(dst[0]);
				int c = Integer.parseInt(dst[1]);
				int t = Integer.parseInt(dst[2]);

				lost.savesMagicalWheel(r, c, t);
			}
			// John's and Kate's position
			String[] pos = in.readLine().split(" ");
			int rJ = Integer.parseInt(pos[0]);
			int cJ = Integer.parseInt(pos[1]);
			int rK = Integer.parseInt(pos[2]);
			int cK = Integer.parseInt(pos[3]);

			lost.J_K_Pos(rJ, cJ, rK, cK);
			int[] results = lost.processResult();

			System.out.println("Case #" + i + 1);

			if (results[0] == Lost.LOST_IN_TIME)
				System.out.println("John " + Lost.LOST_IN_TIME_S);
			else if (results[0] == Lost.UNREACHABLE)
				System.out.println("John " + Lost.UNREACHABLE_S);
			else
				System.out.println("John " + results[0]);

			if (results[1] == Lost.LOST_IN_TIME)
				System.out.println("Kate " + Lost.LOST_IN_TIME_S);
			else if (results[1] == Lost.UNREACHABLE)
				System.out.println("Kate " + Lost.UNREACHABLE_S);
			else
				System.out.println("Kate " + results[1]);
		}
		in.close();
	}

}
