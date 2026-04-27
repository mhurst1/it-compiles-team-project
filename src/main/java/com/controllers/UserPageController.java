package com.controllers;

import com.interviews.Achievement;
import com.interviews.App;
import com.interviews.Difficulty;
import com.interviews.Language;
import com.interviews.Question;
import com.interviews.QuestionList;
import com.interviews.User;
import com.interviews.UserList;
import com.interviews.UserSolution;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;


public class UserPageController {

    @FXML private Button adminDashboardButton;
    @FXML private Button contributorApplicationButton;
    @FXML private Label navAvatarLetter;
    @FXML private Label welcomeLabel;
    @FXML private Label welcomeGreeting;

    @FXML private Label avatarLabel;

    @FXML private Text profileName;
    @FXML private Text profileHandle;
    @FXML private Label profileRole;

    @FXML private Label questionsSolved;
    @FXML private Label currentStreak;
    @FXML private Label rankLabel;
    @FXML private Label votesReceived;
    @FXML private Label downvotesReceived;

    @FXML private VBox questionsSolvedBox;
    @FXML private VBox questionsStarredBox;
    @FXML private VBox recentActivityBox;
    @FXML private VBox myPostedQuestionsPanel;
    @FXML private VBox myPostedQuestionsBox;

    @FXML
    private void initialize() {
        App.configureAdminDashboardButton(adminDashboardButton);
        App.configureContributorApplicationButton(contributorApplicationButton);

        if (App.currentUser != null && App.currentUser.getFirstName() != null && !App.currentUser.getFirstName().isEmpty()) {
            String firstLetter = App.currentUser.getFirstName().substring(0, 1).toUpperCase();
            navAvatarLetter.setText(firstLetter);
        } else {
            navAvatarLetter.setText("U");
        }

        User user = App.getProfileUser();
        if (user == null) {
            welcomeLabel.setText("Unknown User");
            setEmptyState();
            return;
        }

        if (App.currentUser != null) {
            welcomeLabel.setText(App.currentUser.getFirstName());
        } else {
            welcomeLabel.setText("Unknown User");
        }

        boolean viewingOwn = App.currentUser != null
                && App.currentUser.getId() != null
                && App.currentUser.getId().equals(user.getId());
        if (viewingOwn) {
            welcomeGreeting.setText("Welcome, " + user.getFirstName() + "!");
        } else {
            welcomeGreeting.setText(user.getFirstName() + "'s Profile");
        }

        String fullName = safe(user.getFirstName()) + " " + safe(user.getLastName());
        profileName.setText(fullName.trim());
        profileHandle.setText("@" + safe(user.getUsername()));
        profileRole.setText(user.getStatus() != null ? user.getStatus().name() : "USER");

        avatarLabel.setText(getInitial(user.getFirstName()));

        questionsSolved.setText(String.valueOf(sizeOf(user.getAnsweredQuestions())));

        currentStreak.setText(getUserStreak(user) + " days");
        rankLabel.setText("#" + computeUserRank(user));
        votesReceived.setText(String.valueOf(computeUserVotePoints(user)));


        populateSolvedQuestions(user.getAnsweredQuestions());
        populateStarredQuestions(user.getStarredQuestions());
        populateRecentActivity(user);

        if (user.canModifyQuestions()) {
            myPostedQuestionsPanel.setVisible(true);
            myPostedQuestionsPanel.setManaged(true);
            populateMyPostedQuestions(user);
        }
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
    private void goToProfile() {
        try {
            if (App.currentUser == null) {
                App.setRoot("login");
            } else {
                App.setRoot("profile");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAdminDashboard() throws IOException {
        App.goToAdminDashboardIfAllowed();
    }

    @FXML
    private void goToContributorApplication() throws IOException {
        App.goToContributorApplicationIfAllowed();
    }

    private void populateSolvedQuestions(List<Question> questions) {
        questionsSolvedBox.getChildren().clear();

        if (questions == null || questions.isEmpty()) {
            questionsSolvedBox.getChildren().add(new Label("No questions solved yet.") {
                {
                    setStyle("-fx-text-fill: #888888;");
                }
            });
            return;
        }

        QuestionCardController cardBuilder = new QuestionCardController(); 

        int index = 1;
        for (Question q : questions) {
            HBox card = cardBuilder.buildCard(q, index++); 
            questionsSolvedBox.getChildren().add(card);
        }
    }

     private void populateStarredQuestions(List<Question> questions) {
        questionsStarredBox.getChildren().clear();

        if (questions == null || questions.isEmpty()) {
            questionsStarredBox.getChildren().add(new Label("No starred questions.") {
                {
                    setStyle("-fx-text-fill: #888888;");
                }
            });
            return;
        }

        QuestionCardController cardBuilder = new QuestionCardController();

        int index = 1;
        for (Question q : questions) {
            HBox card = cardBuilder.buildCard(q, index++);
            questionsStarredBox.getChildren().add(card);
        }
    }

   private void populateRecentActivity(User user) {
        recentActivityBox.getChildren().clear();

        if (user == null) {
            Label msg = new Label("Login to view recent activity");
            msg.getStyleClass().add("activity-item");
            recentActivityBox.getChildren().add(msg);
            return;
        }

        ArrayList<String> activities = new ArrayList<>();
        ArrayList<Question> answered = user.getAnsweredQuestions();

        ArrayList<Question> userQuestions = new ArrayList<>();
        for (Question q : QuestionList.getInstance().getQuestions()) {
            if (q.getUser() != null &&
                q.getUser().getId().equals(user.getId())) {
                userQuestions.add(q);
            }
        }

        int i = (answered == null ? -1 : answered.size() - 1);
        int j = userQuestions.size() - 1;

        while (activities.size() < 5 && (i >= 0 || j >= 0)) {

            if (i >= 0) {
                activities.add("Posted a solution on " + answered.get(i).getTitle());
                i--;
            }

            if (activities.size() >= 5) break;

            if (j >= 0) {
                activities.add("Posted a question: " + userQuestions.get(j).getTitle());
                j--;
            }
        }

        if (activities.isEmpty()) {
            Label msg = new Label("No recent activity");
            msg.getStyleClass().add("activity-item");
            recentActivityBox.getChildren().add(msg);
            return;
        }

        for (String text : activities) {
            Label item = new Label(text);
            item.getStyleClass().add("activity-item");
            recentActivityBox.getChildren().add(item);
        }
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
        if (votesReceived != null) votesReceived.setText("0");
        if (downvotesReceived != null) downvotesReceived.setText("0");

        if (questionsSolvedBox != null) questionsSolvedBox.getChildren().clear();
        if (questionsStarredBox != null) questionsStarredBox.getChildren().clear();
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


    private int computeUserVotePoints(User user) {
        int total = 0;

        for (Question q : QuestionList.getInstance().getQuestions()) {
            if (q.getSolutionList() == null) continue;

            for (UserSolution sol : q.getSolutionList()) {
                if (sol.getUser() != null &&
                    sol.getUser().getId().equals(user.getId())) {

                    total += sol.getTotalVote();
                }
            }
        }

        return total;
    }

    private int computeUserRank(User currentUser) {
        ArrayList<User> users = new ArrayList<>(UserList.getInstance().getUsers());

        users.sort((a, b) -> {
            int cmp = b.getAnsweredQuestions().size() - a.getAnsweredQuestions().size();
            if (cmp != 0) return cmp;

            cmp = computeUserVotePoints(b) - computeUserVotePoints(a);
            if (cmp != 0) return cmp;

            return getUserStreak(b) - getUserStreak(a);
        });

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(currentUser.getId())) {
                return i + 1;
            }
        }

        return users.size();
    }

    private int getUserStreak(User user) {
        if (user.getAchievements() == null || user.getAchievements().isEmpty()) {
            return 0;
        }
        return user.getAchievements().get(user.getAchievements().size() - 1).getStreak();
    }

    private void populateMyPostedQuestions(User user) {
        myPostedQuestionsBox.getChildren().clear();

        List<Question> mine = new ArrayList<>();
        for (Question q : QuestionList.getInstance().getQuestions()) {
            if (q.getUser() != null && q.getUser().getId().equals(user.getId())) {
                mine.add(q);
            }
        }

        if (mine.isEmpty()) {
            Label msg = new Label("No questions posted yet.");
            msg.setStyle("-fx-text-fill: #888888;");
            myPostedQuestionsBox.getChildren().add(msg);
            return;
        }

        for (Question q : mine) {
            myPostedQuestionsBox.getChildren().add(buildEditableQuestionRow(q));
        }
    }

    private HBox buildEditableQuestionRow(Question q) {
        Label title = new Label(q.getTitle() != null ? q.getTitle() : "Untitled");
        title.getStyleClass().add("question-title");
        VBox titleBox = new VBox(title);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        Label diffBadge = new Label(difficultyText(q.getDifficulty()));
        diffBadge.getStyleClass().addAll("badge", difficultyBadgeClass(q.getDifficulty()));

        Label langBadge = new Label(q.getLanguage() != null ? q.getLanguage().name() : "Unknown");
        langBadge.getStyleClass().add("badge");

        HBox row = new HBox(12, titleBox, langBadge, diffBadge);

        boolean canEdit = App.currentUser != null
                && q.getUser() != null
                && App.currentUser.getId() != null
                && App.currentUser.getId().equals(q.getUser().getId());
        if (canEdit) {
            Button editBtn = new Button("Edit");
            editBtn.getStyleClass().add("btn-edit-question");
            editBtn.setOnAction(e -> {
                App.editingQuestion = q;
                try {
                    App.setRoot("addquestion");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            row.getChildren().add(editBtn);
        }
        row.getStyleClass().add("question-card");
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 14, 12, 14));
        return row;
    }

    private String difficultyText(Difficulty d) {
        if (d == null) return "Unknown";
        switch (d) {
            case EASY: return "Easy";
            case MEDIUM: return "Medium";
            case HARD: return "Hard";
            default: return "Unknown";
        }
    }

    private String difficultyBadgeClass(Difficulty d) {
        if (d == null) return "badge-easy";
        switch (d) {
            case EASY: return "badge-easy";
            case MEDIUM: return "badge-medium";
            case HARD: return "badge-hard";
            default: return "badge-easy";
        }
    }
}
