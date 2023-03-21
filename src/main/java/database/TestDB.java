package database;

import game.Profile;

/**
 * class to test the database.
 *
 * @author jorohr
 */
public class TestDB {
	public static void main(String[]args) {
		
		PlayerProfileHandler dbH = new PlayerProfileHandler();
		
		dbH.deleteProfile(11111);
		Profile p1 = new Profile("userName", "firstName", "lastName", "password");
		Profile p2 = 	new Profile(113,  "userName", "firstName", "lastName", "color", 3, 2, "photo", "password", 1);
		
		dbH.createProfileData(p1);
		dbH.createProfileData(p2);

	}
}
