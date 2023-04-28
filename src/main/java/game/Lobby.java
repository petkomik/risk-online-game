package game;

import java.util.ArrayList;
import java.util.HashMap;
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
	static String[] aiNames = new String[] {"Andrea (AI)", "Jasper (AI)", "Mick (AI)", 
			"Maria (AI)", "Tosho (AI)"};
	
	private ArrayList<Player> playersJoined;
	private ArrayList<Color> avaiableColors;
	private ArrayList<String> avaiableAvatars;
	private ArrayList<String> avaiableAINames;
	private HashMap<Player, Boolean> readyHashMap;

	private String lobbyName;
	public int lobbyRank;
	public int difficultyOfAI;
	public int maxNumberOfPlayers;

	public Lobby() {
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
		this.readyHashMap = new HashMap<Player, Boolean>();
	}
	
	
	public void joinLobby(Player toAdd) {
		if(!this.playersJoined.contains(toAdd)) {
			//this.updateScore();
			playersJoined.add(this.addColorAvatar(toAdd));
			this.setReady(toAdd, false);
			
		}
	}
	
	public void leaveLobby(Player toLeave) {
		if(this.playersJoined.contains(toLeave)) {
			this.playersJoined.remove(toLeave);
			this.updateScore();	
			removeColorAvatar(toLeave);
		}
	}
		
	private void updateScore() {
		int k = 0;
		for (Player i : this.playersJoined) {
			k += i.getRank();
		}
		
		k = Math.round(k / this.getPlayerList().size());
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
			// add multiplayer ai
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
			// add multiplayer ai
			if(!(k instanceof PlayerAI)) {
				players.add(k);
			}
		}
		return players;
	}
	// 
	
	private Player addColorAvatar(Player ply) {
		// TODO
		Color prefC;					
		String prefAv;
		
		if(ply instanceof PlayerSingle) {
			prefC = Color.web(((PlayerSingle) ply).getColor());
			prefAv = ((PlayerSingle) ply).getAvatar();
		}else {
			prefC = Parameter.blueColor;
			prefAv = Parameter.blondBoy;
		}
		Color realC;
		String realAv;
		
		if(this.avaiableColors.contains(prefC)) {
			realC = prefC;
			this.avaiableColors.remove(prefC);
		} else {
			realC = this.avaiableColors.get(0);
			this.avaiableColors.remove(0);
			ply.setColor(colorToHexCode(realC));;
		}
		
		if(this.avaiableAvatars.contains(prefAv)) {
			realAv = prefAv;
			this.avaiableAvatars.remove(prefAv);
		} else {
			realAv = this.avaiableAvatars.get(0);
			this.avaiableAvatars.remove(0);
			ply.setAvatar(realAv);
		}
		
		return ply;
	}
	
	private void removeColorAvatar(Player toLeave) {
		this.avaiableAvatars.add(toLeave.getAvatar());
		this.avaiableColors.add(Color.web(toLeave.getColor()));
	}

	
	public int getLobbyRank() {
		return this.lobbyRank;
	}
	
	public void addAI() {
		String aiN = avaiableAINames.get(this.avaiableAINames.size() - 1);
		this.avaiableAINames.remove(aiN);
		Color aiC = avaiableColors.get(this.avaiableColors.size() - 1);
		this.avaiableColors.remove(aiC);
		String aiA = avaiableAvatars.get(this.avaiableAvatars.size() - 1);
		this.avaiableAvatars.remove(aiA);
	
		PlayerAI aiP = new PlayerAI(aiN, 3000, this.difficultyOfAI);
		aiP.setColor(colorToHexCode(aiC));
		aiP.setAvatar(aiA);
		this.getPlayerList().add(aiP);
		this.setReady(aiP, true);
	
		
	}

	public void removeAI() {
		if (this.getAIPlayerList().size() > 0) {
			Player k = this.getAIPlayerList().get(this.getAIPlayerList().size() - 1);
			this.playersJoined.remove(k);
			this.avaiableAINames.add(k.getName());
			this.avaiableAvatars.add(k.getAvatar());
			this.avaiableColors.add(Color.web(k.getColor()));
				System.out.println(this.avaiableAINames.size() + " " + this.avaiableColors.size() + " " + this.avaiableAvatars.size());
		}
	}
	
	public void updateAILevel() {
		// update the PlayerAI instances difficulty
	}
	
	public boolean isEveryoneReady() {
		for(boolean p : this.readyHashMap.values()) {
			if(!p) { 
				return false;
			};
		}
		return true;
	}
	
	
	public static String colorToHexCode( Color color ) {
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) ).toLowerCase();
    }
	
	public void setReady(Player ply, boolean ready) {
		this.readyHashMap.put(ply, ready);
	}
	
	public boolean isReady(Player ply) {
		return this.readyHashMap.get(ply);
	}

	public String getLobbyName() {
		return lobbyName;
	}		
	
	public void setLobbyName(String lobbyName) {
		this.lobbyName = lobbyName;
	}
}

