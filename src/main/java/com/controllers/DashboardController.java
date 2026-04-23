package com.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import com.interviews.Achievement;
import com.interviews.App;
import com.interviews.DataLoader;
import com.interviews.Difficulty;
import com.interviews.Language;
import com.interviews.Question;
import com.interviews.Status;

public class DashboardController {

    @FXML private Button adminDashboardButton;
    @FXML private Label welcomeLabel;
    @FXML private Label welcomeGreeting;
    @FXML private Label contentSubtitle;
    @FXML private VBox questionCardList;
    @FXML private Button filterAllBtn;
    @FXML private Button filterEasyBtn;
    @FXML private Button filterMediumBtn;
    @FXML private Button filterHardBtn;
    @FXML private TextField searchField;

    // Left sidebar
    @FXML private HBox sidebarAllQuestions;
    @FXML private Label sidebarAllQuestionsCount;
    @FXML private VBox languageSidebarList;

    // Quick stats
    @FXML private Label statSolvedValue;
    @FXML private Label statStreakValue;
    @FXML private Label statRankValue;
    @FXML private Label statUpvoteValue;
    @FXML private Label statDownvoteValue;

    // Recent activity
    @FXML private VBox recentActivityList;

    private ArrayList<Question> questions;
    private Difficulty activeFilter = null;
    private Language activeLanguageFilter = null;
    private HBox activeSidebarItem;

    @FXML
    private void initialize() {
        if (App.currentUser == null || App.currentUser.getStatus() != Status.ADMIN) {
            adminDashboardButton.setVisible(false);
            adminDashboardButton.setManaged(false);
        }

        questions = DataLoader.getQuestions();

        if (App.currentUser != null) {
            String name = App.currentUser.getFirstName();
            welcomeLabel.setText(name);
            welcomeGreeting.setText("Welcome, " + name + "!");
        } else {
            welcomeLabel.setText("Unknown User");
            welcomeGreeting.setText("Welcome!");
        }

        contentSubtitle.setText(questions.size() + " questions across all topics");
        loadCards(questions);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        sidebarAllQuestionsCount.setText(String.valueOf(questions.size()));
        activeSidebarItem = sidebarAllQuestions;
        sidebarAllQuestions.setOnMouseClicked(e -> {
            setActiveSidebarItem(sidebarAllQuestions);
            activeLanguageFilter = null;
            applyFilters();
        });

        populateLanguageSidebar();
        populateQuickStats();
        populateRecentActivity();
    }

    private void populateLanguageSidebar() {
        languageSidebarList.getChildren().clear();
        for (Language lang : Language.values()) {
            if (lang == Language.UNKNOWN) continue;
            long count = questions.stream().filter(q -> q.getLanguage() == lang).count();

            HBox item = new HBox();
            item.setAlignment(Pos.CENTER_LEFT);
            item.getStyleClass().add("sidebar-item");

            Label nameLabel = new Label(formatLanguageName(lang));
            nameLabel.getStyleClass().add("sidebar-item-label");
            HBox.setHgrow(nameLabel, Priority.ALWAYS);

            Label cntLabel = new Label(String.valueOf(count));
            cntLabel.getStyleClass().add("sidebar-count");

            item.getChildren().addAll(nameLabel, cntLabel);

            final Language filter = lang;
            item.setOnMouseClicked(e -> {
                setActiveSidebarItem(item);
                activeLanguageFilter = filter;
                applyFilters();
            });

            languageSidebarList.getChildren().add(item);
        }
    }

    private String formatLanguageName(Language lang) {
        switch (lang) {
            case JAVASCRIPT: return "JavaScript";
            case HTML:       return "HTML";
            case CSS:        return "CSS";
            case LINUX:      return "Linux";
            default: {
                String s = lang.name();
                return s.charAt(0) + s.substring(1).toLowerCase();
            }
        }
    }

    private void setActiveSidebarItem(HBox item) {
        if (activeSidebarItem != null) {
            activeSidebarItem.getStyleClass().remove("sidebar-item-active");
        }
        activeSidebarItem = item;
        item.getStyleClass().add("sidebar-item-active");
    }

