package game.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import game.Lobby;
import game.gui.GUISupportClasses.DesignButton;
import game.gui.GUISupportClasses.ImageViewPane;
import game.gui.GUISupportClasses.MenuButton;
import game.gui.GUISupportClasses.Spacing;
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
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * 
 * @author majda
 * this class handles the events on the very first frame
 */
public class StartPaneController extends StackPane {
	private Stage stage;	
	private VBox vBox;
	private ImageView imgBackground;
	private ImageViewPane imgBackgroundPane;
	private VBox vBoxColor;
	private VBox contentVBox;
	private ImageView riskLogo;
	private DesignButton playButton;
	
	
	public StartPaneController() throws FileNotFoundException {
		super();
		setup();
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
		vBoxColor.setStyle("-fx-background-color: rgba(225, 211, 184, 0.7);");
		
		
		contentVBox = new VBox();
		contentVBox.setAlignment(Pos.CENTER);
		
		riskLogo = new ImageView();
		riskLogo.setImage(new Image(new FileInputStream(Parameter.logoImage)));
		riskLogo.setFitWidth(650);
		riskLogo.setPreserveRatio(true);
		riskLogo.setSmooth(true);
		riskLogo.setCache(true);
		
		playButton = new DesignButton(new Insets(10, 20, 10, 20), 30, 40, 300);
		playButton.setText("Play");

		contentVBox.setSpacing(30);
		
		contentVBox.getChildren().addAll(riskLogo, playButton);
		contentVBox.setPadding(new Insets(0, 0, 50, 0));

		
		
		// maybe add vBoxColor
		this.getChildren().addAll(vBox, vBoxColor, contentVBox);
	
		playButton.setOnAction(new EventHandler<ActionEvent>() {
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
				
//				
//				Lobby lo = new Lobby();
//				lo.addAI();
//				lo.addAI();
//				lo.addAI();
//				lo.addAI();
//	
//				LobbyMenuController lobby;
//				try {
//					lobby = new LobbyMenuController(lo);
//					stage.getScene().setRoot(lobby);
//
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
	
				stage.show();
	    	}
		});
	}	
}
