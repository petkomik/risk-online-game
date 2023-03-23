package game.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MultplayerHostJoinController implements Initializable {
	private Stage stage;
	private AnchorPane anchorPane;
	private double w = MainApp.screenWidth;
	private double h = MainApp.screenHeight;
	
	@FXML
	private Button hostGameButton;
	@FXML
	private Button joinGameButton;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		double btnW = w * 0.163;
		double btnH = h * 0.058;
		double btnX = w * 0.419;
		hostGameButton.setPrefSize(btnW, btnH);
		joinGameButton.setPrefSize(btnW, btnH);
		
		hostGameButton.setLayoutX(btnX);
		joinGameButton.setLayoutX(btnX);

		hostGameButton.setLayoutY(h*0.301);
		joinGameButton.setLayoutY(h*0.382);	
		
		double fontSize = 0.078 * Math.sqrt(Math.pow(btnW, 2.0)+Math.pow(btnH, 2.0));
		hostGameButton.setStyle("-fx-font-size: "+fontSize+"px;");
		joinGameButton.setStyle("-fx-font-size: "+fontSize+"px;");
		
	}

}
