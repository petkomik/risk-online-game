package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Database class to be extended for access to the risiko-database.db
 */
public class Database {

  protected Connection connection;
  protected Properties properties;

  /**
   * Constructor.
   */
  public Database() {
    this.connect(general.Parameter.resourcesdir + general.Parameter.dbName);
    properties = new Properties();
    properties.setProperty("PRAGMA foreign_keys", "ON");
    this.createTables();
  }

  /**
   * Connect to database.
   *
   * @param file file to connect to
   */
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

  /**
   * Disconnect from database.
   */
  public void disconnect() {
    try {
      this.connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    System.out.println("Database was closed");
  }

  /**
   * Drop table from database.
   *
   * @param name name of table to drop
   */
  protected void dropTable(String name) {
    try (Statement statement = connection.createStatement()) {
      String sql = "DROP TABLE IF EXISTS " + name;
      statement.executeUpdate(sql);
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Create tables in database.
   */
  public void createTables() {
    this.createTable_Profiles();
    this.createTable_Games();
  }

  /**
   * Create Profiles table.
   */
  private void createTable_Profiles() {
    try (Statement stm = connection.createStatement()) {
      String sql = "CREATE TABLE IF NOT EXISTS Profiles "
          + "(PlayerID INTGEGER PRIMARY KEY, "
          + "UserName TEXT NOT NULL, "
          + "FirstName TEXT NOT NULL, "
          + "LastName TEXT NOT NULL, "
          + "Wins INTEGER, "
          + "Color TEXT, "
          + "Photo TEXT, "
          + "Loses INTEGER, "
          + "Password TEXT NOT NULL, "
          + "Score INTEGER, "
          + "IsPersonal BIT NOT NULL);";
      stm.executeUpdate(sql);
      connection.commit();

      System.out.println("Table Profiles was created...");
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(0);
    }
  }

  /**
   * Create Games table.
   */
  private void createTable_Games() {
    try (Statement stm = connection.createStatement()) {
      String sql = "CREATE TABLE IF NOT EXISTS Games "
          + "(GameID INTEGER PRIMARY KEY, "
          + "Length INTEGER, "
          + "StartTime INTEGER NOT NULL, "
          + "NumberOfPlayers INTEGER NOT NULL, "
          + "WinnerID INTEGER REFERENCES Players);";
      stm.executeUpdate(sql);
      connection.commit();

      System.out.println("Table Games was created...");
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(0);
    }
  }
}