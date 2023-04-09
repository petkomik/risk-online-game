package game.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;


public class GamePaneController implements Initializable{
	
	private double w = MainApp.screenWidth;
	private double h = MainApp.screenHeight;
	
	
	
	@FXML
	private AnchorPane gameBoard;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		for(Node n : gameBoard.getChildren()) {
			if(n instanceof SVGPath) {
				n.setScaleX(w*(0.2/1536));
				n.setScaleY(h*(-0.2/864));
				n.setLayoutX(w*(n.getLayoutX()/1536));
				n.setLayoutY(h*(n.getLayoutY()/864));
			}
		}
	}


}
