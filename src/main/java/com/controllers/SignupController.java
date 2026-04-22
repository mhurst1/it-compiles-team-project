package com.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

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
    private PasswordField txt_password;

    @FXML
    private PasswordField txt_confirmPassword;

    @FXML
    private TextField txt_username;

    @FXML
    private Label errorLabel;

    @FXML
    void initialize() {
        txt_gradYear.setTextFormatter(new TextFormatter<>(change ->
            change.getControlNewText().matches("\\d{0,4}") ? change : null
        ));
        txt_uscId.setTextFormatter(new TextFormatter<>(change ->
            change.getControlNewText().matches("[A-Za-z]?|[A-Za-z]\\d{0,8}") ? change : null
        ));
    }

    @FXML
    void goToLogin(ActionEvent event) throws IOException {
        App.setRoot("login");
    }

    @FXML
    void signup(ActionEvent event) throws IOException {
        String firstName = txt_firstName.getText().trim();
        String lastName = txt_lastName.getText().trim();
        String username = txt_username.getText().trim();
        String email = txt_email.getText().trim();
        String uscId = txt_uscId.getText().trim();
        String gradYearStr = txt_gradYear.getText().trim();
        String password = txt_password.getText();
        String confirmPassword = txt_confirmPassword.getText();

        if (firstName.isBlank() || lastName.isBlank() || username.isBlank() ||
                email.isBlank() || uscId.isBlank() || gradYearStr.isBlank() || password.isBlank()) {
            errorLabel.setText("Please fill out all required fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }

        if (!password.matches("^(?=.*[A-Z])(?=.*[$!%]).{8,}$")) {
            errorLabel.setText("Password must be at least 8 characters with 1 uppercase and 1 special character ($!%).");
            return;
        }

        if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
            errorLabel.setText("Email must contain a valid @ symbol.");
            return;
        }

        if (gradYearStr.length() != 4) {
            errorLabel.setText("Graduation year must be exactly 4 digits.");
            return;
        }
        int gradYear;
        try {
            gradYear = Integer.parseInt(gradYearStr);
        } catch (NumberFormatException e) {
            errorLabel.setText("Graduation year must be a valid number.");
            return;
        }

        if (!uscId.matches("^[A-Za-z]\\d{8}$")) {
            errorLabel.setText("USC ID must start with a letter followed by 8 digits (e.g. M12345678).");
            return;
        }

        UserList userList = UserList.getInstance();
        if (userList.getUser(username) != null) {
            errorLabel.setText("Username already exists. Please choose a different one.");
            return;
        }
        User newUser = new User(firstName, lastName, username, password, email, gradYear, uscId);
        userList.addUser(newUser);
        userList.save();
        App.currentUser = newUser;
        App.setRoot("userpage");
    }
}
