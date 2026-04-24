package com.controllers;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import com.interviews.Achievement;
import com.interviews.App;
import com.interviews.User;
import com.interviews.UserList;

public class LoginController {


    @FXML
    private Button loginButton;

    @FXML
    private PasswordField txt_password;

    @FXML
    private TextField txt_username;

    @FXML
    private Label errorLabel;

    @FXML
    void login(ActionEvent event) throws IOException {
        String username = txt_username.getText();
        String password = txt_password.getText();

        User user = UserList.getInstance().getUser(username, password);
        if (user != null) {
            App.currentUser = user;
            ArrayList<Achievement> achievements = user.getAchievements();
            if (achievements == null) {
                achievements = new ArrayList<>();
                user.setAchievements(achievements);
            }
            if (achievements.isEmpty()) {
                achievements.add(new Achievement(0, 1, 0, 0));
            }
            Achievement ach = achievements.get(0);
            ach.streakCounter();
            UserList.getInstance().save();
            App.setRoot("userpage");
        } else {
            errorLabel.setText("Invalid username or password. Please try again.");
        }
    }

    @FXML
    private void goToSignup() throws IOException {
        App.setRoot("signup");
    }

    @FXML
    private void continueWithoutLogin() throws IOException {
        App.setRoot("userpage");
    }

    @FXML
    private void forgotPassword() {

    }
}
