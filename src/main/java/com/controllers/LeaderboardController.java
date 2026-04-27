package com.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.interviews.App;
import com.interviews.Question;
import com.interviews.QuestionList;
import com.interviews.Status;
import com.interviews.User;
import com.interviews.UserList;
import com.interviews.UserSolution;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LeaderboardController {

    @FXML private Button adminDashboardButton;
    @FXML private Button contributorApplicationButton;

    @FXML private Label navAvatarLetter;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label welcomeGreeting;

    @FXML
    private Label contentSubtitle;

    @FXML
    private VBox questionCardList;

    @FXML
    private Button filterAllBtn;

    @FXML
    private Button filterEasyBtn;

    @FXML
    private Button filterMediumBtn;

    @FXML
    private Button filterHardBtn;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> rankModeComboBox;

    @FXML
    private VBox firstPlaceCard;

    @FXML
    private VBox secondPlaceCard;

    @FXML
    private VBox thirdPlaceCard;

    private List<User> allSorted;
    private Status activeRoleFilter = null;

    private static final String RANK_QUESTIONS_SOLVED = "Questions Solved";
    private static final String RANK_UPVOTES = "Upvotes";
    private static final String RANK_SOLUTIONS_POSTED = "Solutions Posted";

    @FXML
    private void initialize() {
        App.configureAdminDashboardButton(adminDashboardButton);
        App.configureContributorApplicationButton(contributorApplicationButton);

        if (App.currentUser != null) {
            welcomeLabel.setText(App.currentUser.getFirstName());
            welcomeGreeting.setText("Welcome, " + App.currentUser.getFirstName() + "!");
        } else {
            welcomeLabel.setText("Unknown User");
        }

         if (App.currentUser != null && App.currentUser.getFirstName() != null && !App.currentUser.getFirstName().isEmpty()) {
            String firstLetter = App.currentUser.getFirstName().substring(0, 1).toUpperCase();
            navAvatarLetter.setText(firstLetter);
        } else {
            navAvatarLetter.setText("U");
        }

        contentSubtitle.setText("See how you rank against the community.");
        configureRankModeDropdown();
        allSorted = getSortedUsers();
        refreshRankings();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> refreshRankings());
        setActiveFilter(filterAllBtn);
    }

    private void configureRankModeDropdown() {
        rankModeComboBox.getItems().setAll(RANK_QUESTIONS_SOLVED, RANK_UPVOTES, RANK_SOLUTIONS_POSTED);
        rankModeComboBox.setValue(RANK_QUESTIONS_SOLVED);
        rankModeComboBox.setOnAction(event -> {
            allSorted = getSortedUsers();
            refreshRankings();
        });
    }

    private List<User> getSortedUsers() {
        List<User> all = new ArrayList<>(UserList.getInstance().getUsers());
        Map<String, User> seen = new LinkedHashMap<>();
        for (User u : all) {
            if (u.getUsername() != null && !u.getUsername().isBlank() && !seen.containsKey(u.getUsername())) {
                seen.put(u.getUsername(), u);
            }
        }
        List<User> deduped = new ArrayList<>(seen.values());
        deduped.sort((a, b) -> compareUsersForRank(a, b, getRankMode()));
        return deduped;
    }

    private void populatePodium(List<User> sorted) {
        firstPlaceCard.getChildren().clear();
        secondPlaceCard.getChildren().clear();
        thirdPlaceCard.getChildren().clear();
        if (!sorted.isEmpty()) {
            fillPodiumCard(firstPlaceCard, sorted.get(0), 1, "🥇");
        }
        if (sorted.size() >= 2) {
            fillPodiumCard(secondPlaceCard, sorted.get(1), 2, "🥈");
        }
        if (sorted.size() >= 3) {
            fillPodiumCard(thirdPlaceCard, sorted.get(2), 3, "🥉");
        }
    }

    private void fillPodiumCard(VBox card, User user, int rank, String medal) {
        card.getChildren().clear();

        String placeText = rank == 1 ? "1st Place" : rank == 2 ? "2nd Place" : "3rd Place";
        Label placeLabel = new Label(placeText);
        placeLabel.getStyleClass().add("lb-place-label");

        Label medalLabel = new Label(medal);
        medalLabel.getStyleClass().add("lb-medal");

        Label avatar = new Label(getInitials(user));
        avatar.getStyleClass().add("lb-avatar");

        Label username = new Label(user.getUsername() != null ? user.getUsername() : "—");
        username.getStyleClass().add("lb-card-username");

        Label roleLabel = new Label(user.getStatus().toString());
        roleLabel.getStyleClass().add(badgeClass(user));

        Label stats = new Label(podiumStatsText(user));
        stats.getStyleClass().add("lb-card-stats");

        card.getChildren().addAll(placeLabel, medalLabel, avatar, username, roleLabel, stats);
    }

    private void populateList(List<User> users) {
        questionCardList.getChildren().clear();
        for (int i = 0; i < users.size(); i++) {
            questionCardList.getChildren().add(buildRow(users.get(i), i + 1));
        }
    }

    private void refreshRankings() {
        List<User> filtered = filterRankedUsers();
        populatePodium(filtered);
        populateList(filtered);
    }

    private List<User> filterRankedUsers() {
        String query = searchField.getText();
        String lower = query == null ? "" : query.toLowerCase().trim();
        List<User> filtered = new ArrayList<>();

        for (User u : allSorted) {
            if (activeRoleFilter != null && u.getStatus() != activeRoleFilter) {
                continue;
            }
            if (!lower.isEmpty()) {
                String username = u.getUsername();
                if (username == null || !username.toLowerCase().contains(lower)) {
                    continue;
                }
            }
            filtered.add(u);
        }

        return filtered;
    }

    @FXML
    private void filterAll() {
        activeRoleFilter = null;
        setActiveFilter(filterAllBtn);
        refreshRankings();
    }

    @FXML
    private void filterEasy() {
        activeRoleFilter = Status.USER;
        setActiveFilter(filterEasyBtn);
        refreshRankings();
    }

    @FXML
    private void filterMedium() {
        activeRoleFilter = Status.CONTRIBUTOR;
        setActiveFilter(filterMediumBtn);
        refreshRankings();
    }

    @FXML
    private void filterHard() {
        activeRoleFilter = Status.ADMIN;
        setActiveFilter(filterHardBtn);
        refreshRankings();
    }

    private void setActiveFilter(Button active) {
        filterAllBtn.getStyleClass().removeAll("filter-btn-active");
        filterEasyBtn.getStyleClass().removeAll("filter-btn-active");
        filterMediumBtn.getStyleClass().removeAll("filter-btn-active");
        filterHardBtn.getStyleClass().removeAll("filter-btn-active");
        active.getStyleClass().add("filter-btn-active");
    }

    private HBox buildRow(User user, int rank) {
        boolean isMe = App.currentUser != null &&
            App.currentUser.getUsername() != null &&
            App.currentUser.getUsername().equals(user.getUsername());

        HBox row = new HBox(0);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add(isMe ? "lb-row-me" : "lb-row");

        String rankStr = (rank <= 3 ? rankMedal(rank) + " " : "") + "#" + rank + (isMe ? " ⭐" : "");
        Label rankLabel = new Label(rankStr);
        rankLabel.setMinWidth(70);
        rankLabel.getStyleClass().add(isMe ? "lb-rank-me" : "lb-rank");

        Label usernameLabel = new Label(user.getUsername() != null ? user.getUsername() : "—");
        usernameLabel.setMinWidth(120);
        usernameLabel.getStyleClass().add(isMe ? "lb-user-text-me" : "lb-user-text");

        Label roleLabel = new Label(user.getStatus().toString());
        roleLabel.getStyleClass().add(badgeClass(user));

        HBox roleCell = new HBox(roleLabel);
        roleCell.setMinWidth(130);
        roleCell.setAlignment(Pos.CENTER_LEFT);

        int level = getUserLevel(user);
        Label levelLabel = new Label("Lv " + level);
        levelLabel.setMinWidth(60);
        levelLabel.getStyleClass().add("lb-level");

        Label solvedLabel = new Label(String.valueOf(sizeOf(user.getAnsweredQuestions())));
        solvedLabel.setMinWidth(145);
        solvedLabel.getStyleClass().add("lb-solved");

        Label solutionLabel = new Label(String.valueOf(countSolutionsPosted(user)));
        solutionLabel.setMinWidth(120);
        solutionLabel.getStyleClass().add("lb-solved");

        int votes = getUserVotePoints(user);
        Label votesLabel = new Label("⬆ " + votes);
        votesLabel.setMinWidth(190);
        votesLabel.getStyleClass().add("lb-votes");

        int streak = getStreak(user);
        String streakText = streak == 0 ? "0" : streak + "d 🔥";
        Label streakLabel = new Label(streakText);
        streakLabel.setMinWidth(90);
        streakLabel.getStyleClass().add(streak > 5 ? "lb-streak-hot" : "lb-streak");

        Button viewBtn = new Button("View Profile");
        viewBtn.getStyleClass().add("lb-view-btn");
        viewBtn.setOnAction(event -> viewProfile(user));

        row.getChildren().addAll(rankLabel, usernameLabel, roleCell, levelLabel, solvedLabel, solutionLabel, votesLabel, streakLabel, viewBtn);
        return row;
    }

    private void viewProfile(User user) {
        try {
            App.viewUserPage(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getInitials(User user) {
        return (user.getFirstName() != null && !user.getFirstName().isBlank())
            ? user.getFirstName().substring(0, 1).toUpperCase() : "?";
    }

    private String badgeClass(User user) {
        if (user.getStatus() == Status.ADMIN) {
            return "badge-admin";
        }
        if (user.getStatus() == Status.CONTRIBUTOR) {
            return "badge-contributor";
        }
        return "badge-user";
    }

    private String rankMedal(int rank) {
        if (rank == 1) {
            return "🥇";
        }
        if (rank == 2) {
            return "🥈";
        }
        if (rank == 3) {
            return "🥉";
        }
        return "";
    }

    private int getUserVotePoints(User user) {
        if (user == null || user.getId() == null) {
            return 0;
        }

        int total = 0;
        for (Question q : QuestionList.getInstance().getQuestions()) {
            if (q.getSolutionList() == null) continue;
            for (UserSolution sol : q.getSolutionList()) {
                if (sol.getUser() != null && sol.getUser().getId() != null
                        && sol.getUser().getId().equals(user.getId())) {
                    total += sol.getTotalVote();
                }
            }
        }
        return total;
    }

    private int countSolutionsPosted(User user) {
        if (user == null || user.getId() == null) {
            return 0;
        }

        int total = 0;
        for (Question question : QuestionList.getInstance().getQuestions()) {
            if (question.getSolutionList() == null) continue;
            for (UserSolution solution : question.getSolutionList()) {
                if (solution != null && solution.getUser() != null && solution.getUser().getId() != null
                        && solution.getUser().getId().equals(user.getId())) {
                    total++;
                }
            }
        }
        return total;
    }

    private int compareUsersForRank(User a, User b, String rankMode) {
        if (RANK_UPVOTES.equals(rankMode)) {
            int cmp = Integer.compare(getUserVotePoints(b), getUserVotePoints(a));
            if (cmp != 0) return cmp;

            cmp = Integer.compare(countSolutionsPosted(b), countSolutionsPosted(a));
            if (cmp != 0) return cmp;

            return Integer.compare(sizeOf(b.getAnsweredQuestions()), sizeOf(a.getAnsweredQuestions()));
        }

        if (RANK_SOLUTIONS_POSTED.equals(rankMode)) {
            int cmp = Integer.compare(countSolutionsPosted(b), countSolutionsPosted(a));
            if (cmp != 0) return cmp;

            cmp = Integer.compare(getUserVotePoints(b), getUserVotePoints(a));
            if (cmp != 0) return cmp;

            return Integer.compare(getStreak(b), getStreak(a));
        }

        int cmp = Integer.compare(sizeOf(b.getAnsweredQuestions()), sizeOf(a.getAnsweredQuestions()));
        if (cmp != 0) return cmp;

        cmp = Integer.compare(getUserVotePoints(b), getUserVotePoints(a));
        if (cmp != 0) return cmp;

        return Integer.compare(getStreak(b), getStreak(a));
    }

    private String podiumStatsText(User user) {
        if (RANK_UPVOTES.equals(getRankMode())) {
            return "votes: " + getUserVotePoints(user) + "   solutions: " + countSolutionsPosted(user);
        }
        if (RANK_SOLUTIONS_POSTED.equals(getRankMode())) {
            return "solutions: " + countSolutionsPosted(user) + "   votes: " + getUserVotePoints(user);
        }
        return "solved: " + sizeOf(user.getAnsweredQuestions()) + "   votes: " + getUserVotePoints(user);
    }

    private String getRankMode() {
        String selectedMode = rankModeComboBox.getValue();
        return selectedMode == null ? RANK_QUESTIONS_SOLVED : selectedMode;
    }

    private int getUserLevel(User user) {
        if (user == null || user.getAchievements() == null || user.getAchievements().isEmpty()) {
            return 1;
        }
        return user.getAchievements().get(0).getUserLevel();
    }

    private int sizeOf(ArrayList<?> list) {
        return list == null ? 0 : list.size();
    }

    private int getStreak(User user) {
        if (user != null && user.getAchievements() != null && !user.getAchievements().isEmpty()) {
            return user.getAchievements().get(0).getStreak();
        }
        return 0;
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
            App.viewCurrentUserProfile();
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
}
