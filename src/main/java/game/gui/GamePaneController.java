package game.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import game.gui.GUISupportClasses.ChatButton;
import game.gui.GUISupportClasses.ChatWindow;
import game.gui.GUISupportClasses.DesignButton;
import game.models.Battle;
import game.models.Card;
import game.models.CountryName;
import game.models.Lobby;
import game.models.Player;
import gameState.ChoosePane;
import gameState.GameType;
import gameState.Hint;
import gameState.Period;
import gameState.Phase;
import gameState.SinglePlayerHandler;
import general.AppController;
import general.GameSound;
import general.Parameter;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import network.Client;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * @author majda This class handles the events on the game board
 */
public class GamePaneController implements Initializable {

	private GameSound gameSound = AppController.getGameSound();

	private double w = MainApp.screenWidth;
	private double h = MainApp.screenHeight;

	@FXML
	private AnchorPane gameBoard;
	@FXML
	private Pane map;

	private ArrayList<SVGPath> countries;
	private ArrayList<StackPane> spTroopsDisplay;
	private HashMap<String, Circle> circleTroopsDisplay;
	private HashMap<String, Label> labelTroopsDisplay;
	private int numOfPlayer;
	private int turn;

	private VBox vbPlayerList;
	private Pane[] panesPlayerList;
	private Rectangle[] rectanglesPlayerList;
	private ImageView[] ivTimer;
	private StackPane[] stackPanes;
	private Circle[] circlesPlayerList;
	private ImageView[] avatarImageViews;

	private Pane phaseBoard;
	private VBox vbPhase;
	private Rectangle rectLogoPhase;
	private Rectangle rectPeriod;
	private StackPane spPhase;
	private Circle cirPhase;
	private ImageView ivPhase;
	private StackPane spNum;
	private Circle cirNum;
	private Label labNum;
	private Label labPhase;
	private ImageView firstPhaseLogo;
	private ImageView middlePhaseLogo;
	private ImageView lastPhaseLogo;
	private Button nextPhaseButton;
	private Rectangle rectCards;
	private ImageView cardsImageView;
	private Pane cardsPane;
	private Label numCardsLabel;

	private Pane choosingTroopsPane;
	private DesignButton lessBtn;
	private DesignButton moreBtn;
	private Label numberLabel;
	private Button trueButtonChoosingTroops;
	private Button falseButtonChoosingTroops;
	private Label choosingTroopsPhaseLabel;

	private Button throwDiceButton;
	private ImageView diceIV;

	private SinglePlayerHandler singlePlayerHandler;
	private Client client;

	private Lobby lobby;
	private ArrayList<String> playerColors;
	private ArrayList<String> playerAvatar;
	private ArrayList<Integer> playerIDs;
	private HashMap<Integer, Player> playerIdHash;

	private GameType gameType;
	private Phase currentPhase;
	private Period currentPeriod;

	private Player playerOnGUI;
	private ArrayList<Card> cardsPlayerOnGUI;

	private Pane cardsPopUp;
	private Button tradeButton;
	private Pane dropOnPane1;
	private Pane dropOnPane2;
	private Pane dropOnPane3;

	private Pane battlePane;

	private Pane tutorialMainPane;
	private Label titleLabel;
	private Label explainationLabel;
	private BattleFrameController battleFrame;
	private int currentPlayerID = 0;
	private StackPane[] rankSP;
	private Circle[] rankCircle;
	private Label[] rankLabel;

