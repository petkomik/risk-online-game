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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import network.Client;
import general.AppController;
import general.Parameter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * 
 * @author pmalamov the class handles the join server event
 *
 */

public class MultiplayerJoinWindowController extends StackPane {
	
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
	private HBox ipAddressRow;
	private Label ipAddressLabel;
	private TextField ipAddressField;
	private HBox portNumberRow;
	private Label portNumberLabel;
	private PasswordField portNumberField;
	private HBox buttonRow;
	private DesignButton joinButton;
	private double ratio;
	
	private AnchorPane anchorPane;

	
	public MultiplayerJoinWindowController() throws FileNotFoundException {
		super();
		this.ratio = Screen.getPrimary().getVisualBounds().getWidth() * Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
		this.ratio = Math.min(ratio + 0.3, 1);
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
		VBox.setMargin(banner, new Insets(50 * ratio,0,0,0));
		banner.setPickOnBounds(false);

		topBannerContent = new HBox();
		topBannerContent.setAlignment(Pos.CENTER);
		topBannerContent.setStyle("-fx-background-color: "
				+ "linear-gradient(to right, rgba(100, 68, 31, 1) 60%, "
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
		
		lobbyTextBanner = new Label("JOIN SERVER");
		lobbyTextBanner.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 60 * ratio));
		lobbyTextBanner.setTextFill(Color.WHITE);
		
		Spacing bannerSpacing = new Spacing();
		HBox.setHgrow(bannerSpacing, Priority.ALWAYS);
		bannerSpacing.setVisible(false);
		
		topBannerContent.getChildren().addAll(backButton, bannerContentSpacing, lobbyTextBanner);
		banner.getChildren().addAll(topBannerContent, bannerSpacing);
		
		mainContent = new VBox();
		mainContent.setAlignment(Pos.CENTER);
		mainContent.setSpacing(30 * ratio);
		mainContent.setMaxWidth(700 * ratio);

		ipAddressRow = new HBox();
		ipAddressLabel = new Label();
		ipAddressField = new TextField();
		
		ipAddressRow.setAlignment(Pos.CENTER);
		ipAddressLabel.setText("IP Address:");
		ipAddressLabel.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 40 * ratio));
		ipAddressLabel.setTextFill(Color.web("#64441f"));
		ipAddressLabel.setAlignment(Pos.CENTER_LEFT);
		ipAddressField.setAlignment(Pos.CENTER_LEFT);
		ipAddressField.setPrefWidth(300 * ratio);
		ipAddressField.setFont(Font.font("Verdana", FontWeight.NORMAL, 20 * ratio));
		ipAddressField.setStyle("-fx-background-color: rgba(100, 68, 31, 1), rgba(225, 211, 184, 0.9);" 
							+  "-fx-background-insets: -1 -1 -1 -1, 1 1 1 1;"
							+  "-fx-text-fill: #303030");
		
		portNumberRow = new HBox();
		portNumberLabel = new Label();
		portNumberField = new PasswordField();
		
		portNumberRow.setAlignment(Pos.CENTER);
		portNumberLabel.setText("Port Number:");
		portNumberLabel.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 40 * ratio));
		portNumberLabel.setTextFill(Color.web("#64441f"));
		portNumberLabel.setAlignment(Pos.CENTER_LEFT);
		portNumberField.setAlignment(Pos.CENTER_LEFT);
		portNumberField.setPrefWidth(300 * ratio);
		portNumberField.setFont(Font.font("Verdana", FontWeight.NORMAL, 20 * ratio));
		portNumberField.setStyle("-fx-background-color: rgba(100, 68, 31, 1), rgba(225, 211, 184, 0.9);" 
							+  "-fx-background-insets: -1 -1 -1 -1, 1 1 1 1;"
							+  "-fx-text-fill: #303030");
		
		buttonRow = new HBox();
		joinButton = new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 35, 30 * ratio, 200 * ratio);
		joinButton.setText("Join");
		
		buttonRow.setAlignment(Pos.CENTER_RIGHT);

		ipAddressRow.getChildren().addAll(ipAddressLabel, new Spacing(20), ipAddressField);
		portNumberRow.getChildren().addAll(portNumberLabel, new Spacing(20), portNumberField);
		buttonRow.getChildren().addAll(joinButton);
		
		mainContent.getChildren().addAll(ipAddressRow, portNumberRow, buttonRow);
		contentVBox.getChildren().addAll(banner, new Spacing(50), mainContent, new Spacing(50));
		this.getChildren().addAll(vBox, vBoxColor, contentVBox);
	}
	
	
	public void buttonEvents() {
		joinButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	gameSound.buttonClickForwardSound();
				// to be used as server data inputs
				String ipAddress = ipAddressField.getText();
				String portNumber = portNumberField.getText();

				try {

					if (ipAddress.isBlank()) {
						AppController.setHost(Parameter.hostDefault);
					} else if (ipAddress.matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b|localhost\\b")) {
						AppController.setHost(ipAddress);
					} else {
						throw new WrongTextFieldInputException("The Input can not be resolved to be an IP Address");
					}

					if (portNumber.isBlank()) {
						AppController.setPortNumber(Parameter.portDefault);
					} else if (portNumber.matches(
							"\\b(0|[1-9]\\d{0,4}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])\\b")) {
						AppController.setPortNumber(Integer.parseInt(portNumber));
					} else {
						throw new WrongTextFieldInputException("The Input can not be resolved to be an Port");
					}

					AppController.setClient(Client.createClient(AppController.getHost(), AppController.getPortNumber()));
				} catch (IOException | WrongTextFieldInputException e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText(e1.getMessage() + ((e1 instanceof IOException) ? "\nHost: " + AppController.getHost() + " Port: " + AppController.getPortNumber(): ""));
					alert.setHeaderText("ERROR");
					alert.setTitle("");
					Stage tmp = (Stage) alert.getDialogPane().getScene().getWindow();
					tmp.getIcons().add(new Image(Parameter.errorIcon));
					alert.showAndWait();
					e1.printStackTrace();
					return;
				}

				Node node = (Node) event.getSource();
				// Getting the Stage where the event is happened
				stage = (Stage) node.getScene().getWindow();
				// changing the AnchorPane from the main file
				try {
					anchorPane = (AnchorPane) loadFXML("JoinClientMessengerFrame");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Setting the size of the anchorPane
				anchorPane.setPrefSize(w, h);
				// Setting the AnchorPane as a root of the main scene
				stage.getScene().setRoot(anchorPane);
				// Showing the Stage
				stage.show();
		    }
		});
		
		backButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickBackwardSound();
				Node node = (Node) event.getSource();
				stage = (Stage)node.getScene().getWindow();
				try {
					MultplayerHostJoinController mlt = new MultplayerHostJoinController();
					stage.getScene().setRoot(mlt);
				} catch (IOException e) {
					e.printStackTrace();
				}
				stage.show();
		    }
	   });
	}
		
	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(StartPaneController.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

}
