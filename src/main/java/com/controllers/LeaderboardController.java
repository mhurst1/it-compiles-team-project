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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LeaderboardController {

    @FXML private Button adminDashboardButton;

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
    private VBox firstPlaceCard;

    @FXML
    private VBox secondPlaceCard;

    @FXML
    private VBox thirdPlaceCard;

    private List<User> allSorted;

    @FXML
    private void initialize() {
        if (App.currentUser != null) {
            welcomeLabel.setText(App.currentUser.getFirstName());
            welcomeGreeting.setText("Welcome, " + App.currentUser.getFirstName() + "!");
        } else {
            welcomeLabel.setText("Unknown User");
        }

        if (App.currentUser == null || App.currentUser.getStatus() != Status.ADMIN) {
            adminDashboardButton.setVisible(false);
            adminDashboardButton.setManaged(false);
        }

         if (App.currentUser != null && App.currentUser.getFirstName() != null && !App.currentUser.getFirstName().isEmpty()) {
            String firstLetter = App.currentUser.getFirstName().substring(0, 1).toUpperCase();
            navAvatarLetter.setText(firstLetter);
        } else {
            navAvatarLetter.setText("U");
        }

        contentSubtitle.setText("See how you rank against the community.");
        allSorted = getSortedUsers();
        populatePodium(allSorted);
        populateList(allSorted);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterList(newVal));
        setActiveFilter(filterAllBtn);
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
        deduped.sort((a, b) -> {
            int cmp = b.getAnsweredQuestions().size() - a.getAnsweredQuestions().size();
            if (cmp != 0) return cmp;
            cmp = getUserVotePoints(b) - getUserVotePoints(a);
            if (cmp != 0) return cmp;
            return getStreak(b) - getStreak(a);
        });
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

        Label stats = new Label("⬆ " + getUserVotePoints(user) + "   solved: " + user.getAnsweredQuestions().size());
        stats.getStyleClass().add("lb-card-stats");

        card.getChildren().addAll(placeLabel, medalLabel, avatar, username, roleLabel, stats);
    }

    private void populateList(List<User> users) {
        questionCardList.getChildren().clear();
        for (int i = 0; i < users.size(); i++) {
            questionCardList.getChildren().add(buildRow(users.get(i), i + 1));
        }
    }

    private void filterList(String query) {
        if (query == null || query.isBlank()) {
            populateList(allSorted);
            return;
        }
        String lower = query.toLowerCase();
        List<User> filtered = new ArrayList<>();
        for (User u : allSorted) {
            if (u.getUsername() != null && u.getUsername().toLowerCase().contains(lower)) {
                filtered.add(u);
            }
        }
        populateList(filtered);
    }

    @FXML
    private void filterAll() {
        setActiveFilter(filterAllBtn);
        populatePodium(allSorted);
        populateList(allSorted);
    }

    @FXML
    private void filterEasy() {
        setActiveFilter(filterEasyBtn);
        List<User> filtered = new ArrayList<>();
        for (User u : allSorted) {
            if (u.getStatus() == Status.USER) {
                filtered.add(u);
            }
        }
        populatePodium(filtered);
        populateList(filtered);
    }

    @FXML
    private void filterMedium() {
        setActiveFilter(filterMediumBtn);
        List<User> filtered = new ArrayList<>();
        for (User u : allSorted) {
            if (u.getStatus() == Status.CONTRIBUTOR) {
                filtered.add(u);
            }
        }
        populatePodium(filtered);
        populateList(filtered);
    }

    @FXML
    private void filterHard() {
        setActiveFilter(filterHardBtn);
        List<User> filtered = new ArrayList<>();
        for (User u : allSorted) {
            if (u.getStatus() == Status.ADMIN) {
                filtered.add(u);
            }
        }
        populatePodium(filtered);
        populateList(filtered);
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

        Label solvedLabel = new Label(String.valueOf(user.getAnsweredQuestions().size()));
        solvedLabel.setMinWidth(145);
        solvedLabel.getStyleClass().add("lb-solved");

        int votes = getUserVotePoints(user);
        Label votesLabel = new Label("⬆ " + votes);
        votesLabel.setMinWidth(190);
        votesLabel.getStyleClass().add("lb-votes");

        int streak = getStreak(user);
        String streakText = streak == 0 ? "today" : streak + "d 🔥";
        Label streakLabel = new Label(streakText);
        streakLabel.setMinWidth(90);
        streakLabel.getStyleClass().add(streak > 5 ? "lb-streak-hot" : "lb-streak");

        Button viewBtn = new Button("View Profile");
        viewBtn.getStyleClass().add("lb-view-btn");

        row.getChildren().addAll(rankLabel, usernameLabel, roleCell, solvedLabel, votesLabel, streakLabel, viewBtn);
        return row;
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
        int total = 0;
        for (Question q : QuestionList.getInstance().getQuestions()) {
            if (q.getSolutionList() == null) continue;
            for (UserSolution sol : q.getSolutionList()) {
                if (sol.getUser() != null && sol.getUser().getId().equals(user.getId())) {
                    total += sol.getTotalVote();
                }
            }
        }
        return total;
    }

    private int getStreak(User user) {
        if (user.getAchievements() != null && !user.getAchievements().isEmpty()) {
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
        if (App.currentUser != null && App.currentUser.getStatus() == Status.ADMIN) {
            App.setRoot("admindashboard");
        }
    }
}