    private void populateQuickStats() {
        if (App.currentUser != null) {
            int solved = App.currentUser.getAnsweredQuestions() != null
                         ? App.currentUser.getAnsweredQuestions().size() : 0;
            statSolvedValue.setText(String.valueOf(solved));

            ArrayList<Achievement> ach = App.currentUser.getAchievements();
            if (ach != null && !ach.isEmpty()) {
                Achievement a = ach.get(ach.size() - 1);
                statStreakValue.setText(a.getStreak() + " days");
                statRankValue.setText("#" + a.getLeaderboardPlace());
                statUpvoteValue.setText("↑ " + a.getAllVotePoints());
            } else {
                statStreakValue.setText("0 days");
                statRankValue.setText("#0");
                statUpvoteValue.setText("↑ 0");
            }
            statDownvoteValue.setText("↓ 0");
        } else {
            statSolvedValue.setText("0");
            statStreakValue.setText("0 days");
            statRankValue.setText("#0");
            statUpvoteValue.setText("↑ 0");
            statDownvoteValue.setText("↓ 0");
        }
    }

    private void populateRecentActivity() {
        recentActivityList.getChildren().clear();
        if (App.currentUser == null) {
            Label msg = new Label("Login to view recent activity");
            msg.getStyleClass().add("activity-item");
            recentActivityList.getChildren().add(msg);
            return;
        }
        ArrayList<Question> answered = App.currentUser.getAnsweredQuestions();
        if (answered == null || answered.isEmpty()) {
            Label msg = new Label("No recent activity");
            msg.getStyleClass().add("activity-item");
            recentActivityList.getChildren().add(msg);
            return;
        }
        int start = Math.max(0, answered.size() - 5);
        for (int i = answered.size() - 1; i >= start; i--) {
            Label item = new Label("✅ Solved " + answered.get(i).getTitle());
            item.getStyleClass().add("activity-item");
            recentActivityList.getChildren().add(item);
        }
    }

    @FXML
    private void filterAll() {
        setActiveFilter(filterAllBtn);
        activeFilter = null;
        applyFilters();
    }

    @FXML
    private void filterEasy() {
        setActiveFilter(filterEasyBtn);
        activeFilter = Difficulty.EASY;
        applyFilters();
    }

    @FXML
    private void filterMedium() {
        setActiveFilter(filterMediumBtn);
        activeFilter = Difficulty.MEDIUM;
        applyFilters();
    }

    @FXML
    private void filterHard() {
        setActiveFilter(filterHardBtn);
        activeFilter = Difficulty.DIFFICULT;
        applyFilters();
    }

    private void applyFilters() {
        String query = searchField.getText().trim().toLowerCase();
        ArrayList<Question> result = activeFilter == null ? questions : filterByDifficulty(activeFilter);
        if (activeLanguageFilter != null) {
            ArrayList<Question> byLang = new ArrayList<>();
            for (Question q : result) {
                if (q.getLanguage() == activeLanguageFilter) byLang.add(q);
            }
            result = byLang;
        }
        if (!query.isEmpty()) {
            ArrayList<Question> searched = new ArrayList<>();
            for (Question q : result) {
                if (q.getTitle().toLowerCase().contains(query)) searched.add(q);
            }
            result = searched;
        }
        contentSubtitle.setText(result.size() + " question" + (result.size() == 1 ? "" : "s") + " found");
        loadCards(result);
    }

    private ArrayList<Question> filterByDifficulty(Difficulty difficulty) {
        ArrayList<Question> filtered = new ArrayList<>();
        for (Question q : questions) {
            if (q.getDifficulty() == difficulty) filtered.add(q);
        }
        return filtered;
    }

    private void setActiveFilter(Button active) {
        filterAllBtn.getStyleClass().remove("filter-btn-active");
        filterEasyBtn.getStyleClass().remove("filter-btn-active");
        filterMediumBtn.getStyleClass().remove("filter-btn-active");
        filterHardBtn.getStyleClass().remove("filter-btn-active");
        active.getStyleClass().add("filter-btn-active");
    }

    @FXML
    private void goToHome() {
        try {
            App.setRoot("userpage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToQuestions() {
        try {
            App.setRoot("dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToCommunity() {
        try {
            App.setRoot("leaderboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToProfile() {
        try {
            App.setRoot(App.currentUser != null ? "profile" : "login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAdminDashboard() {
        try {
            if (App.currentUser != null && App.currentUser.getStatus() == Status.ADMIN) {
                App.setRoot("admindashboard");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAddQuestion() {
        try {
            App.setRoot("addquestion");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCards(ArrayList<Question> list) {
        questionCardList.getChildren().clear();
        QuestionCardController cardBuilder = new QuestionCardController();
        for (int i = 0; i < list.size(); i++) {
            HBox card = cardBuilder.buildCard(list.get(i), i + 1);
            questionCardList.getChildren().add(card);
        }
    }
}
