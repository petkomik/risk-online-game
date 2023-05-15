package game.gui;

import game.gui.GuiSupportClasses.Spacing;
import game.models.Lobby;
import java.io.Serializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;

/**
 * the class is responsible for representing a lobby as a GUI.
 *
 * @author pmalamov
 *
 */
public class LobbyGui extends ToggleButton implements Serializable {

  private static final long serialVersionUID = 1L;
  private double ratio;
  private int numberOfPlayersJoined;
  private int maxNumberOfPlayers;
  private String lobbyNameString;
  private int ratingString;
  private Lobby lobby;

  private HBox hbox;
  private Label lobbyName;
  private Label rating;

  private Label playersJoined;

  /**
   * Constructor for the class.
   *
   * @param lobby Information needed to visualize it.
   */

  public LobbyGui(Lobby lobby) {
    super();
    this.lobby = lobby;
    this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
        * Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
    this.ratio = Math.min(ratio + 0.3, 1);
    this.ratingString = lobby.getLobbyRank();
    this.numberOfPlayersJoined = lobby.getPlayersJoined().size();
    this.maxNumberOfPlayers = lobby.getMaxNumberOfPlayers();
    this.lobbyNameString = lobby.getLobbyName();
    setup();
  }

  /**
   * initializes all the GUI items that are needed for the construction of the LobbyGui and places
   * them on the previously planned out place.
   * 
   */
  public void setup() {

    this.setStyle("-fx-background-color: rgba(92,64,51);" + "-fx-background-radius: 25;"
        + "-fx-background-insets: 1 1 1 1;" + "-fx-border-color: transparent;"
        + "-fx-border-width: 6px;" + "-fx-border-radius: 25");
    this.setPrefWidth(800 * ratio);
    this.setAlignment(Pos.CENTER);
    this.setPickOnBounds(true);


    hbox = new HBox();
    hbox.setPadding(new Insets(20, 40, 20, 40));

    lobbyName = new Label();
    lobbyName.setText(lobbyNameString);
    lobbyName.setFont(Font.font("Cooper Black", FontWeight.NORMAL, ratio * 28));
    lobbyName.setMouseTransparent(true);
    lobbyName.setStyle("-fx-background-color: transparent;" + "-fx-text-fill: white");

    rating = new Label();
    rating.setText(ratingString + " ★");
    rating.setFont(Font.font("Cooper Black", FontWeight.NORMAL, ratio * 28));
    rating.setMouseTransparent(true);
    rating.setStyle("-fx-background-color: transparent;" + "-fx-text-fill: white");

    playersJoined = new Label();
    playersJoined
        .setText("" + this.numberOfPlayersJoined + " / " + this.maxNumberOfPlayers + " Players");
    playersJoined.setFont(Font.font("Cooper Black", FontWeight.NORMAL, ratio * 28));
    playersJoined.setStyle("-fx-background-color: transparent;" + "-fx-text-fill: white");


    Spacing spacing1 = new Spacing(1);
    HBox.setHgrow(spacing1, Priority.ALWAYS);
    Spacing spacing2 = new Spacing(1);
    HBox.setHgrow(spacing2, Priority.ALWAYS);
    hbox.getChildren().addAll(lobbyName, spacing2, rating, spacing1, playersJoined);
    this.setGraphic(hbox);

    this.selectedProperty().addListener(((observable, oldValue, newValue) -> {
      if (newValue) {
        this.setStyle("-fx-background-color: rgba(92,64,51);" + "-fx-background-radius: 25;"
            + "-fx-background-insets: 1 1 1 1;" + "-fx-border-color: white;"
            + "-fx-border-width: 6px;" + "-fx-border-radius: 25");
      } else {
        this.setStyle("-fx-background-color: rgba(92,64,51);" + "-fx-background-radius: 25;"
            + "-fx-background-insets: 1 1 1 1;" + "-fx-border-color: transparent;"
            + "-fx-border-width: 6px;" + "-fx-border-radius: 25");
      }
    }));

  }

  public Lobby getLobby() {
    return lobby;
  }

}
