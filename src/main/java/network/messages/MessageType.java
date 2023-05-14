package network.messages;

/**
 * The MessageType enum contains all the message types used in the network communication.
 *
 * @author dignatov
 */

public enum MessageType {
  Connect, Disconnect, MessageServerCloseConnection,

  MessageSend, MessageSendInGame, MessageProfile, MessageToPerson, MessageAllProfiles,

  MessageCreateLobby, MessageJoinLobby, MessageUpdateLobby, MessageUpdateLobbyList,

  MessageinLobby, MessageReadyToPlay,

  MessageCreateGame, MessageJoinGame,

  // In Game
  MessageGameState, MessageGUIRollInitalDice, //
  MessageGUIRollDiceBattle, MessageGUIshowExcption, MessageGUIsetPeriod,

  MessageGUIsetPhase, MessageGUIpossessCountry, MessageGUIconquerCountry,

  MessageGUIsetCurrentPlayer,

  MessageGUIchnagePlayer, MessageGUIchooseNumberOfTroops, MessageGUIcloseTroopsPane,

  MessageGUIsetTroopsOnTerritory, MessageGUIsetTroopsOnTerritoryAndLeft,

  MessageGUImoveTroopsFromTerritoryToOther,

  MessageGUIOpenBattleFrame,

  MessageGUIendBattle, MessageGUIriskCardsTurnedInSuccess,

  MessageGUIselectTerritoryAndSteDisabledTerritories,

  MessageGUIresetAll, MessageGUIupdateRanks, MessageGUIgameIsOver

}
