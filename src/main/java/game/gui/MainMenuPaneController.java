package game.gui;

import game.gui.GUISupportClasses.DesignButton;
import game.gui.GUISupportClasses.ImageViewPane;
import game.gui.GUISupportClasses.Spacing;
import game.models.Lobby;
import game.models.PlayerSingle;
import game.state.GameType;
import game.state.SinglePlayerHandler;
import general.AppController;
import general.GameSound;
import general.Parameter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Class for the Main Menu Pane. Paints GUI and sets Button action events.
 *
 * @author pmikov
 */
public class MainMenuPaneController extends StackPane {


  private GameSound gameSound = AppController.getGameSound();
  private Stage stage;
  private VBox vbox;
  private ImageView imgBackground;
  private ImageViewPane imgBackgroundPane;
  private VBox vboxColor;
  private VBox contentVbox;
  private HBox banner;
  private HBox topBannerContent;
  private Label lobbyTextBanner;
  private VBox mainContent;
  private DesignButton playTutorialButton;
  private DesignButton profileSettingsButton;
  private DesignButton singleplayerButton;
  private DesignButton multiplayerButton;
  private DesignButton logoutButton;
  private double ratio;

  private AnchorPane anchorPane;

  /**
   * Default constructor sets up the pane.
   *
   * @throws FileNotFoundException for map background not found.
   */

  public MainMenuPaneController() throws FileNotFoundException {
    super();
    this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
        * Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
    this.ratio = Math.min(ratio + 0.3, 1);
    setup();
    buttonEvents();
  }

  /**
   * Setup method, creates and positions nodes.
   *
   * @throws FileNotFoundException for map background image not found
   */

  public void setup() throws FileNotFoundException {

    this.setAlignment(Pos.CENTER);

    /*
     * First layer of stack Background map image
     */

    vbox = new VBox();
    vbox.setAlignment(Pos.CENTER);
    vbox.setFillWidth(true);

    imgBackground = new ImageView();
    imgBackground.setImage(new Image(new FileInputStream(Parameter.imagesdir + "world-map.png")));
    imgBackground.setPreserveRatio(false);
    imgBackground.setSmooth(true);
    imgBackground.setCache(true);

    imgBackgroundPane = new ImageViewPane(imgBackground);
    VBox.setVgrow(imgBackgroundPane, Priority.ALWAYS);

    vbox.getChildren().add(imgBackgroundPane);

    /*
     * Second layer of stack Color mask
     */

    vboxColor = new VBox();
    vboxColor.setAlignment(Pos.CENTER);
    vboxColor.setFillWidth(true);
    vboxColor.setStyle("-fx-background-color: rgba(225, 211, 184, 0.9);");

    contentVbox = new VBox();
    contentVbox.setAlignment(Pos.CENTER);

    banner = new HBox();
    banner.setAlignment(Pos.TOP_LEFT);
    VBox.setMargin(banner, new Insets(50 * ratio, 0, 0, 0));
    banner.setPickOnBounds(false);

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

    Spacing bannerContentSpacing = new Spacing();
    HBox.setHgrow(bannerContentSpacing, Priority.ALWAYS);

    lobbyTextBanner = new Label("MAIN MENU");
    lobbyTextBanner.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 60 * ratio));
    lobbyTextBanner.setTextFill(Color.WHITE);

    Spacing bannerSpacing = new Spacing();
    HBox.setHgrow(bannerSpacing, Priority.ALWAYS);
    bannerSpacing.setVisible(false);

    topBannerContent.getChildren().addAll(bannerContentSpacing, lobbyTextBanner);
    banner.getChildren().addAll(topBannerContent, bannerSpacing);

    mainContent = new VBox();
    mainContent.setAlignment(Pos.CENTER);
    mainContent.setSpacing(30 * ratio);
    mainContent.setMaxWidth(600 * ratio);

    playTutorialButton =
        new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 35, 40 * ratio, 450 * ratio);
    playTutorialButton.setText("Play Tutorial");

    profileSettingsButton =
        new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 35, 40 * ratio, 450 * ratio);
    profileSettingsButton.setText("Profile Settings");

    singleplayerButton =
        new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 35, 40 * ratio, 450 * ratio);
    singleplayerButton.setText("Singleplayer");

    multiplayerButton =
        new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 35, 40 * ratio, 450 * ratio);
    multiplayerButton.setText("Multiplayer");

    logoutButton =
        new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 35, 40 * ratio, 450 * ratio);
    logoutButton.setText("Log Out");

    mainContent.setPadding(new Insets(0, 0, 100 * ratio, 0));

    mainContent.getChildren().addAll(playTutorialButton, profileSettingsButton, singleplayerButton,
        multiplayerButton, logoutButton);
    contentVbox.getChildren().addAll(banner, new Spacing(50), mainContent, new Spacing(50));
    this.getChildren().addAll(vbox, vboxColor, contentVbox);
  }

  /** Setting up button events. */

  public void buttonEvents() {
    playTutorialButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();
        Lobby lobby = new Lobby();
        lobby.joinLobby(new PlayerSingle(AppController.getProfile()));
        lobby.addAi();
        lobby.addAi();
        Node node = (Node) event.getSource();
        stage = (Stage) node.getScene().getWindow();
        try {
          FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gameFrame.fxml"));
          AnchorPane anchorPane = (AnchorPane) fxmlLoader.load();
          GamePaneController gamePaneController = fxmlLoader.getController();
          SinglePlayerHandler singleHandler =
              new SinglePlayerHandler(lobby, gamePaneController, GameType.Tutorial);
          gamePaneController.initTutorial(singleHandler, lobby);
          stage.getScene().setRoot(anchorPane);
          stage.show();
          singleHandler.showInitialHint();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    profileSettingsButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();
        Node node = (Node) event.getSource();
        stage = (Stage) node.getScene().getWindow();
        try {
          UpdateSettingsController settingsPane = new UpdateSettingsController();
          stage.getScene().setRoot(settingsPane);

        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }

      }
    });

    singleplayerButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();
        Lobby lobby = new Lobby();
        lobby.joinLobby(new PlayerSingle(AppController.getProfile()));
        Node node = (Node) event.getSource();
        stage = (Stage) node.getScene().getWindow();
        try {
          LobbyMenuController lobbyPane = new LobbyMenuController(lobby, true);
          stage.getScene().setRoot(lobbyPane);

        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }

      }
    });

    multiplayerButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();
        Node node = (Node) event.getSource();
        stage = (Stage) node.getScene().getWindow();
        try {
          MultplayerHostJoinController mlt = new MultplayerHostJoinController();
          stage.getScene().setRoot(mlt);
        } catch (IOException e) {
          e.printStackTrace();
        }

        stage.show();
      }
    });

    logoutButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickBackwardSound();

        AppController.logoutAndSetValuesToNull();
        Node node = (Node) event.getSource();
        stage = (Stage) node.getScene().getWindow();

        try {
          UserAccessPaneController stp = new UserAccessPaneController();
          stage.getScene().setRoot(stp);

        } catch (FileNotFoundException e1) {
          e1.printStackTrace();
        }
        stage.show();
      }
    });
  }

  /**
   * This method is responsible for loading a fxml file.
   *
   * @author majda
   * @param fxml file name without the ending \.fxml
   * @return Parent object, to be set as a root in a Scene object
   * @throws IOException
   * 
   */

  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader =
        new FXMLLoader(CreateProfilePaneController.class.getResource(fxml + ".fxml"));
    return fxmlLoader.load();
  }

}
