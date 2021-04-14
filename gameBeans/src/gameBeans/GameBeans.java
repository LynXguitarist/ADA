package gameBeans;

import java.util.ArrayList;
import java.util.List;

public class GameBeans {

	private static final short JABA = 0;
	private static final int SMALLEST = Integer.MIN_VALUE;

	private List<Integer> piles; // Array of piles

	private int[][] pyramidJaba; // Jaba's points if Jaba is playing
	private int[][] pyramidPieton; // Jaba's points if Pieton is playing

	private int pilesLength, depth; // result = Jaba's points
	private short currentPlayer; // 0-> Jaba, 1-> Pieton

	public GameBeans(int depth, List<Integer> piles, int pilesLength, short currentPlayer) {
		this.pilesLength = pilesLength;
		this.depth = depth;
		
		if (depth > 10)
			depth = Math.min(10, pilesLength);
		
		this.currentPlayer = currentPlayer;

		listToArray(piles, pilesLength);

		pyramidJaba = new int[pilesLength + 1][pilesLength + 1];
		pyramidPieton = new int[pilesLength + 1][pilesLength + 1];
	}

	public void play() {
		for (int j = 0; j <= pilesLength; j++) {
			for (int i = 1; i <= pilesLength + 1 - j; i++) {
				S(i, j + i - 1, currentPlayer);
			}
		}
	}

	public int getResult() {
		if (currentPlayer == JABA)
			return pyramidJaba[0][pilesLength];
		else
			return pyramidPieton[0][pilesLength];
	}

	// ----------------------------------Private_Methods--------------------------------//

	private void S(int i, int j, short p) {
		jabaPlays(i, j);
	}

	private void jabaPlays(int i, int j) {
		int posI = i - 1;

		// Base case
		if (j < i) {
			pyramidJaba[posI][j] = 0;
			pyramidPieton[posI][j] = 0;
			return;
		} else if (j == i) {
			pyramidJaba[posI][j] = piles.get(posI);
			pyramidPieton[posI][j] = 0;
			return;
		} else if (j == i + 1 && depth == 1) {
			pyramidJaba[posI][j] = Math.max(piles.get(posI), piles.get(j - 1));
			pyramidPieton[posI][j] = Math.min(piles.get(posI), piles.get(j - 1));
			return;
		}

		// General case
		int limit = Math.min(depth, j - i + 1);
		int maxSum = SMALLEST;

		// from left
		int current = 0;
		int last = 0;

		// from right
		int currentRight = 0;
		int lastRight = 0;

		for (int k = 0; k < limit; k++) {
			// from left
			current = last + piles.get(posI + k);
			last = current;
			int leftChoice = current + pyramidPieton[posI + k + 1][j];

			maxSum = Math.max(leftChoice, maxSum);

			// from right
			currentRight = lastRight + piles.get(j - 1 - k);
			lastRight = currentRight;
			int rightChoice = currentRight + pyramidPieton[posI][j - k - 1];

			maxSum = Math.max(rightChoice, maxSum);
		}
		pyramidJaba[posI][j] = maxSum;
		// saves Jaba's points if Pieton was first playing
		pietonPlays(i, j);
	}

	private void pietonPlays(int i, int j) {
		boolean isFromStart = true;
		int posI = i - 1;

		int max = piles.get(posI); // first element
		int length = 1; // amount of piles to remove

		// Base case
		if (depth == 1) {
			if (max >= piles.get(j - 1)) // compares first with last element
				pyramidPieton[posI][j] = pyramidJaba[posI + 1][j];
			else
				pyramidPieton[posI][j] = pyramidJaba[posI][j - 1];
			return;
		}

		// from first
		int lastSum = 0;
		int current = piles.get(posI); // starts has second element
		int sum = lastSum + current;

		// from last
		int lastSumL = 0;
		int currentL = piles.get(j - 1); // starts has penultimate element
		int sumL = lastSumL + currentL;

		int limit = Math.min(depth, j - i + 1);

		for (int k = 1; k < limit; k++) {
			// from first
			lastSum = sum;
			current = piles.get(k + posI);
			sum = lastSum + current;
			if (sum > max || (sum == max && !isFromStart)) {
				max = sum;
				length = k + 1;
				isFromStart = true;
			}

			// from last
			lastSumL = sumL;
			currentL = piles.get(j - 1 - k);
			sumL = lastSumL + currentL;
			if (sumL > max) {
				max = sumL;
				length = k + 1;
				isFromStart = false;
			}
		}

		// from last
		lastSumL = 0;
		currentL = piles.get(j - 1); // starts has penultimate element
		sumL = lastSumL + currentL;

		if (sumL > max || (sumL == max && !isFromStart)) { // check last
			max = sumL;
			length = 1;
			isFromStart = false;
		}

		if (isFromStart) {
			pyramidPieton[posI][j] = pyramidJaba[posI + length][j];
		} else {
			pyramidPieton[posI][j] = pyramidJaba[posI][j - length];
		}
	}

	// ------------------------------------DataStructs----------------------------------//

	/**
	 * Transform the list received in constructor to an ArrayList
	 * 
	 * @param list
	 * @param pilesLength
	 */
	private void listToArray(List<Integer> list, int pilesLength) {
		piles = new ArrayList<>(list);
	}

}
