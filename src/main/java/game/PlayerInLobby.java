package game;

import game.models.Player;
import javafx.scene.paint.Color;

public class PlayerInLobby {
	private Player player;
	private Color color;
	private String avatar;
	
	public PlayerInLobby(Player pl, Color c, String ava) {
		this.player = pl;
		this.color = c;
		this.avatar = ava;
	}
	
	public Player getPlayer() {
		return player;
	}

	public Color getColor() {
		return color;
	}

	public String getAvatar() {
		return avatar;
	}
}