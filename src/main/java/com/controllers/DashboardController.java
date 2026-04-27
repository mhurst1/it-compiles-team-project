package com.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import com.interviews.App;
import com.interviews.Difficulty;
import com.interviews.Language;
import com.interviews.Question;
import com.interviews.QuestionList;
import com.interviews.Status;
import com.interviews.User;
import com.interviews.UserList;
import com.interviews.UserSolution;

public class DashboardController {

    @FXML private Button adminDashboardButton;
    @FXML private Button contributorApplicationButton;
    @FXML private Label navAvatarLetter;
    @FXML private Button addQuestionBtn;
    @FXML private Label welcomeLabel;
    @FXML private Label welcomeGreeting;
    @FXML private Label contentSubtitle;
    @FXML private VBox questionCardList;
    @FXML private Button filterAllBtn;
    @FXML private Button filterEasyBtn;
    @FXML private Button filterMediumBtn;
    @FXML private Button filterHardBtn;
    @FXML private Button filterStarredBtn;
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
    @FXML private ComboBox<String> rankModeComboBox;

    // Recent activity
    @FXML private VBox recentActivityList;

    private ArrayList<Question> questions;
    private Difficulty activeFilter = null;
    private Language activeLanguageFilter = null;
    private HBox activeSidebarItem;
    private boolean showingStarredOnly = false;

    private static final String RANK_OVERALL = "Overall";
    private static final String RANK_UPVOTES = "Upvotes";
    private static final String RANK_SOLUTIONS_POSTED = "Solutions Posted";

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

        if (App.currentUser == null || App.currentUser.getStatus() == Status.USER) {
            addQuestionBtn.setVisible(false);
            addQuestionBtn.setManaged(false);
        }

