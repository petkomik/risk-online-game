package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import game.Profile;
import game.models.Player;

/**
 * contains methods to get the data from the database and store it on the database.
 *
 * @author jorohr
 */
public class DatabaseHandler extends Database {
	
	private PreparedStatement stm1;
	
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
	
	public void deletePlayer(int id) {
		try {
			String sql = "DELETE FROM Profiles WHERE PlayerID = " + id + ";";
			Statement stm = super.connection.createStatement();
			stm.executeUpdate(sql);
			this.connection.commit();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updatePlayerInfo(String value,String attribute, Profile p1) {
		try {
			String sql = "UPDATE Profiles SET " + attribute + " = '" + value + "' WHERE PlayerID = " + p1.getId();
			Statement stm = super.connection.createStatement();
			stm.executeUpdate(sql);
			this.connection.commit();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	
	
}
