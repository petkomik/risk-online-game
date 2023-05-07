package game.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.sql.ClientInfoStatus;
import java.util.HashMap;
import java.util.function.BiConsumer;


import game.Lobby;
import game.gui.GUISupportClasses.ArrowButton;
import game.gui.GUISupportClasses.ChatButton;
import game.gui.GUISupportClasses.ChatWindow;
import game.gui.GUISupportClasses.DesignButton;
import game.gui.GUISupportClasses.ImageViewPane;
import game.gui.GUISupportClasses.Spacing;
import game.models.Player;
import game.models.PlayerSingle;
import general.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import network.Client;
import network.ClientHandler;
import network.Server;
import network.messages.MessageConnect;
import network.messages.MessageCreateLobby;
import network.messages.MessageDisconnect;
import network.messages.MessageJoinLobby;
import network.messages.MessageSend;
import network.messages.MessageServerCloseConnection;
import network.messages.MessageUpdateLobby;

/**
 * 
 * @author pmalamov 
 * 
 */

public class ServerMainWindowController extends StackPane {

	/*
	 * all containers for easier understanding of the construction
	 */
	static int counter = 0;
	private double ratio;
	private double ratioBanner;
	private Stage stage;

	private VBox menuAndScrollAndButtons; // top level container
	private HBox backgroundPic; // background
	private HBox backgroundColor; // *
	private ImageView imgBackground; // *
	private ImageViewPane imgBackgroundPane; // *

	private HBox topBannerParent; // banner
	private HBox topBannerContent; // *
	private Label lobbyTextBanner; // *
	private ArrowButton backButton; // *

	private HBox chatDiv; // chatdiv with button
	private ChatButton chatButton; // *
	private static ChatWindow chatPane2; // chatPane
	private static HBox chatPane;

	private HBox menu; // menu
	private HBox searchBar; // searchField + Button
	private TextField searchField; // *
	private DesignButton searchButton; // *

	private HBox buttonsHBox; // Join and Host buttons
	private DesignButton hostGameButton; // *
	private DesignButton joinGameButton; // *
	private Button refreshButton; // *

	private static LobbyMenuController lobbyMenuController;
	private static VBox vBoxLobbyMenuController;

	private static ScrollPane lobbyListContainer; // ScrollPane that will include the Lobbies
	private static volatile VBox vbox; // Lobbies in the scrollPane
	public static HashMap<String, LobbyGUI> lobbyGUIList; // Hashmap with all the Lobbies
	public static Lobby selectedLobby;
	public ServerMainWindowController parent = this;
	
	private static GameSound gameSound = AppController.getGameSound();


	static Server server;
	static Client client;
	private static boolean hostView;

	public ServerMainWindowController() throws Exception {
		super();
		this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
				* Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
		this.ratio = Math.min(1, this.ratio + 0.2);
		this.ratioBanner = Math.min(1, this.ratio + 0.1);
		this.setup();
		this.actionEventsSetup();
		
	}

