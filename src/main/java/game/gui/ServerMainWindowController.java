package game.gui;

import java.io.FileInputStream;
import java.util.HashMap;

import game.gui.GUISupportClasses.ArrowButton;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
	
	StackPane container;
	HBox backgroundPic;
	VBox scrollAndMenu;
	
	ScrollPane lobbyListContainer;
	
	HBox menu;
	ArrowButton backButton;
	Button hostGameButton;
	HBox searchBar;
	
	TextField searchField;
	Button searchButton;
	
	
	
	public ServerMainWindowController() throws Exception {
		this.container = this.setup();
	}
	
	
	public StackPane setup() throws Exception {
		
		/*
		 * to be returned StackPane
		 */
		
		StackPane container = new StackPane();
		HBox backgroundPic = new HBox();//background
		
		VBox scrollAndMenu = new VBox();
		ScrollPane lobbyListContainer = new ScrollPane();//pane
		HashMap<String, HBox> lobbies = new HashMap<>(); // contains all lobbies
		HBox menu = new HBox(100);//Menu
		
		VBox vbox = new VBox();//Lobbies 
		
		/*
		 * test
		 */
		
		for (int i = 0; i < 50; i++) {
		    vbox.getChildren().add(createLabel("Name_des_Avatars"));
		    vbox.getChildren().add(createLobby());
		   
		}
	
		/*
		 * setting up background image
		 */
		
		ImageView imgBackground = new ImageView();
		imgBackground.setImage(new Image(new FileInputStream(Parameter.imagesdir + "world-map.png")));
		imgBackground.setPreserveRatio(true);
		imgBackground.setSmooth(true);
		imgBackground.setCache(true);
		
		backgroundPic.getChildren().add(imgBackground);
		
		/*
		 * setting up ScrollPane
		 */
		
		lobbyListContainer.setMaxSize(700, 500);
		lobbyListContainer.setMinSize(700, 500);
		lobbyListContainer.setStyle("-fx-background: null;"
									+"-fx-background-color: rgba(225,211,184,0.9);"
									+"-fx-background-radius: 0 0 10 10;"
									+"-fx-border-color: rgba(92,64,51);"
									+"-fx-border-width: 4px;"
									+"-fx-border-style: solid;"
									+"-fx-border-radius: 0 0 10 10");
		lobbyListContainer.setHbarPolicy(ScrollBarPolicy.NEVER);
		lobbyListContainer.setCache(true);
		
		
		Platform.runLater(()-> {
			try {
				
			/*
			 * setting up the color shape of the scrollbar and thumb
			 */
				
			ScrollBar vertikalScrollBar = (ScrollBar) lobbyListContainer.lookup(".scroll-bar:vertical");
			vertikalScrollBar.setStyle("-fx-background-color: rgba(196, 164, 132);-fx-background-radius: 10;");
			vertikalScrollBar.setPrefWidth(25);
			vertikalScrollBar.lookup(".thumb").setStyle("-fx-background-color: rgba(92,64,51);");
			
			/*
			 * shape and color of increment and decrement buttons
			 */
			
			Region incButton = (Region) lobbyListContainer.lookup(".increment-button");
			Region decButton = (Region) lobbyListContainer.lookup(".decrement-button");
			incButton.setStyle("-fx-background-color: rgba(92,64,51);"
							+  "-fx-background-radius: 0 0 10 10;"
							+  "-fx-pref-height: 20;"
							+  "-fx-pref-width: 20;");
			decButton.setStyle("-fx-background-color: rgba(92,64,51);"
							+  "-fx-background-radius: 10 10 0 0;"
							+  "-fx-pref-height: 20;"
							+  "-fx-pref-width: 20;");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
				
		});
		
		/*
		 * Setting up the menu visuals
		 */
		
		menu.setPrefSize(700, 80);
		menu.setMaxSize(700, 80);
		menu.setStyle("-fx-background-color: rgba(92,64,51); -fx-background-radius: 10 10 0 0");
		menu.setAlignment(Pos.BASELINE_LEFT);
		menu.setPadding(new Insets(20,20,20,20));
		
		/*
		 * Setting up the buttons in the Menu
		 */
		
		ArrowButton backButton = new ArrowButton(45);
		
		Button hostGameButton = new Button();
		
		/*
		 * setting up color and shape
		 */
		
		hostGameButton.setStyle("-fx-background-color: #b87331;"
				+   "-fx-background-radius: 15;"
				+   "-fx-border-radius: 15;"
				+   "-fx-border-color: #b87331;"
				+   "-fx-border-width: 3px;");
		
		/*
		 * setting up button text
		 */
		
		hostGameButton.setText("HOST GAME");
		hostGameButton.setAlignment(Pos.CENTER);
		hostGameButton.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 20));
		hostGameButton.setTextFill(Color.WHITE);
		hostGameButton.setPrefSize(200, 40);
		hostGameButton.setMaxSize(200, 40);
		
		/*
		 * setting up color while hovering button
		 */
		
		hostGameButton.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
            	hostGameButton.setStyle("-fx-background-color: #b87331;"
        				+ "-fx-background-radius: 15;"
        				+ "-fx-border-radius: 15;"
            			+ "-fx-border-color: #ffffff;"
            			+ "-fx-border-width: 3px;");
            } else {
            	hostGameButton.setStyle("-fx-background-color: #b87331;"
        				+ "-fx-background-radius: 15;"
        				+ "-fx-border-radius: 15;"
            			+ "-fx-border-color: #b87331;"
            			+ "-fx-border-width: 3px;");
            }
        });
		
		/*
		 * setting up the searchBar
		 */
		
		TextField searchField = new TextField();
		searchField.setPrefSize(200, 30);
		searchField.setMaxSize(200, 30);
		searchField.setMinSize(200, 30);
		searchField.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 20));
		searchField.setAlignment(Pos.CENTER);
		//searchField.setPadding(new Insets(15,15,15,15));
		
		Button searchButton = new Button("SEARCH");
		searchButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			//TODO implement search method
			String lobbyName = searchField.getText();
				
			}
			
		});
		
		HBox searchBar = new HBox();
		searchBar.setAlignment(Pos.CENTER);
		searchBar.getChildren().addAll(searchField, searchButton);
		
		/*
		 * setting up buttons action events
		 */
		
		hostGameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event){
			(new GameSound()).buttonClickForwardSound();
			
			try {
				lobbies.put("avatars_name",createLobby());
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			
			vbox.getChildren().clear();
			
			for (String key : lobbies.keySet())
				{
					Label label = createLabel(key);
					HBox hbox = lobbies.get(key);
					vbox.getChildren().addAll(label,hbox);
				}
			lobbyListContainer.setContent(vbox);
			
			}

			
		});

		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
			(new GameSound()).buttonClickBackwardSound();
			//TODO has to return the player to the Host/Join Window
			}
		});
		
		/*
		 * assembling the menu 
		 */
		
		menu.getChildren().addAll(backButton,searchBar,hostGameButton);
		menu.setPadding(new Insets(20,20,20,20));
		
		/*
		 * assembling menu and scrollpane with lobbies
		 */
		
		scrollAndMenu.getChildren().addAll(menu,lobbyListContainer);
		scrollAndMenu.setAlignment(Pos.CENTER);

		/*
		 * adding elements to the main container
		 */
		
		container.getChildren().addAll(backgroundPic,scrollAndMenu);
		return container;
	}
	
	
	
	/*
	 * creates the lobby in the server window
	 */
	
	private HBox createLobby() throws Exception {
		
		/*
		 * lobby size and color
		 */
		
		HBox hBox = new HBox(400);
		hBox.setPrefSize(670, 80);
		hBox.setMaxSize(670, 80);
		hBox.setStyle("-fx-background-color: rgba(225,211,184);-fx-background-radius: 50;");
		hBox.setAlignment(Pos.CENTER);
		//hBox.setPadding(new Insets(20,20,20,20));
		
		/*
		 * setting up the lobby parts
		 */
		
		Button joinButton = new Button("JOIN");
		joinButton.setAlignment(Pos.CENTER);
		joinButton.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 20));
		joinButton.setTextFill(Color.WHITE);
		joinButton.setPrefSize(100, 60);
		joinButton.setMaxSize(100, 60);
		
		joinButton.setStyle("-fx-background-color: #b87331;"
				+   "-fx-background-radius: 15;"
				+   "-fx-border-radius: 15;"
				+   "-fx-border-color: #b87331;"
				+   "-fx-border-width: 3px;");
		
		joinButton.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
            	joinButton.setStyle("-fx-background-color: #b87331;"
        				+ "-fx-background-radius: 15;"
        				+ "-fx-border-radius: 15;"
            			+ "-fx-border-color: #ffffff;"
            			+ "-fx-border-width: 3px;");
            } else {
            	joinButton.setStyle("-fx-background-color: #b87331;"
        				+ "-fx-background-radius: 15;"
        				+ "-fx-border-radius: 15;"
            			+ "-fx-border-color: #b87331;"
            			+ "-fx-border-width: 3px;");
            }
        });
		
		StackPane avatarframe= new StackPane();
		Circle avatarCircle = new Circle(30);
		ImageView avatarI= new ImageView();
		
		avatarCircle.setFill(Parameter.blueColor);
		avatarCircle.setStroke(Color.WHITE);
		avatarCircle.setStrokeWidth(6);	
		
		avatarframe.getChildren().add(avatarCircle);
		
		avatarI.setImage(new Image(new FileInputStream(Parameter.blondBoy)));
		avatarI.setFitWidth(60);
		avatarI.setFitHeight(60);
		avatarI.setPreserveRatio(true);
		avatarI.setSmooth(true);
		avatarI.setCache(true);	
		
		avatarframe.getChildren().add(avatarI);
		/*
		 * assembling the elements
		 */
		
		hBox.getChildren().addAll(avatarframe,joinButton);
		return hBox;
	}
	
	
	
	private Label createLabel(String string) {
		Label name = new Label(string);
		name.setPadding(new Insets(10,20,10,45));
		return name;
	}
	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Scene scene = new Scene(setup(),1300,900);
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