	private ChatButton chatButton;
	private ChatWindow chatWindow;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setUpMapComponents(); // we bring the map's elements from the fxml file
		setUpLeaveGameButton(); // setting up the leave game button on the game board
		setUpPhaseBoard(); // setting up the phase board where the phase, period, cards and current player
							// are displayed
		setUpNextPhaseButton(); // setting up the next level button, it is kind of confirming button for
								// finishing the interaction
		setUpChoosingTroopsPane(); // setting up choosing troops pane, where the player can choose how man troops
									// he wants to play with, in each period and phase where it is needed
		setUpCardsPopUp(); // setting up the cards popup where the player on GUI can see and select his
							// cards
		setUpTutorialsPane(); // setting up tutorial pane for displaying the hints
	}

	/**
	 * Calculates the relative horizontal position of a given value.
	 * 
	 * @param x the value to be calculated
	 * @return the relative horizontal position of the given value based on the
	 *         width of the container
	 */
	private double getRelativeHorz(double x) {
		return (x / 1536.0) * w;
	}

	/**
	 * Calculates the relative vertical position of a given value.
	 * 
	 * @param x the value to be calculated
	 * @return the relative vertical position of the given value based on the height
	 *         of the container
	 */
	private double getRelativeVer(double y) {
		return (y / 864.0) * h;
	}

	/**
	 * Initializes the singleplayerHandler and sets the first player in the lobby
	 * the current player and the player on the gui
	 * 
	 * @param singlePlayerHandler the handler for a singleplayer game
	 * @param lobby               the lobby for getting the playing players
	 */
	public void initSinglePlayer(SinglePlayerHandler singlePlayerHandler, Lobby lobby) {
		this.gameType = GameType.SinglePlayer;
		this.currentPeriod = Period.DICETHROW;
		this.singlePlayerHandler = singlePlayerHandler;
		this.lobby = lobby;
		this.playerColors = new ArrayList<>();
		this.playerAvatar = new ArrayList<>();
		this.playerIDs = new ArrayList<>();
		this.playerIdHash = new HashMap<>();
		this.cardsPlayerOnGUI = new ArrayList<>();

		/* Getting colors, avatars and ids of the players */
		for (Player p : this.lobby.getPlayerList()) {
			playerColors.add(p.getColor());
			playerAvatar.add(p.getAvatar());
			playerIDs.add(p.getID());
			playerIdHash.put(p.getID(), p);
		}

		numOfPlayer = this.lobby.getPlayerList().size();
		setUpThrowDicePeriod();
		setUpPlayerList();

		/* Setting the first player the current player and player on gui */
		for (int i = 0; i < numOfPlayer; i++) {
			circlesPlayerList[i].setFill(Color.web(playerColors.get(i)));
			rectanglesPlayerList[i].setFill(Color.web(playerColors.get(i)));
			panesPlayerList[i].setId(String.valueOf(playerIDs.get(i)));
		}
		rectanglesPlayerList[0].setVisible(true);
		cirPhase.setFill(Color.web(playerColors.get(0)));
		ivPhase.setImage(new Image(playerAvatar.get(0)));
		rectPeriod.setFill(Color.web(playerColors.get(0)));
		rectCards.setFill(Color.web(playerColors.get(0)));
		cardsImageView.setImage(new Image(
				Parameter.phaseLogosdir + "cards" + getColorAsString(Color.web(playerColors.get(0))) + ".png"));

		this.playerOnGUI = this.playerIdHash.get(playerIDs.get(0));
		this.setCurrentPlayer(playerIDs.get(0));
		setUpChatWindow();
	}

	public void initMultiPlayer(Client client, Lobby lobby) {
		setUpChatButton();
		this.gameType = GameType.Multiplayer;
		this.client = client;
		this.currentPeriod = Period.DICETHROW;
		this.lobby = lobby;
		this.playerColors = new ArrayList<>();
		this.playerAvatar = new ArrayList<>();
		this.playerIDs = new ArrayList<>();
		this.playerIdHash = new HashMap<>();
		this.cardsPlayerOnGUI = new ArrayList<>();

		for (Player p : this.lobby.getPlayerList()) {
			playerColors.add(p.getColor());
			playerAvatar.add(p.getAvatar());
			playerIDs.add(p.getID());
			playerIdHash.put(p.getID(), p);
		}

		numOfPlayer = this.lobby.getPlayerList().size();
		setUpThrowDicePeriod();
		setUpPlayerList();

		for (int i = 0; i < numOfPlayer; i++) {
			circlesPlayerList[i].setFill(Color.web(playerColors.get(i)));
			rectanglesPlayerList[i].setFill(Color.web(playerColors.get(i)));
			panesPlayerList[i].setId(String.valueOf(playerIDs.get(i)));
		}
		rectanglesPlayerList[0].setVisible(true);
		cirPhase.setFill(Color.web(playerColors.get(0)));
		ivPhase.setImage(new Image(playerAvatar.get(0)));
		rectPeriod.setFill(Color.web(playerColors.get(0)));
		rectCards.setFill(Color.web(playerColors.get(0)));
		cardsImageView.setImage(new Image(
				Parameter.phaseLogosdir + "cards" + getColorAsString(Color.web(playerColors.get(0))) + ".png"));

		this.playerOnGUI = this.playerIdHash.get(AppController.getProfile().getId());
		this.setCurrentPlayer(playerIDs.get(0));
		setUpChatWindow();
	}

	private void setUpThrowDicePeriod() {
		diceIV = new ImageView(Parameter.dicedir + "dice1.png");
		diceIV.setFitWidth(getRelativeHorz(60.0));
		diceIV.setFitHeight(getRelativeHorz(60.0));
		diceIV.setLayoutX((w - diceIV.getFitWidth()) / 2.0);
		diceIV.setLayoutY(getRelativeVer(695.0));
		diceIV.setPickOnBounds(true);

		throwDiceButton = new DesignButton();
		throwDiceButton.setText("THROW DICE");
		throwDiceButton.setFont(Font.font("Cooper Black", FontWeight.NORMAL, getRelativeHorz(18)));
		throwDiceButton.setPrefSize(getRelativeHorz(200.0), getRelativeVer(45.0));
		throwDiceButton.setLayoutX((w - throwDiceButton.getPrefWidth()) / 2.0);
		throwDiceButton.setLayoutY(getRelativeVer(760.0));
		throwDiceButton.setPickOnBounds(true);
		throwDiceButton.setOnAction(e -> {
			throwDiceButton.setDisable(true);
			switch (gameType) {
			case SinglePlayer:
				this.singlePlayerHandler.playerThrowsInitialDice(this.playerOnGUI.getID());
				break;
			case Tutorial:
				break;
			case Multiplayer:
				client.playerThrowsInitalDice(this.playerOnGUI.getID());

				break;
			default:
				break;
			}
		});

		gameBoard.getChildren().addAll(diceIV, throwDiceButton);
	}

	public void rollInitialDice(int idOfPlayer, int finalValue) {
		System.out.println(idOfPlayer + " " + finalValue);
		Thread thread = new Thread(() -> {
			for (int i = 0; i < 15; i++) {
				int k = (i * 7) % 6 + 1;
				diceIV.setImage(new Image(Parameter.dicedir + "dice" + k + ".png"));
				try {
					Thread.sleep(150);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			diceIV.setImage(new Image(Parameter.dicedir + "dice" + finalValue + ".png"));
			throwDiceButton.setDisable(false);
		});
		thread.start();
	}

	/**
	 * Brings the elements of the map
	 */
	private void setUpMapComponents() {
		double scaleX = (0.7 * w) / map.getPrefWidth();
		double scaleY = (0.7 * h) / map.getPrefHeight();
		map.setScaleX(scaleX);
		map.setScaleY(-scaleY);

		double translateX = -(-3356.693401 * scaleX + 3356.9374584);
		double translateY = -(2109.9775723 * (-scaleY) + 2109.9955679);
		map.setTranslateX(translateX);
		map.setTranslateY(translateY);

		double layoutX = ((w - scaleX * map.getPrefWidth()) / 2.0) - 45.0;
		double layoutY = ((h - scaleY * map.getPrefHeight()) / 2.0) - 40.0;
		map.setLayoutX(layoutX);
		map.setLayoutY(layoutY);

		for (Node n : map.getChildren()) {
			if (n instanceof StackPane) {
				n.setVisible(false);
			}
		}

		countries = new ArrayList<>();
		spTroopsDisplay = new ArrayList<>();
		circleTroopsDisplay = new HashMap<>();
		labelTroopsDisplay = new HashMap<>();

		for (Node n : map.getChildren()) {
			if (n instanceof SVGPath) {
				countries.add((SVGPath) n);
			}
			if (n instanceof StackPane) {
				StackPane tmp = (StackPane) n;
				spTroopsDisplay.add(tmp);
				tmp.setMouseTransparent(true);
				for (Node node : tmp.getChildren()) {
					if (node instanceof Circle) {
						circleTroopsDisplay.put(tmp.getId().substring(2), (Circle) node);
					}
					if (node instanceof Label) {
						Label labTmp = (Label) node;
						labTmp.setAlignment(Pos.CENTER);
						labTmp.setTextFill(Color.WHITE);
						labTmp.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 100px;");
						labelTroopsDisplay.put(tmp.getId().substring(2), labTmp);
					}
				}
			}
		}
	}

	/**
	 * Sets the player list on the side where all players are shown and display who
	 * is now playing
	 */
	public void setUpPlayerList() {
		vbPlayerList = new VBox();
		vbPlayerList.setPrefWidth(getRelativeHorz(192.0));
		vbPlayerList.setPrefHeight(numOfPlayer * getRelativeVer(100.0));
		panesPlayerList = new Pane[numOfPlayer];
		rectanglesPlayerList = new Rectangle[numOfPlayer];
		stackPanes = new StackPane[numOfPlayer];
		circlesPlayerList = new Circle[numOfPlayer];
		avatarImageViews = new ImageView[numOfPlayer];
		ivTimer = new ImageView[numOfPlayer];
		rankSP = new StackPane[numOfPlayer];
		rankCircle = new Circle[numOfPlayer];
		rankLabel = new Label[numOfPlayer];

		/* Creating the needed elements accordingly to the number of players */
		for (int i = 0; i < numOfPlayer; i++) {
			avatarImageViews[i] = new ImageView(playerAvatar.get(i));
			avatarImageViews[i].setFitWidth(getRelativeHorz(80.0));
			avatarImageViews[i].setFitHeight(getRelativeHorz(80.0));
			circlesPlayerList[i] = new Circle(getRelativeHorz(42.0));
			circlesPlayerList[i].setStrokeWidth(getRelativeVer(5));
			circlesPlayerList[i].setStrokeType(StrokeType.CENTERED);
			circlesPlayerList[i].setStroke(Color.WHITE);
			stackPanes[i] = new StackPane(circlesPlayerList[i], avatarImageViews[i]);
			stackPanes[i].setLayoutX(getRelativeHorz(108.0));
			rectanglesPlayerList[i] = new Rectangle(getRelativeHorz(150.0), getRelativeVer(84.0));
			rectanglesPlayerList[i].setStrokeWidth(0);
			rectanglesPlayerList[i].setOpacity(0.44);
			rectanglesPlayerList[i].setArcHeight(5.0);
			rectanglesPlayerList[i].setArcWidth(5.0);
			rectanglesPlayerList[i].setStrokeType(StrokeType.INSIDE);
			rectanglesPlayerList[i].setStrokeWidth(0.0);
			rectanglesPlayerList[i].setVisible(false);

			// Adding BoxBlur-effect
			BoxBlur boxBlur = new BoxBlur();
			boxBlur.setHeight(0.0);
			boxBlur.setWidth(38.25);
			rectanglesPlayerList[i].setEffect(boxBlur);

			// ImageView
			ivTimer[i] = new ImageView(Parameter.phaseLogosdir + "timer.png");
			ivTimer[i].setFitHeight(getRelativeHorz(45.0));
			ivTimer[i].setLayoutX(getRelativeHorz(40.0));
			ivTimer[i].setLayoutY(getRelativeVer(20.0));
			ivTimer[i].setPickOnBounds(true);
			ivTimer[i].setPreserveRatio(true);
			ivTimer[i].setVisible(false);

			rankCircle[i] = new Circle();
			rankCircle[i].setRadius(getRelativeHorz(15.0));
			rankCircle[i].setFill(Color.WHITE);

			rankLabel[i] = new Label("1");
			rankLabel[i].setFont(Font.font("Cooper Black", FontWeight.BOLD, getRelativeHorz(18.0)));
			rankLabel[i].setAlignment(Pos.CENTER);

			rankSP[i] = new StackPane();
			rankSP[i].setLayoutX(getRelativeHorz(95.0));
			rankSP[i].setPrefHeight(getRelativeHorz(15.0));
			rankSP[i].setPrefWidth(getRelativeHorz(15.0));
			rankSP[i].getChildren().addAll(rankCircle[i], rankLabel[i]);
			rankSP[i].setAlignment(Pos.CENTER);
			rankSP[i].setVisible(false);

			panesPlayerList[i] = new Pane(rectanglesPlayerList[i], stackPanes[i], ivTimer[i], rankSP[i]);

		}
		vbPlayerList.getChildren().addAll(panesPlayerList);
		vbPlayerList.setLayoutX(getRelativeHorz(1325.0));
		vbPlayerList.setLayoutY((h - vbPlayerList.getPrefHeight()) / 2.0);
		vbPlayerList.setSpacing(getRelativeVer(20.0));

		gameBoard.getChildren().add(vbPlayerList);
	}

	private void setUpChatWindow() {
		if (this.gameType.equals(gameType.Multiplayer)) {
			chatWindow = ServerMainWindowController.getChatPane();
			chatWindow.setVisible(false);
			chatWindow.setPickOnBounds(true);
			chatWindow.setLayoutX(0.5 * w - chatWindow.getMaxWidth() / 2);
			chatWindow.setLayoutY((0.5 * h - chatWindow.getPrefHeight() / 2));
			gameBoard.getChildren().add(chatWindow);
		}

	}

	private void setUpChatButton() {
		try {
			chatButton = new ChatButton(new Insets(10, 20, 10, 20), 30, 28, 170, true);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		chatButton.setLayoutX((1300.0 / 1536.0) * w);
		chatButton.setLayoutY((40.0 / 864.0) * h);
		chatButton.setPickOnBounds(true);
		chatButton.setFont(Font.font("Cooper Black", FontWeight.NORMAL, (18.0 / 1536.0) * w));

		chatButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gameSound.buttonClickForwardSound();

				if (!chatButton.isSelected()) {
					chatButton.setSelected(false);
					chatWindow.setVisible(false);
					System.out.println(chatWindow.isVisible() + " SERVERMAIN set on 'false' ");
				} else {
					chatButton.setSelected(true);
					chatWindow.setVisible(true);
					System.out.println(chatWindow.isVisible() + " SERVERMAIN set on 'true' ");
				}
				System.out.println(chatWindow.getParent() + " ist chats vater in SMW");

			}
		});

		gameBoard.getChildren().add(chatButton);
	}

	private void setUpLeaveGameButton() {
		Button leaveGameButton = new DesignButton();
		leaveGameButton.setText("LEAVE GAME");
		leaveGameButton.setOnAction(e -> clickLeaveGameButton(e));

		leaveGameButton.setPrefSize((200.0 / 1536.0) * w, (50.0 / 864.0) * h);
		leaveGameButton.setLayoutX((40.0 / 1536.0) * w);
		leaveGameButton.setLayoutY((40.0 / 864.0) * h);
		leaveGameButton.setPickOnBounds(true);
		leaveGameButton.setFont(Font.font("Cooper Black", FontWeight.NORMAL, (18.0 / 1536.0) * w));

		gameBoard.getChildren().add(leaveGameButton);
	}


	/**
	 * Sets up the next phase button, it's kind of confirming button for changing
	 * the turn, phase and period
	 */
	private void setUpNextPhaseButton() {
		nextPhaseButton = new Button();
		ImageView endTurnIV = new ImageView(Parameter.phaseLogosdir + "endturn.png");
		endTurnIV.setFitWidth(getRelativeHorz(31.0));
		endTurnIV.setFitHeight(getRelativeHorz(31.0));
		nextPhaseButton.setGraphic(endTurnIV);
		nextPhaseButton
				.setStyle("-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\"; -fx-background-color: #b87331;"
						+ "-fx-background-radius: 15;" + "-fx-background-insets: 1 1 1 1;" + "-fx-border-radius: 12;"
						+ "-fx-border-color: #b87331;" + "-fx-border-width: " + getRelativeVer(4) + "px;");

		nextPhaseButton.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (newValue) {
				nextPhaseButton.setStyle(
						"-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\"; -fx-background-color: #64441f;"
								+ "-fx-background-radius: 15;" + "-fx-background-insets: 1 1 1 1;"
								+ "-fx-border-radius: 12;" + "-fx-border-color: #ffff;" + "-fx-border-width: "
								+ getRelativeVer(4) + "px;");
			} else {
				nextPhaseButton.setStyle(
						"-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\"; -fx-background-color: #b87331;"
								+ "-fx-background-radius: 15;" + "-fx-background-insets: 1 1 1 1;"
								+ "-fx-border-radius: 12;" + "-fx-border-color: #b87331;" + "-fx-border-width: "
								+ getRelativeVer(4) + "px;");
			}
		});

		nextPhaseButton.setLayoutX(getRelativeHorz(980));
		nextPhaseButton.setLayoutY(getRelativeVer(730.0));
		nextPhaseButton.setMnemonicParsing(false);
		nextPhaseButton.setPrefHeight(getRelativeHorz(72.0));
		nextPhaseButton.setPrefWidth(getRelativeHorz(72.0));
		nextPhaseButton.setVisible(true);
		nextPhaseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gameSound.buttonClickForwardSound();
				System.out.println(playerOnGUI.getName() + " " + playerOnGUI.getID() + "clicked next phase button");
				switch (gameType) {
				case SinglePlayer:
					singlePlayerHandler.endPhaseTurn(currentPeriod, currentPhase, playerOnGUI.getID());
					break;
				case Tutorial:
					break;
				case Multiplayer:
					client.endPhaseTurn(currentPeriod, currentPhase, playerOnGUI.getID());

					break;
				default:
					break;
				}

			}
		});

		gameBoard.getChildren().add(nextPhaseButton);
	}

	/**
	 * Sets up the phase board, shows the current phase or period, phase logo,
	 * avatar and color of the current player and the cards of the player on GUI
	 */
	public void setUpPhaseBoard() {
		phaseBoard = new Pane();
		phaseBoard.setPrefSize(getRelativeHorz(645.0), getRelativeVer(120.0));

		vbPhase = new VBox();
		vbPhase.setPrefSize(getRelativeHorz(300.0), getRelativeVer(120.0));
		vbPhase.setLayoutX(getRelativeHorz(341.0));

		rectLogoPhase = new Rectangle(getRelativeHorz(300.0), getRelativeVer(60.0));
		rectLogoPhase.setArcWidth(5.0);
		rectLogoPhase.setFill(Color.WHITE);
		rectLogoPhase.setStrokeType(StrokeType.INSIDE);
		rectLogoPhase.setStrokeWidth(0.0);

		rectPeriod = new Rectangle();
		rectPeriod.setHeight(getRelativeVer(60.0));
		rectPeriod.setWidth(getRelativeHorz(300.0));

		vbPhase.getChildren().addAll(rectLogoPhase, rectPeriod);

		cirPhase = new Circle();
		cirPhase.setRadius(getRelativeHorz(42.0));
		cirPhase.setStroke(Color.WHITE);
		cirPhase.setStrokeType(StrokeType.INSIDE);
		cirPhase.setStrokeWidth(getRelativeVer(5));
		cirPhase.setStrokeType(StrokeType.OUTSIDE);

		ivPhase = new ImageView();
		ivPhase.setFitHeight(getRelativeHorz(80.0));
		ivPhase.setFitWidth(getRelativeHorz(80.0));
		ivPhase.setPickOnBounds(true);
		ivPhase.setPreserveRatio(true);

		spPhase = new StackPane();
		spPhase.setLayoutX(getRelativeHorz(282.0));
		spPhase.setLayoutY(getRelativeVer(20.0));
		spPhase.getChildren().addAll(cirPhase, ivPhase);

		cirNum = new Circle();
		cirNum.setRadius(getRelativeHorz(20.0));
		cirNum.setFill(Color.WHITE);

		/* Number of the troops left by the player */
		labNum = new Label();
		labNum.setFont(Font.font("Cooper Black", FontWeight.NORMAL, getRelativeHorz(20.0)));
		labNum.setAlignment(Pos.CENTER);

		spNum = new StackPane();
		spNum.setLayoutX(getRelativeHorz(280.0));
		spNum.setLayoutY(getRelativeVer(11.0));
		spNum.setPrefHeight(getRelativeHorz(20.0));
		spNum.setPrefWidth(getRelativeHorz(20.0));
		spNum.getChildren().addAll(cirNum, labNum);

		/* Phase or period name */
		labPhase = new Label("CLAIM");
		labPhase.setPrefSize(getRelativeHorz(300.0), getRelativeVer(40.0));
		labPhase.setLayoutX(getRelativeHorz(341.0));
		labPhase.setLayoutY(((rectPeriod.getHeight() - labPhase.getPrefHeight()) / 2.0) + rectLogoPhase.getHeight());
		labPhase.setAlignment(Pos.CENTER);
		labPhase.setFont(Font.font("Cooper Black", FontWeight.BOLD, getRelativeHorz(30.0)));
		labPhase.setTextFill(Color.WHITE);

		/* Phase logos */
		firstPhaseLogo = new ImageView(Parameter.phaseLogosdir + "reinforce.png");
		firstPhaseLogo.setFitHeight(getRelativeVer(35.0));
		firstPhaseLogo.setFitWidth(getRelativeHorz(35.0));
		firstPhaseLogo.setLayoutX(getRelativeHorz(400.0));
		firstPhaseLogo.setLayoutY((rectLogoPhase.getHeight() - firstPhaseLogo.getFitHeight()) / 2.0);
		firstPhaseLogo.setPickOnBounds(true);
		firstPhaseLogo.setPreserveRatio(true);
		firstPhaseLogo.setVisible(false);

		middlePhaseLogo = new ImageView(Parameter.phaseLogosdir + "claim.png");
		middlePhaseLogo.setFitHeight(getRelativeVer(35.0));
		middlePhaseLogo.setFitWidth(getRelativeHorz(35.0));
		middlePhaseLogo.setLayoutX(getRelativeHorz(473.5));
		middlePhaseLogo.setLayoutY((rectLogoPhase.getHeight() - middlePhaseLogo.getFitHeight()) / 2.0);
		middlePhaseLogo.setPickOnBounds(true);
		middlePhaseLogo.setPreserveRatio(true);

		lastPhaseLogo = new ImageView(Parameter.phaseLogosdir + "fortify.png");
		lastPhaseLogo.setFitHeight(getRelativeVer(35.0));
		lastPhaseLogo.setFitWidth(getRelativeHorz(35.0));
		lastPhaseLogo.setLayoutX(getRelativeHorz(548.0));
		lastPhaseLogo.setLayoutY((rectLogoPhase.getHeight() - lastPhaseLogo.getFitHeight()) / 2.0);
		lastPhaseLogo.setPickOnBounds(true);
		lastPhaseLogo.setPreserveRatio(true);
		lastPhaseLogo.setVisible(false);

		rectCards = new Rectangle();
		rectCards.setArcHeight(5.0);
		rectCards.setArcWidth(5.0);
		rectCards.setHeight(getRelativeVer(60.0));
		rectCards.setLayoutX(getRelativeHorz(99.0));
		rectCards.setLayoutY(getRelativeVer(23.0));
		rectCards.setStrokeType(StrokeType.INSIDE);
		rectCards.setStrokeWidth(0.0);
		rectCards.setWidth(getRelativeHorz(80.0));
		BoxBlur boxBlur = new BoxBlur();
		boxBlur.setHeight(0.0);
		boxBlur.setWidth(38.25);
		rectCards.setEffect(boxBlur);
		rectCards.setVisible(false);

		/* Card's sign */
		cardsImageView = new ImageView();
		cardsImageView.setFitHeight(getRelativeVer(70.0));
		cardsImageView.setLayoutX(getRelativeHorz(39.0));
		cardsImageView.setLayoutY(getRelativeVer(23.0));
		cardsImageView.setPickOnBounds(true);
		cardsImageView.setPreserveRatio(true);
		cardsImageView.setOnMouseClicked(e -> {
			showCardsPopUp();
		});
		cardsImageView.setVisible(false);

		/* Number of cards that the player on gui has */
		numCardsLabel = new Label();
		numCardsLabel.setLayoutX(getRelativeHorz(99.0));
		numCardsLabel.setLayoutY(getRelativeVer(23.0));
		numCardsLabel.setPrefHeight(getRelativeVer(60.0));
		numCardsLabel.setPrefWidth(getRelativeHorz(80.0));
		numCardsLabel.setAlignment(Pos.CENTER);
		numCardsLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, getRelativeHorz(25)));
		numCardsLabel.setTextFill(Color.WHITE);
		numCardsLabel.setVisible(false);

		phaseBoard.getChildren().addAll(vbPhase, spPhase, spNum, labPhase, firstPhaseLogo, middlePhaseLogo,
				lastPhaseLogo, rectCards, cardsImageView, numCardsLabel);

		phaseBoard.setLayoutX(((w - phaseBoard.getPrefWidth()) / 2.0) - vbPhase.getPrefWidth() / 2.0);
		phaseBoard.setLayoutY(getRelativeVer(700.0));
		phaseBoard.setVisible(false);
		phaseBoard.setPickOnBounds(true);
		gameBoard.getChildren().add(phaseBoard);
	}

	/**
	 * Sets up the pane where the player can choose the number of the troops that he
	 * can play with in phases where it is needed
	 */
	public void setUpChoosingTroopsPane() {
		choosingTroopsPane = new Pane();
		choosingTroopsPane.setPrefSize(w, h);
		choosingTroopsPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2);");

		/* Creating button for closing the pane and canceling the choosing process */
		ImageView cancelIV = new ImageView(Parameter.phaseLogosdir + "cancel.png");
		cancelIV.setFitWidth(getRelativeHorz(31.0));
		cancelIV.setFitHeight(getRelativeHorz(31.0));
		falseButtonChoosingTroops = new Button();
		falseButtonChoosingTroops.setGraphic(cancelIV);
		falseButtonChoosingTroops
				.setStyle("-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\"; -fx-background-color: #b87331;"
						+ "-fx-background-radius: 15;" + "-fx-background-insets: 1 1 1 1;" + "-fx-border-radius: 12;"
						+ "-fx-border-color: #b87331;" + "-fx-border-width: 3px;");

		falseButtonChoosingTroops.hoverProperty()
				.addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
					if (newValue) {
						falseButtonChoosingTroops.setStyle(
								"-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\"; -fx-background-color: #64441f;"
										+ "-fx-background-radius: 15;" + "-fx-background-insets: 1 1 1 1;"
										+ "-fx-border-radius: 12;" + "-fx-border-color: #ffff;"
										+ "-fx-border-width: 3px;");
					} else {
						falseButtonChoosingTroops.setStyle(
								"-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\"; -fx-background-color: #b87331;"
										+ "-fx-background-radius: 15;" + "-fx-background-insets: 1 1 1 1;"
										+ "-fx-border-radius: 12;" + "-fx-border-color: #b87331;"
										+ "-fx-border-width: 3px;");
					}
				});
		falseButtonChoosingTroops.setLayoutX(getRelativeHorz(586.0));
		falseButtonChoosingTroops.setLayoutY(getRelativeVer(608.0));
		falseButtonChoosingTroops.setMnemonicParsing(false);
		falseButtonChoosingTroops.setPrefSize(getRelativeHorz(72.0), getRelativeHorz(72.0));

		/* Creating a button for confirming the choice of the player */
		ImageView endTurnIV = new ImageView(Parameter.phaseLogosdir + "endturn.png");
		endTurnIV.setFitWidth(getRelativeHorz(31.0));
		endTurnIV.setFitHeight(getRelativeHorz(31.0));
		trueButtonChoosingTroops = new Button();
		trueButtonChoosingTroops.setGraphic(endTurnIV);
		trueButtonChoosingTroops
				.setStyle("-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\"; -fx-background-color: #b87331;"
						+ "-fx-background-radius: 15;" + "-fx-background-insets: 1 1 1 1;" + "-fx-border-radius: 12;"
						+ "-fx-border-color: #b87331;" + "-fx-border-width: 3px;");

		trueButtonChoosingTroops.hoverProperty()
				.addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
					if (newValue) {
						trueButtonChoosingTroops.setStyle(
								"-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\"; -fx-background-color: #64441f;"
										+ "-fx-background-radius: 15;" + "-fx-background-insets: 1 1 1 1;"
										+ "-fx-border-radius: 12;" + "-fx-border-color: #ffff;"
										+ "-fx-border-width: 3px;");
					} else {
						trueButtonChoosingTroops.setStyle(
								"-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\"; -fx-background-color: #b87331;"
										+ "-fx-background-radius: 15;" + "-fx-background-insets: 1 1 1 1;"
										+ "-fx-border-radius: 12;" + "-fx-border-color: #b87331;"
										+ "-fx-border-width: 3px;");
					}
				});
		trueButtonChoosingTroops.setLayoutX(getRelativeHorz(879.0));
		trueButtonChoosingTroops.setLayoutY(getRelativeVer(608.0));
		trueButtonChoosingTroops.setMnemonicParsing(false);
		trueButtonChoosingTroops.setPrefSize(getRelativeHorz(72.0), getRelativeHorz(72.0));

		/* Selected Number of troops is displayed */
		choosingTroopsPhaseLabel = new Label();
		choosingTroopsPhaseLabel.setPrefSize(getRelativeHorz(204.0), getRelativeVer(72.0));
		choosingTroopsPhaseLabel.setLayoutX((w - choosingTroopsPhaseLabel.getPrefWidth()) / 2.0);
		choosingTroopsPhaseLabel.setLayoutY(getRelativeVer(608.0));
		choosingTroopsPhaseLabel.setAlignment(Pos.CENTER);
		choosingTroopsPhaseLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, getRelativeHorz(30)));

		/* Buttons for increasing and decreasing the number of the troops */
		lessBtn = new DesignButton();
		moreBtn = new DesignButton();
		numberLabel = new Label();

		lessBtn.setText("<");
		moreBtn.setText(">");
		numberLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, getRelativeHorz(34)));
		numberLabel.setTextFill(Color.web("#b87331"));
		numberLabel.textOverrunProperty().set(OverrunStyle.CLIP);
		numberLabel.setAlignment(Pos.CENTER);

		BorderPane numTroopsBP = new BorderPane();
		numTroopsBP.setLeft(lessBtn);
		numTroopsBP.setCenter(numberLabel);
		numTroopsBP.setRight(moreBtn);
		numTroopsBP.setPrefSize(getRelativeHorz(300.0), getRelativeVer(60.0));
		numTroopsBP.setLayoutX((w - numTroopsBP.getPrefWidth()) / 2.0);
		numTroopsBP.setLayoutY(getRelativeVer(514.0));

		Rectangle backgroundChoosingTroops = new Rectangle();
		backgroundChoosingTroops.setFill(Color.web("#ecd9c6"));
		backgroundChoosingTroops.setStrokeType(StrokeType.OUTSIDE);
		backgroundChoosingTroops.setStrokeWidth(3);
		backgroundChoosingTroops.setStroke(Color.web("#b87331"));
		backgroundChoosingTroops.setWidth(getRelativeHorz(400.0));
		backgroundChoosingTroops.setHeight(getRelativeVer(200.0));
		backgroundChoosingTroops.setLayoutX((w - backgroundChoosingTroops.getWidth()) / 2.0);
		backgroundChoosingTroops.setLayoutY(getRelativeVer(500));

		choosingTroopsPane.getChildren().addAll(backgroundChoosingTroops, choosingTroopsPhaseLabel,
				trueButtonChoosingTroops, falseButtonChoosingTroops, numTroopsBP);
		choosingTroopsPane.setVisible(false);
		choosingTroopsPane.setPickOnBounds(true);
		gameBoard.getChildren().add(choosingTroopsPane);

	}

	/**
	 * Sets up the pane where cards are shown and where the player can select and
	 * trade them
	 */
	private void setUpCardsPopUp() {
		cardsPopUp = new Pane();
		cardsPopUp.setPrefSize(w, h);
		cardsPopUp.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2);");
		cardsPopUp.setVisible(false);

		Button cancelButton = new DesignButton();
		cancelButton.setText("CANCEL");
		cancelButton.setPrefSize(getRelativeHorz(180.0), getRelativeVer(45.0));
		cancelButton.setLayoutX(getRelativeHorz(1185.0));
		cancelButton.setLayoutY(getRelativeVer(90.0));
		cancelButton.setFont(Font.font("Cooper Black", FontWeight.NORMAL, getRelativeHorz(20)));
		cancelButton.setOnAction(e -> cardsPopUp.setVisible(false));

		/*
		 * Button for trading the cards, it is disabled when the player selected less
		 * than 3 cards
		 */
		tradeButton = new DesignButton();
		tradeButton.setText("NO TRADE");
		tradeButton.setPrefSize(getRelativeHorz(180.0), getRelativeVer(45.0));
		tradeButton.setLayoutX((w - tradeButton.getPrefWidth()) / 2.0);
		tradeButton.setLayoutY((h - tradeButton.getPrefHeight()) / 2.0);
		tradeButton.setFont(Font.font("Cooper Black", FontWeight.NORMAL, getRelativeHorz(20)));
		tradeButton.setDisable(true);

		Rectangle dropOnCard1 = new Rectangle();
		Rectangle dropOnCard2 = new Rectangle();
		Rectangle dropOnCard3 = new Rectangle();

		dropOnCard1.setStroke(Color.BLACK);
		dropOnCard1.setStrokeType(StrokeType.OUTSIDE);
		dropOnCard1.setStrokeWidth(3);
		dropOnCard1.getStrokeDashArray().addAll(10d, 10d);
		dropOnCard1.setFill(Color.TRANSPARENT);
		dropOnCard1.setWidth(getRelativeHorz(200.0));
		dropOnCard1.setHeight(getRelativeVer(270.0));
		dropOnCard1.setLayoutX(getRelativeHorz(435.0));
		dropOnCard1.setLayoutY(getRelativeVer(125.0));

		dropOnCard2.setStroke(Color.BLACK);
		dropOnCard2.setStrokeType(StrokeType.OUTSIDE);
		dropOnCard2.setStrokeWidth(3);
		dropOnCard2.getStrokeDashArray().addAll(10d, 10d);
		dropOnCard2.setFill(Color.TRANSPARENT);
		dropOnCard2.setWidth(getRelativeHorz(200.0));
		dropOnCard2.setHeight(getRelativeVer(270.0));
		dropOnCard2.setLayoutX(getRelativeHorz(668.0));
		dropOnCard2.setLayoutY(getRelativeVer(77.0));

		dropOnCard3.setStroke(Color.BLACK);
		dropOnCard3.setStrokeType(StrokeType.OUTSIDE);
		dropOnCard3.setStrokeWidth(3);
		dropOnCard3.getStrokeDashArray().addAll(10d, 10d);
		dropOnCard3.setFill(Color.TRANSPARENT);
		dropOnCard3.setWidth(getRelativeHorz(200.0));
		dropOnCard3.setHeight(getRelativeVer(270.0));
		dropOnCard3.setLayoutX(getRelativeHorz(901.0));
		dropOnCard3.setLayoutY(getRelativeVer(125.0));

		dropOnPane1 = new Pane();
		dropOnPane1.setPrefWidth(getRelativeHorz(200.0));
		dropOnPane1.setPrefHeight(getRelativeVer(270.0));
		dropOnPane1.setLayoutX(getRelativeHorz(435.0));
		dropOnPane1.setLayoutY(getRelativeVer(125.0));

		dropOnPane2 = new Pane();
		dropOnPane2.setPrefWidth(getRelativeHorz(200.0));
		dropOnPane2.setPrefHeight(getRelativeVer(270.0));
		dropOnPane2.setLayoutX(getRelativeHorz(668.0));
		dropOnPane2.setLayoutY(getRelativeVer(77.0));

		dropOnPane3 = new Pane();
		dropOnPane3.setPrefWidth(getRelativeHorz(200.0));
		dropOnPane3.setPrefHeight(getRelativeVer(270.0));
		dropOnPane3.setLayoutX(getRelativeHorz(901.0));
		dropOnPane3.setLayoutY(getRelativeVer(125.0));

		cardsPopUp.getChildren().addAll(tradeButton, dropOnCard1, dropOnCard2, dropOnCard3, cancelButton, dropOnPane1,
				dropOnPane2, dropOnPane3);
		cardsPopUp.setPickOnBounds(true);
		gameBoard.getChildren().add(cardsPopUp);

	}

	/**
	 * Sets up the pane where the hints are displayed
	 */
	private void setUpTutorialsPane() {
		tutorialMainPane = new Pane();
		tutorialMainPane.setPrefSize(w, h);
		tutorialMainPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2);");

		Pane tutorialTextPane = new Pane();
		tutorialTextPane.setPrefSize(getRelativeHorz(800.0), getRelativeVer(600.0));
		tutorialTextPane.setStyle("-fx-background-color: #ecd9c6;");

		Button cancelButton = new Button("CANCEL");
		cancelButton.setPrefSize(getRelativeHorz(180.0), getRelativeVer(45.0));
		cancelButton.setLayoutX(getRelativeHorz(1265.0));
		cancelButton.setLayoutY(getRelativeVer(101.0));
		cancelButton.setStyle("-fx-background-color: #cc9966; -fx-background-radius: 15px;");
		cancelButton.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (newValue) {
				cancelButton.setStyle("-fx-background-color: #ac7339; " + "-fx-background-radius: 15px; ");
			} else {
				cancelButton.setStyle("-fx-background-color: #cc9966; -fx-background-radius: 15px;");
			}
		});
		cancelButton.setFont(Font.font("Cooper Black", FontWeight.NORMAL, getRelativeHorz(20)));
		cancelButton.setOnAction(e -> tutorialMainPane.setVisible(false));

		Rectangle titleRect = new Rectangle();
		titleRect.setArcHeight(5.0);
		titleRect.setArcWidth(5.0);
		titleRect.setOpacity(0.44);
		titleRect.setHeight(getRelativeVer(84.0));
		titleRect.setLayoutX(getRelativeHorz(76.0));
		titleRect.setLayoutY(getRelativeVer(21.0));
		titleRect.setStrokeType(StrokeType.INSIDE);
		titleRect.setStrokeWidth(0.0);
		titleRect.setWidth(getRelativeHorz(648.0));
		titleRect.setFill(Color.WHITE);
		BoxBlur boxBlur = new BoxBlur();
		boxBlur.setHeight(0.0);
		boxBlur.setWidth(38.25);
		titleRect.setEffect(boxBlur);

		titleLabel = new Label();
		titleLabel.setPrefSize(getRelativeHorz(606.0), getRelativeVer(62.0));
		titleLabel.setLayoutX(getRelativeHorz(97.0));
		titleLabel.setLayoutY(getRelativeVer(32.0));
		titleLabel.setAlignment(Pos.CENTER);
		titleLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, getRelativeHorz(40.0)));
		titleLabel.setTextFill(Color.web("#5C4033"));

		explainationLabel = new Label();
		explainationLabel.setPrefSize(getRelativeHorz(766.0), getRelativeVer(415.0));
		explainationLabel.setLayoutX(getRelativeHorz(17.0));
		explainationLabel.setLayoutY(getRelativeVer(147.0));
		explainationLabel.setFont(Font.font("Cooper Black", 25));
		explainationLabel.setWrapText(true);

		tutorialTextPane.getChildren().addAll(titleRect, titleLabel, explainationLabel);
		tutorialMainPane.getChildren().addAll(tutorialTextPane, cancelButton);
		tutorialMainPane.setVisible(false);

		gameBoard.getChildren().add(tutorialMainPane);
	}

	public void showTutorialsPane(String title, Hint hint) {
		titleLabel.setText(title);
		explainationLabel.setText(hint.toString());
		tutorialMainPane.setVisible(true);
	}

	public void clickCountry(MouseEvent e) {
		String countryName = ((SVGPath) e.getSource()).getId();
		int idOfPlayer = this.playerOnGUI.getID();
		CountryName country = CountryName.valueOf(countryName);
		switch (gameType) {
		case SinglePlayer:
			System.out.println(this.playerOnGUI.getName() + " " + idOfPlayer + " clicked " + country.toString() + " "
					+ ((currentPhase == null) ? "" : currentPhase.toString()) + " " + currentPeriod.toString());
			singlePlayerHandler.clickCountry(idOfPlayer, country);
			break;
		case Tutorial:
			break;
		case Multiplayer:
			client.clickCountry(idOfPlayer, country);

			break;
		default:
			break;
		}
	}

	/**
	 * Claims the country accordingly to the parameter countryName and change the
	 * color of the country accordingly to the color of the player who has the id in
	 * the parameter and sets the number of the troops on the country on 1
	 * 
	 * @param countryName
	 * @param id
	 */
	public void claimCountry(CountryName countryName, int id) {
		for (Player p : this.lobby.getPlayerList()) {
			if (p.getID() == id) {
				circleTroopsDisplay.get(countryName.toString()).setFill(Color.web(p.getColor()));
				for (SVGPath s : countries) {
					if (s.getId().equals(countryName.toString())) {
						s.setStyle("-fx-fill: " + p.getColor() + ";");
						s.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
							if (newValue) {
								s.setStyle("-fx-fill: " + makeColorHexDarker(Color.web(p.getColor())) + ";");
							} else {
								s.setStyle("-fx-fill: " + p.getColor() + ";");
							}
						});
						break;
					}
				}
			}
		}
		for (StackPane sp : spTroopsDisplay) {
			if (sp.getId().equals("sp" + countryName.toString())) {
				sp.setVisible(true);
			}
		}
		labelTroopsDisplay.get(countryName.toString()).setText(String.valueOf(1));

	}

	public void conquerCountry(CountryName country, int id, int troops) {
		this.claimCountry(country, id);
		labelTroopsDisplay.get(country.toString()).setText(String.valueOf(troops));
	}

	/**
	 * Sets the current player based on their ID, updates the turn counter, and
	 * updates the GUI to reflect the new player's turn.
	 * 
	 * @param id the ID of the player who is the new current player
	 */
	public void setCurrentPlayer(int id) {
		// Retrieve the player object associated with the given ID
		Player player = this.playerIdHash.get(id);

		// Set the current player ID to the given ID
		this.currentPlayerID = id;

		// Find the index of the current player in the list of player IDs
		for (int i = 0; i < playerIDs.size(); i++) {
			if (player.getID() == playerIDs.get(i)) {
				turn = i;
			}
		}

		// Update the GUI to reflect the new turn
		for (int i = 0; i < numOfPlayer; i++) {
			rectanglesPlayerList[i].setVisible(i == turn);
			ivTimer[i].setVisible(i == turn);
		}
		cirPhase.setFill(Color.web(playerColors.get(turn)));
		ivPhase.setImage(new Image(playerAvatar.get(turn)));
		rectPeriod.setFill(Color.web(playerColors.get(turn)));

		// Disable the trade button if the current player is not the player on the GUI
		tradeButton.setDisable(this.currentPlayerID != this.playerOnGUI.getID());
	}

	/**
	 * Sets the number of troops on the given country
	 * 
	 * @param countryName
	 * @param numTroops
	 */
	public void setNumTroops(CountryName countryName, int numTroops) {
		labelTroopsDisplay.get(countryName.toString()).setText(String.valueOf(numTroops));
		;
	}

	/**
	 * Displays the choosing troops pane for the given country with the specified
	 * minimum and maximum number of troops allowed to be selected, and sets the
	 * text of the choosing troops phase label and number label.
	 * 
	 * @param countryName The name of the country for which the choosing troops pane
	 *                    should be displayed.
	 * @param minTroops   The minimum number of troops allowed to be selected.
	 * @param maxTroops   The maximum number of troops allowed to be selected.
	 * @param choosePane  The type of choosing pane to display.
	 */
	public void showChoosingTroopsPane(CountryName countryName, int minTroops, int maxTroops, ChoosePane choosePane) {
		// Check if it's the current player's turn and display the choosing troops pane
		// if it is
		if (this.currentPlayerID == this.playerOnGUI.getID()) {
			choosingTroopsPane.setVisible(true);
		}
		// Set the text of the choosing troops phase label and number label
		choosingTroopsPhaseLabel.setText(choosePane.toString());
		this.numberLabel.setText(String.valueOf(minTroops));
		// Set the visibility of the false button based on the type of choosing pane
		falseButtonChoosingTroops.setVisible(choosePane != ChoosePane.ATTACK_COLONISE);
		// Set the action to be performed when the false button is clicked
		falseButtonChoosingTroops.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Play a button click forward sound and hide the choosing troops pane
				gameSound.buttonClickForwardSound();
				choosingTroopsPane.setVisible(false);
				// Cancel the number of troops selected for the given country and choosing pane
				// based on the game type
				switch (gameType) {
				case SinglePlayer:
					singlePlayerHandler.cancelNumberOfTroops(countryName, choosePane, playerOnGUI.getID());
					break;
				case Tutorial:
					break;
				case Multiplayer:
					client.cancelNumberOfTroops(countryName, choosePane, playerOnGUI.getID());
					break;
				default:
					break;
				}
			}
		});

		// Set the action to be performed when the true button is clicked
		trueButtonChoosingTroops.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Play a button click forward sound and hide the choosing troops pane
				gameSound.buttonClickForwardSound();
				choosingTroopsPane.setVisible(false);
				// Confirm the number of troops selected for the given country and choosing pane
				// based on the game type
				switch (gameType) {
				case SinglePlayer:
					singlePlayerHandler.confirmNumberOfTroops(countryName, Integer.parseInt(numberLabel.getText()),
							choosePane, playerOnGUI.getID());
					break;
				case Tutorial:
					break;
				case Multiplayer:
					client.confirmNumberOfTroops(countryName, Integer.parseInt(numberLabel.getText()), choosePane,
							playerOnGUI.getID());
					break;
				default:
					break;
				}
			}
		});

		// Set the action to be performed when the less button is clicked
		lessBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Play a button click forward sound and update the number of troops selected
				gameSound.buttonClickForwardSound();
				int i = Integer.parseInt(numberLabel.getText());
				i = i == minTroops ? maxTroops : --i;
				numberLabel.setText(String.valueOf(i));
			}
		});

		// Set the action to be performed when the more button is clicked
		moreBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Play a button click forward sound and update the number of troops selected
				gameSound.buttonClickForwardSound();
				int i = Integer.parseInt(numberLabel.getText());
				i = i == maxTroops ? minTroops : ++i;
				numberLabel.setText(String.valueOf(i));
			}
		});
	}

	/**
	 * Closes choosing troops pane
	 */
	public void closeChoosingTroopsPane() {
		choosingTroopsPane.setVisible(false);

	}

	/**
	 * This method displays the pop-up window containing the Risk cards owned by the
	 * player on the GUI. The pop-up window is composed of a ScrollPane that holds
	 * an AnchorPane, which in turn holds the cards represented as VBoxes. The cards
	 * are added to an HBox, which is added to the AnchorPane. Each card VBox
	 * contains a country name pane, an army pane, and a mouse click event that
	 * listens for selecting and deselecting the card and moving it between the
	 * pop-up window and the three drop panes that represent the player's trade-in
	 * selection. The selected cards are added to an ArrayList. The trade button is
	 * enabled only when three cards are selected. When the trade button is clicked,
	 * the selected cards are sent to the server to be traded in for armies.
	 * 
	 * @return void
	 */
	public void showCardsPopUp() {
		cardsPopUp.getChildren().removeIf(x -> x instanceof HBox);
		dropOnPane1.getChildren().removeIf(x -> x instanceof VBox);
		dropOnPane2.getChildren().removeIf(x -> x instanceof VBox);
		dropOnPane3.getChildren().removeIf(x -> x instanceof VBox);

		ArrayList<Card> cards = this.cardsPlayerOnGUI;
		ArrayList<String> selectedCards = new ArrayList<>();

		AnchorPane cardsAnchorPane = new AnchorPane();

		ScrollPane cardsScrollPane = new ScrollPane(cardsAnchorPane);
		cardsScrollPane.setMinHeight(getRelativeVer(300.0));
		cardsScrollPane.setPrefWidth(getRelativeHorz(666.0));
		cardsScrollPane.setLayoutX(getRelativeHorz(435.0));
		cardsScrollPane.setLayoutY(getRelativeVer(510.0));

		HBox hbCards = new HBox();
		hbCards.setSpacing(20);
		hbCards.setPrefHeight(getRelativeVer(270.0));

		for (Card c : cards) {
			VBox vbCard = new VBox();
			vbCard.setMinSize(200, 270);
			vbCard.setPrefSize(getRelativeHorz(200.0), getRelativeVer(270.0));
			vbCard.setOnMouseClicked(e -> {
				VBox tmpVB = (VBox) e.getSource();
				tmpVB.setLayoutX(0);
				tmpVB.setLayoutY(0);
				Pane tmpP = (Pane) tmpVB.getChildren().get(0);
				Label tmpL = (Label) tmpP.getChildren().get(0);

				if (!(dropOnPane1.getChildren().contains(tmpVB) || dropOnPane2.getChildren().contains(tmpVB)
						|| dropOnPane3.getChildren().contains(tmpVB))) {
					if (dropOnPane1.getChildren().size() == 0) {
						hbCards.getChildren().remove(tmpVB);
						dropOnPane1.getChildren().add(tmpVB);
						selectedCards.add(tmpL.getText());
					} else if (dropOnPane2.getChildren().size() == 0) {
						hbCards.getChildren().remove(tmpVB);
						dropOnPane2.getChildren().add(tmpVB);
						selectedCards.add(tmpL.getText());
					} else if (dropOnPane3.getChildren().size() == 0) {
						hbCards.getChildren().remove(tmpVB);
						dropOnPane3.getChildren().add(tmpVB);
						selectedCards.add(tmpL.getText());
					}
				} else if (dropOnPane1.getChildren().contains(tmpVB)) {
					dropOnPane1.getChildren().remove(tmpVB);
					hbCards.getChildren().add(tmpVB);
					selectedCards.removeIf(x -> x.equals(tmpL.getText()));
				} else if (dropOnPane2.getChildren().contains(tmpVB)) {
					dropOnPane2.getChildren().remove(tmpVB);
					hbCards.getChildren().add(tmpVB);
					selectedCards.removeIf(x -> x.equals(tmpL.getText()));
				} else if (dropOnPane3.getChildren().contains(tmpVB)) {
					dropOnPane3.getChildren().remove(tmpVB);
					hbCards.getChildren().add(tmpVB);
					selectedCards.removeIf(x -> x.equals(tmpL.getText()));
				}

				if (dropOnPane1.getChildren().size() != 0 && dropOnPane2.getChildren().size() != 0
						&& dropOnPane3.getChildren().size() != 0 && this.currentPhase.equals(Phase.REINFORCE)) {
					tradeButton.setDisable(false);
					tradeButton.setText("TRADE");
				} else {
					tradeButton.setDisable(true);
					tradeButton.setText("NO TRADE");
				}
			});
			tradeButton.setOnAction(e -> {
				switch (gameType) {
				case SinglePlayer:
					this.singlePlayerHandler.turnInRiskCards(selectedCards, playerOnGUI.getID());
					break;
				case Tutorial:
					break;
				case Multiplayer:
					this.client.turnInRiskCards(selectedCards, playerOnGUI.getID());

					break;
				default:
					break;
				}
			});
			VBox countryNamePane = new VBox();
			StackPane countryArmyPane = new StackPane();

			countryNamePane.setStyle("-fx-background-color: " + this.playerOnGUI.getColor() + ";");
			countryArmyPane.setStyle("-fx-background-color: #ecd9c6;");

			countryNamePane.setAlignment(Pos.CENTER);
			countryArmyPane.setAlignment(Pos.CENTER);

			countryNamePane.setPrefSize(getRelativeHorz(200.0), getRelativeVer(70.0));
			countryArmyPane.setPrefSize(getRelativeHorz(200.0), getRelativeVer(200.0));

			if (c.isJoker()) {
				VBox armiesVB = new VBox();
				armiesVB.setPrefSize(55.0, 150.0);
				armiesVB.setAlignment(Pos.CENTER);
				armiesVB.setSpacing(10.0);
				armiesVB.setLayoutX((countryArmyPane.getPrefWidth() - armiesVB.getPrefWidth()) / 2.0);
				armiesVB.setLayoutY((countryArmyPane.getPrefHeight() - armiesVB.getPrefHeight()) / 2.0);

				ImageView armyIV1 = new ImageView(Parameter.infantry);
				armyIV1.setFitHeight(55.0);
				armyIV1.setPreserveRatio(true);
				armyIV1.setSmooth(true);
				armyIV1.setCache(true);

				ImageView armyIV2 = new ImageView(Parameter.artillery);
				armyIV2.setFitHeight(55.0);
				armyIV2.setPreserveRatio(true);
				armyIV2.setSmooth(true);
				armyIV2.setCache(true);

				ImageView armyIV3 = new ImageView(Parameter.cavalry);
				armyIV3.setFitHeight(55.0);
				armyIV3.setPreserveRatio(true);
				armyIV3.setSmooth(true);
				armyIV3.setCache(true);

				Label countryNameLabel = new Label("Joker");
				countryNameLabel.setAlignment(Pos.CENTER);
				countryNameLabel.setPadding(new Insets(10, 10, 10, 10));
				countryNameLabel.setFont(Font.font("Cooper Black", FontWeight.NORMAL, getRelativeHorz(20.0)));
				countryNameLabel.setTextAlignment(TextAlignment.CENTER);

				armiesVB.getChildren().addAll(armyIV1, armyIV2, armyIV3);
				countryArmyPane.getChildren().add(armiesVB);
				countryNamePane.getChildren().add(countryNameLabel);
			} else {
				String path = null;
				switch (c.getCardSymbol()) {
				case 1:
					path = Parameter.infantry;
					break;
				case 5:
					path = Parameter.cavalry;
					break;
				case 10:
					path = Parameter.artillery;
					break;
				default:
					break;
				}
				ImageView armyIV = new ImageView(path);
				armyIV.setFitHeight(100);
				armyIV.setPreserveRatio(true);
				armyIV.setSmooth(true);
				armyIV.setCache(true);

				ImageView countryIV = new ImageView(c.getPngDir());
				countryIV.setFitWidth(countryArmyPane.getPrefWidth() - 30.0);
				countryIV.setFitHeight(countryArmyPane.getPrefHeight() - 50.0);
				countryIV.setLayoutX((countryArmyPane.getPrefWidth() - countryIV.getFitWidth()) / 2.0);
				countryIV.setLayoutY((countryArmyPane.getPrefHeight() - countryIV.getFitHeight()) / 2.0);

				countryArmyPane.getChildren().add(countryIV);
				countryArmyPane.getChildren().add(armyIV);

				Label countryNameLabel = new Label(c.getName().toString().replaceAll("([a-z])([A-Z][a-z])", "$1\n$2")
						.replaceAll("([a-zA-Z])([A-Z])", "$1 $2"));
				countryNameLabel.setAlignment(Pos.CENTER);
				countryNameLabel.setPadding(new Insets(10, 10, 10, 10));
				countryNameLabel.setFont(Font.font("Cooper Black", FontWeight.NORMAL, getRelativeHorz(20.0)));
				countryNameLabel.setTextAlignment(TextAlignment.CENTER);

				countryNamePane.getChildren().add(countryNameLabel);
			}

			vbCard.getChildren().addAll(countryNamePane, countryArmyPane);
			hbCards.getChildren().add(vbCard);
		}
		cardsAnchorPane.getChildren().add(hbCards);
		cardsPopUp.getChildren().add(cardsScrollPane);
		cardsPopUp.setVisible(true);
	}

	/**
	 * Sets the current game phase and updates the GUI accordingly.
	 * 
	 * @param phase The current game phase.
	 */
	public void setPhase(Phase phase) {
		// Update phase logos visibility based on the current phase
		firstPhaseLogo.setVisible(phase == Phase.REINFORCE);
		middlePhaseLogo.setVisible(phase == Phase.ATTACK);
		lastPhaseLogo.setVisible(phase == Phase.FORTIFY);

		// Update phase label
		labPhase.setText(phase.toString());

		// Update troops left to deploy visibility and trade button disable state based
		// on the current phase
		spNum.setVisible(phase == Phase.REINFORCE);
		tradeButton.setDisable(phase != Phase.REINFORCE);

		// Set current phase
		this.currentPhase = phase;
	}

	/**
	 * Sets the current game period and updates the GUI accordingly.
	 * 
	 * @param period The current game period.
	 */
	public void setPeriod(Period period) {
		// Set the path to the phase logo image based on the current period
		String path = Parameter.phaseLogosdir;
		switch (period) {
		case COUNTRYPOSESSION:
			labPhase.setText(period.toString());
			path += "claim.png";
			break;
		case INITIALDEPLOY:
			labPhase.setText(period.toString());
			path += "claim.png";
			break;
		case MAINPERIOD:
			path += "attack.png";
			break;
		default:
			break;
		}

		// Update middle phase logo image based on the current period
		middlePhaseLogo.setImage(new Image(path));

		// Update visibility of dice and throw dice button based on the current period
		diceIV.setVisible(period == Period.DICETHROW);
		throwDiceButton.setVisible(period == Period.DICETHROW);

		// Update visibility of cards-related GUI elements based on the current period
		phaseBoard.setVisible(period != Period.DICETHROW);
		numCardsLabel.setVisible(period == Period.MAINPERIOD);
		cardsImageView.setVisible(period == Period.MAINPERIOD);
		rectCards.setVisible(period == Period.MAINPERIOD);

		// Set current period
		this.currentPeriod = period;
	}

	/**
	 * Sets the player currently shown on the GUI and updates the corresponding GUI
	 * elements.
	 * 
	 * @param idOfPlayer The ID of the player to be shown on the GUI.
	 * @param cards      The cards held by the player.
	 */
	public void setPlayerOnGUI(int idOfPlayer, ArrayList<Card> cards) {
		// Update the player and cards data
		this.playerOnGUI = this.playerIdHash.get(idOfPlayer);
		this.cardsPlayerOnGUI = cards;

		// Update cards-related GUI elements based on the color of the player
		for (int i = 0; i < playerIDs.size(); i++) {
			if (playerIDs.get(i) == idOfPlayer) {
				rectCards.setFill(Color.web(playerColors.get(i)));
				cardsImageView.setImage(new Image(
						Parameter.phaseLogosdir + "cards" + getColorAsString(Color.web(playerColors.get(i))) + ".png"));
				break;
			}
		}

		// Update the number of cards label and hide the cards popup
		numCardsLabel.setText(String.valueOf(cards.size()));
		this.cardsPopUp.setVisible(false);
	}

	/**
	 * Turns the color of a country on the map to grey.
	 * 
	 * @param countryName The name of the country to be turned grey.
	 */
	public void turnCountryGrey(CountryName countryName) {
		// Update the color of the country to grey
		for (SVGPath s : countries) {
			if (s.getId().equals(countryName.toString())) {
				ColorAdjust colorAdjust = new ColorAdjust();
				colorAdjust.setBrightness(-0.2);
				s.setEffect(colorAdjust);
			}
		}
	}

	/**
	 * Highlights the given country with a lighting effect.
	 * 
	 * @param countryName The name of the country to be highlighted.
	 */
	public void pointUpCountry(CountryName countryName) {
		// loop through all countries to find the one with the given name
		for (SVGPath s : countries) {
			if (s.getId().equals(countryName.toString())) {
				// create a lighting effect to highlight the country
				Lighting lighting = new Lighting();
				lighting.setBumpInput(null);
				lighting.setDiffuseConstant(1.68);
				lighting.setSpecularConstant(2.0);
				lighting.setSpecularExponent(40.0);
				lighting.setSurfaceScale(10.0);

				Light.Distant light = new Light.Distant();
				light.setColor((Color) s.getFill());

				lighting.setLight(light);

				// apply the lighting effect to the country
				s.setEffect(lighting);
			}
		}
	}

	/**
	 * Removes any effects applied to the given country.
	 * 
	 * @param countryName The name of the country to remove effects from.
	 */
	public void resetCountryEffect(CountryName countryName) {
		// loop through all countries to find the one with the given name
		for (SVGPath s : countries) {
			if (s.getId().equals(countryName.toString())) {
				// remove any effects applied to the country
				s.setEffect(null);
			}
		}
	}

	/**
	 * Enables the given country so that it can be clicked on.
	 * 
	 * @param countryName The name of the country to be enabled.
	 */
	public void activateCountry(CountryName countryName) {
		// loop through all countries to find the one with the given name
		for (SVGPath s : countries) {
			if (s.getId().equals(countryName.toString())) {
				// enable the country
				s.setDisable(false);
			}
		}
	}

	/**
	 * Disables the given country so that it cannot be clicked on.
	 * 
	 * @param countryName The name of the country to be disabled.
	 */
	public void deactivateCountry(CountryName countryName) {
		// loop through all countries to find the one with the given name
		for (SVGPath s : countries) {
			if (s.getId().equals(countryName.toString())) {
				// disable the country
				s.setDisable(true);
			}
		}
	}

	/**
	 * Removes the amount of troops left to deploy label.
	 */
	public void removeAmountOfTroopsLeftToDeploy() {
		spNum.setVisible(false);
	}

	/**
	 * Sets the amount of troops left to deploy label to the given number.
	 * 
	 * @param number The number to display in the label.
	 */
	public void setAmountOfTroopsLeftToDeploy(int number) {
		// set the label text to the given number
		labNum.setText(String.valueOf(number));
	}

	/**
	 * This method is called to end the game and display the podium with the
	 * results. It takes an ArrayList of Players sorted by their rank and creates a
	 * new EndGamePodiumController object with this list and a boolean indicating
	 * whether the game type is SinglePlayer or not. It then sets the scene of the
	 * current stage to the EndGamePodiumController root.
	 * 
	 * @param playersByRank An ArrayList of Players sorted by their rank
	 */
	public void endGame(ArrayList<Player> playersByRank) {
		Stage stage = (Stage) gameBoard.getScene().getWindow();
		try {
			EndGamePodiumController end = new EndGamePodiumController(playersByRank,
					this.gameType.equals(GameType.SinglePlayer));
			stage.getScene().setRoot(end);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens the battle frame for a given battle object. The battle frame contains
	 * the GUI for the battle between two players.
	 * 
	 * @param battle The battle object that contains information about the battle.
	 */
	public void openBattleFrame(Battle battle) {
		// Create a new pane for the battle frame
		battlePane = new Pane();
		// Set the preferred size of the pane
		battlePane.setPrefSize(w, h);
		// Set the style of the pane to a semi-transparent black color
		battlePane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2);");

		// Determine if the player is the attacker or defender
		boolean attacker = this.playerOnGUI.getID() == battle.getAttackerId();

		// Depending on the game type, create the battle frame with the appropriate
		// constructor
		switch (gameType) {
		case SinglePlayer:
			try {
				// Create a new battle frame for single player mode
				this.battleFrame = new BattleFrameController(battle, this.singlePlayerHandler, attacker);
				// Set the preferred size of the battle frame
				this.battleFrame.setPrefSize(w, h);
				// Add the battle frame to the battle pane
				battlePane.getChildren().add(battleFrame);
				// Add the battle pane to the game board
				gameBoard.getChildren().add(battlePane);
				// Set the correct number of troops for the battle frame
				battleFrame.setCorrectTroops();
				// Add a listener to the window size so the troops can be updated if the window
				// size changes
				Stage stage = (Stage) gameBoard.getScene().getWindow();
				stage.getScene().heightProperty().addListener(new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight,
							Number newSceneHeight) {
						if (newSceneHeight.doubleValue() != oldSceneHeight.doubleValue()) {
							try {
								battleFrame.setCorrectTroops();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
				});
			} catch (Exception e) {
				// Print stack trace if an exception occurs
				e.printStackTrace();
			}
			break;
		case Tutorial:
			// Do nothing for tutorial mode
			break;
		case Multiplayer:
			try {
				// Create a new battle frame for multiplayer mode
				this.battleFrame = new BattleFrameController(battle, this.client, attacker, chatWindow);
				// Set the preferred size of the battle frame
				this.battleFrame.setPrefSize(w, h);
				// Add the battle frame to the battle pane
				battlePane.getChildren().add(battleFrame);
				// Add the battle pane to the game board
				gameBoard.getChildren().add(battlePane);
				// Set the correct number of troops for the battle frame
				battleFrame.setCorrectTroops();
				// Add a listener to the window size so the troops can be updated if the window
				// size changes
				Stage stage = (Stage) gameBoard.getScene().getWindow();
				stage.getScene().heightProperty().addListener(new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight,
							Number newSceneHeight) {
						if (newSceneHeight.doubleValue() != oldSceneHeight.doubleValue()) {
							try {
								battleFrame.setCorrectTroops();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * This method is used to roll the dice for a battle between two players.
	 * 
	 * @param attackerDiceValues an array of integers containing the dice values of
	 *                           the attacker
	 * @param defenderDiceValues an array of integers containing the dice values of
	 *                           the defender
	 * @param troopsInAttackAt   an integer representing the number of troops in the
	 *                           attacking territory
	 * @param troopsInAttackDf   an integer representing the number of troops in the
	 *                           defending territory
	 * @param numberOfDice       an array of integers representing the number of
	 *                           dice each player rolled
	 * @throws FileNotFoundException if the file containing the dice images cannot
	 *                               be found
	 */
	public void rollDiceBattle(int[] attackerDiceValues, int[] defenderDiceValues, int troopsInAttackAt,
			int troopsInAttackDf, int[] numberOfDice) throws FileNotFoundException {
		this.battleFrame.rollBattleDice(attackerDiceValues, defenderDiceValues, troopsInAttackAt, troopsInAttackDf,
				numberOfDice);
	}

	/**
	 * Closes the battle frame and performs additional actions if the game type is
	 * multiplayer. If the game type is multiplayer, the method removes the chat
	 * window from the game board, gets the chat window from the
	 * BattleFrameController and adds it back to the game board. It then removes all
	 * the children from the battle pane.
	 */
	public void closeBattleFrame() {
		// Hide the battle pane
		this.battlePane.setVisible(false);

		// If the game type is multiplayer, remove the chat window from the game board,
		// get the chat window from the
		// BattleFrameController, add it back to the game board, and remove all the
		// children from the battle pane.
		if (this.gameType.equals(GameType.Multiplayer)) {
			this.gameBoard.getChildren().remove(chatWindow);
			this.chatWindow = ((BattleFrameController) this.battlePane.getChildren().get(0)).getChatWindow();
			this.gameBoard.getChildren().add(chatWindow);
			this.battlePane.getChildren().removeIf(x -> true);
		}
	}

	/**
	 * Displays an error dialog with the specified message.
	 * 
	 * @param message the message to be displayed in the error dialog
	 */
	public void showException(String message) {
		// Create a new alert dialog of type ERROR and set the content and header text
		// to the specified message
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(message);
		alert.setHeaderText("ERROR");
		alert.setTitle("");

		// Set the icon of the alert dialog to the error icon specified in the Parameter
		// class
		Stage tmp = (Stage) alert.getDialogPane().getScene().getWindow();
		tmp.getIcons().add(new Image(Parameter.errorIcon));

		// Show the alert dialog and wait for the user to close it
		alert.showAndWait();
	}

	/**
	 * Returns a hexadecimal representation of the specified color in the format
	 * #RRGGBB.
	 * 
	 * @param c the color to be converted to hexadecimal format
	 * @return a string representation of the input color in hexadecimal format,
	 *         with the red, green, and blue components represented by their
	 *         hexadecimal equivalents.
	 */
	private String toHex(Color c) {
		// Convert the RGB values of the input color to their hexadecimal equivalents
		// and format them as #RRGGBB
		String colorHex = String.format("#%02X%02X%02X", (int) (c.getRed() * 255), (int) (c.getGreen() * 255),
				(int) (c.getBlue() * 255));
		return colorHex;
	}

	/**
	 * Returns a darker shade of the specified color in hexadecimal format.
	 * 
	 * @param c the color to be made darker
	 * @return a string representation of the darker shade of the color in
	 *         hexadecimal format. The shade is obtained by subtracting 20 from the
	 *         RGB values of the input color and converting the result to
	 *         hexadecimal format.
	 */
	private String makeColorHexDarker(Color c) {
		// Subtract 20 from the RGB values of the input color, and convert the result to
		// hexadecimal format
		String colorHex = String.format("#%02X%02X%02X",
				(int) ((c.getRed() * 255 - 20) >= 0 ? c.getRed() * 255 - 20 : 0),
				(int) ((c.getGreen() * 255 - 20) >= 0 ? c.getGreen() * 255 - 20 : 0),
				(int) ((c.getBlue() * 255 - 20) >= 0 ? c.getBlue() * 255 - 20 : 0));
		return colorHex;
	}

	/**
	 * Returns the color name as a string representation based on the specified
	 * Color object.
	 * 
	 * @param color the color object to convert to a string representation
	 * @return the string representation of the color name. Possible values are
	 *         "Blue", "Green", "Orange", "Purple", "Red", or "Yellow". If the
	 *         specified color object does not match any of the predefined color
	 *         values, an empty string is returned.
	 */
	private String getColorAsString(Color color) {
		if (color.equals(Parameter.blueColor)) {
			return "Blue";
		} else if (color.equals(Parameter.greenColor)) {
			return "Green";
		} else if (color.equals(Parameter.orangeColor)) {
			return "Orange";
		} else if (color.equals(Parameter.purpleColor)) {
			return "Purple";
		} else if (color.equals(Parameter.redColor)) {
			return "Red";
		} else if (color.equals(Parameter.yellowColor)) {
			return "Yellow";
		} else {
			return "";
		}
	}

	/**
	 * Sets the ranking of each player in the game and updates the display
	 * accordingly.
	 * 
	 * @param playersRanking an integer array containing the ranking of each player,
	 *                       where the index of the array corresponds to the player
	 *                       number. The ranking should be greater than or equal to
	 *                       0, where 0 indicates that the player is not ranked.
	 */
	public void setPlayersRanking(int[] playersRanking) {
		for (int i = 0; i < rankSP.length; i++) {
			rankLabel[i].setText(String.valueOf(playersRanking[i]));
			if (playersRanking[i] > 0) {
				// If the player is ranked, show their rank and make the corresponding display
				// element visible
				rankSP[i].setVisible(true);
			} else {
				// If the player is not ranked, hide the corresponding display element
				rankSP[i].setVisible(false);
			}
		}
	}

	private void clickLeaveGameButton(ActionEvent e) {
		Node node = (Node) e.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		MainMenuPaneController mainMenu;
		ServerMainWindowController serverMainWindowController;
		switch (gameType) {
		case SinglePlayer:
			try {
				mainMenu = new MainMenuPaneController();
				stage.getScene().setRoot(mainMenu);
			} catch (FileNotFoundException ev) {
				ev.printStackTrace();
			}
			break;
		case Tutorial:
			try {
				mainMenu = new MainMenuPaneController();
				stage.getScene().setRoot(mainMenu);
			} catch (FileNotFoundException ev) {
				ev.printStackTrace();
			}
			break;
		case Multiplayer:
			try {
				serverMainWindowController = new ServerMainWindowController();
				stage.getScene().setRoot(serverMainWindowController);
				for (Lobby lobby : client.getLobbies().values()) {
					serverMainWindowController.lobbyGUIList.put(lobby.getLobbyName(), new LobbyGUI(lobby));
				}
				serverMainWindowController.drawLobbies(true);
			} catch (Exception ev) {
				ev.printStackTrace();
			}

			break;
		default:
			break;
		}

	}

	public ChatWindow getChatWindow() {
		return chatWindow;
	}

}