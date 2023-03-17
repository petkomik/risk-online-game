package risk5openjfx.openjfx;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StartPaneController {
	private Stage stage;
	private AnchorPane anchorPane;
	
	public void clickPlay(ActionEvent e) throws IOException {
		
		(new GameSound()).buttonClickForwardSound();
		
		Node node = (Node)e.getSource();
		stage = (Stage)node.getScene().getWindow();
		anchorPane = (AnchorPane) loadFXML("createProfile");
		stage.getScene().setRoot(anchorPane);
		stage.show();

	}
	
	private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartPaneController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
	}
    
}
