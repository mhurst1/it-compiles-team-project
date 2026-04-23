package com.controllers;

import com.interviews.Achievement;
import com.interviews.App;
import com.interviews.Question;
import com.interviews.User;
import com.interviews.Status;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.List;



import java.io.IOException;


public class UserPageController {

    @FXML private Button adminDashboardButton;
    @FXML private Label welcomeLabel;
    @FXML private Label welcomeGreeting;

    @FXML private Label avatarLabel;

    @FXML private Text profileName;
    @FXML private Text profileHandle;
    @FXML private Label profileRole;

    @FXML private Label questionsSolved;
    @FXML private Label currentStreak;
    @FXML private Label rankLabel;
    @FXML private Label upvotesReceived;
    @FXML private Label downvotesReceived;

    @FXML private VBox questionsSolvedBox;
    @FXML private VBox mySolutionsBox;
    @FXML private VBox recentActivityBox;

    @FXML
    private void initialize() {
         if (App.currentUser == null || App.currentUser.getStatus() != Status.ADMIN) {
            adminDashboardButton.setVisible(false);
            adminDashboardButton.setManaged(false);
        }

        User user = App.currentUser;
        if (user == null) {
            welcomeLabel.setText("Unknown User");
            setEmptyState();
            return;
        }

        if (App.currentUser != null) {
            String name = App.currentUser.getFirstName();
            welcomeLabel.setText(name);
            welcomeGreeting.setText("Welcome, " + name + "!");
        } else {
            welcomeLabel.setText("Unknown User");
            welcomeGreeting.setText("Welcome!");
        }

        String fullName = safe(user.getFirstName()) + " " + safe(user.getLastName());
        profileName.setText(fullName.trim());
        profileHandle.setText("@" + safe(user.getUsername()));
        profileRole.setText(user.getStatus() != null ? user.getStatus().name() : "USER");

        avatarLabel.setText(getInitial(user.getFirstName()));

        questionsSolved.setText(String.valueOf(sizeOf(user.getAnsweredQuestions())));

        Achievement achievement = getFirstAchievement(user);
        if (achievement != null) {
            currentStreak.setText(achievement.getStreak() + " days");
            rankLabel.setText("#" + achievement.getLeaderboardPlace());
            upvotesReceived.setText(String.valueOf(achievement.getAllVotePoints()));
        } else {
            currentStreak.setText("0 days");
            rankLabel.setText("#0");
            upvotesReceived.setText("0");
        }

        downvotesReceived.setText("0");

        populateSolvedQuestions(user.getAnsweredQuestions());
        populateSolutions(user);
        populateRecentActivity(user);
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
        if (App.currentUser != null && App.currentUser.getStatus() == Status.ADMIN) {
            App.setRoot("admindashboard");
        }
    }

    private void populateSolvedQuestions(List<Question> questions) {
        questionsSolvedBox.getChildren().clear();

        if (questions == null || questions.isEmpty()) {
            questionsSolvedBox.getChildren().add(new Label("No questions solved yet."));
            return;
        }

        for (Question q : questions) {
            String title = q != null && q.getTitle() != null ? q.getTitle() : "Untitled Question";
            Label item = new Label("• " + title);
            questionsSolvedBox.getChildren().add(item);
        }
    }

    private void populateSolutions(User user) {
        mySolutionsBox.getChildren().clear();

        // Your User class currently does not expose a getSolutions() method.
        // So for now, show a placeholder until that getter is added.
        mySolutionsBox.getChildren().add(new Label("No solutions loaded yet."));
    }

    private void populateRecentActivity(User user) {
        recentActivityBox.getChildren().clear();

        // Placeholder activity based on available data
        recentActivityBox.getChildren().add(new Label("Joined CodeBoard"));
        recentActivityBox.getChildren().add(new Label("Questions solved: " + sizeOf(user.getAnsweredQuestions())));
        recentActivityBox.getChildren().add(new Label("Starred questions: " + sizeOf(user.getStarredQuestions())));
    }

    private Achievement getFirstAchievement(User user) {
        if (user.getAchievements() == null || user.getAchievements().isEmpty()) {
            return null;
        }
        return user.getAchievements().get(0);
    }

    private void setEmptyState() {
        if (avatarLabel != null) avatarLabel.setText("U");
        if (profileName != null) profileName.setText("Unknown User");
        if (profileHandle != null) profileHandle.setText("@unknown");
        if (profileRole != null) profileRole.setText("USER");

        if (questionsSolved != null) questionsSolved.setText("0");
        if (currentStreak != null) currentStreak.setText("0 days");
        if (rankLabel != null) rankLabel.setText("#0");
        if (upvotesReceived != null) upvotesReceived.setText("0");
        if (downvotesReceived != null) downvotesReceived.setText("0");

        if (questionsSolvedBox != null) questionsSolvedBox.getChildren().clear();
        if (mySolutionsBox != null) mySolutionsBox.getChildren().clear();
        if (recentActivityBox != null) recentActivityBox.getChildren().clear();
    }

    private String getInitial(String firstName) {
        if (firstName == null || firstName.isBlank()) {
            return "U";
        }
        return firstName.substring(0, 1).toUpperCase();
    }

    private int sizeOf(List<?> list) {
        return list == null ? 0 : list.size();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}