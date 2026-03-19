package com.interviews;

import java.io.IOException;
import javafx.fxml.FXML;

/**
 * This class serves as the primary controller for the JavaFX application. It contains methods that handle
 */
public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
