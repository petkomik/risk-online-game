package database;

import java.time.LocalTime;

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
		DatabaseHandler dbH = new DatabaseHandler();
		Player p1 = new Player(11111, 4, "Max", "blue", "C:\\user\\exmaplePath\\furtherExample\\photo.jpg", LocalTime.now(), true);
		dbH.createPlayerData(p1);
	}
}
