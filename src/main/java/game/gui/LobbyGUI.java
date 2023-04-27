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
	private String participantsString;

	private HBox hBox;
	private DesignButton joinButton;
	private TextField lobbyName;
	private TextField rating;
	private TextField participants;
	
	public LobbyGUI() {
		super();
		this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
				* Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
		this.ratio = Math.min(ratio + 0.3, 1);
		this.lobbyNameString = "LobbyName";
		this.ratingString = "3000";
		this.participantsString = " 2/6";
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
		//this.participantsString = lobby...
		setup();
	}
	
	public HBox getVisual() {
		return hBox;
	}

	public void setup() {

		hBox = new HBox();
		hBox.setPrefSize(ratio * 850, ratio * 120);
		hBox.setMaxSize(ratio * 850, ratio * 120);
		hBox.setMinSize(ratio * 850, ratio * 120);
		hBox.setStyle("-fx-background-color: rgba(92,64,51);-fx-background-radius: 25;");
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setPadding(new Insets(20, 20, 20, 20));

		/*
		 * setting up the lobby parts
		 */

		joinButton = new DesignButton(new Insets(ratio * 10, ratio * 10, ratio * 10, ratio * 10), 12, ratio * 20,
				ratio * 130);
		joinButton.setText("JOIN");
		joinButton.setAlignment(Pos.CENTER);

		lobbyName = new TextField();
		lobbyName.setText(lobbyNameString);
		lobbyName.setFont(Font.font("Cooper Black", FontWeight.NORMAL, ratio * 22));
		lobbyName.setEditable(false);
		lobbyName.setMouseTransparent(true);
		lobbyName.setStyle("-fx-background-color: transparent;" 
						 + "-fx-text-fill: white");

		rating = new TextField();
		rating.setText(ratingString + " \u2605");
		rating.setFont(Font.font("Cooper Black", FontWeight.NORMAL, ratio * 22));
		rating.setEditable(false);
		rating.setMouseTransparent(true);
		rating.setStyle("-fx-background-color: transparent;"
					  + "-fx-text-fill: white");

		/*
		 * assembling the elements
		 */
		
		hBox.getChildren().addAll(lobbyName, new Spacing(ratio * 20), rating, new Spacing(ratio * 20), joinButton);

		joinButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				(new GameSound()).buttonClickForwardSound();

			}
		});
	}
}
