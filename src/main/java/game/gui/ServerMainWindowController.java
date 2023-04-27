package game.gui;

import java.io.FileInputStream;
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

/**
 * 
 * @author pmalamov
 * 
 */

public class ServerMainWindowController extends Application {

	/*
	 * all containers for easier understanding of the construction
	 */
	static int counter = 0;
	private double ratio;
	
	private StackPane container; 				//main container
	private VBox scrollAndMenu;
	private HBox backgroundPic;					//background
	private ImageView imgBackground;			//*
	private ImageViewPane imgBackgroundPane;	//*

	private HBox banner;						//Banner + components
	private HBox topBannerContent;				//*
	private Label lobbyTextBanner;				//*
	private VBox contentVBox;					//*
	private ArrowButton backButton;				//*


	private HBox menu;							//menu
	private DesignButton hostGameButton;		//*
	
	private HBox searchBar;						//searchField + Button
	private TextField searchField;				//*
	private DesignButton searchButton;			//*
	
	private ScrollPane lobbyListContainer;		//ScrollPane that will include the Lobbies
	private VBox vbox;							//Lobbies in the scrollPane
	private Lobby lobby;
	private HashMap<String,LobbyGUI> lobbyList;	//Hashmap with all the Lobbies

	public ServerMainWindowController() throws Exception {
		super();
		this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
				* Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
		this.ratio = Math.min(ratio + 0.3, 1);
		this.container = this.setup();
	}

	public StackPane setup() throws Exception {

		/*
		 * to be returned StackPane
		 */
		container = new StackPane();
		backgroundPic = new HBox();					

		scrollAndMenu = new VBox();
		lobbyListContainer = new ScrollPane();		
		lobbyList = new HashMap<String,LobbyGUI>(); 
		menu = new HBox();							
		vbox = new VBox();							

		/*
		 * setting up background image
		 */

		imgBackground = new ImageView();
		imgBackground.setImage(new Image(new FileInputStream(Parameter.imagesdir + "world-map.png")));
		imgBackground.setPreserveRatio(false);
		imgBackground.setSmooth(true);
		imgBackground.setCache(true);

		imgBackgroundPane = new ImageViewPane(imgBackground);
		HBox.setHgrow(imgBackgroundPane, Priority.ALWAYS);
		backgroundPic.getChildren().add(imgBackgroundPane);

		/*
		 * setting up banner layer
		 */

		banner = new HBox();
		banner.setAlignment(Pos.TOP_LEFT);
		VBox.setMargin(banner, new Insets(50 * ratio, 0, 0, -15 * ratio));
		banner.setPickOnBounds(false);

		topBannerContent = new HBox();
		topBannerContent.setAlignment(Pos.CENTER);
		topBannerContent.setStyle("-fx-background-color: " + "linear-gradient(to right, rgba(100, 68, 31, 1) 60%, "
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

		lobbyTextBanner = new Label("LOBBIES");
		lobbyTextBanner.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 60 * ratio));
		lobbyTextBanner.setTextFill(Color.WHITE);

		Spacing bannerSpacing = new Spacing();
		HBox.setHgrow(bannerSpacing, Priority.ALWAYS);
		bannerSpacing.setVisible(false);

		topBannerContent.getChildren().addAll(backButton, bannerContentSpacing, lobbyTextBanner);
		banner.getChildren().addAll(topBannerContent, bannerSpacing);

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

		hostGameButton = new DesignButton(new Insets(ratio * 10, ratio * 10, ratio * 10, ratio * 10), 12,
										  ratio * 20, ratio * 230);

		/*
		 * setting up button text
		 */

		hostGameButton.setText("CREATE GAME");
		hostGameButton.setAlignment(Pos.CENTER);
		hostGameButton.setMinHeight(ratio * 60);

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

				try {
					lobbyList.put("avatars_name " + counter, new LobbyGUI());
					counter++;
				} catch (Exception e) {
					e.printStackTrace();
				}

				vbox.getChildren().clear();

				for (String key : lobbyList.keySet()) {
					Label label = createLabel(key);
					vbox.getChildren().addAll(new Spacing(5), lobbyList.get(key).getVisual(), new Spacing(5));
				}
				lobbyListContainer.setContent(vbox);

			}

		});

		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				(new GameSound()).buttonClickBackwardSound();
				// TODO has to return the player to the Host/Join Window
			}
		});

		/*
		 * assembling the menu
		 */

		menu.getChildren().addAll(searchBar, new Spacing(50 * ratio), hostGameButton);
		menu.setPadding(new Insets(ratio * 20, ratio * 20, ratio * 20, ratio * 20));

		/*
		 * assembling menu and scrollpane with lobbies
		 */

		scrollAndMenu.getChildren().addAll(menu, lobbyListContainer);
		scrollAndMenu.setAlignment(Pos.CENTER);

		/*
		 * adding elements to the main container
		 */
		ChatWindow test = new ChatWindow();

		contentVBox = new VBox();
		contentVBox.getChildren().addAll(banner, new Spacing(50), scrollAndMenu, new Spacing(100));
		contentVBox.setAlignment(Pos.CENTER);
		contentVBox.setSpacing(30 * ratio);
		contentVBox.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth());

		container.getChildren().addAll(backgroundPic, contentVBox);
		return container;
	}

	/*
	 * creates the lobby in the server window
	 */

	private Label createLabel(String string) {
		Label name = new Label(string);
		name.setPadding(new Insets(10, 20, 10, 45));
		return name;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Scene scene = new Scene(setup(), 1300, 900);
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);
		primaryStage.setTitle("MainServerWindow");
		primaryStage.setMinHeight(800);
		primaryStage.setMinWidth(1300);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
