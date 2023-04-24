package game.models;

public class PlayerSingle extends Player {

	private static final long serialVersionUID = 1L;

	public PlayerSingle(String name, int id) {
		super(name, id);
	}
	
	public PlayerSingle(Player player) {
		super(player);
	}

}
