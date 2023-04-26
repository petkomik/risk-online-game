package game.gui;

import database.Profile;
import game.Lobby;
import game.gui.GUISupportClasses.DesignButton;
import game.gui.GUISupportClasses.Spacing;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;

public class LobbyGUI extends HBox {

	private double ratio;
	private Lobby lobby;
	private String lobbyNameString;
	private String ratingString;

	private HBox hBox;
	private DesignButton joinButton;
	private TextField lobbyName;
	private TextField rating;
	
	public LobbyGUI() {
		super();
		this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
				* Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
		this.ratio = Math.min(ratio + 0.3, 1);
		this.lobbyNameString = "LobbyName";
		this.ratingString = "3000";
		setup();
	
		
	}
	
	public LobbyGUI(Profile profile) {
		super();
		this.lobby = lobby;
		this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
				* Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
		this.ratio = Math.min(ratio + 0.3, 1);
		this.lobbyNameString = profile.getUserName();
		//this.ratingString = profile.getRating();
		setup();
	}
	
	public HBox getVisual() {
		return hBox;
	}

	public void setup() {

		hBox = new HBox();
		hBox.setPrefSize(ratio * 850, ratio * 80);
		hBox.setMaxSize(ratio * 850, ratio * 80);
		hBox.setStyle("-fx-background-color: rgba(225,60,184);-fx-background-radius: 25;");
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setPadding(new Insets(20, 20, 20, 20));

		/*
		 * setting up the lobby parts
		 */

		joinButton = new DesignButton(new Insets(ratio * 10, ratio * 10, ratio * 10, ratio * 10), 12, ratio * 20,
				ratio * 130);
		joinButton.setText("JOIN");
		joinButton.setAlignment(Pos.CENTER);

		lobbyName = new TextField(lobbyNameString);
		lobbyName.setFont(Font.font("Cooper Black", FontWeight.NORMAL, ratio * 20));
		lobbyName.setEditable(false);
		lobbyName.setMouseTransparent(true);

		rating = new TextField(ratingString);
		rating.setFont(Font.font("Cooper Black", FontWeight.NORMAL, ratio * 20));
		rating.setEditable(false);
		rating.setMouseTransparent(true);

		/*
		 * assembling the elements
		 */
		
		hBox.getChildren().addAll(lobbyName, new Spacing(ratio * 60), rating, new Spacing(ratio * 60), joinButton);

		joinButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				(new GameSound()).buttonClickForwardSound();

			}
		});
	}
}