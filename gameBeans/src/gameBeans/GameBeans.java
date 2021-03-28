package gameBeans;

import java.util.LinkedList;
import java.util.List;

public class GameBeans {

	private static final int SMALLEST_SIZE = -100; // its said in the description that -100 is the smallest size

	private List<Integer> piles; // List of piles

	private int depth, result, pilesLeft;
	private short currentPlayer; // 0->Jaba, 1->Pieton

	public GameBeans(int depth, List<Integer> piles, int pilesLength, short currentPlayer) {
		this.depth = depth;
		this.piles = piles;
		this.currentPlayer = currentPlayer;

		result = 0;
		pilesLeft = pilesLength;
	}

	public void play() {
		while (pilesLeft > 0) {
			if (currentPlayer == 0)
				jabaPlays();
			else
				pietonPlays();
		}
	}

	public int getResult() {
		return result;
	}

	// ----------------------------------Private_Methods--------------------------------//

	private void jabaPlays() {

	}

	// Options:
	// - First element
	// - First to First + depth options(0 -> 0 + depth)
	// - Last element
	// - Last element + depth options(last -> last - depth)
	private void pietonPlays() {
		int max = SMALLEST_SIZE;
		List<Integer> currentOption = new LinkedList<>();
		
		// Has depth * 2 options
		for(int i = 0; i < depth * 2; i++) {
			
		}
	}

}
