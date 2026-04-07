package com.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import com.interviews.App;

/**
 * Controller for the primary JavaFX view.
 */
public class HomeController {

    /**
     * Switches from the primary view to the secondary view.
     *
     * @throws IOException if the secondary FXML view cannot be loaded
     */
    @FXML
    private void goToLogin() throws IOException {
        App.setRoot("login");
    }
}
