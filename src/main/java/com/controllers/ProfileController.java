package com.controllers;

import com.interviews.App;
import com.interviews.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.io.IOException;

public class ProfileController {

    @FXML private Label navUserLabel;
    @FXML private Label avatarLabel;
    @FXML private Text profileName;
    @FXML private Text profileHandle;
    @FXML private Label profileRole;

    @FXML private Label profileAvatarLabel;
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label gradYearLabel;
    @FXML private Label memberSinceLabel;

    @FXML
    private void initialize() {
        User user = App.currentUser;

        if (user == null) {
            setEmptyState();
            return;
        }

        String firstName = safe(user.getFirstName());
        String lastName = safe(user.getLastName());
        String fullName = (firstName + " " + lastName).trim();

        navUserLabel.setText(safe(user.getUsername()));
        avatarLabel.setText(getInitial(firstName));

        profileName.setText(fullName.isBlank() ? "Unknown User" : fullName);
        profileHandle.setText("@" + safe(user.getUsername()));
        profileRole.setText(user.getStatus() != null ? user.getStatus().name() : "USER");

        profileAvatarLabel.setText(getInitial(firstName));
        nameLabel.setText(fullName.isBlank() ? "Unknown User" : fullName);
        usernameLabel.setText(safe(user.getUsername()));
        emailLabel.setText(safe(user.getEmail()));
        gradYearLabel.setText(String.valueOf(user.getGraduationYear()));
        memberSinceLabel.setText("April 2026");
    }

    @FXML
    private void goToHome() throws IOException {
        App.setRoot("userpage");
    }

    @FXML
    private void goToQuestions() throws IOException {
        App.setRoot("dashboard");
    }

    @FXML
    private void goToCommunity() throws IOException {
        App.setRoot("leaderboard");
    }

    @FXML
    private void goToProfile() throws IOException {
        App.setRoot("profile");
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        App.currentUser = null;
        App.setRoot("login");
    }

    @FXML
    private void editInformation() {
    }

    @FXML
    private void changePassword() {
    }

    @FXML
    private void uploadPhoto() {
    }

    private void setEmptyState() {
        if (navUserLabel != null) navUserLabel.setText("User");
        if (avatarLabel != null) avatarLabel.setText("U");
        if (profileName != null) profileName.setText("Unknown User");
        if (profileHandle != null) profileHandle.setText("@unknown");
        if (profileRole != null) profileRole.setText("USER");

        if (profileAvatarLabel != null) profileAvatarLabel.setText("U");
        if (nameLabel != null) nameLabel.setText("Unknown User");
        if (usernameLabel != null) usernameLabel.setText("unknown");
        if (emailLabel != null) emailLabel.setText("unknown@example.com");
        if (gradYearLabel != null) gradYearLabel.setText("");
        if (memberSinceLabel != null) memberSinceLabel.setText("April 2026");
    }

    private String getInitial(String value) {
        if (value == null || value.isBlank()) {
            return "U";
        }
        return value.substring(0, 1).toUpperCase();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}