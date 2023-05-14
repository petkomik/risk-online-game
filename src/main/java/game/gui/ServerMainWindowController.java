package game.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.BiConsumer;
import game.gui.GUISupportClasses.ArrowButton;
import game.gui.GUISupportClasses.ChatButton;
import game.gui.GUISupportClasses.ChatWindow;
import game.gui.GUISupportClasses.DesignButton;
import game.gui.GUISupportClasses.ImageViewPane;
import game.gui.GUISupportClasses.Spacing;
import game.models.Lobby;
import game.models.Player;
import game.models.PlayerSingle;
import general.AppController;
import general.GameSound;
import general.Parameter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
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
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import network.Client;
import network.Server;
import network.messages.MessageCreateLobby;
import network.messages.MessageDisconnect;
import network.messages.MessageJoinLobby;
import network.messages.MessageServerCloseConnection;
import network.messages.MessageUpdateLobby;

/**
 * this class is responsible for constructing the GUI pane, when you have joined a server or hosted
 * one. its the server main menu with all the available lobbies in it.
 * 
 * @author pmalamov
 * 
 */

public class ServerMainWindowController extends VBox {

  static int counter = 0;
  private double ratio;
  private double ratioBanner;
  private Stage stage;

  private static VBox menuAndScrollAndButtons;
  private static HBox backgroundPic;
  private static HBox backgroundColor;
  private ImageView imgBackground;
  private ImageViewPane imgBackgroundPane;

  private static HBox topBannerParent;
  private HBox topBannerContent;
  private Label lobbyTextBanner;
  private ArrowButton backButton;

  private HBox chatDiv;
  private ChatButton chatButton;
  private static ChatWindow chatPane;

  private HBox menu;
  private HBox searchBar;
  private TextField searchField;
  private static DesignButton searchButton;
  private static Text rankText;

  private HBox buttonsHBox;
  private DesignButton hostGameButton;
  private DesignButton joinGameButton;
  private Button cancelButton;

  private static LobbyMenuController lobbyMenuController;
  private static VBox vBoxLobbyMenuController;

  private static ScrollPane lobbyListContainer;
  private static volatile VBox vbox;
  public static HashMap<String, LobbyGUI> lobbyGUIList;
  public static HashMap<String, LobbyGUI> lobbyGUIListSearch;
  public static Lobby selectedLobby;

  private static StackPane topContainer;
  private static GameSound gameSound = AppController.getGameSound();

  static Server server;
  static Client client;

  /**
   * Constructor for the class.
   * 
   * @throws FileNotFoundException is thrown because of the background and avatar images
   */

  public ServerMainWindowController() throws FileNotFoundException {
    super();
    this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
        * Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
    this.ratio = Math.min(1, this.ratio + 0.2);
    this.ratioBanner = Math.min(1, this.ratio + 0.1);
    this.setup();

  }

  /**
   * initializes all the GUI items that are needed for the construction of the GUI and places them
   * on the previously planned out place.
   * 
   * @throws FileNotFoundException is thrown because of the background and avatar images
   */

