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

/**
 * @author majda
 * This class handles the events on the game board 
 */
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
				n.setScaleX(0.2);
				n.setScaleY(-0.2);
				// parsing the x coord into double value
				double x = Double.parseDouble(coord[i][0]);
				// setting the x coord during the run time and relative to the screen
				n.setLayoutX(((x+n.getLayoutX())/1536)*w-x);
				// parsing the y coord into double value
				double y = Double.parseDouble(coord[i][1]);
				// setting the y coord during the run time and relative to the screen
				n.setLayoutY(((y+n.getLayoutY())/864)*h-y);
				i++;
			}
		}
	}
	/*
	 * coordinates are read from the file coord.txt and saved in the 
	 * variable coord
	 */
	private void setUpCoords() {
		coord = new String[48][2];
		try {
			FileReader fr = new FileReader(Parameter.coord);
			BufferedReader br = new BufferedReader(fr);
			String tmp = "";
			int i = 0;
			while((tmp = br.readLine()) != null) {
				coord[i] = tmp.split(","); // splitting the coordinates
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
