package game.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import game.exceptions.WrongTextFieldInputException;
import game.gui.GUISupportClasses.ArrowButton;
import game.gui.GUISupportClasses.DesignButton;
import game.gui.GUISupportClasses.ImageViewPane;
import game.gui.GUISupportClasses.Spacing;
import general.AppController;
import general.Parameter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
import javafx.stage.Stage;

/**
 * 
 * @author majda The class handles the event on the create profile frame
 */
public class CreateProfilePaneController extends StackPane {
	
	private double w = MainApp.screenWidth;
	private double h = MainApp.screenHeight;
	private GameSound gameSound = new GameSound();
	private Stage stage;	
	private VBox vBox;
	private ImageView imgBackground;
	private ImageViewPane imgBackgroundPane;
	private VBox vBoxColor;
	private VBox contentVBox;
	private HBox banner;
	private HBox topBannerContent;
	private Label lobbyTextBanner;
	private ArrowButton backButton;
	private VBox mainContent;
	private HBox firstNameRow;
	private Label firstNameLabel;
	private TextField firstNameField;
	private HBox lastNameRow;
	private Label lastNameLabel;
	private TextField lastNameField;
	private HBox usernameRow;
	private Label usernameLabel;
	private TextField usernameField;
	private HBox passwordRow;
	private Label passwordLabel;
	private PasswordField passwordField;
	private HBox buttonRow;
	private DesignButton createProfileButton;
	
	private AnchorPane anchorPane;

	
	public CreateProfilePaneController() throws FileNotFoundException {
		super();
		setup();
		buttonEvents();
	}

