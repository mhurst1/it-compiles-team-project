package com.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import com.interviews.App;

/**
 * Controller for the primary JavaFX view.
 */
public class PrimaryController {

    /**
     * Switches from the primary view to the secondary view.
     *
     * @throws IOException if the secondary FXML view cannot be loaded
     */
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
