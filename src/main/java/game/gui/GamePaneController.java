package game.gui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import general.Parameter;
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
	
	private String[][] coord;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setUpCoords();
		int i = 0;
		for(Node n : gameBoard.getChildren()) {
			if(n instanceof SVGPath) {
				n.setScaleX(w*(0.2/1536));
				n.setScaleY(h*(-0.2/864));
				double x = Double.parseDouble(coord[i][0]);
				n.setLayoutX(((x+n.getLayoutX())/1536)*w-x);
				double y = Double.parseDouble(coord[i][1]);
				n.setLayoutY(((y+n.getLayoutY())/864)*h-y);
				i++;
			}
		}
	}
	
	private void setUpCoords() {
		coord = new String[48][2];
		try {
			FileReader fr = new FileReader(Parameter.coord);
			BufferedReader br = new BufferedReader(fr);
			String tmp = "";
			int i = 0;
			while((tmp = br.readLine()) != null) {
				coord[i] = tmp.split(",");
				i++;
			}
			fr.close();
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
