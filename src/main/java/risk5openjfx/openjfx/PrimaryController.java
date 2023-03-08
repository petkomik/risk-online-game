package risk5openjfx.openjfx;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        Hallo.setRoot("secondary");
    }
}
