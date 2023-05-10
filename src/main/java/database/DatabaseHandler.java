package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * contains methods to get the Profile data from the database and store it on the database.
 *
 * @author jorohr
 */
public class DatabaseHandler extends Database {
	
	private PreparedStatement stm1;
	private PreparedStatement stm2;
	
	public DatabaseHandler() {
		super();
	}
	
	public void createProfileData(Profile p) {
		try {
			String sql = "INSERT INTO Profiles(PlayerID, UserName, FirstName, LastName, Color, Wins, Loses, Photo, Password, IsPersonal) VALUES (?,?,?,?,?,?,?,?,?,?);";
			stm1 = super.connection.prepareStatement(sql);
			
			stm1.setInt(1,p.getId());
			stm1.setString(2, p.getUserName());
			stm1.setString(3, p.getFirstName());
			stm1.setString(4, p.getLastName());
			stm1.setString(5,p.getColor());
			stm1.setInt(6,p.getWins());
			stm1.setInt(7, p.getLoses());
			stm1.setString(8,p.getPhoto());
			stm1.setString(9, p.getPassword());
			stm1.setInt(10, p.isPersonal());		
			
			stm1.executeUpdate();
			this.connection.commit();
			stm1.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteProfile(int id) {
		try {
			String sql = "DELETE FROM Profiles WHERE PlayerID = " + id + ";";
			Statement stm = super.connection.createStatement();
			stm.executeUpdate(sql);
			this.connection.commit();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateProfileInfo(String value,String attribute, Profile p1) {
		try {
			String sql = "UPDATE Profiles SET " + attribute + " = '" + value + "' WHERE PlayerID = " + p1.getId();
			Statement stm = super.connection.createStatement();
			stm.executeUpdate(sql);
			this.connection.commit();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Profile getProfileByID(int id) {
		try(Statement stm = this.connection.createStatement()){
			String sql = "SELECT * FROM Profiles WHERE PlayerID= " + id + ";";
			
			ResultSet rs = stm.executeQuery(sql);
			if(rs.next()) {
				String color = rs.getString("Color");
				String userName = rs.getString("UserName");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				int wins = rs.getInt("Wins");
				int loses = rs.getInt("Loses");
				String photo = rs.getString("Photo");	
				String password = rs.getString("password");
				int isPersonal = 1;
				
				return new Profile(id, userName, firstName, lastName, color, wins, loses, photo, password, isPersonal);
			}
			rs.close();					
		}catch(SQLException e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	public ArrayList<Profile> getAllProfiles(){
		ArrayList<Profile> list = new ArrayList<Profile>();
		try(Statement stm = this.connection.createStatement()){
			String sql = "SELECT * FROM Profiles;";
			
			ResultSet rs = stm.executeQuery(sql);
			while(rs.next()) {
				int id = rs.getInt("PlayerID");
				String color = rs.getString("Color");
				String userName = rs.getString("UserName");
				String firstName = rs.getString("FirstName");
				String lastName = rs.getString("lastName");
				int wins = rs.getInt("Wins");
				int loses = rs.getInt("Loses");
				String photo = rs.getString("Photo");	
				String password = rs.getString("password");
				int isPersonal = 1;
				
				list.add(new Profile(id, userName, firstName, lastName, color, wins, loses, photo, password, isPersonal));
			}
			rs.close();					
		}catch(SQLException e) {
			e.printStackTrace();
		}	
		return list;
	}
	public GameStatistic getGameStatistic(int gameID) {
		try(Statement stm = this.connection.createStatement()){
			String sql = "SELECT * FROM Games WHERE GameID = " + gameID + ";";
			
			ResultSet rs = stm.executeQuery(sql);
			if(rs.next()) {
				int length = rs.getInt("Length");
				LocalDateTime startTime = LocalDateTime.parse(rs.getString("StartTime"));
				int winnerID = rs.getInt("WinnerID");
				int numbPlayers = rs.getInt("NumberOfPlayers");
				return new GameStatistic(gameID, length, startTime, winnerID, numbPlayers);
			}
			rs.close();
						
		}catch(SQLException e) {
			e.printStackTrace();
		}	
		return null;	
	}
	
	public void createGameStatistic(GameStatistic g) {
		try {
			String sql = "INSERT INTO Games(GameID, StartTime, NumberOfPlayers) VALUES (?,?,?);";
			stm1 = super.connection.prepareStatement(sql);
			
			stm1.setInt(1, g.getGameID());
			stm1.setString(2, g.getStartTime().toString());
			stm1.setInt(3, g.getNumberOfPlayers());
					
			stm1.executeUpdate();
			this.connection.commit();
			stm1.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateGameStatistic(String value,String attribute, GameStatistic g1) {
		try {
			String sql = "UPDATE Games SET " + attribute + " = '" + value + "' WHERE GameID = " + g1.getGameID();
			Statement stm = super.connection.createStatement();
			stm.executeUpdate(sql);
			this.connection.commit();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateGameStatistic(int value,String attribute, GameStatistic g1) {
		try {
			String sql = "UPDATE Games SET " + attribute + " = '" + value + "' WHERE GameID = " + g1.getGameID();
			Statement stm = super.connection.createStatement();
			stm.executeUpdate(sql);
			this.connection.commit();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
