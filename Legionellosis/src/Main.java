import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import legionellosis.Legionellosis;

public class Main {

	public static void main(String[] args) throws IOException {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		String[] L_C = in.readLine().split(" ");
		int L = Integer.parseInt(L_C[0]);
		int C = Integer.parseInt(L_C[1]);

		Legionellosis leg = new Legionellosis(L);
		for (int i = 0; i < C; i++) {
			String[] l1_l2 = in.readLine().split(" ");
			int l1 = Integer.parseInt(l1_l2[0]);
			int l2 = Integer.parseInt(l1_l2[1]);

			leg.addEdge(l1, l2);
		}
		int S = Integer.parseInt(in.readLine());
		
		// Updates the interviews done
		leg.updateSickPeople(S);
		
		for (int i = 0; i < S; i++) {
			String[] h_d = in.readLine().split(" ");
			int h = Integer.parseInt(h_d[0]);
			int d = Integer.parseInt(h_d[1]);
			
			leg.addMovement(h, d);
		}
		
		List<Integer> result = new ArrayList<>(leg.getPerilousLocations());
		for(Integer r: result)
			System.out.print(r);
	}

}
