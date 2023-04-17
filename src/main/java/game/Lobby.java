package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.paint.Color;

import game.models.*;
import general.Parameter;

/*
 * Class to model a lobby - both singleplayer and multiplayer
 * For the purpose of GUI representation
 * 
 * @author pmikov
 * 
 * To Implement: Player score -> Lobby Rank
 */

public class Lobby {
	
	static Color[] colors = new Color[] {Parameter.blueColor, Parameter.greenColor, 
			Parameter.orangeColor, Parameter.purpleColor, 
			Parameter.redColor, Parameter.yellowColor};
	static String[] avatars = new String[] {Parameter.blondBoy, Parameter.gingerGirl,
			Parameter.bruntetteBoy, Parameter.mustacheMan,
			Parameter.earringsGirl, Parameter.hatBoy};
	
	
	ArrayList<PlayerInLobby> playersInLobby;
	ArrayList<Player> playersJoined;
	ArrayList<Color> avaiableColors;
	ArrayList<String> avaiableAvatars;
	int lobbyRank;

	public Lobby() {
		this.playersInLobby = new ArrayList<PlayerInLobby>();
		this.playersJoined = new ArrayList<Player>();
		this.lobbyRank = 0;
		this.avaiableColors = new ArrayList<Color>();
		for (Color k : colors) {
			this.avaiableColors.add(k);
		}
		this.avaiableAvatars = new ArrayList<String>();
		for(String m : avatars) {
			this.avaiableAvatars.add(m);
		}
	}
	
	class PlayerInLobby {
		Player player;
		Color color;
		String avatar;
		
		public PlayerInLobby(Player pl, Color c, String ava) {
			this.player = pl;
			this.color = c;
			this.avatar = ava;
		}
	}
	
	public void joinLobby(Player toAdd) {
		if(!this.playersJoined.contains(toAdd)) {
			this.playersJoined.add(toAdd);
			this.updateScore();
			playersInLobby.add(this.addColorAvatar(toAdd));
			
		}
	}
	
	public void leaveLobby(Player toLeave) {
		if(this.playersJoined.contains(toLeave)) {
			this.playersJoined.remove(toLeave);
			this.updateScore();
			Iterator<PlayerInLobby> itt = playersInLobby.iterator();
			while(itt.hasNext()) {
				PlayerInLobby k = itt.next();
				if(k.player.equals(toLeave)) {
					this.removeColorAvatar(k);
					this.playersInLobby.remove(k);
					break;
				}
			}
		}
	}
		
	private void updateScore() {
		int k = 0;
		for (Player i : this.playersJoined) {
			k += i.getRank();
		}
		
		k = Math.round(k / this.playersInLobby.size());
		this.lobbyRank = k;
		
	}
	
	
	public List<Player> getPlayerList(){
		return playersJoined;

	}
	
	public List<Player> getAIPlayerList(){
		ArrayList<Player> AIplayersInLobby = new ArrayList<Player>();
		Iterator<Player> itt = playersJoined.iterator();
		while(itt.hasNext()) {
			Player k = itt.next();
			if(k instanceof PlayerAI) {
				AIplayersInLobby.add(k);
			}
		}
		
		return AIplayersInLobby;
	}
	
	public List<Player> getHumanPlayerList(){
		ArrayList<Player> players= new ArrayList<Player>();
		Iterator<Player> itt = playersJoined.iterator();
		while(itt.hasNext()) {
			Player k = itt.next();
			if(k instanceof PlayerMP) {
				players.add(k);
			}
		}
		return players;
	}
	
	public PlayerInLobby addColorAvatar(Player ply) {
		Color prefC = Parameter.blueColor; 						//= ply.getPrefColor();
		String prefAv = Parameter.blondBoy;						//= ply.getPrefAvatar();
		Color realC;
		String realAv;
		
		if(this.avaiableColors.contains(prefC)) {
			realC = prefC;
			this.avaiableColors.remove(prefC);
		} else {
			realC = this.avaiableColors.get(0);
			this.avaiableColors.remove(0);
		}
		
		if(this.avaiableAvatars.contains(prefAv)) {
			realAv = prefAv;
			this.avaiableAvatars.remove(prefAv);
		} else {
			realAv = this.avaiableAvatars.get(0);
			this.avaiableAvatars.remove(0);
		}
		
		return new PlayerInLobby(ply, realC, realAv);
	}
	
	public void removeColorAvatar(PlayerInLobby ply) {
		
	}

	
	public int getLobbyRank() {
		return this.lobbyRank;
	}
	
}