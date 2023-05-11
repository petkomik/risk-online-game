package network.messages;

/**
 * enum containing all messagetypes
 * @author dignatov
 *
 */

public enum MessageType {
	Connect, 
	Disconnect,
	MessageServerCloseConnection,
	MessageSend, 
	MessageSendInGame,
	MessageProfile, 
	MessageToPerson,
	MessageAllProfiles,
	
	MessageCreateLobby,
	MessageJoinLobby,
	MessageUpdateLobby,
	MessageUpdateLobbyList,
	MessageinLobby,
	MessageReadyToPlay,
	
	MessageCreateGame,
	MessageJoinGame,
	
	// In Game
	MessageGameState,
	MessageGUIRollInitalDice, // 
	MessageGUIRollDiceBattle,
	MessageGUIshowExcption,
	MessageGUIsetPeriod,
	MessageGUIsetPhase,
	MessageGUIpossessCountry,
	MessageGUIconquerCountry,
	MessageGUIsetCurrentPlayer,
	
	MessageGUIchnagePlayer,
	MessageGUIchooseNumberOfTroops,
	MessageGUIcloseTroopsPane,
	MessageGUIsetTroopsOnTerritory,
	MessageGUIsetTroopsOnTerritoryAndLeft,
	MessageGUImoveTroopsFromTerritoryToOther,
	MessageGUIOpenBattleFrame,
	
	MessageGUIendBattle,
	MessageGUIriskCardsTurnedInSuccess,
	MessageGUIselectTerritoryAndSteDisabledTerritories,
	MessageGUIresetAll,
	MessageGUIupdateRanks,
	MessageGUIgameIsOver
	
	
	
	

	// MessageJoinGame Nachricht
	// MessageCreateGame
	
}
