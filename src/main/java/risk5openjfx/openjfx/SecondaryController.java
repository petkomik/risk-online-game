package risk5openjfx.openjfx;

import java.io.IOException;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        Hallo.setRoot("primary");
    }
}