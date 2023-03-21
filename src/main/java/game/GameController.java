package game;

import database.PlayerProfileHandler;

/**
 * Class for the actual game logic handling
 * @author srogalsk
 *
 */

public class GameController {
	
	private Profile profile;
	
	public boolean createFirstProfile(String userName, String firstName, String lastName,  String password) {
		try {
		profile = new Profile(firstName, lastName, userName, password);
		PlayerProfileHandler dbH = new PlayerProfileHandler();
		dbH.createProfileData(profile);
		return true;
		} catch(Exception e) {
			return false;
		}
		
	}
	
	
	
	
	
}
