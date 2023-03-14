package database;

/**
 * class to test the database.
 *
 * @author jorohr
 */
public class TestDB {
	public static void main(String[]args) {
		CreateDatabase db = new CreateDatabase();
		db.createTables();
	}
}
