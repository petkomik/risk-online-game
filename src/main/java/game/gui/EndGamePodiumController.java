package game.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import game.gui.GUISupportClasses.ArrowButton;
import game.gui.GUISupportClasses.ImageViewPane;
import game.gui.GUISupportClasses.Spacing;
import game.models.Lobby;
import game.models.Player;
import general.AppController;
import general.GameSound;
import general.Parameter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * this class is responsible for constructing the GUI pane that comes after a game has finished.
 * 
 * @author pmalamov
 *
 */

public class EndGamePodiumController extends StackPane {
  private GameSound gameSound = AppController.getGameSound();

  private int players;
  private ArrayList<Player> playerList;
  private boolean singleplayer;
  private double ratio;
  private Stage stage;

  private HBox backgroundPic;
  private HBox backgroundColor;
  private ImageView imgBackground;
  private ImageViewPane imgBackgroundPane;
  public ArrayList<String> pathsEndGame;
  private HBox topBannerParent;
  private HBox topBannerContent;
  private Label lobbyTextBanner;
  private ArrowButton backButton;

  private VBox vboxIcons;
  private Text caption;

  private HBox captionBox;
  private HBox avatars;
  private StackPane firstP;
  private StackPane secondP;
  private StackPane thirdP;
  private Circle circleFirstP;
  private Circle circleSecondP;
  private Circle circleThirdP;
  private ImageView circleFirstI;
  private ImageView circleSecondI;
  private ImageView circleThirdI;

  private HBox place;
  private ImageView firstPlaceCup;
  private ImageView secondPlaceCup;
  private ImageView thirdPlaceCup;

  /**
   * Constructor for the class.
   * 
   * @param podiumPlayers has the top 3 players saved, we use their avatar to construct the GUI
   * @param singleplayer is a boolean parameter that we use to define how the back button will work
   * @throws FileNotFoundException is thrown because of the background and avatar images
   */

  public EndGamePodiumController(List<Player> podiumPlayers, boolean singleplayer)
      throws FileNotFoundException {
    super();
    this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
        * Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
    this.ratio = Math.min(ratio + 0.3, 1);
    this.players = podiumPlayers.size();
    this.playerList = (ArrayList<Player>) podiumPlayers;
    this.pathsEndGame = new ArrayList<String>();
    this.singleplayer = singleplayer;
    this.setup();
    this.actionEventsSetup();
  }

  /**
   * initializes all the GUI items that are needed for the construction of the GUI and places them
   * on the previously planned out place.
   * 
   * @throws FileNotFoundException is thrown because of the background and avatar images
   */

