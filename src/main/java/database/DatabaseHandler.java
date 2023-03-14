package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import game.models.Player;

/**
 * contains methods to get the data from the database and store it on the databse.
 *
 * @author jorohr
 */
public class DatabaseHandler extends Database {
	
	private PreparedStatement stm1;
	public DatabaseHandler() {
		super();
	}
	
	public void createPlayerData(Player p) {
		try {
			String sql = "INSERT INTO Players(PlayerID, Name, Color, Wins, GameTime, Photo, IsPersonal) VALUES (?,?,?,?,?,?,?);";
			stm1 = super.connection.prepareStatement(sql);
			
			stm1.setInt(1,p.getId());
			stm1.setString(2, p.getName());
			stm1.setString(3,p.getColor());
			stm1.setInt(4,p.getWins());
			stm1.setString(5, p.getGameTime().toString());
			stm1.setString(6,p.getPicturePath());
			stm1.setInt(7, p.isPersonal() ? 1 : 0);		
			
			stm1.executeUpdate();
			this.connection.commit();
			stm1.close();
			System.out.println("\nHat geklappt mit PersonalPlayer:\nName: " + p.getName() + "\nID: " + p.getId() + "\n");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
