package game.gui;

import database.Profile;
import game.gui.GuiSupportClasses.ArrowButton;
import game.gui.GuiSupportClasses.ChatButton;
import game.gui.GuiSupportClasses.ChatWindow;
import game.gui.GuiSupportClasses.DesignButton;
import game.gui.GuiSupportClasses.ImageViewPane;
import game.gui.GuiSupportClasses.PlayerCard;
import game.gui.GuiSupportClasses.Spacing;
import game.models.Difficulty;
import game.models.Lobby;
import game.models.Player;
import game.models.PlayerSingle;
import game.state.GameType;
import game.state.SinglePlayerHandler;
import general.AppController;
import general.GameSound;
import general.Parameter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import network.Client;
import network.messages.MessageReadyToPlay;
import network.messages.MessageUpdateLobby;

/**
 * Class for the Battle Frame.
 *
 * @author pmikov
 * 
 */

public class LobbyMenuController extends StackPane {

  private Lobby lobby;

  private VBox vbox;
  private ImageView imgBackground;
  private ImageViewPane imgBackgroundPane;
  private VBox vboxColor;
  private VBox contentVbox;

  private HBox topBannerParent;
  private HBox topBannerContent;
  private static ArrowButton backButton;

  private Label lobbyTextBanner;
  private ChatButton chatButton;
  private HBox chatDiv;

  private HBox mainContent;
  private FlowPane playerCardsPane;

  private VBox settingsReadyPane;
  private VBox settingsPane;
  private HBox settingsBanner;
  private Label settingsName;
  private VBox settingsControlPane;

  private VBox numberPlayersDiv;
  private Label numberPlayersLabel;
  private HBox numberPlayersControls;
  private ArrowButton lessBtnPlayers;
  private Label labelBtnPlayers;
  private ArrowButton moreBtnPlayers;

  private VBox numberOfAiDiv;
  private Label numberOfAiLabel;
  private HBox numberOfAiControls;
  private ArrowButton lessBtnAi;
  private Label labelBtnAi;
  private ArrowButton moreBtnAi;

  private VBox aiDifficultyDiv;
  private Label aiDifficultyLabel;
  private HBox aiDifficultyControls;
  private ArrowButton lessBtnDiff;
  private Label labelBtnDiff;
  private ArrowButton moreBtnDiff;

  private HBox readyButtonPane;
  private DesignButton readyBtn;

  public DesignButton getReadyBtn() {
    return readyBtn;
  }

  double ratio;
  boolean singleplayerLobby;
  private String dirAvatarOnThisPc;
  private ChatWindow chatWindow;

  private Client client = AppController.getClient();
  private GameSound gameSound = AppController.getGameSound();

  public LobbyMenuController() {

  }

  /** Constructor. */

  public LobbyMenuController(Lobby lobby, boolean singleplayerLobby) throws FileNotFoundException {
    this.lobby = lobby;
    this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
        * Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
    this.ratio = Math.min(ratio + 0.3, 1);
    this.singleplayerLobby = singleplayerLobby;
    this.setup();
    setUpPlayerCards();
  }

  /**
   * This method builds and paints the lobby gui Includes action handlers for the buttons.
   *
   * @throws FileNotFoundException if some of the images cannot be found is eihter world-map.png or
   *         any avatar png
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

    this.getChildren().addAll(vbox, vboxColor);

    contentVbox = new VBox();
    contentVbox.setAlignment(Pos.CENTER);

    /*
     * Setting up top banner Includes topBannerContent and topBannerSpacing topBannerContent has a
     * back Button, Label and a spacing inbetween
     */

    topBannerParent = new HBox();
    topBannerParent.setAlignment(Pos.TOP_LEFT);
    StackPane.setMargin(topBannerParent, new Insets(50 * ratio, 0, 0, 0));
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

