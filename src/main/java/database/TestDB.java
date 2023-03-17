package database;

import java.time.LocalTime;

import game.Profile;
import game.models.Player;

/**
 * class to test the database.
 *
 * @author jorohr
 */
public class TestDB {
	public static void main(String[]args) {
		CreateDatabase db = new CreateDatabase();
		db.createTables();
		PlayerProfileHandler dbH = new PlayerProfileHandler();
		
		dbH.deleteProfile(11111);
		Profile p1 = new Profile(11,  "userName", "firstName", "lastName", "color", 3, 2, "photo", "password", 1);
		Profile p2 = 	new Profile(113,  "userName", "firstName", "lastName", "color", 3, 2, "photo", "password", 1);
		
		dbH.createProfileData(p1);
		dbH.createProfileData(p2);

	}
}
