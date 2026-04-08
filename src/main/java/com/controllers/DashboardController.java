package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.interviews.App;

/**
 * Controller for the dashboard view.
 */
public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private void initialize() {
        if (App.currentUser != null) {
            welcomeLabel.setText("Welcome, " + App.currentUser.getFirstName() + "!");
        }
    }
}
