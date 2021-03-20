package team;

public class TeamFormation {

	private int[][] constraints;
	private int[][] teams;

	private int roles, players;

	public TeamFormation(int roles, int players) {
		this.constraints = new int[roles][2];
		this.teams = new int[roles][players + 1];

		this.roles = roles;
		this.players = players;
	}
	
	/**
	 * role = index || type = true if MAX, false if MIN || value = number of players
	 * for that role
	 * 
	 * @param role
	 * @param type
	 * @param value
	 */
	public void addConstraint(int role, boolean type, int value) {
		int[] cons = getMaxAndMin(type, value);

		constraints[role][0] = cons[0];
		constraints[role][1] = cons[1];
	}

	public long numFormations() {
		if(roles == 0 && (players >= constraints[0][0] && players <= constraints[0][1])) {
			return 1;
		}else if(roles == 0 && (players < constraints[0][0] || players > constraints[0][1])){
			return 0;
		}
		// algorithm - here we use dynamic solution(page 17 - ada01-progDinamica)
		// use the for here
		return 0;
	}

	public boolean isFirstPhase() {
		return roles == 0;
	}

	// -------------------------------Private_Methods-------------------------//
	private int[] getMaxAndMin(boolean type, int value) {
		int[] cons = new int[2];
		if (!type) { // MIN
			cons[0] = value;
			cons[1] = players;
		} else { // MAX
			cons[0] = 0;
			cons[1] = value;
		}
		return cons;
	}
}
