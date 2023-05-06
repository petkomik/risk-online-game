package game.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import game.Lobby;
import game.exceptions.WrongTextFieldInputException;
import game.gui.GUISupportClasses.ArrowButton;
import game.gui.GUISupportClasses.DesignButton;
import game.gui.GUISupportClasses.ImageViewPane;
import game.gui.GUISupportClasses.Spacing;
import general.AppController;
import general.Parameter;
import javafx.application.Platform;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * 
 * @author pmikov jorohr
 * This class handles the profile updating
 * 
 */

public class UpdateSettingsController extends StackPane {
	
	VBox vBox;
	ImageView imgBackground;
	ImageViewPane imgBackgroundPane;
	VBox vBoxColor;
	VBox contentVBox;
	
	HBox topBannerParent;
	HBox topBannerContent;
	ArrowButton backButton;
	Label lobbyTextBanner;

	StackPane stackAvatar;
	Circle circleAvatar;
	ImageView imageAvatar;

	VBox mainContent;
	
	HBox infoRow;
	VBox leftInfo;
	VBox rightInfo;

	HBox usernameRow;
	Label usernameLabel;
	TextField usernameTextField;
	
	HBox passwordRow;
	Label passwordLabel;
	PasswordField passwordField;
	
	HBox firstNameRow;
	Label firstNameLabel;
	TextField firstNameField;
	
	HBox lastNameRow;
	Label lastNameLabel;
	TextField lastNameField;

	HBox colorRow;
	Label colorLabel;
	ArrowButton colorLeft;
	Rectangle colorCurrent;
	ArrowButton colorRight;
	
	HBox avatarRow;
	Label avatarLabel;
	ArrowButton avatarLeft;
	ImageView avatarCurrent;
	ArrowButton avatarRight;
	
	HBox winsRow;
	Label winsLabel;
	Label winsText;
	
	HBox losesRow;
	Label losesLabel;
	Label losesText;
	
	HBox buttonRow;
	DesignButton updateSaveButton;
	DesignButton deleteButton;
	
	double ratio;
	String username;
	String password;
	String firstName;
	String lastName;
	
	static Color[] colors = new Color[] {Parameter.blueColor, Parameter.greenColor, 
			Parameter.orangeColor, Parameter.purpleColor, 
			Parameter.redColor, Parameter.yellowColor};
	static String[] avatars = new String[] {Parameter.blondBoy, Parameter.gingerGirl,
			Parameter.bruntetteBoy, Parameter.mustacheMan,
			Parameter.earringsGirl, Parameter.hatBoy};

	public UpdateSettingsController() throws FileNotFoundException {
		this.ratio = Screen.getPrimary().getVisualBounds().getWidth() * Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
		this.ratio = Math.min(ratio + 0.3, 1);
		this.setupBackground();
		this.setupMainContent();
		this.setupBanner();
		this.setupPlayerInfo();
		this.buttonEvents();
	}
	
	public void setupBackground() throws FileNotFoundException {
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
		
		this.getChildren().addAll(vBox, vBoxColor);
		
		contentVBox = new VBox();
		contentVBox.setAlignment(Pos.CENTER);

	}
	
