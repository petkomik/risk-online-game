//@author Petko Mikov
package game;

import java.util.ArrayList;
import java.util.List;

import game.models.*;

public class Lobby {
	
	List<Player> playersInLobby;
	int lobbyRank;
	
	public Lobby() {
		playersInLobby = new ArrayList<Player>();
		lobbyRank = 0;

	}
	
	public void joinLobby(PlayerMP toAdd) {
		this.playersInLobby.add(toAdd);
		this.updateScore();
	}
	
	
	private void updateScore() {
		int k = 0;
		for (Player i : this.playersInLobby) {
			k += i.getRank();
		}
		
		k = Math.round(k / this.playersInLobby.size());
		this.lobbyRank = k;
		
	}
	
	public List<Player> getPlayerList(){
		return playersInLobby;
	};
	
	public int getLobbyRank() {
		return this.lobbyRank;
	}
}