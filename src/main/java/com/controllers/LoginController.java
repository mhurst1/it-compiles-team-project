package com.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import com.interviews.App;
import com.interviews.User;
import com.interviews.UserList;

/**
 * Controller for the secondary JavaFX view.
 */
public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private TextField txt_password;

    @FXML
    private TextField txt_username;

    @FXML
    private Label errorLabel;

    @FXML
    void goToHome(ActionEvent event) throws IOException {
        App.setRoot("home");
    }

    @FXML
    void login(ActionEvent event) throws IOException {
        String username = txt_username.getText();
        String password = txt_password.getText();

        User user = UserList.getInstance().getUser(username, password);
        if (user != null) {
            App.currentUser = user;
            App.setRoot("dashboard");
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }
}
