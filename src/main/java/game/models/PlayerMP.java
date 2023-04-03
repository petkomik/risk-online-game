package game.models;

import game.Profile;
import network.ClientHandler;

public class PlayerMP extends Player {
	
	private Profile profile;
	
	public PlayerMP(Profile profile, ClientHandler clientHandler) {
		super(profile.getUserName(), profile.getId(), clientHandler);
		this.profile = profile;
	}

	
//	public int getRank() {
//		int wins = profile.getWins();
//		int losses = profile.getLoses();
//		int rank = wins / (wins + losses);
//		return rank;
//	}
	
}
