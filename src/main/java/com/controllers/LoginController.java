package com.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
//import java.model.*;

import com.interviews.App;

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
    void goToHome(ActionEvent event) throws IOException {
        App.setRoot("home");
    }

    @FXML
    void login(ActionEvent event) {

    }
}
