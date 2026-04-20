package com.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.interviews.App;
import com.interviews.DataLoader;
import com.interviews.Difficulty;
import com.interviews.Question;
import com.interviews.Status;
import com.interviews.User;
import com.interviews.UserSolution;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AdminDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label avatarLabel;

    @FXML
    private Label dashboardSubtitle;

    @FXML
    private Label totalUsersValue;

    @FXML
    private Label totalUsersTrend;

    @FXML
    private Label totalQuestionsValue;

    @FXML
    private Label totalQuestionsTrend;

    @FXML
    private Label totalUpvotesValue;

    @FXML
    private Label totalUpvotesTrend;

    @FXML
    private Label totalSolutionsValue;

    @FXML
    private Label totalSolutionsTrend;

    @FXML
    private VBox userManagementRows;

    @FXML
    private VBox questionManagementRows;

    @FXML
    private VBox leaderboardRows;

    @FXML
    private Label actionMessage;

    private List<User> users;
    private List<Question> questions;
    private Map<String, UserMetrics> metricsByUser;

    @FXML
    private void initialize() {
        users = DataLoader.getUsers();
        questions = DataLoader.getQuestions();
        metricsByUser = buildUserMetrics(dedupeUsers(users), questions);

        configureWelcomeState();
        populateStatCards();
        populateUserManagement();
        populateQuestionManagement();
        populateLeaderboard();
    }

    @FXML
    private void goHome() {
        try {
            App.setRoot("dashboard");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openQuestions() {
        try {
            App.setRoot("dashboard");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openCommunity() {
        try {
            App.setRoot("leaderboard");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void configureWelcomeState() {
        User currentUser = App.currentUser;
        String displayName = "Admin";

        if (currentUser != null) {
            if (currentUser.getFirstName() != null && !currentUser.getFirstName().isBlank()) {
                displayName = currentUser.getFirstName();
            } else if (currentUser.getUsername() != null && !currentUser.getUsername().isBlank()) {
                displayName = currentUser.getUsername();
            }
        }

        welcomeLabel.setText(displayName);
        avatarLabel.setText(displayName.substring(0, 1).toUpperCase(Locale.US));
        dashboardSubtitle.setText("Platform overview for " + users.size() + " users and "
                + questions.size() + " questions.");
    }

    private void populateStatCards() {
        int totalVotes = 0;
        int totalSolutions = 0;
        int questionsWithSolutions = 0;
        int pendingReviews = 0;

        for (Question question : questions) {
            ArrayList<UserSolution> solutions = question.getSolutionList();
            if (solutions != null && !solutions.isEmpty()) {
                questionsWithSolutions++;
                totalSolutions += solutions.size();
                for (UserSolution solution : solutions) {
                    totalVotes += Math.max(0, solution.getTotalVote());
                }
            } else {
                pendingReviews++;
            }
        }

        totalUsersValue.setText(formatNumber(users.size()));
        totalUsersTrend.setText("Loaded from users.json");

        totalQuestionsValue.setText(formatNumber(questions.size()));
        totalQuestionsTrend.setText("Loaded from question.json");

        totalUpvotesValue.setText(formatNumber(totalVotes));
        totalUpvotesTrend.setText(totalSolutions == 0 ? "No upvotes yet" : totalSolutions + " solution votes tracked");

        totalSolutionsValue.setText(formatNumber(totalSolutions));
        totalSolutionsTrend.setText(questionsWithSolutions + " questions with solutions");
    }

    private void populateUserManagement() {
        userManagementRows.getChildren().clear();

        List<User> sortedUsers = new ArrayList<>(users);
        sortedUsers.sort(Comparator.comparing(this::resolveUsername, String.CASE_INSENSITIVE_ORDER));

        for (User user : sortedUsers) {
            UserMetrics metrics = metricsByUser.get(resolveUsername(user));
            HBox row = createPanelRow();
            row.getChildren().addAll(
                    textCell(resolveUsername(user), "panel-cell user-name-cell", 150),
                    badgeCell(roleLabel(user.getStatus()), roleClass(user.getStatus()), 95),
                    textCell(String.valueOf(metrics != null ? metrics.getSolvedCount() : 0), "panel-cell", 70),
                    accentCell(String.valueOf(metrics != null ? metrics.getVoteTotal() : 0), 70),
                    actionButton("Edit", "action-btn primary-action",
                            "Edit user flow for @" + resolveUsername(user) + " is ready to connect.")
            );
            userManagementRows.getChildren().add(row);
        }
    }

    private void populateQuestionManagement() {
        questionManagementRows.getChildren().clear();

        List<QuestionSnapshot> rankedQuestions = buildQuestionSnapshots();
        rankedQuestions.sort(Comparator.comparing(QuestionSnapshot::getTitle, String.CASE_INSENSITIVE_ORDER));

        for (QuestionSnapshot snapshot : rankedQuestions) {
            HBox row = createPanelRow();
            row.getChildren().addAll(
                    textCell(snapshot.getTitle(), "panel-cell question-title-cell", 220),
                    badgeCell(difficultyLabel(snapshot.getDifficulty()), difficultyClass(snapshot.getDifficulty()), 84),
                    accentCell(String.valueOf(snapshot.getVoteTotal()), 55),
                    textCell(snapshot.getAuthor(), "panel-cell by-cell", 96),
                    actionButton("Edit", "action-btn primary-action",
                            "Edit question flow for \"" + snapshot.getTitle() + "\" is ready to connect."),
                    actionButton("Remove", "action-btn danger-action",
                            "Remove question flow for \"" + snapshot.getTitle() + "\" is ready to connect.")
            );
            questionManagementRows.getChildren().add(row);
        }
    }

    private void populateLeaderboard() {
        leaderboardRows.getChildren().clear();

        List<UserMetrics> leaderboard = new ArrayList<>(metricsByUser.values());
        leaderboard.sort(Comparator.comparingInt(UserMetrics::getVoteTotal).reversed()
                .thenComparing(UserMetrics::getUsername, String.CASE_INSENSITIVE_ORDER));

        int limit = Math.min(5, leaderboard.size());
        for (int i = 0; i < limit; i++) {
            UserMetrics metrics = leaderboard.get(i);
            HBox row = createPanelRow();
            row.getChildren().addAll(
                    textCell("#" + (i + 1), "panel-cell rank-cell", 52),
                    textCell(metrics.getUsername(), "panel-cell user-name-cell", 130),
                    badgeCell(roleLabel(metrics.getStatus()), roleClass(metrics.getStatus()), 95),
                    accentCell(String.valueOf(metrics.getVoteTotal()), 70)
            );
            leaderboardRows.getChildren().add(row);
        }
    }

    private List<QuestionSnapshot> buildQuestionSnapshots() {
        List<QuestionSnapshot> snapshots = new ArrayList<>();

        for (Question question : questions) {
            int totalVotes = 0;
            int solutionCount = 0;

            ArrayList<UserSolution> solutions = question.getSolutionList();
            if (solutions != null) {
                solutionCount = solutions.size();
                for (UserSolution solution : solutions) {
                    totalVotes += Math.max(0, solution.getTotalVote());
                }
            }

            snapshots.add(new QuestionSnapshot(
                    safeQuestionTitle(question.getTitle()),
                    question.getDifficulty(),
                    totalVotes,
                    resolveUsername(question.getUser()),
                    solutionCount
            ));
        }

        return snapshots;
    }

    private Map<String, UserMetrics> buildUserMetrics(List<User> userList, List<Question> questionList) {
        Map<String, UserMetrics> results = new LinkedHashMap<>();

        for (User user : userList) {
            String key = resolveUsername(user);
            results.putIfAbsent(key, new UserMetrics(user, key));
        }

        for (Question question : questionList) {
            ArrayList<UserSolution> solutions = question.getSolutionList();
            if (solutions == null) {
                continue;
            }

            for (UserSolution solution : solutions) {
                User user = solution.getUser();
                if (user == null) {
                    continue;
                }

                String key = resolveUsername(user);
                UserMetrics metrics = results.computeIfAbsent(key, ignored -> new UserMetrics(user, key));
                metrics.addSolvedQuestion();
                metrics.addVotes(Math.max(0, solution.getTotalVote()));
            }
        }

        return results;
    }

    private List<User> dedupeUsers(List<User> loadedUsers) {
        Map<String, User> uniqueUsers = new LinkedHashMap<>();

        for (User user : loadedUsers) {
            String key = buildUserKey(user);
            if (!uniqueUsers.containsKey(key)) {
                uniqueUsers.put(key, user);
            }
        }

        return new ArrayList<>(uniqueUsers.values());
    }

    private String buildUserKey(User user) {
        if (user == null) {
            return "unknown-user";
        }
        if (user.getUsername() != null && !user.getUsername().isBlank()) {
            return "username:" + user.getUsername().trim().toLowerCase(Locale.US);
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            return "email:" + user.getEmail().trim().toLowerCase(Locale.US);
        }
        return "id:" + user.getId();
    }

    private String resolveUsername(User user) {
        if (user == null) {
            return "unknown";
        }
        if (user.getUsername() != null && !user.getUsername().isBlank()) {
            return user.getUsername();
        }
        if (user.getFirstName() != null && !user.getFirstName().isBlank()) {
            return user.getFirstName().toLowerCase(Locale.US);
        }
        return "unknown";
    }

    private String safeQuestionTitle(String title) {
        if (title == null || title.isBlank()) {
            return "Untitled Question";
        }
        return title;
    }

    private HBox createPanelRow() {
        HBox row = new HBox(10);
        row.getStyleClass().add("panel-row");
        row.setPadding(new Insets(10, 14, 10, 14));
        return row;
    }

    private Label textCell(String text, String styleClasses, double width) {
        Label label = new Label(text);
        label.getStyleClass().addAll(parseStyleClasses(styleClasses));
        label.setMinWidth(width);
        label.setPrefWidth(width);
        label.setMaxWidth(width);
        return label;
    }

    private Label accentCell(String text, double width) {
        return textCell(text, "panel-cell accent-cell", width);
    }

    private Label badgeCell(String text, String badgeClass, double width) {
        Label badge = new Label(text);
        badge.getStyleClass().addAll("panel-cell", "mini-badge", badgeClass);
        badge.setMinWidth(width);
        badge.setPrefWidth(width);
        badge.setMaxWidth(width);
        return badge;
    }

    private Button actionButton(String text, String styleClasses, String message) {
        Button button = new Button(text);
        button.getStyleClass().addAll(parseStyleClasses(styleClasses));
        button.setOnAction(event -> showActionMessage(message));
        return button;
    }

    private List<String> parseStyleClasses(String styleClasses) {
        List<String> classes = new ArrayList<>();
        for (String styleClass : styleClasses.split(" ")) {
            if (!styleClass.isBlank()) {
                classes.add(styleClass.trim());
            }
        }
        return classes;
    }

    private String difficultyLabel(Difficulty difficulty) {
        if (difficulty == null) {
            return "Unknown";
        }
        switch (difficulty) {
            case EASY:
                return "Easy";
            case MEDIUM:
                return "Medium";
            case DIFFICULT:
                return "Hard";
            default:
                return "Unknown";
        }
    }

    private String difficultyClass(Difficulty difficulty) {
        if (difficulty == null) {
            return "badge-neutral";
        }
        switch (difficulty) {
            case EASY:
                return "badge-easy";
            case MEDIUM:
                return "badge-medium";
            case DIFFICULT:
                return "badge-hard";
            default:
                return "badge-neutral";
        }
    }

    private String roleLabel(Status status) {
        if (status == null) {
            return "USER";
        }
        switch (status) {
            case ADMIN:
                return "ADMIN";
            case CONTRIBUTOR:
                return "CONTRIBUTOR";
            case USER:
            default:
                return "USER";
        }
    }

    private String roleClass(Status status) {
        if (status == null) {
            return "role-user";
        }
        switch (status) {
            case ADMIN:
                return "role-admin";
            case CONTRIBUTOR:
                return "role-contributor";
            case USER:
            default:
                return "role-user";
        }
    }

    private String formatNumber(int value) {
        return String.format(Locale.US, "%,d", value);
    }

    private void showActionMessage(String message) {
        actionMessage.setText(message);
    }

    private static final class UserMetrics {
        private final User user;
        private final String username;
        private int solvedCount;
        private int voteTotal;

        private UserMetrics(User user, String username) {
            this.user = user;
            this.username = username;
        }

        private void addSolvedQuestion() {
            solvedCount++;
        }

        private void addVotes(int votes) {
            voteTotal += votes;
        }

        private int getSolvedCount() {
            return solvedCount;
        }

        private int getVoteTotal() {
            return voteTotal;
        }

        private String getUsername() {
            return username;
        }

        private Status getStatus() {
            return user != null ? user.getStatus() : Status.USER;
        }
    }

    private static final class QuestionSnapshot {
        private final String title;
        private final Difficulty difficulty;
        private final int voteTotal;
        private final String author;
        private final int solutionCount;

        private QuestionSnapshot(String title, Difficulty difficulty, int voteTotal, String author, int solutionCount) {
            this.title = title;
            this.difficulty = difficulty;
            this.voteTotal = voteTotal;
            this.author = author;
            this.solutionCount = solutionCount;
        }

        private String getTitle() {
            return title;
        }

        private Difficulty getDifficulty() {
            return difficulty;
        }

        private int getVoteTotal() {
            return voteTotal;
        }

        private String getAuthor() {
            return author;
        }

        private int getSolutionCount() {
            return solutionCount;
        }
    }
}
