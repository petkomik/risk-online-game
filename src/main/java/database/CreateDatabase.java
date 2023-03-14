package database;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Create a database for the game risk.
 *
 * @author jorohr
 */
public class CreateDatabase extends Database {
	
	public CreateDatabase() {
		super();
	}

	public void createTables() {
		CreateDatabase cdb = new CreateDatabase();
		cdb.createTable_Players();
		cdb.createTable_Games();
		cdb.disconnect();
	}

	private void createTable_Players() {

		try (Statement stm = connection.createStatement()){
			String sql = "CREATE TABLE IF NOT EXISTS Players "
					+ "(PlayerID INTGEGER PRIMARY KEY, "
					+ "Name TEXT NOT NULL, "
					+ "Wins INTEGER NOT NULL, "
					+ "Color TEXT NOT NULL,"
					+ "Photo TEXT NOT NULL, "
					+ "GameTime TEXT NOT NULL, "
					+ "IsPersonal BIT NOT NULL);";
			stm.executeUpdate(sql);
			connection.commit();
			
			System.out.println("Table Players was created...");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void createTable_Games() {
		try (Statement stm = connection.createStatement()){
			String sql = "CREATE TABLE IF NOT EXISTS Games "
					+ "(GameID INTEGER PRIMARY KEY, "
					+ "Length INTEGER NOT NULL, "
					+ "StartTime INTEGER NOT NULL, "
					+ "NumberOfPlayers INTEGER NOT NULL, "
					+ "WinnerID INTEGER NOT NULL REFERENCES Players);";
			stm.executeUpdate(sql);
			connection.commit();
			
			System.out.println("Table Games was created...");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
