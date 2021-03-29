package gameBeans;

import java.util.List;

public class GameBeans {

	private List<Integer> piles; // List of piles

	private int depth, result, pilesLeft; // result = Jaba's points
	private short currentPlayer; // 0-> Jaba, 1-> Pieton

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
	// - First element + depth options(0 -> 0 + depth)
	// - Last element
	// - Last element + depth options(last -> last - depth)
	private void pietonPlays() {
		boolean isFromStart = true;

		int max = piles.get(0); // first element
		int length = 1; // amount of piles to remove
		int lastIndex = piles.size() - 1;

		if (depth == 1) {
			if (max > piles.get(lastIndex)) // compares first with last element
				piles.remove(0);
			else
				piles.remove(lastIndex);
		}
		// from first
		int lastSum = piles.get(0); // starts has first element
		int current = piles.get(1); // starts has second element
		int sum = lastSum + current;

		if (sum > max) { // check first with second
			max = sum;
			length = 2;
		}
		// from last
		int lastSumL = piles.get(lastIndex); // starts has last element
		int currentL = piles.get(lastIndex - 1); // starts has penultimate element
		int sumL = lastSumL + currentL;

		if (lastSumL > max) { // check last
			max = lastSumL;
			length = 1;
			isFromStart = false;
		}
		if (sumL > max) { // check last with penultimate
			max = sumL;
			length = 2;
			isFromStart = false;
		}

		for (int i = 2; i < depth; i++) {
			// from first
			lastSum = sum;
			current = piles.get(i);
			sum = lastSum + current;
			if (sum > max) {
				max = sum;
				length = i + 1;
				isFromStart = true;
			}
			// from last
			lastSumL = sumL;
			currentL = piles.get(lastIndex - i);
			sumL = lastSumL + currentL;
			if (sumL > max) {
				max = sumL;
				length = i + 1;
				isFromStart = false;
			}
		}
		removePiles(isFromStart, length);
	}

	private void removePiles(boolean isFromStart, int length) {

		for (int i = 0; i < length; i++) {
			if (isFromStart)
				piles.remove(0);
			else
				piles.remove(piles.size() - 1);
		}
	}

}
