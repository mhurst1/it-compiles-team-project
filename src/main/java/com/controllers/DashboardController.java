package com.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.interviews.App;
import com.interviews.DataLoader;
import com.interviews.Difficulty;
import com.interviews.Question;
import com.interviews.Status;

public class DashboardController {

    @FXML private Button adminDashboardButton;

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

    private ArrayList<Question> questions;
    private Difficulty activeFilter = null;

    @FXML
    private void initialize() {
        questions = DataLoader.getQuestions();

        if (App.currentUser != null) {
            String name = App.currentUser.getFirstName();
            welcomeLabel.setText(name);
            welcomeGreeting.setText("Welcome, " + name + "!");
        } else {
            welcomeLabel.setText("Name");
            welcomeGreeting.setText("Welcome!");
        }


        if (App.currentUser == null || App.currentUser.getStatus() != Status.ADMIN) {
            adminDashboardButton.setVisible(false);
            adminDashboardButton.setManaged(false);
}

        contentSubtitle.setText(questions.size() + " questions across all topics");
        loadCards(questions);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
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
        if (!query.isEmpty()) {
            ArrayList<Question> searched = new ArrayList<>();
            for (Question q : result) {
                if (q.getTitle().toLowerCase().contains(query)) {
                    searched.add(q);
                }
            }
            result = searched;
        }
        contentSubtitle.setText(result.size() + " question" + (result.size() == 1 ? "" : "s") + " found");
        loadCards(result);
    }

    private ArrayList<Question> filterByDifficulty(Difficulty difficulty) {
        ArrayList<Question> filtered = new ArrayList<>();
        for (Question q : questions) {
            if (q.getDifficulty() == difficulty) {
                filtered.add(q);
            }
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
        private void goToAdminDashboard() throws IOException {
            if (App.currentUser != null && App.currentUser.getStatus() == Status.ADMIN) {
                App.setRoot("admindashboard");
            }
        }

    @FXML
    private void goToProfile() {
        try {
            App.setRoot("profile");
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
