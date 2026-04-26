package com.controllers;

import java.io.IOException;

import com.interviews.App;
import com.interviews.User;
import com.interviews.UserList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ContributorApplicationController {

    @FXML private Button adminDashboardButton;
    @FXML private Button contributorApplicationButton;
    @FXML private Label navAvatarLetter;
    @FXML private Label welcomeLabel;

    @FXML private Label applicantNameLabel;
    @FXML private Label applicantRoleLabel;
    @FXML private TextField experienceField;
    @FXML private TextArea motivationField;
    @FXML private Label feedbackLabel;
    @FXML private Button submitButton;
    @FXML private VBox formCard;

    @FXML
    private void initialize() {
        if (App.currentUser == null || !App.isCurrentUserStandardUser()) {
            Platform.runLater(() -> {
                try {
                    App.setRoot(App.currentUser == null ? "login" : "userpage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }

        App.configureAdminDashboardButton(adminDashboardButton);
        App.configureContributorApplicationButton(contributorApplicationButton);
        populateUserState();
        populateApplicationState();
    }

    private void populateUserState() {
        User user = App.currentUser;
        String firstName = user.getFirstName() != null ? user.getFirstName() : "User";
        String lastName = user.getLastName() != null ? user.getLastName() : "";
        String username = user.getUsername() != null ? user.getUsername() : "unknown";

        welcomeLabel.setText(firstName);
        navAvatarLetter.setText(firstName.isBlank() ? "U" : firstName.substring(0, 1).toUpperCase());
        applicantNameLabel.setText((firstName + " " + lastName).trim());
        applicantRoleLabel.setText("Current role: " + user.getStatus().name() + "  |  @" + username);
    }

    private void populateApplicationState() {
        User user = App.currentUser;
        if (user == null || !user.hasContributorApplicationPending()) {
            return;
        }

        experienceField.setText(user.getContributorApplicationExperience());
        motivationField.setText(user.getContributorApplicationMotivation());
        setFormDisabled(true);
        showFeedback("Application submitted. An admin must approve it before you become a contributor.", true);
    }

    @FXML
    private void submitApplication() {
        if (App.currentUser == null || !App.isCurrentUserStandardUser()) {
            showFeedback("Only standard users can apply for contributor access.", false);
            return;
        }

        if (experienceField.getText().trim().isEmpty() || motivationField.getText().trim().isEmpty()) {
            showFeedback("Please fill out both fields before submitting.", false);
            return;
        }

        App.currentUser.submitContributorApplication(
                experienceField.getText(),
                motivationField.getText()
        );
        UserList.getInstance().save();

        showFeedback("Application submitted. An admin must approve it before you become a contributor.", true);
        setFormDisabled(true);
        App.configureContributorApplicationButton(contributorApplicationButton);
    }

    private void setFormDisabled(boolean disabled) {
        experienceField.setDisable(disabled);
        motivationField.setDisable(disabled);
        submitButton.setDisable(disabled);
    }

    private void showFeedback(String message, boolean success) {
        feedbackLabel.setText(message);
        feedbackLabel.setStyle(success ? "-fx-text-fill: #16a34a;" : "-fx-text-fill: #dc2626;");
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
        App.setRoot(App.currentUser != null ? "profile" : "login");
    }

    @FXML
    private void goToAdminDashboard() throws IOException {
        App.goToAdminDashboardIfAllowed();
    }

    @FXML
    private void goToContributorApplication() throws IOException {
        App.goToContributorApplicationIfAllowed();
    }
}