    lobbyTextBanner = new Label("LOBBY");
    lobbyTextBanner.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 60 * ratio));
    lobbyTextBanner.setTextFill(Color.WHITE);

    Spacing bannerSpacing = new Spacing();
    HBox.setHgrow(bannerSpacing, Priority.ALWAYS);
    bannerSpacing.setVisible(false);

    chatButton = new ChatButton(new Insets(10 * ratio, 20 * ratio, 10 * ratio, 20 * ratio), 30,
        28 * ratio, 170 * ratio, true);
    chatButton.setAlignment(Pos.CENTER);

    chatDiv = new HBox();
    chatDiv.getChildren().add(chatButton);
    chatDiv.minHeightProperty().bind(chatDiv.maxHeightProperty());
    chatDiv.maxHeightProperty().bind(chatDiv.prefHeightProperty());
    chatDiv.setPrefHeight(100 * ratio);
    chatDiv.setPadding(new Insets(0, 50 * ratio, 0, 0));
    chatDiv.setAlignment(Pos.CENTER);
    chatDiv.setVisible(!this.singleplayerLobby);

    topBannerContent.getChildren().addAll(backButton, bannerContentSpacing, lobbyTextBanner);
    topBannerParent.getChildren().addAll(topBannerContent, bannerSpacing, chatDiv);

    /*
     * Setting up the main content Includes mainContent pane with playerCardsPane and
     * settingsReadyPane playerCardsPane holds the cards of the joined players settingsReadyPane
     * includes settings controlls and ready button
     */

    mainContent = new HBox();
    mainContent.setAlignment(Pos.CENTER);
    mainContent.setSpacing(50 * ratio);
    mainContent.setFillHeight(true);
    mainContent.setPadding(new Insets(50 * ratio, 0, 50 * ratio, 0));

    playerCardsPane = new FlowPane();
    playerCardsPane.setOrientation(Orientation.HORIZONTAL);
    playerCardsPane.minHeightProperty().bind(playerCardsPane.maxHeightProperty());
    playerCardsPane.maxHeightProperty().bind(playerCardsPane.prefHeightProperty());
    playerCardsPane.setPrefHeight(450 * ratio);
    playerCardsPane.minWidthProperty().bind(playerCardsPane.maxWidthProperty());
    playerCardsPane.maxWidthProperty().bind(playerCardsPane.prefWidthProperty());
    playerCardsPane.setPrefWidth(660 * ratio);
    playerCardsPane.setHgap(20 * ratio);
    playerCardsPane.setVgap(20 * ratio);
    playerCardsPane.setAlignment(Pos.TOP_LEFT);

    setUpPlayerCards();

    settingsReadyPane = new VBox();
    settingsPane = new VBox();
    settingsBanner = new HBox();
    settingsControlPane = new VBox();
    settingsName = new Label("SETTINGS");

    numberPlayersDiv = new VBox();
    numberPlayersLabel =
        new Label(this.singleplayerLobby ? "Number of Players" : " Maximum Players");
    numberPlayersControls = new HBox();
    lessBtnPlayers = new ArrowButton(30 * ratio);
    labelBtnPlayers = new Label();
    moreBtnPlayers = new ArrowButton(30 * ratio);

    numberOfAiDiv = new VBox();
    numberOfAiLabel = new Label("Number of AI Players");
    numberOfAiControls = new HBox();
    lessBtnAi = new ArrowButton(30 * ratio);
    labelBtnAi = new Label("0");
    // TODO
    moreBtnAi = new ArrowButton(30 * ratio);

    aiDifficultyDiv = new VBox();
    aiDifficultyLabel = new Label("AI Player Difficulty");
    aiDifficultyControls = new HBox();
    lessBtnDiff = new ArrowButton(30 * ratio);
    labelBtnDiff = new Label();
    moreBtnDiff = new ArrowButton(30 * ratio);

    readyButtonPane = new HBox();
    readyBtn = new DesignButton(new Insets(5 * ratio, 25 * ratio, 5 * ratio, 20 * ratio), 25,
        28 * ratio, 300 * ratio);

    /*
     * Settings Pane
     */

    settingsPane.minHeightProperty().bind(settingsPane.maxHeightProperty());
    settingsPane.maxHeightProperty().bind(settingsPane.prefHeightProperty());
    settingsPane.setPrefHeight(370 * ratio);

    settingsPane.minWidthProperty().bind(settingsPane.maxWidthProperty());
    settingsPane.maxWidthProperty().bind(settingsPane.prefWidthProperty());
    settingsPane.setPrefWidth(320 * ratio);

    settingsPane.setStyle("-fx-background-color: rgba(100, 68, 31, 0.7);");
    settingsPane.setFillWidth(true);

    /*
     * Settings Banner
     */

    settingsBanner.setStyle("-fx-background-color: #64441f;");
    settingsBanner.setAlignment(Pos.TOP_CENTER);
    settingsBanner.setPadding(new Insets(10 * ratio, 20 * ratio, 10 * ratio, 20 * ratio));

    settingsName.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 32 * ratio));
    settingsName.setTextFill(Color.WHITE);
    settingsName.setAlignment(Pos.CENTER);

    /*
     * Settings Controlls
     */

    settingsControlPane.setSpacing(20 * ratio);
    settingsControlPane.setFillWidth(true);
    settingsControlPane.setPadding(new Insets(30 * ratio, 50 * ratio, 30 * ratio, 50 * ratio));
    settingsControlPane.setAlignment(Pos.CENTER);

    numberPlayersDiv.setAlignment(Pos.CENTER);
    numberOfAiDiv.setAlignment(Pos.CENTER);
    aiDifficultyDiv.setAlignment(Pos.CENTER);

    numberPlayersLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, 20 * ratio));
    numberPlayersLabel.setTextFill(Color.WHITE);
    numberPlayersLabel.setAlignment(Pos.CENTER);

    numberOfAiLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, 20 * ratio));
    numberOfAiLabel.setTextFill(Color.WHITE);
    numberOfAiLabel.setAlignment(Pos.CENTER);

    aiDifficultyLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, 20 * ratio));
    aiDifficultyLabel.setTextFill(Color.WHITE);
    aiDifficultyLabel.setAlignment(Pos.CENTER);

    moreBtnPlayers.setRotate(180);
    labelBtnPlayers
        .setText(String.valueOf(this.singleplayerLobby ? this.lobby.getHumanPlayerList().size()
            : this.lobby.getMaxNumberOfPlayers()));

    moreBtnAi.setRotate(180);
    labelBtnAi.setText(String.valueOf(this.lobby.getAiPlayerList().size()));

    moreBtnDiff.setRotate(180);
    labelBtnDiff.setText(lobby.getDifficultyOfAi().toString());

    labelBtnPlayers.setFont(Font.font("Cooper Black", FontWeight.BOLD, 30 * ratio));
    labelBtnPlayers.textOverrunProperty().set(OverrunStyle.CLIP);
    labelBtnPlayers.setMinWidth(50 * ratio);
    labelBtnPlayers.setAlignment(Pos.CENTER);
    labelBtnPlayers.setTextFill(Color.WHITE);

    labelBtnAi.setFont(Font.font("Cooper Black", FontWeight.BOLD, 30 * ratio));
    labelBtnAi.textOverrunProperty().set(OverrunStyle.CLIP);
    labelBtnAi.setMinWidth(50 * ratio);
    labelBtnAi.setAlignment(Pos.CENTER);
    labelBtnAi.setTextFill(Color.WHITE);

    labelBtnDiff.setFont(Font.font("Cooper Black", FontWeight.BOLD, 30 * ratio));
    labelBtnDiff.textOverrunProperty().set(OverrunStyle.CLIP);
    labelBtnDiff.setMinWidth(50 * ratio);
    labelBtnDiff.setAlignment(Pos.CENTER);
    labelBtnDiff.setTextFill(Color.WHITE);

    numberPlayersControls.setAlignment(Pos.CENTER);
    numberOfAiControls.setAlignment(Pos.CENTER);
    aiDifficultyControls.setAlignment(Pos.CENTER);

    numberPlayersDiv.setAlignment(Pos.CENTER);
    numberOfAiDiv.setAlignment(Pos.CENTER);
    aiDifficultyDiv.setAlignment(Pos.CENTER);


    readyBtn.setText("Ready");
    settingsReadyPane.setSpacing(30 * ratio);
    settingsReadyPane.setAlignment(Pos.TOP_CENTER);
    readyButtonPane.setAlignment(Pos.CENTER);

    settingsBanner.getChildren().add(settingsName);
    readyButtonPane.getChildren().add(readyBtn);

    Spacing spacing1 = new Spacing(1);
    Spacing spacing2 = new Spacing(1);
    numberPlayersControls.getChildren().addAll(lessBtnPlayers, spacing1, labelBtnPlayers, spacing2,
        moreBtnPlayers);
    Spacing spacing3 = new Spacing(1);
    Spacing spacing4 = new Spacing(1);
    numberOfAiControls.getChildren().addAll(lessBtnAi, spacing3, labelBtnAi, spacing4, moreBtnAi);
    Spacing spacing5 = new Spacing(1);
    Spacing spacing6 = new Spacing(1);
    aiDifficultyControls.getChildren().addAll(lessBtnDiff, spacing5, labelBtnDiff, spacing6,
        moreBtnDiff);

    numberPlayersDiv.getChildren().addAll(numberPlayersLabel, numberPlayersControls);
    numberOfAiDiv.getChildren().addAll(numberOfAiLabel, numberOfAiControls);
    aiDifficultyDiv.getChildren().addAll(aiDifficultyLabel, aiDifficultyControls);

    numberPlayersDiv.setSpacing(7 * ratio * ratio);
    numberOfAiDiv.setSpacing(7 * ratio);
    aiDifficultyDiv.setSpacing(7 * ratio);

    settingsControlPane.getChildren().addAll(numberPlayersDiv, numberOfAiDiv, aiDifficultyDiv);
    settingsPane.getChildren().addAll(settingsBanner, settingsControlPane);

    settingsReadyPane.getChildren().addAll(settingsPane, readyButtonPane);
    chatWindow = ServerMainWindowController.getChatPane();
    mainContent.getChildren().addAll(playerCardsPane, settingsReadyPane);
    contentVbox.getChildren().addAll(mainContent);

    /*
     * Action Handlers for all the buttons
     */

    backButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickBackwardSound();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        try {
          MainMenuPaneController mainMenu = new MainMenuPaneController();
          stage.getScene().setRoot(mainMenu);
        } catch (IOException e) {
          e.printStackTrace();
        }

        stage.show();

      }
    });

    moreBtnDiff.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();
        String current = labelBtnDiff.getText();
        if (current.equals(Difficulty.EASY.toString())) {
          lobby.setDifficultyOfAi(Difficulty.CASUAL);
          labelBtnDiff.setText(Difficulty.CASUAL.toString());
        } else if (current.equals(Difficulty.CASUAL.toString())) {
          lobby.setDifficultyOfAi(Difficulty.HARD);
          labelBtnDiff.setText(Difficulty.HARD.toString());
        }

        if (!singleplayerLobby) {
          client.sendMessage(new MessageUpdateLobby(lobby));
        }

      }
    });

    lessBtnDiff.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();
        String current = labelBtnDiff.getText();
        if (current.equals(Difficulty.HARD.toString())) {
          lobby.setDifficultyOfAi(Difficulty.CASUAL);
          labelBtnDiff.setText(Difficulty.CASUAL.toString());
        } else if (current.equals(Difficulty.CASUAL.toString())) {
          lobby.setDifficultyOfAi(Difficulty.EASY);
          labelBtnDiff.setText(Difficulty.EASY.toString());
        }

        if (!singleplayerLobby) {
          client.sendMessage(new MessageUpdateLobby(lobby));
        }
      }
    });

    moreBtnAi.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();
        // TODO
        int labelBefore = Integer.parseInt(labelBtnAi.getText());
        if (lobby.getAiPlayerList().size() < 5
            && lobby.getMaxNumberOfPlayers() > lobby.getPlayerList().size()) {
          lobby.addAi();
          labelBtnAi.setText(String.valueOf(lobby.getAiPlayerList().size()));
        }
        try {
          setUpPlayerCards();
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }

        // TODO add remove ai players
        if (!singleplayerLobby) {
          client.sendMessage(new MessageUpdateLobby(lobby));
        }
      }
    });

    lessBtnAi.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();
        if (lobby.getAiPlayerList().size() > 0) {
          lobby.removeAi();
          labelBtnAi.setText(String.valueOf(lobby.getAiPlayerList().size()));
        }
        try {
          setUpPlayerCards();
        } catch (FileNotFoundException e) {
          e.printStackTrace();

        }
        if (!singleplayerLobby) {
          client.sendMessage(new MessageUpdateLobby(lobby));
        }
      }
    });

    moreBtnPlayers.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();
        if (singleplayerLobby) {
          if (lobby.getPlayerList().size() < 6) {
            Profile newProfile = new SecondaryPlayerDialog().addPlayerDialog();
            if (newProfile != null) {
              PlayerSingle playerSingle = new PlayerSingle(newProfile);
              lobby.joinLobby(playerSingle);
              labelBtnPlayers.setText(String.valueOf(lobby.getHumanPlayerList().size()));
            }
          }
          try {
            setUpPlayerCards();
          } catch (FileNotFoundException e) {
            e.printStackTrace();

          }

        } else {
          if (lobby.getMaxNumberOfPlayers() < 6) {
            lobby.setMaxNumberOfPlayers(lobby.getMaxNumberOfPlayers() + 1);
            labelBtnPlayers.setText(String.valueOf(lobby.getMaxNumberOfPlayers()));
          }
          try {
            setUpPlayerCards();
          } catch (FileNotFoundException e) {
            e.printStackTrace();

          }
          if (!singleplayerLobby) {
            client.sendMessage(new MessageUpdateLobby(lobby));
          }
        }
      }
    });

    lessBtnPlayers.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();

        if (singleplayerLobby) {
          if (lobby.getHumanPlayerList().size() > 1) {
            lobby.leaveLobby(lobby.getHumanPlayerList().get(lobby.getHumanPlayerList().size() - 1));
            labelBtnPlayers.setText(String.valueOf(lobby.getHumanPlayerList().size()));
          }
          try {
            setUpPlayerCards();
          } catch (FileNotFoundException e) {
            e.printStackTrace();
          }

        } else {
          if (lobby.getMaxNumberOfPlayers() > 2
              && lobby.getMaxNumberOfPlayers() > lobby.getPlayerList().size()) {
            lobby.setMaxNumberOfPlayers(lobby.getMaxNumberOfPlayers() - 1);
            labelBtnPlayers.setText(String.valueOf(lobby.getMaxNumberOfPlayers()));
          }
          try {
            setUpPlayerCards();
          } catch (FileNotFoundException e) {
            e.printStackTrace();

          }
          if (!singleplayerLobby) {
            client.sendMessage(new MessageUpdateLobby(lobby));
          }
          System.out.println("last");
        }
      }
    });

    readyBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();
        boolean ready = readyBtn.getText().equals("Ready");
        if (ready) {
          readyBtn.setText("Not Ready");
        } else {
          readyBtn.setText("Ready");
        }
        if (singleplayerLobby) {
          ArrayList<Player> humans = (ArrayList<Player>) lobby.getHumanPlayerList();
          for (Player pl : humans) {
            lobby.setReady(pl, ready);
            try {
              setUpPlayerCards();
            } catch (FileNotFoundException e) {
              e.printStackTrace();
            }
          }
          if (lobby.isEveryoneReady() && lobby.getPlayerList().size() > 1) {
            System.out.println("start game");
            try {
              Node node = (Node) event.getSource();
              Stage stage = (Stage) node.getScene().getWindow();
              FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gameFrame.fxml"));
              AnchorPane anchorPane = (AnchorPane) fxmlLoader.load();
              GamePaneController gamePaneController = fxmlLoader.getController();
              SinglePlayerHandler singleHandler =
                  new SinglePlayerHandler(lobby, gamePaneController, GameType.SinglePlayer);
              gamePaneController.initSinglePlayer(singleHandler, lobby);
              stage.getScene().setRoot(anchorPane);
              stage.show();
            } catch (IOException e) {
              e.printStackTrace();
            }

          }
        } else {
          for (Player player : lobby.getHumanPlayerList()) {

            if (player.getId() == client.getProfile().getId()) {
              if (lobby.getReadyHashMap().get(player)) {
                lobby.setReady(player, false);
              } else {
                lobby.setReady(player, true);
              }
            }
          }
          if (lobby.isEveryoneReady() && lobby.getPlayerList().size() > 1) {
            client.sendMessage(new MessageReadyToPlay(lobby));
          } else {
            client.sendMessage(new MessageUpdateLobby(lobby));
          }
        }

      }
    });

    chatButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();

        if (!chatButton.isSelected()) {
          chatButton.setSelected(false);
          chatWindow.setVisible(false);
        } else {
          chatButton.setSelected(true);
          chatWindow.setVisible(true);
        }
        System.out.println(chatWindow.getParent() + " ist chatPanes vater in SMW");
      }
    });

    this.getChildren().addAll(contentVbox, topBannerParent);
    if (!singleplayerLobby) {
      this.getChildren().add(chatWindow);

    }
  }

  /** Repaints the player cards based on current lobby info. */

  public void setUpPlayerCards() throws FileNotFoundException {
    playerCardsPane.getChildren().removeAll(playerCardsPane.getChildren());
    // ArrayList<PlayerInLobby> players = new
    // ArrayList<>(lobby.getPlayersInLobby());
    ArrayList<Player> players = new ArrayList<>(lobby.getPlayerList());
    Iterator<Player> itt = players.iterator();
    while (itt.hasNext()) {
      Player ply = itt.next();
      dirAvatarOnThisPc = new String();

      for (String avatar : Parameter.allAvatars) {
        if (ply.getAvatar().contains(avatar)) {
          dirAvatarOnThisPc = Parameter.avatarsdir + avatar;
        }
      }

      PlayerCard plyc = new PlayerCard(ply, dirAvatarOnThisPc, Color.web(ply.getColor()), ratio,
          lobby.isReady(ply));
      if (lobby.getAiPlayerList().contains(ply)) {
        plyc.setReady(true);
        lobby.setReady(ply, true);
      }
      playerCardsPane.getChildren().add(plyc);
    }
  }

  public ArrowButton getBackButton() {
    return backButton;
  }

  private static Parent loadFxml(String fxml) throws IOException {
    FXMLLoader fxmlLoader =
        new FXMLLoader(CreateProfilePaneController.class.getResource(fxml + ".fxml"));
    return fxmlLoader.load();
  }

  /** Returns Chat Window. */

  public ChatWindow getChatWindow() {
    this.getChildren().remove(chatWindow);
    ServerMainWindowController.setChatPain(chatWindow);

    return chatWindow;
  }

  /** Disables controlls for guest. */


  public void disableForGuest(int id) {
    if (lobby.getLobbyHost() != id) {
      settingsControlPane.setDisable(true);
    }
  }

  /** Enables controlls for host. */

  public void enableForNewHost(int id) {
    if (lobby.getLobbyHost() == id) {
      settingsControlPane.setDisable(false);
    }
  }
}
