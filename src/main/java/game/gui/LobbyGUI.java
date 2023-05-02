package game.gui;

import java.io.FileNotFoundException;
import java.io.Serializable;

import database.Profile;
import game.Lobby;
import game.gui.GUISupportClasses.DesignButton;
import game.gui.GUISupportClasses.Spacing;
import game.models.PlayerSingle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LobbyGUI extends ToggleButton implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double ratio;
	private int numberOfPlayersJoined;
	private int maxNumberOfPlayers;
	private String lobbyNameString;
	private int ratingString;
	private Lobby lobby;

	private HBox hBox;
	private Label lobbyName;
	private Label rating;

	private Label playersJoined;
	
	public LobbyGUI() {
		super();
		this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
				* Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
		this.ratio = Math.min(ratio + 0.3, 1);
		this.ratingString = (int) (Math.random() * 100);
		this.numberOfPlayersJoined = 1;
		this.maxNumberOfPlayers = 6;
		setup();
	
		
	}
	
	public LobbyGUI(Lobby lobby) {
		super();
		this.lobby = lobby;
		this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
				* Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
		this.ratio = Math.min(ratio + 0.3, 1);
		this.ratingString = lobby.getLobbyRank();
		this.numberOfPlayersJoined = 1;
		this.maxNumberOfPlayers = 6;
		this.lobbyNameString = lobby.getLobbyName();
		setup();
	}
	
	public LobbyGUI(String name, int joinedPlayers, int maxPlayers, int rank) {
		
	}
	
	public HBox getVisual() {
		return hBox;
	}

	public void setup() {
		
		this.setStyle("-fx-background-color: rgba(92,64,51);"
				+ "-fx-background-radius: 25;"
				+ "-fx-background-insets: 1 1 1 1;"
				+ "-fx-border-color: transparent;"
			    + "-fx-border-width: 6px;"
			    + "-fx-border-radius: 25");
		this.setPrefWidth(800 * ratio);
		this.setAlignment(Pos.CENTER);
		this.setPickOnBounds(true);


		hBox = new HBox();
		hBox.setPadding(new Insets(20, 40, 20, 40));

		lobbyName = new Label();
		lobbyName.setText(lobbyNameString);
		lobbyName.setFont(Font.font("Cooper Black", FontWeight.NORMAL, ratio * 28));
		lobbyName.setMouseTransparent(true);
		lobbyName.setStyle("-fx-background-color: transparent;" 
						 + "-fx-text-fill: white");

		rating = new Label();
		rating.setText(ratingString + " \u2605");
		rating.setFont(Font.font("Cooper Black", FontWeight.NORMAL, ratio * 28));
		rating.setMouseTransparent(true);
		rating.setStyle("-fx-background-color: transparent;"
					  + "-fx-text-fill: white");

		playersJoined = new Label();
		playersJoined.setText("" + this.numberOfPlayersJoined + " / " + this.maxNumberOfPlayers + " Players");
		playersJoined.setFont(Font.font("Cooper Black", FontWeight.NORMAL, ratio * 28));
		playersJoined.setStyle("-fx-background-color: transparent;"
					  + "-fx-text-fill: white");


		Spacing spacing1 = new Spacing(1);
		HBox.setHgrow(spacing1, Priority.ALWAYS);
		Spacing spacing2 = new Spacing(1);
		HBox.setHgrow(spacing2, Priority.ALWAYS);
		hBox.getChildren().addAll(lobbyName, spacing2, rating, spacing1, playersJoined);
		this.setGraphic(hBox);
		
		this.selectedProperty().addListener(((observable, oldValue, newValue) -> {
			if(newValue) {
				this.setStyle("-fx-background-color: rgba(92,64,51);"
						+ "-fx-background-radius: 25;"
    					+ "-fx-background-insets: 1 1 1 1;"
						+ "-fx-border-color: white;"
        			    + "-fx-border-width: 6px;"
        			    + "-fx-border-radius: 25");
			} else {
				this.setStyle("-fx-background-color: rgba(92,64,51);"
						+ "-fx-background-radius: 25;"
    					+ "-fx-background-insets: 1 1 1 1;"
						+ "-fx-border-color: transparent;"
        			    + "-fx-border-width: 6px;"
        			    + "-fx-border-radius: 25");
			}
		}));

	}
	
	public String getRating() {
		return rating.getText();
	}
	
	public Lobby getLobby() {
		return lobby;
	}

	public void setLobbyName(String newUsername) {
		lobbyNameString = newUsername;
		
	}
	public String getLobbyNameString() {
		return lobbyNameString;
	}
}
