package com.controllers;

import java.io.IOException;

import com.interviews.App;
import com.interviews.Difficulty;
import com.interviews.Language;
import com.interviews.Question;
import com.interviews.UserSolution;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class QuestionCardController {

    public HBox buildCard(Question question, int index) {
        
        Circle dot = new Circle(5);
        boolean solved = false;
        if (App.currentUser != null && App.currentUser.getAnsweredQuestions() != null) {
            for (Question q : App.currentUser.getAnsweredQuestions()) {
                if (q.getId().equals(question.getId())) {
                    solved = true;
                    break;
                }
            }
        }
        dot.getStyleClass().add(solved ? "dot-solved" : "dot-unsolved");

        Label number = new Label(String.valueOf(index));
        number.getStyleClass().add("question-number");

        Label title = new Label(question.getTitle() != null ? question.getTitle() : "Untitled");
        title.getStyleClass().add("question-title");


        VBox titleBox = new VBox(2, title);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        Label badge = new Label(difficultyText(question.getDifficulty()));
        badge.getStyleClass().addAll("badge", difficultyBadgeClass(question.getDifficulty()));

        Label languageBadge = new Label(languageText(question.getLanguage()));
        languageBadge.getStyleClass().add("badge");

        int totalVotes = 0;
        if (question.getSolutionList() != null) {
            for (UserSolution sol : question.getSolutionList()) {
                totalVotes += sol.getTotalVote();
            }
        }
        Label votes = new Label("↑ " + totalVotes);
        votes.getStyleClass().add("vote-count");

        Label arrow = new Label("›");
        arrow.getStyleClass().add("arrow");

        HBox card = new HBox(12, dot, number, titleBox, languageBadge, badge, votes, arrow);
        card.getStyleClass().add("question-card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(12, 14, 12, 14));

        card.setOnMouseClicked(e -> {
            App.currentQuestion = question;
            App.currentCategory = difficultyText(question.getDifficulty()); // placeholder until Question has a category field
            try {
                App.setRoot("browsesolutions");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        return card;
    }

    private String difficultyText(Difficulty d) {
        if (d == null) {
            return "Unknown";
        }
        switch (d) {
            case EASY:
                return "Easy";
            case MEDIUM:
                return "Medium";
            case HARD:
                return "Hard";
            default:
                return "Unknown";
        }
    }

     private String languageText(Language l) {
        if (l == null) return "Unknown";
        return l.name();
    }

    private String difficultyBadgeClass(Difficulty d) {
        if (d == null) {
            return "badge-easy";
        }
        switch (d) {
            case EASY:
                return "badge-easy";
            case MEDIUM:
                return "badge-medium";
            case HARD:
                return "badge-hard";
            default:
                return "badge-easy";
        }
    
    }
}
