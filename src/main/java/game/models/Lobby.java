package game.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import database.GameStatistic;
import javafx.scene.paint.Color;
import general.Parameter;


/*
 * Class to model a lobby - both singleplayer and multiplayer
 * For the purpose of GUI representation
 * 
 * @author pmikov
 * 
 */

public class Lobby implements Serializable{

    	private static final long serialVersionUID = 1L;
	static String[] colors = Parameter.allColors;
	static String[] avatars = new String[] {Parameter.blondBoy, Parameter.gingerGirl,
			Parameter.bruntetteBoy, Parameter.mustacheMan,
			Parameter.earringsGirl, Parameter.hatBoy};
	static String[] aiNames = new String[] {"Andrea (AI)", "Jasper (AI)", "Mick (AI)", 
			"Maria (AI)", "Tosho (AI)"};
	
	private ArrayList<Player> playersJoined;
	private ArrayList<String> avaiableColors;
	private ArrayList<String> avaiableAvatars;
	private ArrayList<String> avaiableAINames;
	private HashMap<Player, Boolean> readyHashMap;
	
	private String lobbyName;
	private int lobbyHost;

	public int lobbyRank;
	public Difficulty difficultyOfAI;
	private int  maxNumberOfPlayers;
	private GameStatistic gameStatistic;
	
	/**
	 * Default constructor. Sets values to default value.
	 */
	
