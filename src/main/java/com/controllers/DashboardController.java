package com.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.interviews.App;
import com.interviews.DataLoader;
import com.interviews.Difficulty;
import com.interviews.Question;

public class DashboardController {

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

    private ArrayList<Question> questions;

    @FXML
    private void initialize() {
        questions = DataLoader.getQuestions();

        if (App.currentUser != null) {
            String name = App.currentUser.getFirstName();
            welcomeLabel.setText(name);
            welcomeGreeting.setText("Welcome, " + name + "!");
        } else {
            welcomeLabel.setText("");
            welcomeGreeting.setText("Welcome!");
        }

        contentSubtitle.setText(questions.size() + " questions across all topics");
        loadCards(questions);
    }

    @FXML
    private void filterAll() {
        setActiveFilter(filterAllBtn);
        contentSubtitle.setText(questions.size() + " questions across all topics");
        loadCards(questions);
    }

    @FXML
    private void filterEasy() {
        setActiveFilter(filterEasyBtn);
        ArrayList<Question> filtered = filterByDifficulty(Difficulty.EASY);
        contentSubtitle.setText(filtered.size() + " easy questions");
        loadCards(filtered);
    }

    @FXML
    private void filterMedium() {
        setActiveFilter(filterMediumBtn);
        ArrayList<Question> filtered = filterByDifficulty(Difficulty.MEDIUM);
        contentSubtitle.setText(filtered.size() + " medium questions");
        loadCards(filtered);
    }

    @FXML
    private void filterHard() {
        setActiveFilter(filterHardBtn);
        ArrayList<Question> filtered = filterByDifficulty(Difficulty.DIFFICULT);
        contentSubtitle.setText(filtered.size() + " hard questions");
        loadCards(filtered);
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
