module risk5openjfx.openjfx {
	requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens risk5openjfx.openjfx to javafx.fxml;
    exports risk5openjfx.openjfx;
}
