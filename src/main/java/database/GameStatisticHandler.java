package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import game.GameStatistic;

/**
 * contains methods to get the Profile data from the database and store it on the database.
 *
 * @author jorohr
 */
public class GameStatisticHandler extends Database {
	
	private PreparedStatement stm1;
	
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
			String sql = "INSERT INTO Games(GameID, Length, StartTime, WinnerID, NumberOfPlayers) VALUES (?,?,?,?,?);";
			stm1 = super.connection.prepareStatement(sql);
			
			stm1.setInt(1, g.getGameID());
			stm1.setInt(2,g.getLength());
			stm1.setString(3, g.getStartTime().toString());
			stm1.setInt(4,g.getIdOfWinner());
			stm1.setInt(5,g.getNumberOfPlayers());
					
			stm1.executeUpdate();
			this.connection.commit();
			stm1.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateGameStatistic(String value,String attribute, GameStatistic g1) {
		try {
			String sql = "UPDATE Games SET " + attribute + " = '" + value + "' WHERE PlayerID = " + g1.getGameID();
			Statement stm = super.connection.createStatement();
			stm.executeUpdate(sql);
			this.connection.commit();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}