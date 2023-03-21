package game;

import database.CreateDatabase;
import database.PlayerProfileHandler;

/**
 * Class for the actual game logic handling
 * @author srogalsk
 *
 */

public class GameController {
	
	private Profile profile;
	private static CreateDatabase db;
	
	public boolean createFirstProfile(String userName, String firstName, String lastName,  String password) {
		try {
		profile = new Profile(firstName, lastName, userName, password);
		db = new CreateDatabase();
		db.createTables();
		PlayerProfileHandler dbH = new PlayerProfileHandler();
		dbH.createProfileData(profile);
		return true;
		} catch(Exception e) {
			return false;
		}
		
	}
	
	
	
	
	
}