  public void setup() throws FileNotFoundException {

    lobbyGUIList = new HashMap<String, LobbyGUI>();
    topContainer = new StackPane();

    /*
     * to be returned StackPane
     */

    backgroundPic = new HBox();

    menu = new HBox();
    menuAndScrollAndButtons = new VBox();
    lobbyListContainer = new ScrollPane();

    /*
     * setting up background image
     */

    imgBackground = new ImageView();
    imgBackground.setImage(new Image(new FileInputStream(Parameter.imagesdir + "world-map.png")));
    imgBackground.setPreserveRatio(false);
    imgBackground.setSmooth(true);
    imgBackground.setCache(true);

    backgroundColor = new HBox();
    backgroundColor.setAlignment(Pos.CENTER);
    backgroundColor.setFillHeight(true);
    backgroundColor.setStyle("-fx-background-color: rgba(225, 211, 184, 0.85);");
    backgroundColor.setDisable(true);

    imgBackgroundPane = new ImageViewPane(imgBackground);
    HBox.setHgrow(imgBackgroundPane, Priority.ALWAYS);
    backgroundPic.getChildren().add(imgBackgroundPane);

    /*
     * setting up banner layer with chat button
     */

    topBannerParent = new HBox();
    topBannerParent.setAlignment(Pos.TOP_LEFT);
    StackPane.setMargin(topBannerParent, new Insets(50 * ratioBanner, 0, 0, 0));
    topBannerParent.setPickOnBounds(false);

    topBannerContent = new HBox();
    topBannerContent.setAlignment(Pos.CENTER);
    topBannerContent
        .setStyle("-fx-background-color: " + "linear-gradient(to right, rgba(100, 68, 31, 1) 60%, "
            + "rgba(100, 68, 31, 0.7) 75%, rgba(100, 68, 31, 0) 95%);");
    topBannerContent.setMaxWidth(1000 * ratioBanner);
    topBannerContent.setMinWidth(800 * ratioBanner);
    topBannerContent.setPadding(
        new Insets(10 * ratioBanner, 150 * ratioBanner, 10 * ratioBanner, 30 * ratioBanner));
    topBannerContent.minHeightProperty().bind(topBannerContent.maxHeightProperty());
    topBannerContent.maxHeightProperty().bind(topBannerContent.prefHeightProperty());
    topBannerContent.setPrefHeight(100 * ratioBanner);
    HBox.setHgrow(topBannerContent, Priority.ALWAYS);

    backButton = new ArrowButton(60 * ratioBanner);

    Spacing bannerContentSpacing = new Spacing();
    HBox.setHgrow(bannerContentSpacing, Priority.ALWAYS);

    lobbyTextBanner = new Label("SERVER MENU");
    lobbyTextBanner.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 60 * ratioBanner));
    lobbyTextBanner.setTextFill(Color.WHITE);

    Spacing bannerSpacing = new Spacing();
    HBox.setHgrow(bannerSpacing, Priority.ALWAYS);
    bannerSpacing.setVisible(false);

    chatButton = new ChatButton(
        new Insets(10 * ratioBanner, 20 * ratioBanner, 10 * ratioBanner, 20 * ratioBanner), 30,
        28 * ratioBanner, 170 * ratioBanner, true);
    chatButton.setAlignment(Pos.CENTER);
    chatDiv = new HBox();
    chatDiv.getChildren().add(chatButton);
    chatDiv.minHeightProperty().bind(chatDiv.maxHeightProperty());
    chatDiv.maxHeightProperty().bind(chatDiv.prefHeightProperty());
    chatDiv.setPrefHeight(100 * ratioBanner);
    chatDiv.setPadding(new Insets(0, 50 * ratioBanner, 0, 0));
    chatDiv.setAlignment(Pos.CENTER);

    backButton = new ArrowButton(60 * ratioBanner);

    topBannerContent.getChildren().addAll(backButton, bannerContentSpacing, lobbyTextBanner);
    topBannerParent.getChildren().addAll(topBannerContent, bannerSpacing, chatDiv);

    /*
     * initializing the chat
     */

    chatPane = new ChatWindow();
    chatPane.setVisible(false);
    chatPane.setPickOnBounds(true);

    /*
     * setting up ScrollPane
     */

    lobbyListContainer.getStylesheets()
        .add(this.getClass().getResource("application.css").toExternalForm());
    lobbyListContainer.setPrefSize(ratio * 900, ratio * 500);
    lobbyListContainer.setMaxSize(ratio * 900, ratio * 500);
    lobbyListContainer.setHbarPolicy(ScrollBarPolicy.NEVER);
    lobbyListContainer.setCache(true);
    lobbyListContainer.setMaxWidth(ScrollPane.USE_PREF_SIZE);
    lobbyListContainer.setVbarPolicy(ScrollBarPolicy.ALWAYS);
    lobbyListContainer.setPadding(new Insets(20 * ratio, 20 * ratio, 20 * ratio, 20 * ratio));
    lobbyListContainer.setFitToWidth(true);

    vbox = new VBox();
    vbox.setAlignment(Pos.CENTER);
    vbox.setSpacing(15);

    /*
     * Setting up the menu visuals
     */

    menu.setPrefSize(ratio * 900, ratio * 100);
    menu.setMaxSize(ratio * 900, ratio * 100);
    menu.setStyle("-fx-background-color: rgba(92,64,51); -fx-background-radius: 10 10 0 0");
    menu.setAlignment(Pos.CENTER_LEFT);
    menu.setPadding(new Insets(ratio * 20, ratio * 20, ratio * 20, ratio * 20));
    menu.setMaxWidth(ScrollPane.USE_PREF_SIZE);

    /*
     * setting up button & text
     */

    hostGameButton = new DesignButton(new Insets(ratio * 20, ratio * 10, ratio * 20, ratio * 10),
        50, ratio * 30, ratio * 350);
    hostGameButton.setText("CREATE LOBBY");
    hostGameButton.setAlignment(Pos.CENTER);
    hostGameButton.setMinHeight(ratio * 60);

    joinGameButton = new DesignButton(new Insets(ratio * 20, ratio * 10, ratio * 20, ratio * 10),
        50, ratio * 30, ratio * 350);
    joinGameButton.setText("JOIN LOBBY");
    joinGameButton.setAlignment(Pos.CENTER);
    joinGameButton.setMinHeight(ratio * 60);

    cancelButton = new Button();
    ImageView img = new ImageView();
    img.setImage(new Image(new FileInputStream(Parameter.phaseLogosdir + "cancel.png")));
    img.setFitHeight(60 * ratio);
    img.setPreserveRatio(true);
    img.setSmooth(true);
    img.setCache(true);
    cancelButton.setGraphicTextGap(10);
    cancelButton.setGraphic(img);
    cancelButton.setStyle("-fx-background-color: transparent");

    /*
     * setting up the searchBar and Button
     */

    searchField = new TextField();
    searchField.setPrefSize(ratio * 300, ratio * 60);
    searchField.setMaxSize(ratio * 300, ratio * 60);
    searchField.setMinSize(ratio * 300, ratio * 60);
    searchField.setFont(Font.font("Cooper Black", FontWeight.NORMAL, ratio * 20));
    searchField.setAlignment(Pos.CENTER_LEFT);
    searchField
        .setStyle("-fx-background-color: rgba(225,211,184,0.9); -fx-background-radius: 10 0 0 10");

    searchButton = new DesignButton(new Insets(ratio * 10, ratio * 10, ratio * 10, ratio * 10), 12,
        ratio * 20, ratio * 130);
    searchButton.setText("SEARCH");
    searchButton.setAlignment(Pos.CENTER);
    searchButton.setMinHeight(ratio * 60);
    searchButton
        .setStyle("-fx-background-color: " + "radial-gradient(focus-distance 0% , center 50% 50% , "
            + "radius 75% , #b87331, #64441f);" + "-fx-background-insets: 1 1 1 1;"
            + "-fx-background-radius: 0 12 12 0;" + "-fx-border-radius: 0 12 12 0;"
            + "-fx-border-color: transparent;" + "-fx-border-width: 4px;");

    searchBar = new HBox();
    searchBar.setAlignment(Pos.CENTER);
    searchBar.getChildren().addAll(searchField, searchButton);

    /*
     * assembling the menu
     */

    rankText = new Text();
    rankText.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 20 * ratio));
    rankText.setFill(Color.WHITE);

    menu.getChildren().addAll(searchBar, new Spacing(1), rankText, new Spacing(1), cancelButton);
    menu.setPadding(new Insets(ratio * 20, ratio * 20, ratio * 20, ratio * 20));

    /*
     * assembling the host and join buttons
     */

    buttonsHBox = new HBox();
    buttonsHBox.setAlignment(Pos.CENTER);
    buttonsHBox.getChildren().addAll(hostGameButton, joinGameButton);
    buttonsHBox.setSpacing(20 * ratio);
    buttonsHBox.setPadding(new Insets(30 * ratio, 0, 0, 0));

    /*
     * assembling menu and scrollpane with lobbies
     */

    menuAndScrollAndButtons.getChildren().addAll(menu, lobbyListContainer, buttonsHBox);
    menuAndScrollAndButtons.setAlignment(Pos.CENTER);
    menuAndScrollAndButtons.setPadding(new Insets(50 * ratio, 0, 0, 0));

    /*
     * adding elements to the main container
     */

    lobbyMenuController = new LobbyMenuController();
    vBoxLobbyMenuController = new VBox();

    vBoxLobbyMenuController.getChildren().add(lobbyMenuController);
    vBoxLobbyMenuController.setVisible(false);
    vBoxLobbyMenuController.setPickOnBounds(true);

    topContainer.getChildren().addAll(backgroundPic, backgroundColor, menuAndScrollAndButtons,
        topBannerParent, chatPane, vBoxLobbyMenuController);

    this.getChildren().add(topContainer);
  }

  /**
   * method for setting up the action events of the buttons. it is separated from the setup method
   * for more clarity.
   */

  public void actionEventsSetup() {

    chatButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();

        if (!chatButton.isSelected()) {
          chatButton.setSelected(false);
          chatPane.setVisible(false);
        } else {
          chatButton.setSelected(true);
          chatPane.setVisible(true);
        }
      }
    });

    searchButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        String lobbyName = searchField.getText();
        if (!lobbyName.isEmpty()) {
          lobbyGUIListSearch = new HashMap<String, LobbyGUI>();

          for (String key : lobbyGUIList.keySet()) {
            if (key.contains(lobbyName)) {
              lobbyGUIListSearch.put(key, lobbyGUIList.get(key));
            }
          }

          drawLobbies(false);
        } else {
          drawLobbies(true);
        }

      }

    });

    searchButton.hoverProperty()
        .addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
          if (newValue) {
            searchButton
                .setStyle("-fx-background-color: #64441f;" + "-fx-background-insets: 1 1 1 1;"
                    + "-fx-background-radius: 0 12 12 0;" + "-fx-border-radius: 0 12 12 0;"
                    + "-fx-border-color: #ffff;" + "-fx-border-width: 4px;");
          } else {
            searchButton.setStyle(
                "-fx-background-color: " + "radial-gradient(focus-distance 0% , center 50% 50% , "
                    + "radius 75% , #b87331, #64441f);" + "-fx-background-insets: 1 1 1 1;"
                    + "-fx-background-radius: 0 12 12 0;" + "-fx-border-radius: 0 12 12 0;"
                    + "-fx-border-color: transparent;" + "-fx-border-width: 4px;");
          }
        });

    cancelButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        cancelButton.setRotate(cancelButton.getRotate() + 60);
        searchField.setText("");
        searchButton.fire();
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1)));
        timer.play();
        timer.setOnFinished(x -> {
          cancelButton.setRotate(0);
        });
        if (cancelButton.getRotate() == 180) {
          AppController.getGameSound().buttonClickHelicopterSound();
        }
      }

    });

    backButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickBackwardSound();

        Node node = (Node) event.getSource();
        stage = (Stage) node.getScene().getWindow();

        try {
          MultplayerHostJoinController mlt = new MultplayerHostJoinController();
          stage.getScene().setRoot(mlt);
          if (!client.isHost()) {
            client.sendMessage(new MessageDisconnect(client.getProfile()));
            Platform.runLater(()->{
            	client.setStopFlag(true);
            	client.closeEverything();
            	
            });
            
          } else {
            client.sendMessage(new MessageServerCloseConnection());
          }

        } catch (IOException e) {
          e.printStackTrace();
        }

        stage.show();

      }
    });

    hostGameButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();

        Lobby aLobby = new Lobby(client.getProfile().getId());
        aLobby.joinLobby(new PlayerSingle(client.getProfile()));

        // checks if the lobbyname is taken

        BiConsumer<String, Lobby> addLobby = (clientUsername, lobby) -> {
          int i = 1;
          String newUsername = clientUsername;
          while (client.getLobbies().containsKey(newUsername)) {
            newUsername = clientUsername + i;
            i++;
          }
          client.getLobbies().put(newUsername, lobby);
          lobby.setLobbyName(newUsername);
        };
        addLobby.accept(client.getProfile().getUserName(), aLobby);
        client.sendMessage(new MessageCreateLobby(aLobby));
        client.setInALobby(true);
        drawLobbyMenu(aLobby);
        Platform.runLater(() -> {
          lobbyMenuController.getReadyBtn().fire();
        });

        System.out.println("im in lobby " + aLobby.getLobbyName());
      }

    });

    joinGameButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();

        for (LobbyGUI lobbyEnt : lobbyGUIList.values()) {
          if (lobbyEnt.isSelected()) {
            selectedLobby = lobbyEnt.getLobby();
          }
        }
        if ((selectedLobby != null)
            && (selectedLobby.getPlayerList().size() < selectedLobby.getMaxNumberOfPlayers())) {

          selectedLobby.joinLobby(new PlayerSingle(client.getProfile()));
          client.sendMessage(new MessageJoinLobby(selectedLobby));
          client.setInALobby(true);
          drawLobbyMenu(selectedLobby);

        }

      }
    });

  }

  /**
   * this method draws the lobbyGUIs in the ServerMainWindow. LobbyGUI is the representation of the
   * lobby class in the GUI
   * 
   * @param all is a boolean to check if all lobbies should be drawn or only ones from the search
   *        bar
   */

  public static void drawLobbies(boolean all) {

    Platform.runLater(() -> {

      vbox.getChildren().clear();

      if (all) {

        for (String key : lobbyGUIList.keySet()) {
          vbox.getChildren().add(lobbyGUIList.get(key));
        }
      } else {

        for (String key : lobbyGUIListSearch.keySet()) {
          vbox.getChildren().add(lobbyGUIListSearch.get(key));
        }
      }

      lobbyListContainer.setContent(vbox);
      /*
       * sets the selected Lobby empty so that it doesn't connect to the lobby with the old
       * information
       */
      selectedLobby = null;
    });

  }

  /**
   * this method creates a new LobbyMenuController with the new lobby information and draws it.
   * 
   * @param lobby is used to draw the LobbyMenuController with the lobby information
   */

  public static void drawLobbyMenu(Lobby lobby) {
    Platform.runLater(() -> {

      try {
        lobbyMenuController = new LobbyMenuController(lobby, false);
        lobbyMenuController.getBackButton().setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            /*
             * setting the top layer(vBoxLobbyMenuController) invisible
             */
            gameSound.buttonClickBackwardSound();
            vBoxLobbyMenuController.setVisible(false);
            lobbyMenuController.getChatWindow();
            for (Player player : lobby.getHumanPlayerList()) {
              if (player.getId() == client.getProfile().getId()) {

                /*
                 * changes the lobbyHost/Owner
                 */

                if (lobby.getLobbyHost() == client.getProfile().getId()
                    && lobby.getHumanPlayerList().size() > 1) {

                  lobby.setLobbyHost(lobby.getHumanPlayerList().get(1).getId());

                  for (Player playerRemain : lobby.getHumanPlayerList()) {
                    lobbyMenuController.enableForNewHost(playerRemain.getId());
                  }

                }
                lobby.leaveLobby(player);
                client.setInALobby(false);
                client.sendMessage(new MessageUpdateLobby(lobby));
                searchButton.fire();
              }
            }

          }
        });

        /*
         * updating the LobbyMenuGUI
         */

        vBoxLobbyMenuController.getChildren().clear();
        vBoxLobbyMenuController.getChildren().add(lobbyMenuController);

        /*
         * disables the lobby settings for the lobby guests
         */
        for (Player player : lobby.getHumanPlayerList()) {
          if (player.getId() == client.getProfile().getId()) {
            lobbyMenuController.disableForGuest(client.getProfile().getId());
          }
        }
        vBoxLobbyMenuController.setVisible(true);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }

    });
  }

  /**
   * a method for adding the chatPane to the ServerMainWindow parent when leaving a lobby.
   * 
   * @param chatWindow the ChatWindow instance that is used as a chat
   */
  public static void setChatPain(ChatWindow chatWindow) {
    chatPane = chatWindow;
    topContainer.getChildren().clear();
    topContainer.getChildren().addAll(backgroundPic, backgroundColor, menuAndScrollAndButtons,
        topBannerParent, chatPane, vBoxLobbyMenuController);

  }

  /**
   * a method for initializing a server and a client when starting the ServerMainWindow pane.
   */
  public static void initServer() {

    int port = AppController.getPortNumber();
    String host = AppController.getHost();
    try {

      server = Server.createServer(port);
      client = Client.createClient(host, port);
      client.listenForMessage();
      AppController.setClient(client);
      chatPane.setClient(client);
      client.setChat(chatPane);
      chatPane.addLabel(host);
      client.setHost(true);
      int rank = 100 + (client.getProfile().getWins() * 3 - client.getProfile().getLoses());
      rankText.setText("Your Rating: " + rank + " \u2605");
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  /**
   * a method for initializing a client when starting the ServerMainWindow pane.
   */
  public void initClient() {
    client = AppController.getClient();
    client.listenForMessage();
    chatPane.setClient(client);
    client.setChat(chatPane);
    client.setHost(false);
    int rank = 100 + (client.getProfile().getWins() * 3 - client.getProfile().getLoses());
    rankText.setText("Your Rating: " + rank + " \u2605");

  }

  /**
   * a method for changing the pane from the ServerMainWindow to the GamePaneController. it is used
   * when all participants in a lobby are ready.
   * 
   * @param lobby hands over the players information so taht the GamePaneController can be created
   *        e.g. PlayerAI or PlayerSingle, playersavatar and so on
   */
  public static void startMultyplayerGame(Lobby lobby) {
    try {
      Stage stage = (Stage) topContainer.getScene().getWindow();
      FXMLLoader fxmlLoader =
          new FXMLLoader(CreateProfilePaneController.class.getResource("gameFrame.fxml"));
      AnchorPane anchorPane = (AnchorPane) fxmlLoader.load();
      GamePaneController gamePaneController = fxmlLoader.getController();
      gamePaneController.initMultiPlayer(client, lobby);

      client.setGamePane(gamePaneController);
      stage.getScene().setRoot(anchorPane);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public static ChatWindow getChatPane() {
    return chatPane;
  }

  public static Lobby getSelectedLobby() {
    return selectedLobby;
  }

  public static DesignButton getSearchButton() {
    return searchButton;
  }

  public void setClient(Client client) {
    this.client = client;
    client.setChat(chatPane);
    chatPane.setClient(client);
  }
}