  public void setup() throws FileNotFoundException {
    vboxIcons = new VBox();
    vboxIcons.setAlignment(Pos.CENTER);

    avatars = new HBox(82 * ratio);
    avatars.setAlignment(Pos.BOTTOM_CENTER);
    place = new HBox(60 * ratio);
    place.setAlignment(Pos.BOTTOM_CENTER);

    /*
     * setting background Image and color
     */

    backgroundPic = new HBox();
    backgroundColor = new HBox();

    imgBackground = new ImageView();
    imgBackground.setImage(new Image(new FileInputStream(Parameter.imagesdir + "world-map.png")));
    imgBackground.setPreserveRatio(false);
    imgBackground.setSmooth(true);
    imgBackground.setCache(true);

    imgBackgroundPane = new ImageViewPane(imgBackground);
    HBox.setHgrow(imgBackgroundPane, Priority.ALWAYS);
    backgroundPic.getChildren().add(imgBackgroundPane);

    backgroundColor.setAlignment(Pos.CENTER);
    backgroundColor.setFillHeight(true);
    backgroundColor.setStyle("-fx-background-color: rgba(225, 211, 184, 0.9);");

    /*
     * setting up banner layer
     */

    topBannerParent = new HBox();
    topBannerParent.setAlignment(Pos.TOP_LEFT);
    VBox.setMargin(topBannerParent, new Insets(50 * ratio, 0, 0, 0));
    topBannerParent.setPickOnBounds(false);

    topBannerContent = new HBox();
    topBannerContent.setAlignment(Pos.CENTER);
    topBannerContent
        .setStyle("-fx-background-color: " + "linear-gradient(to right, rgba(100, 68, 31, 1) 60%, "
            + "rgba(100, 68, 31, 0.7) 75%, rgba(100, 68, 31, 0) 95%);");
    topBannerContent.setMaxWidth(800 * ratio);
    topBannerContent.setMinWidth(500 * ratio);
    topBannerContent.setPadding(new Insets(10 * ratio, 150 * ratio, 10 * ratio, 30 * ratio));
    topBannerContent.minHeightProperty().bind(topBannerContent.maxHeightProperty());
    topBannerContent.maxHeightProperty().bind(topBannerContent.prefHeightProperty());
    topBannerContent.setPrefHeight(100 * ratio);
    HBox.setHgrow(topBannerContent, Priority.ALWAYS);

    backButton = new ArrowButton(60 * ratio);

    Spacing bannerContentSpacing = new Spacing();
    HBox.setHgrow(bannerContentSpacing, Priority.ALWAYS);

    lobbyTextBanner = new Label("GAME OVER");
    lobbyTextBanner.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 60 * ratio));
    lobbyTextBanner.setTextFill(Color.WHITE);

    Spacing bannerSpacing = new Spacing();
    HBox.setHgrow(bannerSpacing, Priority.ALWAYS);
    bannerSpacing.setVisible(false);

    topBannerContent.getChildren().addAll(backButton, bannerContentSpacing, lobbyTextBanner);
    topBannerParent.getChildren().addAll(topBannerContent, bannerSpacing);

    /*
     * setting up players avatars - in the VBox avatars
     */

    firstP = new StackPane();
    secondP = new StackPane();
    thirdP = new StackPane();

    /*
     * replacing the avatar dir that has been received from the other user else it tries to access
     * the dir of the game host and it blows up
     */

    for (Player player : this.playerList) {
      for (String avatar : Parameter.allAvatars) {
        if (player.getAvatar().contains(avatar)) {
          pathsEndGame.add(Parameter.avatarsdir + avatar);
        }
      }


    }

    /*
     * image and shape for the first avatar
     */

    circleFirstP = new Circle(90 * ratio);
    circleFirstI = new ImageView();
    circleFirstP.setFill(Color.web(this.playerList.get(0).getColor()));
    circleFirstP.setStroke(Color.WHITE);
    circleFirstP.setStrokeWidth(8 * ratio);

    firstP.getChildren().add(circleFirstP);

    circleFirstI.setImage(new Image(new FileInputStream(pathsEndGame.get(0))));
    circleFirstI.setFitWidth(180 * ratio);
    circleFirstI.setFitHeight(180 * ratio);
    circleFirstI.setPreserveRatio(true);
    circleFirstI.setSmooth(true);
    circleFirstI.setCache(true);

    firstP.getChildren().add(circleFirstI);

    /*
     * image and shape for the second avatar
     */

    circleSecondP = new Circle(90 * ratio);
    circleSecondI = new ImageView();
    circleSecondP.setFill(Color.web(this.playerList.get(1).getColor()));
    circleSecondP.setStroke(Color.WHITE);
    circleSecondP.setStrokeWidth(8 * ratio);

    secondP.getChildren().add(circleSecondP);

    circleSecondI.setImage(new Image(new FileInputStream(pathsEndGame.get(1))));
    circleSecondI.setFitWidth(180 * ratio);
    circleSecondI.setFitHeight(180 * ratio);
    circleSecondI.setPreserveRatio(true);
    circleSecondI.setSmooth(true);
    circleSecondI.setCache(true);

    secondP.getChildren().add(circleSecondI);

    /*
     * image and shape for the third avatar
     */

    circleThirdP = new Circle(90 * ratio);
    circleThirdI = new ImageView();
    circleThirdP.setStroke(Color.WHITE);
    circleThirdP.setStrokeWidth(8 * ratio);

    thirdP.getChildren().add(circleThirdP);
    thirdP.setVisible(false);

    circleThirdI.setFitWidth(180 * ratio);
    circleThirdI.setFitHeight(180 * ratio);
    circleThirdI.setPreserveRatio(true);
    circleThirdI.setSmooth(true);
    circleThirdI.setCache(true);
    circleThirdI.setVisible(false);

    thirdP.getChildren().add(circleThirdI);

    if (players > 2) {
      circleThirdP.setFill(Color.web(this.playerList.get(2).getColor()));
      circleThirdI.setImage(new Image(new FileInputStream(pathsEndGame.get(2))));
      thirdP.setVisible(true);
      circleThirdI.setVisible(true);

    }

    /*
     * adding the avatars to their box
     */

    avatars.getChildren().addAll(secondP, firstP, thirdP);
    avatars.setPadding(new Insets(20, 0, 20, 0));

    /*
     * setting up the images for the cups
     */

    firstPlaceCup = new ImageView();
    firstPlaceCup.setImage(new Image(new FileInputStream(Parameter.podiumdir + "gold.png")));
    firstPlaceCup.setFitWidth(220 * ratio);
    firstPlaceCup.setFitHeight(220 * ratio);
    firstPlaceCup.setPreserveRatio(true);
    firstPlaceCup.setSmooth(true);
    firstPlaceCup.setCache(true);

    secondPlaceCup = new ImageView();
    secondPlaceCup.setImage(new Image(new FileInputStream(Parameter.podiumdir + "silver.png")));
    secondPlaceCup.setFitWidth(200 * ratio);
    secondPlaceCup.setFitHeight(200 * ratio);
    secondPlaceCup.setPreserveRatio(true);
    secondPlaceCup.setSmooth(true);
    secondPlaceCup.setCache(true);

    thirdPlaceCup = new ImageView();
    thirdPlaceCup.setImage(new Image(new FileInputStream(Parameter.podiumdir + "bronze.png")));
    thirdPlaceCup.setFitWidth(200 * ratio);
    thirdPlaceCup.setFitHeight(200 * ratio);
    thirdPlaceCup.setPreserveRatio(true);
    thirdPlaceCup.setSmooth(true);
    thirdPlaceCup.setCache(true);

    /*
     * adding the cups to their vBox
     */

    place.getChildren().addAll(secondPlaceCup, firstPlaceCup, thirdPlaceCup);
    place.setPadding(new Insets(20 * ratio, 0, 20 * ratio, 0));

    /*
     * adding endgame text
     */

    captionBox = new HBox();
    captionBox.setAlignment(Pos.CENTER);
    captionBox.setStyle("-fx-background-color: "
        + "linear-gradient(to right,transparent 5%, rgba(100, 68, 31, 0.7) 20%,"
        + " rgba(100, 68, 31, 1) 40%, rgba(100, 68, 31, 1) 60%, "
        + "rgba(100, 68, 31, 0.7) 80%, rgba(100, 68, 31, 0) 95%);");
    captionBox.minHeightProperty().bind(captionBox.maxHeightProperty());
    captionBox.maxHeightProperty().bind(captionBox.prefHeightProperty());
    captionBox.minWidthProperty().bind(captionBox.maxWidthProperty());
    captionBox.maxWidthProperty().bind(captionBox.prefWidthProperty());
    captionBox.setPrefHeight(130 * ratio);
    captionBox.setPrefWidth(700 * ratio);
    HBox.setHgrow(captionBox, Priority.ALWAYS);

    caption = new Text("WINNER");
    caption.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 100 * ratio));
    caption.setFill(Color.WHITE);
    caption.setTextAlignment(TextAlignment.CENTER);

    captionBox.getChildren().add(caption);

    /*
     * adding the caption, cups and avatars to the vBox
     */

    vboxIcons.getChildren().addAll(captionBox, avatars, place);

    /*
     * adding everything to the top container
     */

    this.getChildren().addAll(backgroundPic, backgroundColor, vboxIcons, topBannerParent);
    StackPane.setMargin(topBannerParent, new Insets(50 * ratio, 0, 0, 0));

    actionEventsSetup();

  }

  /**
   * method for setting up the action events of the buttons. it is separated from the setup method
   * for more clarity.
   */

  public void actionEventsSetup() {
    backButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();

        Node node = (Node) event.getSource();
        stage = (Stage) node.getScene().getWindow();

        if (singleplayer) {
          MainMenuPaneController mainMenu;
          try {
            mainMenu = new MainMenuPaneController();
            stage.getScene().setRoot(mainMenu);
          } catch (FileNotFoundException e) {
            e.printStackTrace();
          }
        } else {
          ServerMainWindowController serverMenu;
          try {
            serverMenu = new ServerMainWindowController();
            serverMenu.setClient(AppController.getClient());
            serverMenu.actionEventsSetup();
            serverMenu.setRankText();
            for (Lobby lobby : AppController.getClient().getLobbies().values()) {
              serverMenu.lobbyGUIList.put(lobby.getLobbyName(), new LobbyGUI(lobby));
            }
            serverMenu.drawLobbies(true);
            AppController.getClient().setClientsLobby(null);
            AppController.getClient().setInAGame(false);
            stage.getScene().setRoot(serverMenu);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

      }

    });
  }
}
