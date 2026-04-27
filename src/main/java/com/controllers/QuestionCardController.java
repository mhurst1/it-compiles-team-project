package com.controllers;

import java.io.IOException;
import java.util.ArrayList;
import com.interviews.UserList;

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
        return buildCard(question, index, null);
    }

    public HBox buildCard(Question question, int index, Runnable starChangedHandler) {
        
        Circle dot = new Circle(5);
        boolean solved = false;
        if (App.currentUser != null && App.currentUser.getAnsweredQuestions() != null) {
            for (Question q : App.currentUser.getAnsweredQuestions()) {
                if (sameQuestion(q, question)) {
                    solved = true;
                    break;
                }
            }
        }
        dot.getStyleClass().add(solved ? "dot-solved" : "dot-unsolved");


        boolean starred = isStarred(question);

        Label star = new Label();
        updateStarLabel(star, starred);
        star.setOnMouseClicked(event -> {
            event.consume();
            toggleStar(question);
            updateStarLabel(star, isStarred(question));
            if (starChangedHandler != null) {
                starChangedHandler.run();
            }
        });

        

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

        HBox card = new HBox(12, dot, star, number, titleBox, languageBadge, badge, votes, arrow);
        card.getStyleClass().add("question-card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(12, 14, 12, 14));

        card.setOnMouseClicked(e -> {
            App.currentQuestion = question;
            App.currentCategory = difficultyText(question.getDifficulty()); 
            try {
                App.setRoot("browsesolutions");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        return card;
    }

    private boolean isStarred(Question question) {
        if (App.currentUser == null || question == null || App.currentUser.getStarredQuestions() == null) {
            return false;
        }

        for (Question starredQuestion : App.currentUser.getStarredQuestions()) {
            if (sameQuestion(starredQuestion, question)) {
                return true;
            }
        }
        return false;
    }

    private void toggleStar(Question question) {
        if (App.currentUser == null || question == null) {
            return;
        }

        ArrayList<Question> starredQuestions = App.currentUser.getStarredQuestions();
        if (starredQuestions == null) {
            starredQuestions = new ArrayList<>();
            App.currentUser.setStarredQuestions(starredQuestions);
        }

        if (isStarred(question)) {
            starredQuestions.removeIf(starredQuestion -> sameQuestion(starredQuestion, question));
        } else {
            starredQuestions.add(question);
        }

        UserList.getInstance().save();
    }

    private void updateStarLabel(Label star, boolean starred) {
        star.setText(starred ? "\u2605" : "\u2606");
        star.getStyleClass().removeAll("star-filled", "star-empty");
        star.getStyleClass().add(starred ? "star-filled" : "star-empty");
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