	public Lobby() {
		this.playersJoined = new ArrayList<Player>();
		this.lobbyRank = 0;
		this.difficultyOfAI = Difficulty.EASY;
		this.maxNumberOfPlayers = 6;
		this.avaiableColors = new ArrayList<String>();
		for (String k : colors) {
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
		this.setDifficultyOfAI(Difficulty.EASY);
	}
	
	/** Sets Id of lobby admin and calls default constructor. */
	
	public Lobby(int lobbyHost) {
		this();
		this.lobbyHost = lobbyHost;
	}
	
	/**
	 * Player joins lobby. Appropriate Color and Avatars are set to the player.
	 * 
	 * @param toAdd Player instance that we want to add
	 */
	
	public void joinLobby(Player toAdd) {
		if(!this.playersJoined.contains(toAdd)) {
			playersJoined.add(this.addColorAvatar(toAdd));
			this.updateScore();
			this.setReady(toAdd, false);
			
		}
	}
	
	/**
	 * Leave method. Removes player instance from the lobby instance.
	 * 
	 * @param toLeave
	 */
	
	public void leaveLobby(Player toLeave) {
		if(this.playersJoined.contains(toLeave)) {
			this.playersJoined.remove(toLeave);
			this.updateScore();	
			removeColorAvatar(toLeave);
		}
	}
	
	/** Recalculates the score for the lobby. */

	private void updateScore() {
		int k = 0;
		for (Player i : this.playersJoined) {
			k += i.getRank();
		}
		
		if(this.getPlayerList().isEmpty()){
			k=0;
		}else {		
		k = Math.round(k / this.getPlayerList().size());
		}
		this.lobbyRank = k;
		
	}
	
	/** Returns a List with Ai Players present in the lobby. */
	
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
	
	/** Returns a list with the human players in the lobby. */
	
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
	
	/** 
	 * Sets non overlapping color and avatar to the player. 
	 * Updates the list with left avaiable.
	 *
	 * @param ply Player instance that we want to add
	 * @return Player instance with correctly set color and avatars.
	 */
	
	private Player addColorAvatar(Player ply) {
		String prefC;					
		String prefAv;
		
		if(ply instanceof PlayerSingle) {
			prefC = ply.getColor();
			prefAv = ((PlayerSingle) ply).getAvatar();
		}else {
			prefC = this.colorToHexCode(Parameter.blueColor);
			prefAv = Parameter.blondBoy;
		}
		String realC;
		String realAv;
		
		if(this.avaiableColors.contains(prefC)) {
			realC = prefC;
			this.avaiableColors.remove(prefC);
		} else {
			realC = this.avaiableColors.get(0);
			this.avaiableColors.remove(0);
			ply.setColor(realC);
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
	
	/** For when a player leaves. Adding Player's color avatar back to avaiable list. */
	
	private void removeColorAvatar(Player toLeave) {
		this.avaiableAvatars.add(toLeave.getAvatar());
		this.avaiableColors.add(toLeave.getColor());
	}

	/** Adds an Ai Player to the lobby. */
	
	public void addAI() {
		String aiN = avaiableAINames.get(this.avaiableAINames.size() - 1);
		this.avaiableAINames.remove(aiN);
		String aiC = avaiableColors.get(this.avaiableColors.size() - 1);
		this.avaiableColors.remove(aiC);
		String aiA = avaiableAvatars.get(this.avaiableAvatars.size() - 1);
		this.avaiableAvatars.remove(aiA);
	
		PlayerAI aiP = new PlayerAI(aiN, 3000, this.difficultyOfAI);
		aiP.setColor(aiC);
		aiP.setAvatar(aiA);
		this.getPlayerList().add(aiP);
		this.setReady(aiP, true);
	
	}
	
	/** Updates the file image path to the dir of local pc. */
	
	public void updateAvatarDir() {
		for(Player ply : this.playersJoined) {
			for (String avatar : Parameter.allAvatars) {
				if (ply.getAvatar().contains(avatar)) {
					ply.setAvatar(Parameter.avatarsdir + avatar);
				}
			}
		}	
	}

	/** Removes one of the Ai instances. */
	
	public void removeAI() {
		if (this.getAIPlayerList().size() > 0) {
			Player k = this.getAIPlayerList().get(this.getAIPlayerList().size() - 1);
			this.playersJoined.remove(k);
			this.avaiableAINames.add(k.getName());
			this.avaiableAvatars.add(k.getAvatar());
			this.avaiableColors.add(k.getColor());
				System.out.println(this.avaiableAINames.size() + " " + this.avaiableColors.size() + " " + this.avaiableAvatars.size());
		}
	}
	
	/** Updates the Difficulty of Ai player to match lobby settings. */
	
	public void updateAILevel() {
		for(Player ply : this.playersJoined) {
			if(ply instanceof PlayerAI) {
				((PlayerAI) ply).setLevel(difficultyOfAI);
			}
		}
	}
	
	/** Checks if everyone is ready. */
	
	public boolean isEveryoneReady() {
		for(boolean p : this.readyHashMap.values()) {
			if(!p) { 
				return false;
			};
		}
		return true;
	}
	
	/** Converts a Color instance to a hex code as a String. */
	
	public static String colorToHexCode( Color color ) {
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) ).toLowerCase();
    }
	
	public void setReady(Player ply, boolean ready) {
		this.readyHashMap.put(ply, ready);
	}

	public List<Player> getPlayerList(){
	    return playersJoined;
	    
	}
	
	public int getLobbyRank() {
	    return this.lobbyRank;
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

	public static String[] getAvatars() {
		return avatars;
	}


	public static String[] getAiNames() {
		return aiNames;
	}


	public ArrayList<Player> getPlayersJoined() {
		return playersJoined;
	}


	public ArrayList<String> getAvaiableColors() {
		return avaiableColors;
	}


	public ArrayList<String> getAvaiableAvatars() {
		return avaiableAvatars;
	}


	public ArrayList<String> getAvaiableAINames() {
		return avaiableAINames;
	}


	public HashMap<Player, Boolean> getReadyHashMap() {
		return readyHashMap;
	}


	public Difficulty getDifficultyOfAI() {
		return difficultyOfAI;
	}


	public int getMaxNumberOfPlayers() {
		return maxNumberOfPlayers;
	}
	
	public static void setColors(String[] colors) {
		Lobby.colors = colors;
	}


	public void setAvatars(String[] avatars) {
		Lobby.avatars = avatars;
	}


	public void setAiNames(String[] aiNames) {
		Lobby.aiNames = aiNames;
	}


	public void setPlayersJoined(ArrayList<Player> playersJoined) {
		this.playersJoined = playersJoined;
	}


	public void setAvaiableColors(ArrayList<String> avaiableColors) {
		this.avaiableColors = avaiableColors;
	}


	public void setAvaiableAvatars(ArrayList<String> avaiableAvatars) {
		this.avaiableAvatars = avaiableAvatars;
	}


	public void setAvaiableAINames(ArrayList<String> avaiableAINames) {
		this.avaiableAINames = avaiableAINames;
	}


	public void setReadyHashMap(HashMap<Player, Boolean> readyHashMap) {
		this.readyHashMap = readyHashMap;
	}


	public void setLobbyRank(int lobbyRank) {
		this.lobbyRank = lobbyRank;
	}


	public void setDifficultyOfAI(Difficulty difficultyOfAI) {
		this.difficultyOfAI = difficultyOfAI;
		this.updateAILevel();	
	}


	public void setMaxNumberOfPlayers(int maxNumberOfPlayers) {
		this.maxNumberOfPlayers = maxNumberOfPlayers;
	}
	
	public void setGameStatistic(GameStatistic gameStatistic) {
		this.gameStatistic = gameStatistic;
	}


	public GameStatistic getGameStatistic() {
		return this.gameStatistic;
	}
	
	public static String[] getColors() {
		return colors;
	}


	public int getLobbyHost() {
		return lobbyHost;
	}


	public void setLobbyHost(int lobbyHost) {
		this.lobbyHost = lobbyHost;
	}

}

