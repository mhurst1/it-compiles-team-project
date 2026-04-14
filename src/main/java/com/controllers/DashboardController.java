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
    private Label welcomeGreeting;

    @FXML
    private void initialize() {
        if (App.currentUser != null) {
            String name = App.currentUser.getFirstName();
            welcomeLabel.setText("Welcome, " + name + "!");
            welcomeGreeting.setText("Welcome, " + name + "!");
        } else {
            welcomeGreeting.setText("Welcome!");
        }
    }
}
