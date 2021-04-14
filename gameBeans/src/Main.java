import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import gameBeans.GameBeans;

public class Main {

	private static final String PIETON = "PIETON";
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		int testCases = Integer.parseInt(in.readLine());

		for (int i = 0; i < testCases; i++) {
			String[] p_d = in.readLine().split(" "); // Number of piles and depth
			int numberPiles = Integer.parseInt(p_d[0]);
			int depth = Integer.parseInt(p_d[1]);

			String[] pilesS = in.readLine().split(" "); // piles in String
			
			List<Integer> piles = new LinkedList<>(); // Array of piles

			short currentPlayer = 0;
			String startingPlayer = in.readLine();
			if(startingPlayer.toUpperCase().equals(PIETON)) // get the startingPlayer
				currentPlayer = 1;
			
			// Piles -> String to integer
			for (int j = 0; j < numberPiles; j++) {
				piles.add(Integer.parseInt(pilesS[j]));
			}
			
			GameBeans game = new GameBeans(depth, piles, numberPiles, currentPlayer);
			game.play();
			
			// Prints result
			System.out.println(game.getResult());
		}

	}

}
