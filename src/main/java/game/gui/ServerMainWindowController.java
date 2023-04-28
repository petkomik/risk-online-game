package game.gui;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import game.Lobby;
import game.gui.GUISupportClasses.ArrowButton;
import game.gui.GUISupportClasses.ChatWindow;
import game.gui.GUISupportClasses.DesignButton;
import game.gui.GUISupportClasses.ImageViewPane;
import game.gui.GUISupportClasses.Spacing;
import general.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
	
	private VBox scrollAndMenu;
	private HBox backgroundPic;					//background
	private HBox backgroundColor;				//*
	private ImageView imgBackground;			//*
	private ImageViewPane imgBackgroundPane;	//*

	private HBox topBannerParent;				//banner
	private HBox topBannerContent;				//*
	private Label lobbyTextBanner;				//*
	private ArrowButton backButton;				//*


	private HBox menu;							//menu
	private HBox searchBar;						//searchField + Button
	private TextField searchField;				//*
	private DesignButton searchButton;			//*
	
	private HBox buttonsHBox;					//Join and Host buttons
	private DesignButton hostGameButton;		//*
	private DesignButton joinGameButton;		//*
	private Button refreshButton;
	
	private ScrollPane lobbyListContainer;		//ScrollPane that will include the Lobbies
	private VBox vbox;							//Lobbies in the scrollPane
	private HashMap<String, LobbyGUI> lobbyGUIList;	//Hashmap with all the Lobbies
	private HashMap<String, Lobby> lobbyList;
	private Lobby selectedLobby;				


	public ServerMainWindowController() throws Exception {
		super();
		this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
				* Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
		this.ratio = Math.min(1, this.ratio + 0.2);
		this.setup();
	}

	public void setup() throws Exception {

		/*
		 * to be returned StackPane
		 */
		backgroundPic = new HBox();					

		scrollAndMenu = new VBox();
		lobbyListContainer = new ScrollPane();		
		lobbyGUIList = new HashMap<String, LobbyGUI>(); 
		menu = new HBox();							
		vbox = new VBox();	
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(15);
		
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
		 * setting up banner layer
		 */

		topBannerParent = new HBox(); 
		topBannerParent.setAlignment(Pos.TOP_LEFT);
		StackPane.setMargin(topBannerParent, new Insets(50 * ratio,0,0,0));
		topBannerParent.setPickOnBounds(false);

		topBannerContent = new HBox();
		topBannerContent.setAlignment(Pos.CENTER);
		topBannerContent.setStyle("-fx-background-color: "
				+ "linear-gradient(to right, rgba(100, 68, 31, 1) 60%, "
				+ "rgba(100, 68, 31, 0.7) 75%, rgba(100, 68, 31, 0) 95%);");
		topBannerContent.setMaxWidth(1000 * ratio);
		topBannerContent.setMinWidth(800 * ratio);
		topBannerContent.setPadding(new Insets(10 * ratio, 150 * ratio, 10 * ratio, 30 * ratio));
		topBannerContent.minHeightProperty().bind(topBannerContent.maxHeightProperty());
		topBannerContent.maxHeightProperty().bind(topBannerContent.prefHeightProperty());
		topBannerContent.setPrefHeight(100 * ratio);
		HBox.setHgrow(topBannerContent, Priority.ALWAYS);
		
		backButton = new ArrowButton(60 * ratio);

		Spacing bannerContentSpacing = new Spacing();
		HBox.setHgrow(bannerContentSpacing, Priority.ALWAYS);
		
		lobbyTextBanner = new Label("SERVER MENU");
		lobbyTextBanner.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 60 * ratio));
		lobbyTextBanner.setTextFill(Color.WHITE);
		
		Spacing bannerSpacing = new Spacing();
		HBox.setHgrow(bannerSpacing, Priority.ALWAYS);
		bannerSpacing.setVisible(false);
			
		topBannerContent.getChildren().addAll(backButton, bannerContentSpacing, lobbyTextBanner);
		topBannerParent.getChildren().addAll(topBannerContent, bannerSpacing);

		/*
		 * setting up ScrollPane
		 */
		
		lobbyListContainer.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
		lobbyListContainer.setMaxSize(ratio * 900, ratio * 500);
		lobbyListContainer.setMinSize(ratio * 900, ratio * 500);
		lobbyListContainer.setHbarPolicy(ScrollBarPolicy.NEVER);
		lobbyListContainer.setCache(true);
		lobbyListContainer.setMaxWidth(ScrollPane.USE_PREF_SIZE);
		lobbyListContainer.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		lobbyListContainer.setPadding(new Insets(20 * ratio, 20 * ratio, 20 * ratio, 20 * ratio));
		lobbyListContainer.setFitToWidth(true);


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
		 * Setting up the buttons in the Menu
		 */

		backButton = new ArrowButton(45);

	

		/*
		 * setting up button & text
		 */
		hostGameButton = new DesignButton(new Insets(ratio * 20, ratio * 10, ratio * 20, ratio * 10), 50,
				  ratio * 30, ratio * 350);
		hostGameButton.setText("CREATE LOBBY");
		hostGameButton.setAlignment(Pos.CENTER);
		hostGameButton.setMinHeight(ratio * 60);
		
		joinGameButton = new DesignButton(new Insets(ratio * 20, ratio * 10, ratio * 20, ratio * 10), 50,
				  ratio * 30, ratio * 350);
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
		
		refreshButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				refreshButton.setRotate(refreshButton.getRotate() + 90);
				
				//client.sendMessage(new MessageRefresh())
				// return HashMap<String, Lobby>
			}
			
			
		});
		
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

		searchButton = new DesignButton(new Insets(ratio * 10, ratio * 10, ratio * 10, ratio * 10), 12,
										ratio * 20, ratio * 130);
		searchButton.setText("SEARCH");
		searchButton.setAlignment(Pos.CENTER);
		searchButton.setMinHeight(ratio * 60);
		searchButton.setStyle("-fx-background-color: " + "radial-gradient(focus-distance 0% , center 50% 50% , "
				+ "radius 75% , #b87331, #64441f);" + "-fx-background-insets: 1 1 1 1;"
				+ "-fx-background-radius: 0 12 12 0;" + "-fx-border-radius: 0 12 12 0;"
				+ "-fx-border-color: transparent;" + "-fx-border-width: 4px;");
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

		searchBar = new HBox();
		searchBar.setAlignment(Pos.CENTER);
		searchBar.getChildren().addAll(searchField, searchButton);

		/*
		 * setting up buttons action events
		 */

		hostGameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				(new GameSound()).buttonClickForwardSound();

				LobbyGUI lobbyEntry = new LobbyGUI();
				
				try {
					lobbyGUIList.put("avatars_name " + counter, lobbyEntry);
					counter++;
				} catch (Exception e) {
					e.printStackTrace();
				}

				vbox.getChildren().clear();

				for (String key : lobbyGUIList.keySet()) {
					vbox.getChildren().add(lobbyGUIList.get(key));
				}
				lobbyListContainer.setContent(vbox);

				
				lobbyEntry.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						(new GameSound()).buttonClickBackwardSound();
						selectedLobby = lobbyEntry.getLobby();
						for(LobbyGUI lobbyEnt : lobbyGUIList.values()) {
							lobbyEnt.setSelected(false);
						}
						
						lobbyEntry.setSelected(true);
						
					}
				});
				
	
			}
			
			//TODO send message to CLientHandler to create a lobby
		});

		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				(new GameSound()).buttonClickBackwardSound();
				// TODO has to return the player to the Host/Join Window and disconnect from the server
			}
		});
		
		joinGameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				(new GameSound()).buttonClickBackwardSound();
				// TODO join the lobby and send a message to the server so that the lobby knows who the new paticipant is
			}
		});
		
		

		/*
		 * assembling the menu
		 */

		menu.getChildren().addAll(searchBar, new Spacing(1), refreshButton);
		menu.setPadding(new Insets(ratio * 20, ratio * 20, ratio * 20, ratio * 20));

		/*
		 * assembling menu and scrollpane with lobbies
		 */
		
		buttonsHBox = new HBox();
		buttonsHBox.setAlignment(Pos.CENTER);
		buttonsHBox.getChildren().addAll(hostGameButton,joinGameButton);
		buttonsHBox.setSpacing(20*ratio);
		buttonsHBox.setPadding(new Insets(30*ratio,0,0,0));


		scrollAndMenu.getChildren().addAll(menu, lobbyListContainer, buttonsHBox);
		scrollAndMenu.setAlignment(Pos.CENTER);
		scrollAndMenu.setPadding(new Insets(50 * ratio, 0, 0, 0));

		/*
		 * adding elements to the main container
		 */

		
		this.getChildren().addAll(backgroundPic, backgroundColor, scrollAndMenu, topBannerParent);
	}
	
	public static void initServer() {
		
		Server server;
		Client client;
		ClientHandler handler;
		Socket socket;
		int port = AppController.getPortNumber();
		String host = AppController.getHost();
		try {
			
			server = Server.createServer(port);
			client = Client.createClient(host, port);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
	}
	
	public void initClient( ) {
		
	}

}
