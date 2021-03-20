import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import team.TeamFormation;

public class Main {

	private static final String TYPE_MAX = "MAX";

	public static void main(String[] args) throws NumberFormatException, IOException {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		int players = Integer.parseInt(in.readLine());
		int roles = Integer.parseInt(in.readLine());

		// roles-1 because roles go from 0 to r-1
		TeamFormation team = new TeamFormation(roles - 1, players);

		for (int i = 0; i < roles; i++) {
			String[] line = in.readLine().split(" ");

			String modifier = line[0].toUpperCase();
			int number = Integer.parseInt(line[1]);

			team.addConstraint(i, modifier.equals(TYPE_MAX), number);
		}
		System.out.println(team.numFormations());
	}

}
