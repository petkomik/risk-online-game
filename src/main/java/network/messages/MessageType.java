package network.messages;

/**
 * enum containing all messagetypes
 * @author srogalsk
 *
 */

public enum MessageType {
	Connect, // hat sich verbunden, bei jedem neuen Client, profile, 
	Disconnect, // verlassen vom Server IM CHAT
	MessageSend, // v
	MessageProfile, //fragwürdig 
	MessageMove, //,GameStart (name) Settings in GameStart  
	MessagePlacingTroops,
	MessageAttack,
	MessageDiceThrow, // von Server an die Clients mit dem Wert, die random wird vom Server
	MessagePlayerTurn, // 
	MessagePossessCountry,
	MessageChooseCountry,
	MessagePlaceTroops,
	MessageDiceThrowRequest; // was an dem server gesendet wird (nicht wirklich viel)
	// MessageJoinGame Nachricht
	// MessageCreateGame
	
}
