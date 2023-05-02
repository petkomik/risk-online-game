package network.messages;

import java.util.ArrayList;
import java.util.HashMap;

import game.models.Player;
import general.Parameter;
import javafx.scene.paint.Color;

public class MessageFullLobby extends Message {

	static String[] avatars = new String[] { Parameter.blondBoy, Parameter.gingerGirl, Parameter.bruntetteBoy,
			Parameter.mustacheMan, Parameter.earringsGirl, Parameter.hatBoy };
	static String[] aiNames;

	private ArrayList<Player> playersJoined;
//private ArrayList<Color> avaiableColors;
	private ArrayList<String> avaiableAvatars;
	private ArrayList<String> avaiableAINames;
	private HashMap<Player, Boolean> readyHashMap;
	private String[] allColors;

	private String lobbyName;
	public int lobbyRank;
	public int difficultyOfAI;
	public int maxNumberOfPlayers;
	

	private static final long serialVersionUID = 1L;

	public MessageFullLobby(ArrayList<Player> playersJoined,
			// ArrayList<Color> avaiableColors,
			ArrayList<String> avaiableAvatars, 
			ArrayList<String> avaiableAINames,
			HashMap<Player, Boolean> readyHashMap,
			String lobbyName, 
			int lobbyRank, 
			int difficultyOfAI, 
			int maxNumberOfPlayers) {

		super(MessageType.MessageFullLobby);

		this.playersJoined = playersJoined;
		this.avaiableAvatars = avaiableAvatars;
		this.avaiableAINames = avaiableAINames;
		this.readyHashMap = readyHashMap;
		this.allColors = Parameter.allColors;
		this.lobbyName = lobbyName;
		this.lobbyRank = lobbyRank;
		this.difficultyOfAI = difficultyOfAI;
		this.maxNumberOfPlayers = maxNumberOfPlayers;
	}

	public String[] getAvatars() {
		return avatars;
	}

	public String[] getAiNames() {
		return aiNames;
	}

	public ArrayList<Player> getPlayersJoined() {
		return playersJoined;
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

	public String getLobbyName() {
		return lobbyName;
	}

	public int getLobbyRank() {
		return lobbyRank;
	}

	public int getDifficultyOfAI() {
		return difficultyOfAI;
	}

	public int getMaxNumberOfPlayers() {
		return maxNumberOfPlayers;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
