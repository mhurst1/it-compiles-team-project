package com.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import com.interviews.App;

/**
 * Controller for the secondary JavaFX view.
 */
public class LoginController {

    /**
     * Switches the view back to the primary screen.
     *
     * @throws IOException if there is an error loading the primary view
     */
    @FXML
    private void goToHome() throws IOException {
        App.setRoot("home");
    }
}