	public void setupBanner() throws FileNotFoundException {
		topBannerParent = new HBox(); 
		topBannerParent.setAlignment(Pos.TOP_LEFT);
		topBannerParent.setPickOnBounds(false);

		topBannerContent = new HBox();
		topBannerContent.setAlignment(Pos.CENTER);
		topBannerContent.setStyle("-fx-background-color: "
				+ "linear-gradient(to right, rgba(100, 68, 31, 1) 60%, "
				+ "rgba(100, 68, 31, 0.7) 75%, rgba(100, 68, 31, 0) 95%);");
		topBannerContent.setMaxWidth(1100 * ratio);
		topBannerContent.setMinWidth(1100 * ratio);
		topBannerContent.setPadding(new Insets(10 * ratio, 150 * ratio, 10 * ratio, 30 * ratio));
		topBannerContent.minHeightProperty().bind(topBannerContent.maxHeightProperty());
		topBannerContent.maxHeightProperty().bind(topBannerContent.prefHeightProperty());
		topBannerContent.setPrefHeight(100 * ratio);
		HBox.setMargin(topBannerContent, new Insets(50 * ratio,0,0,0));
		HBox.setHgrow(topBannerContent, Priority.ALWAYS);
		
		backButton = new ArrowButton(60 * ratio);

		Spacing bannerContentSpacing = new Spacing();
		HBox.setHgrow(bannerContentSpacing, Priority.ALWAYS);
		
		lobbyTextBanner = new Label("PROFILE SETTINGS");
		lobbyTextBanner.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 60 * ratio));
		lobbyTextBanner.setTextFill(Color.WHITE);
		
		Spacing bannerSpacing = new Spacing();
		HBox.setHgrow(bannerSpacing, Priority.ALWAYS);
		bannerSpacing.setVisible(false);
		
		stackAvatar = new StackPane();
		circleAvatar = new Circle();
		imageAvatar = new ImageView();
		
		stackAvatar.setAlignment(Pos.CENTER);
		stackAvatar.setPadding(new Insets(30 * ratio, 30 * ratio, 30 * ratio, 30 * ratio));
		stackAvatar.minHeightProperty().bind(stackAvatar.maxHeightProperty());
		stackAvatar.maxHeightProperty().bind(stackAvatar.prefHeightProperty());
		stackAvatar.setPrefHeight(200 * ratio);
		
		circleAvatar.setRadius(70 * ratio);
		circleAvatar.setStroke(Color.WHITE);
		circleAvatar.setStrokeWidth(8 * ratio);
		
		imageAvatar.setFitWidth(120 * ratio);
		imageAvatar.setFitHeight(120 * ratio);
		imageAvatar.setPreserveRatio(true);
		imageAvatar.setSmooth(true);
		imageAvatar.setCache(true);
		
		stackAvatar.getChildren().addAll(circleAvatar, imageAvatar);
		topBannerContent.getChildren().addAll(backButton, bannerContentSpacing, lobbyTextBanner);
		topBannerParent.getChildren().addAll(topBannerContent, bannerSpacing, stackAvatar);
		this.getChildren().add(topBannerParent);
	}
	
	public void setupMainContent() throws FileNotFoundException {
		mainContent = new VBox();
		mainContent.setAlignment(Pos.CENTER);
		mainContent.setSpacing(50 * ratio);
		mainContent.setFillWidth(true);
		mainContent.setPadding(new Insets(100 * ratio, 150 * ratio, 50 * ratio, 150 * ratio));
		
		infoRow = new HBox();
		infoRow.setAlignment(Pos.CENTER);
		infoRow.setSpacing(50 * ratio);
		infoRow.setStyle("-fx-background-color: rgba(100, 68, 31, 0.7);");
		infoRow.minWidthProperty().bind(infoRow.maxWidthProperty());
		infoRow.maxWidthProperty().bind(infoRow.prefWidthProperty());
		infoRow.setPrefWidth(1150 * ratio);
		infoRow.minHeightProperty().bind(infoRow.maxHeightProperty());
		infoRow.maxHeightProperty().bind(infoRow.prefHeightProperty());
		infoRow.setPrefHeight(330 * ratio);

		leftInfo = new VBox();
		leftInfo.setAlignment(Pos.CENTER);
		leftInfo.minWidthProperty().bind(leftInfo.maxWidthProperty());
		leftInfo.maxWidthProperty().bind(leftInfo.prefWidthProperty());
		leftInfo.setPrefWidth(500 * ratio);
		leftInfo.setSpacing(25 * ratio);
		
		rightInfo = new VBox();
		rightInfo.setAlignment(Pos.CENTER);
		rightInfo.minWidthProperty().bind(rightInfo.maxWidthProperty());
		rightInfo.maxWidthProperty().bind(rightInfo.prefWidthProperty());
		rightInfo.setPrefWidth(500 * ratio);
		rightInfo.setSpacing(25 * ratio);

		usernameRow = new HBox();
		usernameRow.setAlignment(Pos.CENTER);
		usernameLabel = new Label("Username:");
		usernameLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, 40 *ratio));
		usernameLabel.setTextFill(Color.WHITE);
		usernameTextField = new TextField();
		usernameRow.getChildren().addAll(usernameLabel, new Spacing(10), usernameTextField);

		passwordRow = new HBox();
		passwordRow.setAlignment(Pos.CENTER);
		passwordLabel = new Label("Password:");
		passwordLabel.setTextFill(Color.WHITE);
		passwordLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, 40 *ratio));
		passwordField = new PasswordField();
		passwordRow.getChildren().addAll(passwordLabel, new Spacing(10), passwordField);

		firstNameRow = new HBox();
		firstNameRow.setAlignment(Pos.CENTER);
		firstNameLabel = new Label("First Name:");
		firstNameLabel.setTextFill(Color.WHITE);
		firstNameLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, 40 *ratio));
		firstNameField = new TextField();	
		firstNameRow.getChildren().addAll(firstNameLabel, new Spacing(10), firstNameField);

		lastNameRow = new HBox();
		lastNameRow.setAlignment(Pos.CENTER);
		lastNameLabel = new Label("Last Name:");
		lastNameLabel.setTextFill(Color.WHITE);
		lastNameLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, 40 *ratio));
		lastNameField = new TextField();
		lastNameRow.getChildren().addAll(lastNameLabel, new Spacing(10), lastNameField);

		colorRow = new HBox();
		colorRow.setAlignment(Pos.CENTER);
		colorRow.setSpacing(30 * ratio);
		colorLabel = new Label("Color:");
		colorLabel.setTextFill(Color.WHITE);
		colorLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, 40 *ratio));
		colorLeft = new ArrowButton(30 * ratio);
		colorCurrent= new Rectangle(0, 0, 40 * ratio, 40 * ratio);
		colorRight = new ArrowButton(30 * ratio);
		colorRight.setRotate(180);
		colorRow.getChildren().addAll(colorLabel, new Spacing(10), colorLeft, colorCurrent, colorRight);

		avatarRow = new HBox();
		avatarRow.setAlignment(Pos.CENTER);
		avatarRow.setSpacing(25 * ratio);
		avatarLabel = new Label("Avatar:");
		avatarLabel.setTextFill(Color.WHITE);
		avatarLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, 40 *ratio));
		avatarLeft = new ArrowButton(30 * ratio);
		avatarCurrent = new ImageView();
		avatarCurrent.setFitWidth(50 * ratio);
		avatarCurrent.setFitHeight(50 * ratio);
		avatarCurrent.setPreserveRatio(true);
		avatarCurrent.setSmooth(true);
		avatarCurrent.setCache(true);
		avatarRight = new ArrowButton(30 * ratio);
		avatarRight.setRotate(180);
		avatarRow.getChildren().addAll(avatarLabel, new Spacing(10), avatarLeft, avatarCurrent, avatarRight);

		winsRow = new HBox();
		winsRow.setAlignment(Pos.CENTER);
		winsLabel = new Label("Wins:");
		winsLabel.setTextFill(Color.WHITE);
		winsLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, 40 *ratio));
		winsText = new Label();
		winsText.setFont(Font.font("Cooper Black", FontWeight.BOLD, 30 * ratio));
		winsText.setTextFill(Color.WHITE);
		winsText.textOverrunProperty().set(OverrunStyle.CLIP);
		winsRow.getChildren().addAll(winsLabel, new Spacing(10), winsText);
		
		losesRow = new HBox();
		losesRow.setAlignment(Pos.CENTER);
		losesLabel = new Label("Loses:");
		losesLabel.setTextFill(Color.WHITE);
		losesLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, 40 *ratio));
		losesText = new Label();
		losesText.setFont(Font.font("Cooper Black", FontWeight.BOLD, 30 * ratio));
		losesText.setTextFill(Color.WHITE);
		losesText.textOverrunProperty().set(OverrunStyle.CLIP);
		losesRow.getChildren().addAll(losesLabel, new Spacing(10), losesText);
		
		leftInfo.getChildren().addAll(usernameRow, passwordRow, firstNameRow, lastNameRow);
		rightInfo.getChildren().addAll(colorRow, avatarRow, winsRow, losesRow);
		infoRow.getChildren().addAll(leftInfo, rightInfo);

		buttonRow = new HBox();
		buttonRow.setAlignment(Pos.CENTER);
		buttonRow.setSpacing(30 * ratio);
		
		updateSaveButton = new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 35, 35 * ratio, 350 * ratio);
		updateSaveButton.setText("Edit Profile");

		deleteButton = new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 35, 35 * ratio, 350 * ratio);
		deleteButton.setText("Delete Profile");
		
		buttonRow.getChildren().addAll(updateSaveButton, deleteButton);
		mainContent.getChildren().addAll(infoRow, buttonRow);
		this.getChildren().add(mainContent);
	}
	
	public void setupPlayerInfo() throws FileNotFoundException {
		MainApp.getAppController();
		usernameTextField.setText(AppController.getProfile().getUserName());
		passwordField.setText(AppController.getProfile().getPassword());
		firstNameField.setText(AppController.getProfile().getFirstName());
		lastNameField.setText(AppController.getProfile().getLastName());
		winsText.setText(String.valueOf(AppController.getProfile().getWins()));
		losesText.setText(String.valueOf(AppController.getProfile().getLoses()));
		
		imageAvatar.setImage(new Image(new FileInputStream(
				Parameter.avatarsdir + AppController.getProfile().getPhoto())));
		circleAvatar.setFill(Color.web(AppController.getProfile().getColor()));
		
		avatarCurrent.setImage(new Image(new FileInputStream(
				Parameter.avatarsdir + AppController.getProfile().getPhoto())));
		colorCurrent.setFill(Color.web(AppController.getProfile().getColor()));
		
		TextField[] fields = new TextField[] {usernameTextField, passwordField, 
											firstNameField, lastNameField};
		for(TextField field : fields) {
			field.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 30 * ratio));
			field.setPadding(new Insets(5, 10, 5, 10));
			field.setAlignment(Pos.CENTER_RIGHT);
			field.setPrefWidth(200 * ratio);
			field.setStyle("-fx-background-color: transparent;" 
						+  "-fx-text-fill: white;");
			field.setEditable(false);
		}
	}
	
	public void buttonEvents() {
		backButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickBackwardSound();
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
		
		colorLeft.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	String hex = AppController.getProfile().getColor();
		    	for(int i = 0; i < colors.length; i++) {
		    		if(colorToHexCode(colors[i]).equals(hex)) {
		    			circleAvatar.setFill(colors[(i+5)%6]);
		    			colorCurrent.setFill(colors[(i+5)%6]);
						try {
							AppController.updateProfile(
									colorToHexCode(colors[(i+5)%6]), "Color");
						} catch (WrongTextFieldInputException e) {
							e.printStackTrace();
						}
		    		}
		    	}
		    }	
		});
		
		colorRight.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	String hex = AppController.getProfile().getColor();
		    	for(int i = 0; i < colors.length; i++) {
		    		if(colorToHexCode(colors[i]).equals(hex)) {
		    			circleAvatar.setFill(colors[(i+1)%6]);
		    			colorCurrent.setFill(colors[(i+1)%6]);
		    			try {
							AppController.updateProfile(
									colorToHexCode(colors[(i+1)%6]), "Color");
						} catch (WrongTextFieldInputException e) {
							e.printStackTrace();
						}
		    		}
		    	}
		    }	
		});
		
		avatarLeft.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	String avatar = AppController.getProfile().getPhoto();
		    	for(int i = 0; i < avatars.length; i++) {
		    		if(avatars[i].equals(Parameter.avatarsdir + avatar)) {
		    			try {
			    			avatarCurrent.setImage(new Image(new FileInputStream(avatars[(i+5)%6])));
							imageAvatar.setImage(new Image(new FileInputStream(avatars[(i+5)%6])));
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
		    			try {
							AppController.updateProfile(avatars[(i+5)%6].replace(
									Parameter.avatarsdir, ""), "Photo");
						} catch (WrongTextFieldInputException e) {
							e.printStackTrace();
						}
		    		}
		    	}
		    }	
		});
		
		avatarRight.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	String avatar = AppController.getProfile().getPhoto();
		    	for(int i = 0; i < avatars.length; i++) {
		    		if(avatars[i].equals(Parameter.avatarsdir + avatar)) {
		    			try {
			    			avatarCurrent.setImage(new Image(new FileInputStream(avatars[(i+1)%6])));
							imageAvatar.setImage(new Image(new FileInputStream(avatars[(i+1)%6])));
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
		    			try {
							AppController.updateProfile(avatars[(i+1)%6].replace(
									Parameter.avatarsdir, ""), "Photo");
						} catch (WrongTextFieldInputException e) {
							e.printStackTrace();
						}
		    		}
		    	}
		    }	
		});
		
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickBackwardSound();
				 // Display confirmation dialog
			    Alert alert = new Alert(AlertType.CONFIRMATION);
			    alert.setTitle("Confirm Delete");
			    alert.setHeaderText("Are you sure you want to delete your profile?");
			    alert.setContentText("This includes deleting all the statistics \nconnected with this profile. \nThis action cannot be undone.");
			    // Set the focus to the cancel button
			    Platform.runLater(() -> alert.getDialogPane().lookupButton(ButtonType.CANCEL).requestFocus());
			    
			    Optional<ButtonType> result = alert.showAndWait();
			    if (result.isPresent() && result.get() == ButtonType.OK) {
			        // User clicked OK, proceed with delete
			        MainApp.getAppController();
			        AppController.deleteProfile();
					Node node = (Node)event.getSource();
					Stage stage = (Stage)node.getScene().getWindow();

					try {
						UserAccessPaneController stp = new UserAccessPaneController();
						stage.getScene().setRoot(stp);

					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					stage.show();
			    } else {
			    }
	    	}
		});
		
		updateSaveButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
	    		TextField[] fields = new TextField[] {usernameTextField, 
	    				passwordField, firstNameField, lastNameField};

		    	if(updateSaveButton.getText().equals("Edit Profile")) {
		    		updateSaveButton.setText("Save Changes");
					for(TextField field : fields) {
						field.setAlignment(Pos.CENTER_LEFT);
						field.setStyle("-fx-background-color: rgba(100, 68, 31, 1), rgba(225, 211, 184, 0.9);" 
											+  "-fx-background-insets: -1 -1 -1 -1, 1 1 1 1;"
											+  "-fx-text-fill: #303030;");
						field.setEditable(true);
					}
		    	} else {
		    		updateSaveButton.setText("Edit Profile");
		    		for(TextField field : fields) {
		    			field.setAlignment(Pos.CENTER_RIGHT);
		    			field.setStyle("-fx-background-color: transparent;" 
									+  "-fx-text-fill: white;");
						field.setEditable(false);
					}
		    		
		    		try {
						MainApp.getAppController();
						if(!usernameTextField.getText().equals(AppController.getProfile().getUserName())) {
							System.out.println(AppController.getProfile().getUserName() + " " + usernameTextField.getText());
							AppController.updateProfile(usernameTextField.getText().trim(), "UserName");
						}
						if(!passwordField.getText().equals(AppController.getProfile().getPassword())) {
							AppController.updateProfile(passwordField.getText().trim(), "Password");
						}
						if(!firstNameField.getText().equals(AppController.getProfile().getFirstName())) {
							AppController.updateProfile(firstNameField.getText().trim(), "FirstName");
						}
						if(!lastNameField.getText().equals(AppController.getProfile().getLastName())) {
							AppController.updateProfile(lastNameField.getText().trim(), "LastName");
						}

					} catch (WrongTextFieldInputException e1) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setContentText(e1.getMessage());
						alert.setHeaderText("ERROR");
						alert.setTitle("");
						Stage tmp = (Stage)alert.getDialogPane().getScene().getWindow();
						tmp.getIcons().add(new Image(Parameter.errorIcon));
						alert.showAndWait();
					}	
		    	}
		    	
		    }
		});
	}
	
	public static String colorToHexCode( Color color ) {
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) ).toLowerCase();
    }
}


