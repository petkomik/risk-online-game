package game;

import game.models.Player;
import javafx.scene.paint.Color;

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