	public void setup() throws Exception {

		lobbyGUIList = new HashMap<String, LobbyGUI>();
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
		topBannerContent.setStyle("-fx-background-color: " + "linear-gradient(to right, rgba(100, 68, 31, 1) 60%, "
				+ "rgba(100, 68, 31, 0.7) 75%, rgba(100, 68, 31, 0) 95%);");
		topBannerContent.setMaxWidth(1000 * ratioBanner);
		topBannerContent.setMinWidth(800 * ratioBanner);
		topBannerContent
				.setPadding(new Insets(10 * ratioBanner, 150 * ratioBanner, 10 * ratioBanner, 30 * ratioBanner));
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

		chatButton = new ChatButton(new Insets(10 * ratioBanner, 20 * ratioBanner, 10 * ratioBanner, 20 * ratioBanner),
				30, 28 * ratioBanner, 170 * ratio, true);
		chatButton.setAlignment(Pos.CENTER);
		chatDiv = new HBox();
		chatDiv.getChildren().add(chatButton);
		chatDiv.minHeightProperty().bind(chatDiv.maxHeightProperty());
		chatDiv.maxHeightProperty().bind(chatDiv.prefHeightProperty());
		chatDiv.setPrefHeight(100 * ratioBanner);
		chatDiv.setPadding(new Insets(0, 50 * ratioBanner, 0, 0));
		chatDiv.setAlignment(Pos.CENTER);

		backButton = new ArrowButton(45);

		topBannerContent.getChildren().addAll(backButton, bannerContentSpacing, lobbyTextBanner);
		topBannerParent.getChildren().addAll(topBannerContent, bannerSpacing, chatDiv);

		/*
		 * initializing the chat
		 */
		chatPane = new HBox();
		chatPane.setVisible(false);
		chatPane.setPickOnBounds(true);
		chatPane2 = new ChatWindow();
		chatPane2.setVisible(false);
		chatPane2.setPickOnBounds(true);
		chatPane2.getChildren().add(chatPane);
		/*
		 * setting up ScrollPane
		 */

		lobbyListContainer.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
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
		hostGameButton = new DesignButton(new Insets(ratio * 20, ratio * 10, ratio * 20, ratio * 10), 50, ratio * 30,
				ratio * 350);
		hostGameButton.setText("CREATE LOBBY");
		hostGameButton.setAlignment(Pos.CENTER);
		hostGameButton.setMinHeight(ratio * 60);

		joinGameButton = new DesignButton(new Insets(ratio * 20, ratio * 10, ratio * 20, ratio * 10), 50, ratio * 30,
				ratio * 350);
		joinGameButton.setText("JOIN LOBBY");
		joinGameButton.setAlignment(Pos.CENTER);
		joinGameButton.setMinHeight(ratio * 60);

		refreshButton = new Button();
		ImageView img = new ImageView();
		img.setImage(new Image(new FileInputStream(Parameter.refreshIcon)));
		img.setFitHeight(60 * ratio);
		img.setPreserveRatio(true);
		img.setSmooth(true);
		img.setCache(true);
		refreshButton.setGraphicTextGap(10);
		refreshButton.setGraphic(img);
		refreshButton.setStyle("-fx-background-color: transparent");

		/*
		 * setting up the searchBar and Button
		 */

		searchField = new TextField();
		searchField.setPrefSize(ratio * 300, ratio * 60);
		searchField.setMaxSize(ratio * 300, ratio * 60);
		searchField.setMinSize(ratio * 300, ratio * 60);
		searchField.setFont(Font.font("Cooper Black", FontWeight.NORMAL, ratio * 20));
		searchField.setAlignment(Pos.CENTER_LEFT);
		searchField.setStyle("-fx-background-color: rgba(225,211,184,0.9); -fx-background-radius: 10 0 0 10");

		searchButton = new DesignButton(new Insets(ratio * 10, ratio * 10, ratio * 10, ratio * 10), 12, ratio * 20,
				ratio * 130);
		searchButton.setText("SEARCH");
		searchButton.setAlignment(Pos.CENTER);
		searchButton.setMinHeight(ratio * 60);
		searchButton.setStyle("-fx-background-color: " + "radial-gradient(focus-distance 0% , center 50% 50% , "
				+ "radius 75% , #b87331, #64441f);" + "-fx-background-insets: 1 1 1 1;"
				+ "-fx-background-radius: 0 12 12 0;" + "-fx-border-radius: 0 12 12 0;"
				+ "-fx-border-color: transparent;" + "-fx-border-width: 4px;");

		searchBar = new HBox();
		searchBar.setAlignment(Pos.CENTER);
		searchBar.getChildren().addAll(searchField, searchButton);

		/*
		 * assembling the menu
		 */

		menu.getChildren().addAll(searchBar, new Spacing(1), refreshButton);
		menu.setPadding(new Insets(ratio * 20, ratio * 20, ratio * 20, ratio * 20));

		/*
		 * assemblingthe host and join buttons
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

		this.getChildren().addAll(backgroundPic, backgroundColor, menuAndScrollAndButtons, topBannerParent, chatPane2,
				vBoxLobbyMenuController);
	}

	/*
	 * setting up buttons action events
	 */

	public void actionEventsSetup() {

		chatButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gameSound.buttonClickForwardSound();

				if (!chatButton.isSelected()) {
					chatButton.setSelected(false);
					chatPane.setVisible(false);
					chatPane2.setVisible(false);
					System.out.println("lets try the chat SERVERMAIN 348");
					System.out.println(chatPane.isVisible()+ "SERVERMAIN 349  ");
				} else {
					chatButton.setSelected(true);
					chatPane.setVisible(true);
					chatPane2.setVisible(true);
					System.out.println("lets try the chat SERVERMAIN 348");
					System.out.println(chatPane.isVisible()+ "SERVERMAIN 354  ");
				}
			}
		});

		searchButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO implement search method
				String lobbyName = searchField.getText();

			}

		});

		searchButton.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (newValue) {
				searchButton.setStyle("-fx-background-color: #64441f;" + "-fx-background-insets: 1 1 1 1;"
						+ "-fx-background-radius: 0 12 12 0;" + "-fx-border-radius: 0 12 12 0;"
						+ "-fx-border-color: #ffff;" + "-fx-border-width: 4px;");
			} else {
				searchButton.setStyle("-fx-background-color: " + "radial-gradient(focus-distance 0% , center 50% 50% , "
						+ "radius 75% , #b87331, #64441f);" + "-fx-background-insets: 1 1 1 1;"
						+ "-fx-background-radius: 0 12 12 0;" + "-fx-border-radius: 0 12 12 0;"
						+ "-fx-border-color: transparent;" + "-fx-border-width: 4px;");
			}
		});

		refreshButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				refreshButton.setRotate(refreshButton.getRotate() + 90);

				// client.sendMessage(new MessageRefresh())
				// return HashMap<String, Lobby>
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
						client.closeEverything();
					} else {
						client.sendMessage(new MessageServerCloseConnection());
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

				stage.show();

//				TODO disconnect from server
//				if(hostView) {
//					client.sendMessage(new MessageServerCloseConnection());
//				} else {
//					client.sendMessage(new MessageDisconnect(client.getProfile()));
//					client.closeEverything();
//				}
			}
		});
		hostGameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gameSound.buttonClickForwardSound();
				// send profile createLobby message
				// TODO have to set the my lobby here
				// the lobby in that we get in client and set as myLobby(clientsLobby) is the
				// lobby of the other person

				Lobby aLobby = new Lobby();
			
				aLobby.joinLobby(new PlayerSingle(client.getProfile()));
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
				System.out.println(aLobby.getLobbyName());
				client.setInALobby(true);
				client.sendMessage(new MessageCreateLobby(aLobby));
				
				drawLobbyMenu(aLobby);
				
				System.out.println("im in lobby " + aLobby.getLobbyName());
				// stage.getScene().setRoot(lobbyMenuController);

			}

			// TODO send message to CLientHandler to create a lobby
		});

		joinGameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO add if the lobby is full not joining
				gameSound.buttonClickForwardSound();