	public void setup() throws FileNotFoundException {
		
		this.setAlignment(Pos.CENTER);
		
		/*
		 * First layer of stack
		 * Background map image
		 */
		
		vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		vBox.setFillWidth(true);

		imgBackground = new ImageView();
		imgBackground.setImage(new Image(new FileInputStream(Parameter.imagesdir + "world-map.png")));
		imgBackground.setPreserveRatio(false);
		imgBackground.setSmooth(true);
		imgBackground.setCache(true);
		
		imgBackgroundPane = new ImageViewPane(imgBackground);
		VBox.setVgrow(imgBackgroundPane, Priority.ALWAYS);
		
		vBox.getChildren().add(imgBackgroundPane);
		
		/* 
		 * Second layer of stack
		 * Color mask
		 */
		
		vBoxColor = new VBox();
		vBoxColor.setAlignment(Pos.CENTER);
		vBoxColor.setFillWidth(true);
		vBoxColor.setStyle("-fx-background-color: rgba(225, 211, 184, 0.9);");
		
		contentVBox = new VBox();
		contentVBox.setAlignment(Pos.CENTER);
		
		banner = new HBox(); 
		banner.setAlignment(Pos.TOP_LEFT);
		VBox.setMargin(banner, new Insets(50,0,0,0));
		banner.setPickOnBounds(false);

		topBannerContent = new HBox();
		topBannerContent.setAlignment(Pos.CENTER);
		topBannerContent.setStyle("-fx-background-color: "
				+ "linear-gradient(to right, rgba(100, 68, 31, 1) 60%, "
				+ "rgba(100, 68, 31, 0.7) 75%, rgba(100, 68, 31, 0) 95%);");
		topBannerContent.setMaxWidth(800);
		topBannerContent.setMinWidth(500);
		topBannerContent.setPadding(new Insets(10, 150, 10, 30));
		topBannerContent.minHeightProperty().bind(topBannerContent.maxHeightProperty());
		topBannerContent.maxHeightProperty().bind(topBannerContent.prefHeightProperty());
		topBannerContent.setPrefHeight(100);
		HBox.setHgrow(topBannerContent, Priority.ALWAYS);
		
		backButton = new ArrowButton();
		
		Spacing bannerContentSpacing = new Spacing();
		HBox.setHgrow(bannerContentSpacing, Priority.ALWAYS);
		
		lobbyTextBanner = new Label("SIGN UP");
		lobbyTextBanner.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 60));
		lobbyTextBanner.setTextFill(Color.WHITE);
		
		Spacing bannerSpacing = new Spacing();
		HBox.setHgrow(bannerSpacing, Priority.ALWAYS);
		bannerSpacing.setVisible(false);
		
		topBannerContent.getChildren().addAll(backButton, bannerContentSpacing, lobbyTextBanner);
		banner.getChildren().addAll(topBannerContent, bannerSpacing);
		
		mainContent = new VBox();
		mainContent.setAlignment(Pos.CENTER);
		mainContent.setSpacing(30);
		mainContent.setMaxWidth(600);

		firstNameRow = new HBox();
		firstNameLabel = new Label();
		firstNameField = new TextField();
		
		firstNameRow.setAlignment(Pos.CENTER);
		firstNameLabel.setText("First Name:");
		firstNameLabel.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 40));
		firstNameLabel.setTextFill(Color.web("#64441f"));
		firstNameLabel.setAlignment(Pos.CENTER_LEFT);
		firstNameField.setAlignment(Pos.CENTER_LEFT);
		firstNameField.setPrefWidth(300);
		firstNameField.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
		firstNameField.setStyle("-fx-background-color: rgba(100, 68, 31, 1), rgba(225, 211, 184, 0.9);" 
							+  "-fx-background-insets: -1 -1 -1 -1, 1 1 1 1;"
							+  "-fx-text-fill: #303030");
		
		lastNameRow = new HBox();
		lastNameLabel = new Label();
		lastNameField = new TextField();
		
		lastNameRow.setAlignment(Pos.CENTER);
		lastNameLabel.setText("Last Name:");
		lastNameLabel.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 40));
		lastNameLabel.setTextFill(Color.web("#64441f"));
		lastNameLabel.setAlignment(Pos.CENTER_LEFT);
		lastNameField.setAlignment(Pos.CENTER_LEFT);
		lastNameField.setPrefWidth(300);
		lastNameField.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
		lastNameField.setStyle("-fx-background-color: rgba(100, 68, 31, 1), rgba(225, 211, 184, 0.9);" 
							+  "-fx-background-insets: -1 -1 -1 -1, 1 1 1 1;"
							+  "-fx-text-fill: #303030");
		
		usernameRow = new HBox();
		usernameLabel = new Label();
		usernameField = new TextField();
		
		usernameRow.setAlignment(Pos.CENTER);
		usernameLabel.setText("Username:");
		usernameLabel.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 40));
		usernameLabel.setTextFill(Color.web("#64441f"));
		usernameLabel.setAlignment(Pos.CENTER_LEFT);
		usernameField.setAlignment(Pos.CENTER_LEFT);
		usernameField.setPrefWidth(300);
		usernameField.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
		usernameField.setStyle("-fx-background-color: rgba(100, 68, 31, 1), rgba(225, 211, 184, 0.9);" 
							+  "-fx-background-insets: -1 -1 -1 -1, 1 1 1 1;"
							+  "-fx-text-fill: #303030");
		
		passwordRow = new HBox();
		passwordLabel = new Label();
		passwordField = new PasswordField();
		
		passwordRow.setAlignment(Pos.CENTER);
		passwordLabel.setText("Password:");
		passwordLabel.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 40));
		passwordLabel.setTextFill(Color.web("#64441f"));
		passwordLabel.setAlignment(Pos.CENTER_LEFT);
		passwordField.setAlignment(Pos.CENTER_LEFT);
		passwordField.setPrefWidth(300);
		passwordField.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
		passwordField.setStyle("-fx-background-color: rgba(100, 68, 31, 1), rgba(225, 211, 184, 0.9);" 
							+  "-fx-background-insets: -1 -1 -1 -1, 1 1 1 1;"
							+  "-fx-text-fill: #303030");
		
		buttonRow = new HBox();
		createProfileButton = new DesignButton(new Insets(7, 20, 7, 20), 30, 30, 200);
		createProfileButton.setText("Sign Up");
		
		buttonRow.setAlignment(Pos.CENTER_RIGHT);

		firstNameRow.getChildren().addAll(firstNameLabel, new Spacing(20), firstNameField);
		lastNameRow.getChildren().addAll(lastNameLabel, new Spacing(20), lastNameField);
		usernameRow.getChildren().addAll(usernameLabel, new Spacing(20), usernameField);
		passwordRow.getChildren().addAll(passwordLabel, new Spacing(20), passwordField);
		buttonRow.getChildren().addAll(createProfileButton);
		
		mainContent.getChildren().addAll(firstNameRow, lastNameRow, usernameRow, passwordRow, buttonRow);
		contentVBox.getChildren().addAll(banner, new Spacing(50), mainContent, new Spacing(50));
		this.getChildren().addAll(vBox, vBoxColor, contentVBox);
	}
	
	public void buttonEvents() {
		createProfileButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
				gameSound.buttonClickForwardSound();
				String firstName = firstNameField.getText();
				String lastName = lastNameField.getText();
				String username = usernameField.getText();
				String password = passwordField.getText();
				try {
					// TODO integration
					MainApp.getAppController().createFirstProfile(firstName, lastName, username, password);

					Node node = (Node) event.getSource();
					// Getting the Stage where the event is happened
					stage = (Stage) node.getScene().getWindow();
					// changing the AnchorPane from the main file
					anchorPane = (AnchorPane) loadFXML("mainMenu");
					// Setting the size of the anchorPane
					anchorPane.setPrefSize(w, h);
					// Setting the AnchorPane as a root of the main scene
					stage.getScene().setRoot(anchorPane);
					// Showing the Stage
					stage.show();
				} catch (WrongTextFieldInputException e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText(e1.getMessage());
					alert.setHeaderText("ERROR");
					alert.setTitle("");
					Stage tmp = (Stage)alert.getDialogPane().getScene().getWindow();
					tmp.getIcons().add(new Image(Parameter.errorIcon));
					alert.showAndWait();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
		});
		
		backButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
				
				Node node = (Node)event.getSource();
				stage = (Stage)node.getScene().getWindow();

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
	 * 
	 * @param fxml, file name without the ending .fxml
	 * @return Parent object, to be set as a root in a Secene object
	 * @throws IOException
	 * 
	 *                     This method is responsible for loading a fxml file
	 */
	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(CreateProfilePaneController.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}
}
