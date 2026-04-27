package com.controllers;

import com.interviews.App;
import com.interviews.DataWriter;
import com.interviews.QuestionList;
import com.interviews.User;
import com.interviews.UserList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import java.io.IOException;

public class ProfileController {

    @FXML private Button adminDashboardButton;
    @FXML private Button contributorApplicationButton;

    @FXML private Label navAvatarLetter;

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label avatarLabel;
    @FXML
    private Text profileName;
    @FXML
    private Text profileHandle;
    @FXML
    private Label profileRole;

    @FXML
    private Label profileAvatarLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label gradYearLabel;
    @FXML
    private Label memberSinceLabel;
    @FXML
    private Label sectionSubtitle;

    @FXML
    private TextField nameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField gradYearField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button editInfoButton;
    @FXML
    private Button backToLeaderboardButton;

    private boolean editing = false;
    private boolean readOnlyProfile = false;
    private User profileUser;

   @FXML
    private void initialize() {
        App.configureAdminDashboardButton(adminDashboardButton);
        App.configureContributorApplicationButton(contributorApplicationButton);

        User navUser = App.currentUser;
        User user = App.getProfileUser();
        profileUser = user;
        readOnlyProfile = !App.isViewingCurrentUserProfile();

        if (user == null) {
            welcomeLabel.setText("Unknown User");
            setEmptyState();
            return;
        }

        if (navUser != null && navUser.getFirstName() != null && !navUser.getFirstName().isEmpty()) {
            String firstLetter = navUser.getFirstName().substring(0, 1).toUpperCase();
            navAvatarLetter.setText(firstLetter);
        } else {
            navAvatarLetter.setText("U");
        }

        String name = "Unknown User";
         if (navUser != null) {
            name = navUser.getFirstName();
            welcomeLabel.setText(name);
        } else {
            welcomeLabel.setText("Unknown User");
        }

        welcomeLabel.setText(name);

        updateDisplay(user);
        setEditMode(false);
        configureProfileAccess();

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
    private void goBackToLeaderboard() throws IOException {
        App.setRoot("leaderboard");
    }

    @FXML
    private void goToProfile() throws IOException {
        App.viewCurrentUserProfile();
    }

    @FXML
    private void goToAdminDashboard() throws IOException {
        App.goToAdminDashboardIfAllowed();
    }

    @FXML
    private void goToContributorApplication() throws IOException {
        App.goToContributorApplicationIfAllowed();
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        UserList.getInstance().save();
        QuestionList.getInstance().saveAll();
        App.currentUser = null;
        App.setRoot("login");
    }

    @FXML
    private void editInformation() {
        if (readOnlyProfile) {
            showError("You can only edit your own profile.");
            return;
        }

        User user = profileUser;
        if (user == null) {
            showError("No user is currently logged in.");
            return;
        }

        if (!editing) {
            populateFields(user);
            setEditMode(true);
            return;
        }

        saveEdits();
    }



    private void saveEdits() {
        User user = profileUser;

        String fullName = safe(nameField.getText()).trim();
        String username = safe(usernameField.getText()).trim();
        String email = safe(emailField.getText()).trim();
        String password = safe(passwordField.getText()).trim();
        String gradYearText = safe(gradYearField.getText()).trim();


        if (fullName.isBlank() || username.isBlank() || email.isBlank() || gradYearText.isBlank()
                || password.isBlank()) {
            showError("Please fill out all editable fields.");
            return;
        }

        int graduationYear;
        try {
            graduationYear = Integer.parseInt(gradYearText);
        } catch (NumberFormatException e) {
            showError("Graduation year must be a number.");
            return;
        }

        if (graduationYear < 2000 || graduationYear > 2099) {
            showError("Graduation year must be between 2000 and 2099.");
            return;
        }

        if (isDuplicateUsername(username, user)) {
            showError("That username is already taken.");
            return;
        }

        String[] nameParts = splitFullName(fullName);
        String firstName = nameParts[0];
        String lastName = nameParts[1];

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setGraduationYear(graduationYear);

        boolean saved = DataWriter.saveUsers(UserList.getInstance().getUsers());
        if (!saved) {
            showError("Profile changes were made in memory, but could not be saved to JSON.");
            return;
        }

        updateDisplay(user);
        setEditMode(false);
    }

    private void populateFields(User user) {
        String fullName = (safe(user.getFirstName()) + " " + safe(user.getLastName())).trim();

        nameField.setText(fullName);
        usernameField.setText(safe(user.getUsername()));
        emailField.setText(safe(user.getEmail()));
        gradYearField.setText(String.valueOf(user.getGraduationYear()));
    }

    private void updateDisplay(User user) {
        String firstName = safe(user.getFirstName());
        String lastName = safe(user.getLastName());
        String fullName = (firstName + " " + lastName).trim();

        avatarLabel.setText(getInitial(firstName));

        profileName.setText(fullName.isBlank() ? "Unknown User" : fullName);
        profileHandle.setText("@" + safe(user.getUsername()));
        profileRole.setText(user.getStatus() != null ? user.getStatus().name() : "USER");

        profileAvatarLabel.setText(getInitial(firstName));
        nameLabel.setText(fullName.isBlank() ? "Unknown User" : fullName);
        usernameLabel.setText(safe(user.getUsername()));
        emailLabel.setText(safe(user.getEmail()));
        passwordField.setText(safe(user.getPassword()));
        gradYearLabel.setText(String.valueOf(user.getGraduationYear()));
        memberSinceLabel.setText("April 2026");
    }

    private void setEditMode(boolean isEditing) {
        editing = isEditing;

        nameLabel.setVisible(!isEditing);
        nameLabel.setManaged(!isEditing);
        usernameLabel.setVisible(!isEditing);
        usernameLabel.setManaged(!isEditing);
        emailLabel.setVisible(!isEditing);
        emailLabel.setManaged(!isEditing);
        gradYearLabel.setVisible(!isEditing);
        gradYearLabel.setManaged(!isEditing);

        nameField.setVisible(isEditing);
        nameField.setManaged(isEditing);
        usernameField.setVisible(isEditing);
        usernameField.setManaged(isEditing);
        emailField.setVisible(isEditing);
        emailField.setManaged(isEditing);
        passwordField.setVisible(isEditing);
        passwordField.setManaged(isEditing);
        gradYearField.setVisible(isEditing);
        gradYearField.setManaged(isEditing);

        if (editInfoButton != null) {
            editInfoButton.setText(isEditing ? "Save" : "Edit Information");
        }
    }

    private void configureProfileAccess() {
        if (sectionSubtitle != null) {
            sectionSubtitle.setText(readOnlyProfile
                    ? "Viewing public account details"
                    : "View and edit account details");
        }
        if (editInfoButton != null) {
            editInfoButton.setVisible(!readOnlyProfile);
            editInfoButton.setManaged(!readOnlyProfile);
            editInfoButton.setDisable(readOnlyProfile);
        }
        if (backToLeaderboardButton != null) {
            backToLeaderboardButton.setVisible(readOnlyProfile);
            backToLeaderboardButton.setManaged(readOnlyProfile);
        }
        if (readOnlyProfile) {
            setEditMode(false);
        }
    }

    private boolean isDuplicateUsername(String username, User currentUser) {
        for (User u : UserList.getInstance().getUsers()) {
            if (u != currentUser && u.getUsername() != null && u.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private String[] splitFullName(String fullName) {
        String cleaned = fullName.trim();
        if (cleaned.isBlank()) {
            return new String[] { "unknown", "unknown" };
        }

        String[] parts = cleaned.split("\\s+");
        if (parts.length == 1) {
            return new String[] { parts[0], "" };
        }

        String firstName = parts[0];
        StringBuilder lastName = new StringBuilder();
        for (int i = 1; i < parts.length; i++) {
            if (i > 1) {
                lastName.append(" ");
            }
            lastName.append(parts[i]);
        }
        return new String[] { firstName, lastName.toString() };
    }

    private void setEmptyState() {
        if (avatarLabel != null)
            avatarLabel.setText("U");
        if (profileName != null)
            profileName.setText("Unknown User");
        if (profileHandle != null)
            profileHandle.setText("@unknown");
        if (profileRole != null)
            profileRole.setText("USER");

        if (profileAvatarLabel != null)
            profileAvatarLabel.setText("U");
        if (nameLabel != null)
            nameLabel.setText("Unknown User");
        if (usernameLabel != null)
            usernameLabel.setText("unknown");
        if (emailLabel != null)
            emailLabel.setText("unknown@example.com");
        if (gradYearLabel != null)
            gradYearLabel.setText("");
        if (memberSinceLabel != null)
            memberSinceLabel.setText("April 2026");
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

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Profile Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
}