//				Node node = (Node) event.getSource();
//				stage = (Stage) node.getScene().getWindow();

				for (LobbyGUI lobbyEnt : lobbyGUIList.values()) {
					if (lobbyEnt.isSelected()) {
						selectedLobby = lobbyEnt.getLobby();
					}
				}
				if ((selectedLobby != null) && (selectedLobby.getPlayerList().size() < 6)) {

					selectedLobby.joinLobby(new PlayerSingle(client.getProfile()));
					client.sendMessage(new MessageJoinLobby(selectedLobby));
					client.setInALobby(true);
					drawLobbyMenu(selectedLobby);

				}

				// TODO join the lobby and send a message to the server so that the lobby knows
				// who the new paticipant is
			}
		});

	}

	public static void drawLobbyMenu(Lobby lobby) {
		Platform.runLater(() -> {

			try {
				lobbyMenuController = new LobbyMenuController(lobby, false);
				LobbyMenuController.getBackButton().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						gameSound.buttonClickBackwardSound();
						vBoxLobbyMenuController.setVisible(false);
						//chatPane.toFront();
						System.out.println(chatPane.getParent() + " ist sein vater");
						for (Player player : lobby.getHumanPlayerList()) {
							if (player.getID() == client.getProfile().getId()) {
								
								lobby.leaveLobby(player);
								client.sendMessage(new MessageUpdateLobby(lobby));
							}
						}
						
						
					}
				});

				vBoxLobbyMenuController.getChildren().clear();
				vBoxLobbyMenuController.getChildren().add(lobbyMenuController);
				vBoxLobbyMenuController.setVisible(true);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});
	}
	
	public static void setChatPain(ChatWindow chatWindow) {
		chatPane2 = chatWindow;
		chatPane.getChildren().add(chatPane2);
	}

	public static void drawLobbies() {

		Platform.runLater(() -> {
			vbox.getChildren().clear();
			for (String key : lobbyGUIList.keySet()) {
				vbox.getChildren().add(lobbyGUIList.get(key));
			}
			System.out.println(lobbyGUIList.size());
			lobbyListContainer.setContent(vbox);
		});
	}

	public static void initServer() {

		int port = AppController.getPortNumber();
		String host = AppController.getHost();
		try {

			server = Server.createServer(port);
			client = Client.createClient(host, port);
			client.listenForMessage();
			AppController.setClient(client);
			chatPane2.setClient(client);
			client.setChat(chatPane2);
			chatPane2.addLabel(host);
			client.setHost(true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		hostView = true;
	}

	public void initClient() {
		client = AppController.getClient();
		client.listenForMessage();
		chatPane2.setClient(client);
		client.setChat(chatPane2);
		this.hostView = false;
		client.setHost(false);
		// client.sendMessage(new MessageConnect(AppController.getProfile()));

	}

	public static ChatWindow getChatPane() {
		return chatPane2;
	}

	public static Lobby getSelectedLobby() {
		return selectedLobby;
	}
}
