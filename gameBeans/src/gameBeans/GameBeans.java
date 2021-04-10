package gameBeans;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class GameBeans {

	private List<Pair<Integer, Integer>> piles; // List of piles
	private List<Pair<Integer, Integer>>[] pilesInPos;

	private int[][] pyramid;

	private int depth, result; // result = Jaba's points
	private short currentPlayer; // 0-> Jaba, 1-> Pieton

	public GameBeans(int depth, List<Pair<Integer, Integer>> piles, int pilesLength, short currentPlayer) {
		this.depth = depth;
		this.currentPlayer = currentPlayer;
		listToArray(piles, pilesLength);

		createDataStructure(pilesLength * depth * 2);

		pyramid = new int[pilesLength][pilesLength * depth];

		result = 0;
	}

	public void play() {
		if (currentPlayer == 0) {
			pilesInPos[0] = piles; // init list
			jabaPlays();
		} else {
			pietonPlays(0, piles);
		}
	}

	public int getResult() {
		return result;
	}

	// ----------------------------------Private_Methods--------------------------------//

	private void jabaPlays() {
		// Base case
		
		for(int i = 1; i <= depth + 1; i++) {
			for(int j = i-1; j <= depth; j++) {
				
			}
		}

	}

	// Options:
	// - First element
	// - First element + depth options(0 -> 0 + depth)
	// - Last element
	// - Last element + depth options(last -> last - depth)
	private void pietonPlays(int pos, List<Pair<Integer, Integer>> pilesAux) {
		boolean isFromStart = true;

		int max = piles.get(0).getValue(); // first element
		int length = 1; // amount of piles to remove
		int lastIndex = piles.size() - 1;

		// Base case
		if (depth == 1) {
			if (max > piles.get(lastIndex).getValue()) // compares first with last element
				removePilesAux(pilesAux, pos, true, 1);
			else
				removePilesAux(pilesAux, pos, false, 1);
			return;
		}

		// from first
		int lastSum = 0;
		int current = piles.get(0).getValue(); // starts has second element
		int sum = lastSum + current;

		if (sum > max) { // check first with second
			max = sum;
			length = 2;
		}

		// from last
		int lastSumL = 0;
		int currentL = piles.get(lastIndex).getValue(); // starts has penultimate element
		int sumL = lastSumL + currentL;

		for (int i = 1; i < depth || i < pilesAux.size(); i++) {
			// from first
			lastSum = sum;
			current = piles.get(i).getValue();
			sum = lastSum + current;
			if (sum > max || (sum == max && !isFromStart)) {
				max = sum;
				length = i + 1;
				isFromStart = true;
			}

			// from last
			lastSumL = sumL;
			currentL = piles.get(lastIndex - i).getValue();
			sumL = lastSumL + currentL;
			if (sumL > max) {
				max = sumL;
				length = i + 1;
				isFromStart = false;
			}
		}

		// from last
		lastSumL = 0;
		currentL = piles.get(lastIndex).getValue(); // starts has penultimate element
		sumL = lastSumL + currentL;

		if (currentL > max || (sum == max && !isFromStart)) { // check last
			max = currentL;
			length = 1;
			isFromStart = false;
		}
		if (sumL > max || (sum == max && !isFromStart && length != 1)) { // check last with penultimate
			max = sumL;
			length = 2;
			isFromStart = false;
		}

		if (pos == 0)
			removePiles(isFromStart, length);
		else
			removePilesAux(pilesAux, pos, isFromStart, length);
	}

	// --------------------------RemovePiles-------------------------------//

	/**
	 * Removes one or more elements from the pileAux
	 * 
	 * @param isFromStart
	 * @param length
	 */
	private void removePilesAux(List<Pair<Integer, Integer>> pilesAux, int pos, boolean isFromStart, int length) {
		for (int i = 0; i < length; i++) {
			if (isFromStart)
				pilesAux.remove(0);
			else
				pilesAux.remove(piles.size() - 1);
		}
		pilesInPos[pos] = pilesAux;
	}

	/**
	 * Removes one or more elements from the pile
	 * 
	 * @param isFromStart
	 * @param length
	 */
	private void removePiles(boolean isFromStart, int length) {
		for (int i = 0; i < length; i++) {
			if (isFromStart)
				piles.remove(0);
			else
				piles.remove(piles.size() - 1);
		}
		pilesInPos[0] = piles;
	}

	// -----------------------------------DataStructs--------------------------------//

	/**
	 * Transform the list received in constructor to an ArrayList
	 * 
	 * @param list
	 * @param pilesLength
	 */
	private void listToArray(List<Pair<Integer, Integer>> list, int pilesLength) {
		piles = new ArrayList<>(list);
	}

	@SuppressWarnings("unchecked")
	private void createDataStructure(int vecLength) {
		pilesInPos = new List[vecLength];
		for (int i = 0; i < pilesInPos.length; i++)
			pilesInPos[i] = new LinkedList<>();
	}

}
