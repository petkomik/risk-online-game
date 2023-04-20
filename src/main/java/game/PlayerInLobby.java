package game;

import game.models.Player;
import javafx.scene.paint.Color;

public class PlayerInLobby {
	private Player player;
	private Color color;
	private String avatar;
	private boolean isReady;
	
	public PlayerInLobby(Player pl, Color c, String ava) {
		this.player = pl;
		this.color = c;
		this.avatar = ava;
		this.isReady = false;
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

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}
}