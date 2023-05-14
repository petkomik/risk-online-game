package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Contains methods to get the profile data from the database and store it on the database.
 *
 * @author jorohr
 */
public class DatabaseHandler extends Database {
  private PreparedStatement stm1;

  /**
   * Creates a new instance of DatabaseHandler.
   */
  public DatabaseHandler() {
    super();
  }

  /**
   * Inserts the given Profile data into the database.
   *
   * @param p the Profile object to insert.
   */
  public void createProfileData(Profile p) {
    try {
      String sql = "INSERT INTO Profiles(PlayerID, UserName, FirstName, LastName,"
          + " Color, Wins, Loses, Photo, Password) " + "VALUES (?,?,?,?,?,?,?,?,?);";
      stm1 = super.connection.prepareStatement(sql);

      stm1.setInt(1, p.getId());
      stm1.setString(2, p.getUserName());
      stm1.setString(3, p.getFirstName());
      stm1.setString(4, p.getLastName());
      stm1.setString(5, p.getColor());
      stm1.setInt(6, p.getWins());
      stm1.setInt(7, p.getLoses());
      stm1.setString(8, p.getPhoto());
      stm1.setString(9, p.getPassword());

      stm1.executeUpdate();
      this.connection.commit();
      stm1.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Deletes the Profile data for the given id from the database.
   *
   * @param id the id of the Profile data to delete
   */
  public void deleteProfile(int id) {
    try {
      String sql = "DELETE FROM Profiles WHERE PlayerID = " + id + ";";
      Statement stm = super.connection.createStatement();
      stm.executeUpdate(sql);
      this.connection.commit();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Updates the given attribute of the given Profile object in the database.
   *
   * @param value the new value of the attribute.
   * @param attribute the attribute to update.
   * @param id the Profile object to update.
   */
  public void updateProfileInfo(String value, String attribute, int id) {
    try {
      String sql = "UPDATE Profiles SET " + attribute + " = '" + value + "' WHERE PlayerID = " + id;
      Statement stm = super.connection.createStatement();
      stm.executeUpdate(sql);
      this.connection.commit();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Updates the given attribute of the given Profile object in the database.
   *
   * @param value the new value of the attribute.
   * @param attribute the attribute to update.
   * @param id the Profile object to update.
   */
  public void updateProfileInfo(int value, String attribute, int id) {
    try {
      String sql = "UPDATE Profiles SET " + attribute + " = '" + value + "' WHERE PlayerID = " + id;
      Statement stm = super.connection.createStatement();
      stm.executeUpdate(sql);
      this.connection.commit();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Retrieves the Profile data for the given id from the database.
   *
   * @param id the id of the Profile data to retrieve.
   * @return the retrieved Profile object, or null if no Profile data was found for the given id.
   */
  public Profile getProfileById(int id) {
    try (Statement stm = this.connection.createStatement()) {
      String sql = "SELECT * FROM Profiles WHERE PlayerID= " + id + ";";

      ResultSet rs = stm.executeQuery(sql);
      if (rs.next()) {
        String color = rs.getString("Color");
        String userName = rs.getString("UserName");
        String firstName = rs.getString("firstName");
        String lastName = rs.getString("lastName");
        int wins = rs.getInt("Wins");
        int loses = rs.getInt("Loses");
        String photo = rs.getString("Photo");
        String password = rs.getString("password");

        return new Profile(id, userName, firstName, lastName, color, photo, wins, loses, password);
      }
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Retrieves all profiles from the Profiles table in the database and returns them as an ArrayList
   * of Profile objects.
   *
   * @return ArrayList of Profile objects representing all profiles in the database
   */
  public ArrayList<Profile> getAllProfiles() {
    ArrayList<Profile> list = new ArrayList<Profile>();
    try (Statement stm = this.connection.createStatement()) {
      String sql = "SELECT * FROM Profiles;";

      ResultSet rs = stm.executeQuery(sql);
      while (rs.next()) {
        int id = rs.getInt("PlayerID");
        String color = rs.getString("Color");
        String userName = rs.getString("UserName");
        String firstName = rs.getString("FirstName");
        String lastName = rs.getString("lastName");
        int wins = rs.getInt("Wins");
        int loses = rs.getInt("Loses");
        String photo = rs.getString("Photo");
        String password = rs.getString("password");

        list.add(
            new Profile(id, userName, firstName, lastName, color, photo, wins, loses, password));
      }
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  /**
   * Retrieves the game statistic with the specified game ID from the Games table in the database.
   *
   * @param gameId the ID of the game whose statistic is to be retrieved
   * @return the GameStatistic object with the specified game ID, or null if no such statistic is
   *         found
   */
  public GameStatistic getGameStatistic(int gameId) {
    try (Statement stm = this.connection.createStatement()) {
      String sql = "SELECT * FROM Games WHERE GameID = " + gameId + ";";

      ResultSet rs = stm.executeQuery(sql);
      if (rs.next()) {
        int length = rs.getInt("Length");
        LocalDateTime startTime = LocalDateTime.parse(rs.getString("StartTime"));
        int winnerId = rs.getInt("WinnerID");
        int numbPlayers = rs.getInt("NumberOfPlayers");
        return new GameStatistic(gameId, length, startTime, winnerId, numbPlayers);
      }
      rs.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Inserts a new game statistic into the Games table in the database.
   *
   * @param g the GameStatistic object to be inserted
   */
  public void createGameStatistic(GameStatistic g) {
    try {
      String sql =
          "INSERT INTO Games(GameID, StartTime, NumberOfPlayers, Length) VALUES (?,?,?,?);";
      stm1 = super.connection.prepareStatement(sql);

      stm1.setInt(1, g.getGameId());
      stm1.setString(2, g.getStartTime().toString());
      stm1.setInt(3, g.getNumberOfPlayers());
      stm1.setInt(4, 0);

      stm1.executeUpdate();
      this.connection.commit();
      stm1.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Updates a specified attribute of a game statistic in the Games table in the database.
   *
   * @param value the new value to be assigned to the attribute
   * @param attribute the name of the attribute to be updated
   * @param g1 the GameStatistic object whose attribute is to be updated
   */
  public void updateGameStatistic(String value, String attribute, GameStatistic g1) {
    try {
      String sql =
          "UPDATE Games SET " + attribute + " = '" + value + "' WHERE GameID = " + g1.getGameId();
      Statement stm = super.connection.createStatement();
      stm.executeUpdate(sql);
      this.connection.commit();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
