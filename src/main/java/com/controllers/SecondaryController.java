package com.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import com.interviews.App;

/**
 * Controller for the secondary JavaFX view.
 */
public class SecondaryController {

    /**
     * Switches the view back to the primary screen.
     *
     * @throws IOException if there is an error loading the primary view
     */
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}
