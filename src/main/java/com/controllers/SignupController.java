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

public class SignupController {

    @FXML
    private Button loginButton;

    @FXML
    private TextField txt_firstName;

    @FXML
    private TextField txt_lastName;

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_uscId;

    @FXML
    private TextField txt_gradYear;

    @FXML
    private TextField txt_password;

    @FXML
    private TextField txt_confirmPassword;

    @FXML
    private TextField txt_username;

    @FXML
    private Label errorLabel;

    @FXML
    void goToLogin(ActionEvent event) throws IOException {
        App.setRoot("login");
    }

    @FXML
    void signup(ActionEvent event) throws IOException {
        String username = txt_username.getText();
        String password = txt_password.getText();

        User user = UserList.getInstance().getUser(username, password);
        if (user != null) {
            App.currentUser = user;
            App.setRoot("dashboard");
        } else {
            errorLabel.setText("Please fill out all required feilds.");
        }
    }
}
