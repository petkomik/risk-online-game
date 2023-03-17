//@author Stefan Rogalski

package game.models;

import java.time.LocalTime;

import game.Profile;

public class PlayerMP extends Player {
	
	private Profile profile;
	
	public PlayerMP(Profile profile) {
		
		this.profile = profile;
	}

}
