package game.models;

import database.Profile;
import general.Parameter;
import javafx.scene.paint.Color;

/**
 * This method models a human player. Adds a Profile instance as an attribute.
 * Sets isAi method to false.
 *
 * @author pmikov
 */

public class PlayerSingle extends Player {
	private Profile profile;
	private static final long serialVersionUID = 1L;

	public PlayerSingle(String name, int id) {
		super(name, id);
		this.isAI = false;
	}
	
	public PlayerSingle(Player player) {
		super(player);
		this.isAI = false;
	}
	
	public PlayerSingle(Profile profile) {
		super(profile.getUserName(), profile.getId());
		this.profile = profile;
		this.setColor(profile.getColor());
		this.setAvatar(Parameter.avatarsdir + profile.getPhoto());
		this.setRank(100 + (profile.getWins()*3-profile.getLoses()));
		this.isAI = false;
	}

}
