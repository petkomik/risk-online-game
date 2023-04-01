package game.models;

/**
 * @author srogalsk
 */

import java.time.LocalTime;

import game.Profile;

public class PlayerMP extends Player {
	
	private Profile profile;
	
	public PlayerMP(Profile profile) {
		super(profile.getUserName(), profile.getId());
		this.profile = profile;
	}

	
//	public int getRank() {
//		int wins = profile.getWins();
//		int losses = profile.getLoses();
//		int rank = wins / (wins + losses);
//		return rank;
//	}
	
}
