package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.paint.Color;

import game.models.*;
import game.models.PlayerAI;
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
	static String[] aiNames = new String[] {"Jasper (AI)", "Andrea (AI)", "Mick (AI)", 
			"Tosho (AI)", "Maria (AI)"};
	

	
	
	ArrayList<PlayerInLobby> playersInLobby;
	ArrayList<Player> playersJoined;
	ArrayList<Color> avaiableColors;
	ArrayList<String> avaiableAvatars;
	ArrayList<String> avaiableAINames;

	public int lobbyRank;
	public int difficultyOfAI;
	public int maxNumberOfPlayers;

	public Lobby() {
		this.playersInLobby = new ArrayList<PlayerInLobby>();
		this.playersJoined = new ArrayList<Player>();
		this.lobbyRank = 0;
		this.difficultyOfAI = 0;
		this.maxNumberOfPlayers = 6;
		this.avaiableColors = new ArrayList<Color>();
		for (Color k : colors) {
			this.avaiableColors.add(k);
		}
		this.avaiableAvatars = new ArrayList<String>();
		for(String k : avatars) {
			this.avaiableAvatars.add(k);
		}
		this.avaiableAINames = new ArrayList<String>();
		for(String k : aiNames) {
			this.avaiableAINames.add(k);
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
	
	private PlayerInLobby addColorAvatar(Player ply) {
		// TODO
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
	
	private void removeColorAvatar(PlayerInLobby ply) {
		this.avaiableAvatars.add(ply.avatar);
		this.avaiableColors.add(ply.color);
	}

	
	public int getLobbyRank() {
		return this.lobbyRank;
	}
	
	public void addAI() {
		System.out.println(this.avaiableAINames.size());

		String aiN = aiNames[this.avaiableAINames.size() - 1];
		this.avaiableAINames.remove(aiN);
		Color aiC = colors[this.avaiableColors.size() - 1];
		this.avaiableColors.remove(aiN);
		String aiA = avatars[this.avaiableAvatars.size() - 1];
		this.avaiableAvatars.remove(aiN);
	
		PlayerAI aiP = new PlayerAI(aiN, 3000, this.difficultyOfAI);
		this.getPlayerList().add(aiP);
		
		PlayerInLobby plyC = new PlayerInLobby(aiP, aiC, aiA);
		this.playersInLobby.add(plyC);
		
	}
	
	public void removeAI() {
		
		
	}
	
	public void updateAILevel() {
		// update the PlayerAI instances difficulty
	}
}