package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Database class to be extended for access to the risiko-database.db
 *
 * @author jorohr
 */
public class Database {

	protected Connection connection;
	protected Properties properties;

	public Database() {
		this.connect(general.Parameter.resourcesdir + general.Parameter.dbName);
		properties = new Properties();
		properties.setProperty("PRAGMA foreign_keys", "ON");
	}

	private void connect(String file) {
		try {
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + file);
			connection.setAutoCommit(false);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Database was opened"); 
	}

	protected void disconnect(){
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Database was closed");
	}
	
	protected void dropTable(String name) {
		try (Statement statement = connection.createStatement()) {
			String sql = "DROP TABLE IF EXISTS " + name;
			statement.executeUpdate(sql);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
