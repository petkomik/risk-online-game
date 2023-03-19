package risk5openjfx.openjfx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import general.Parameter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
/**
 * 
 * @author majda
 * this class handles the events on the very first frame
 */
public class StartPaneController implements Initializable{
	private Stage stage;
	private AnchorPane anchorPane;
	private double w = Parameter.screenWidth;
	private double h = Parameter.screenHeight;
	
	@FXML
	private ImageView imageView;
	@FXML
	private Button playButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		imageView.setFitWidth(w*0.391);
		imageView.setFitHeight(h*0.347);
		imageView.setLayoutX(w*0.305);
		imageView.setLayoutY(h*0.076);
		playButton.setPrefSize(w*0.13, h*0.058);
		playButton.setLayoutX(w*0.435);
		playButton.setLayoutY(h*0.471);
	}
	
	/**
	 * this method shows the create profile frame after the player has clicked the button 'play'
	 * @param e
	 * @throws IOException
	 */
	public void clickPlay(ActionEvent e) throws IOException {
		
		(new GameSound()).buttonClickForwardSound();
		
		Node node = (Node)e.getSource();
		stage = (Stage)node.getScene().getWindow();
		anchorPane = (AnchorPane) loadFXML("createProfile");
		stage.getScene().setRoot(anchorPane);
		stage.show();

	}
	/**
     * 
     * @param fxml, file name without the ending .fxml
     * @return Parent object, to be set as a root in a Secene object
     * @throws IOException
     * 
     * This method is responsible for loading a fxml file
     */
	private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartPaneController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
	}
    
}