        questions = QuestionList.getInstance().getQuestions();
      
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
        configureRankModeSelector();
        populateQuickStats();
        populateRecentActivity();
    }

    private void configureRankModeSelector() {
        rankModeComboBox.getItems().setAll(RANK_OVERALL, RANK_UPVOTES, RANK_SOLUTIONS_POSTED);
        rankModeComboBox.setValue(RANK_OVERALL);
        rankModeComboBox.setOnAction(event -> updateRankStat());
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
            statStreakValue.setText(getUserStreak(App.currentUser) + " days");
            statUpvoteValue.setText(String.valueOf(computeUserVotePoints()));
            updateRankStat();
        } else {
            statSolvedValue.setText("0");
            statStreakValue.setText("0 days");
            statRankValue.setText("#0");
            statUpvoteValue.setText("0");
        }
    }

    private int computeUserVotePoints() {
        return computeUserVotePoints(App.currentUser);
    }

    private int computeUserVotePoints(User user) {
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

    private int computeUserRank(User currentUser) {
        return computeUserRank(currentUser, getRankMode());
    }

    private int computeUserRank(User currentUser, String rankMode) {
        if (currentUser == null || currentUser.getId() == null) {
            return 0;
        }

        ArrayList<User> rankedUsers = new ArrayList<>(UserList.getInstance().getUsers());
        rankedUsers.sort((a, b) -> compareUsersForRank(a, b, rankMode));

        for (int i = 0; i < rankedUsers.size(); i++) {
            User user = rankedUsers.get(i);
            if (user != null && user.getId() != null && user.getId().equals(currentUser.getId())) {
                return i + 1;
            }
        }

        return 0;
    }

    private int compareUsersForRank(User a, User b, String rankMode) {
        if (RANK_UPVOTES.equals(rankMode)) {
            int cmp = Integer.compare(computeUserVotePoints(b), computeUserVotePoints(a));
            if (cmp != 0) return cmp;

            cmp = Integer.compare(countSolutionsPosted(b), countSolutionsPosted(a));
            if (cmp != 0) return cmp;

            return Integer.compare(sizeOf(b.getAnsweredQuestions()), sizeOf(a.getAnsweredQuestions()));
        }

        if (RANK_SOLUTIONS_POSTED.equals(rankMode)) {
            int cmp = Integer.compare(countSolutionsPosted(b), countSolutionsPosted(a));
            if (cmp != 0) return cmp;

            cmp = Integer.compare(computeUserVotePoints(b), computeUserVotePoints(a));
            if (cmp != 0) return cmp;

            return Integer.compare(getUserStreak(b), getUserStreak(a));
        }

        int cmp = Integer.compare(sizeOf(b.getAnsweredQuestions()), sizeOf(a.getAnsweredQuestions()));
        if (cmp != 0) return cmp;

        cmp = Integer.compare(computeUserVotePoints(b), computeUserVotePoints(a));
        if (cmp != 0) return cmp;

        return Integer.compare(getUserStreak(b), getUserStreak(a));
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

    private void updateRankStat() {
        if (App.currentUser == null) {
            statRankValue.setText("#0");
            return;
        }

        statRankValue.setText("#" + computeUserRank(App.currentUser, getRankMode()));
    }

    private String getRankMode() {
        String selectedMode = rankModeComboBox.getValue();
        return selectedMode == null ? RANK_OVERALL : selectedMode;
    }

    private int getUserStreak(User user) {
        if (user == null || user.getAchievements() == null || user.getAchievements().isEmpty()) {
            return 0;
        }
        return user.getAchievements().get(user.getAchievements().size() - 1).getStreak();
    }

    private int sizeOf(ArrayList<?> list) {
        return list == null ? 0 : list.size();
    }

    private void populateRecentActivity() {
        recentActivityList.getChildren().clear();

        if (App.currentUser == null) {
            Label msg = new Label("Login to view recent activity");
            msg.getStyleClass().add("activity-item");
            recentActivityList.getChildren().add(msg);
            return;
        }

        ArrayList<String> activities = new ArrayList<>();
        ArrayList<Question> answered = App.currentUser.getAnsweredQuestions();

        ArrayList<Question> userQuestions = new ArrayList<>();
        for (Question q : QuestionList.getInstance().getQuestions()) {
            if (q.getUser() != null &&
                q.getUser().getId().equals(App.currentUser.getId())) {
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
            recentActivityList.getChildren().add(msg);
            return;
        }

        for (String text : activities) {
            Label item = new Label(text);
            item.getStyleClass().add("activity-item");
            recentActivityList.getChildren().add(item);
        }
    }
    
    @FXML
    private void filterAll() {
        setActiveFilter(filterAllBtn);
        showingStarredOnly = false;
        activeFilter = null;
        applyFilters();
    }

    @FXML
    private void filterEasy() {
        setActiveFilter(filterEasyBtn);
        showingStarredOnly = false;
        activeFilter = Difficulty.EASY;
        applyFilters();
    }

    @FXML
    private void filterMedium() {
        setActiveFilter(filterMediumBtn);
        showingStarredOnly = false;
        activeFilter = Difficulty.MEDIUM;
        applyFilters();
    }

    @FXML
    private void filterHard() {
        setActiveFilter(filterHardBtn);
        showingStarredOnly = false;
        activeFilter = Difficulty.HARD;
        applyFilters();
    }

    private void applyFilters() {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        ArrayList<Question> result = showingStarredOnly
                ? getStarredQuestionsForCurrentUser()
                : activeFilter == null ? questions : filterByDifficulty(activeFilter);
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
                if (q.getTitle() != null && q.getTitle().toLowerCase().contains(query)) searched.add(q);
            }
            result = searched;
        }
        String label = showingStarredOnly ? "starred question" : "question";
        contentSubtitle.setText(result.size() + " " + label + (result.size() == 1 ? "" : "s") + " found");
        loadCards(result);
    }

    private ArrayList<Question> getStarredQuestionsForCurrentUser() {
        ArrayList<Question> starred = new ArrayList<>();
        if (App.currentUser == null || App.currentUser.getStarredQuestions() == null) {
            return starred;
        }

        for (Question question : questions) {
            for (Question starredQuestion : App.currentUser.getStarredQuestions()) {
                if (sameQuestion(starredQuestion, question)) {
                    starred.add(question);
                    break;
                }
            }
        }

        return starred;
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
        filterStarredBtn.getStyleClass().remove("filter-btn-active");
        if (active != null) {
            active.getStyleClass().add("filter-btn-active");
        }
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
    private void goToAdminDashboard() {
        try {
            App.goToAdminDashboardIfAllowed();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToContributorApplication() {
        try {
            App.goToContributorApplicationIfAllowed();
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
        ArrayList<Question> sortedQuestions = sortByUpvotes(list);
        for (int i = 0; i < sortedQuestions.size(); i++) {
            HBox card = cardBuilder.buildCard(sortedQuestions.get(i), i + 1,
                    showingStarredOnly ? this::applyFilters : null);
            questionCardList.getChildren().add(card);
        }
    }

    private ArrayList<Question> sortByUpvotes(ArrayList<Question> list) {
        ArrayList<Question> sortedQuestions = new ArrayList<>(list);
        sortedQuestions.sort((a, b) -> Integer.compare(getQuestionVoteTotal(b), getQuestionVoteTotal(a)));
        return sortedQuestions;
    }

    private int getQuestionVoteTotal(Question question) {
        if (question == null || question.getSolutionList() == null) {
            return 0;
        }

        int total = 0;
        for (UserSolution solution : question.getSolutionList()) {
            if (solution != null) {
                total += solution.getTotalVote();
            }
        }
        return total;
    }

    @FXML
    private void filterStarred() {
        showingStarredOnly = true;
        activeFilter = null;
        activeLanguageFilter = null;
        setActiveSidebarItem(sidebarAllQuestions);
        setActiveFilter(filterStarredBtn);
        applyFilters();
    }

    private boolean sameQuestion(Question first, Question second) {
        if (first == null || second == null) {
            return false;
        }
        if (first.getId() != null && second.getId() != null) {
            return first.getId().equals(second.getId());
        }
        return first == second;
    }
}
