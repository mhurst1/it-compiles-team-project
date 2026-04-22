package com.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import com.interviews.App;
import com.interviews.Comment;
import com.interviews.DataLoader;
import com.interviews.Difficulty;
import com.interviews.Question;
import com.interviews.QuestionList;
import com.interviews.Section;
import com.interviews.UserSolution;

public class BrowseSolutions {

    @FXML
    private Label navUsername;
    @FXML
    private Label questionTitle;
    @FXML
    private Label categoryBadge;
    @FXML
    private Label difficultyBadge;
    @FXML
    private Label answersLabel;
    @FXML
    private Label questionDescription;
    @FXML
    private TextArea solutionInput;
    @FXML
    private VBox solutionsContainer;

    private final HashMap<UUID, Integer> voteState = new HashMap<>();

    @FXML
    public void initialize() {
        if (App.currentUser != null) {
            navUsername.setText(App.currentUser.getFirstName());
        }

        Question q = App.currentQuestion;
        if (q == null || q.getSolutionList() == null || q.getSolutionList().isEmpty()) {
            for (Question candidate : DataLoader.getQuestions()) {
                ArrayList<UserSolution> sols = candidate.getSolutionList();
                if (sols != null && !sols.isEmpty()) {
                    q = candidate;
                    App.currentQuestion = q;
                    break;
                }
            }
        }

        if (q == null) {
            questionTitle.setText("No question selected");
            questionDescription.setText("");
            answersLabel.setText("0 Answers");
            updateDifficultyBadge(null);
            return;
        }

        questionTitle.setText(q.getTitle() != null ? q.getTitle() : "");
        questionDescription.setText(buildDescriptionText(q));
        updateDifficultyBadge(q.getDifficulty());
        if (App.currentCategory != null && !App.currentCategory.isBlank()) {
            categoryBadge.setText(App.currentCategory);
        }

        ArrayList<UserSolution> solutions = q.getSolutionList();
        int count = (solutions == null) ? 0 : solutions.size();
        answersLabel.setText(count + (count == 1 ? " Answer" : " Answers"));

        populateSolutions(q);
    }

    private String buildDescriptionText(Question q) {
        StringBuilder sb = new StringBuilder();
        if (q.getDescription() != null && !q.getDescription().isBlank()) {
            sb.append(q.getDescription());
        }
        ArrayList<Section> sections = q.getQuestionContent();
        if (sections != null) {
            for (Section s : sections) {
                if (s.getSectionTitle() != null && !s.getSectionTitle().isBlank()) {
                    sb.append("\n\n").append(s.getSectionTitle());
                }
                if (s.getSectionText() != null && !s.getSectionText().isBlank()) {
                    sb.append("\n").append(s.getSectionText());
                }
                if (s.getSectionContent() != null) {
                    for (String line : s.getSectionContent()) {
                        sb.append("\n").append(line);
                    }
                }
            }
        }
        return sb.toString();
    }

    private void updateDifficultyBadge(Difficulty difficulty) {
        if (difficulty == null) {
            difficultyBadge.setText("N/A");
            difficultyBadge.getStyleClass().setAll("diff-badge");
            return;
        }
        switch (difficulty) {
            case EASY:
                difficultyBadge.setText("Easy");
                difficultyBadge.getStyleClass().setAll("diff-badge", "diff-easy");
                break;
            case MEDIUM:
                difficultyBadge.setText("Medium");
                difficultyBadge.getStyleClass().setAll("diff-badge", "diff-medium");
                break;
            case DIFFICULT:
                difficultyBadge.setText("Hard");
                difficultyBadge.getStyleClass().setAll("diff-badge", "diff-hard");
                break;
            default:
                difficultyBadge.setText("N/A");
                difficultyBadge.getStyleClass().setAll("diff-badge");
        }
    }

    private void populateSolutions(Question q) {
        solutionsContainer.getChildren().clear();

        ArrayList<UserSolution> solutions = q.getSolutionList();
        if (solutions == null || solutions.isEmpty()) {
            Label empty = new Label("No community solutions yet. Be the first to post one!");
            empty.getStyleClass().add("no-solutions-label");
            empty.setWrapText(true);
            solutionsContainer.getChildren().add(empty);
            return;
        }

        for (UserSolution solution : solutions) {
            solutionsContainer.getChildren().add(buildSolutionCard(solution));
        }
    }

    private VBox buildSolutionCard(UserSolution solution) {
        UUID solId = solution.getSoulutionId();
        voteState.putIfAbsent(solId, 0);

        VBox card = new VBox(0);
        card.getStyleClass().add("solution-card");

        VBox codeBox = new VBox();
        codeBox.getStyleClass().add("solution-code-box");
        codeBox.setPadding(new Insets(14, 16, 14, 16));
        Label codeLabel = new Label(solution.getDescription() != null ? solution.getDescription() : "");
        codeLabel.setWrapText(true);
        codeLabel.getStyleClass().add("solution-code-text");
        codeBox.getChildren().add(codeLabel);

        VBox bottom = new VBox(10);
        bottom.setPadding(new Insets(12, 14, 12, 14));

        String authorName = (solution.getUser() != null
                && solution.getUser().getUsername() != null
                && !solution.getUser().getUsername().isBlank())
                        ? solution.getUser().getUsername()
                        : "Anonymous";

        HBox userRow = new HBox(10);
        userRow.setAlignment(Pos.CENTER_LEFT);
        Circle avatar = new Circle(14);
        avatar.getStyleClass().add("nav-avatar");
        Label usernameLabel = new Label(authorName);
        usernameLabel.getStyleClass().add("solution-username");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button thumbUp = new Button("▲  " + solution.getTotalVote());
        thumbUp.getStyleClass().add("vote-btn");

        Button thumbDown = new Button("▼");
        thumbDown.getStyleClass().add("vote-btn");

        if (voteState.get(solId) == 1) {
            thumbUp.getStyleClass().add("vote-btn-active");
        } else if (voteState.get(solId) == -1) {
            thumbDown.getStyleClass().add("vote-btn-active");
        }

        thumbUp.setOnAction(e -> {
            int current = voteState.get(solId);
            if (current == 0) {
                solution.totalVote++;
                voteState.put(solId, 1);
            } else if (current == 1) {
                solution.totalVote--;
                voteState.put(solId, 0);
            } else {
                solution.totalVote += 2;
                voteState.put(solId, 1);
            }
            thumbUp.setText("▲  " + solution.getTotalVote());
            refreshVoteStyles(thumbUp, thumbDown, voteState.get(solId));
            QuestionList.getInstance().saveAll();
        });

        thumbDown.setOnAction(e -> {
            int current = voteState.get(solId);
            if (current == 0) {
                solution.totalVote--;
                voteState.put(solId, -1);
            } else if (current == -1) {
                solution.totalVote++;
                voteState.put(solId, 0);
            } else {
                solution.totalVote -= 2;
                voteState.put(solId, -1);
            }
            thumbUp.setText("▲  " + solution.getTotalVote());
            refreshVoteStyles(thumbUp, thumbDown, voteState.get(solId));
            QuestionList.getInstance().saveAll();
        });

        userRow.getChildren().addAll(avatar, usernameLabel, spacer, thumbUp, thumbDown);

        HBox replyRow = new HBox(8);
        replyRow.setAlignment(Pos.CENTER_LEFT);
        TextField replyField = new TextField();
        replyField.setPromptText("Add a comment...");
        replyField.getStyleClass().add("reply-field");
        HBox.setHgrow(replyField, Priority.ALWAYS);
        Button replyBtn = new Button("Post");
        replyBtn.getStyleClass().add("reply-btn");

        VBox commentsSection = new VBox(6);
        commentsSection.getStyleClass().add("comments-section");
        buildCommentsList(solution, commentsSection);

        replyBtn.setOnAction(e -> {
            String text = replyField.getText().trim();
            if (text.isEmpty() || App.currentUser == null)
                return;
            solution.getReplies().add(new Comment(App.currentUser, text));
            QuestionList.getInstance().saveAll();
            replyField.clear();
            buildCommentsList(solution, commentsSection);
        });

        replyRow.getChildren().addAll(replyField, replyBtn);

        bottom.getChildren().addAll(userRow, replyRow, commentsSection);
        card.getChildren().addAll(codeBox, bottom);
        return card;
    }

    private void buildCommentsList(UserSolution solution, VBox commentsSection) {
        commentsSection.getChildren().clear();
        ArrayList<Comment> replies = solution.getReplies();
        if (replies == null || replies.isEmpty())
            return;
        for (Comment comment : new ArrayList<>(replies)) {
            commentsSection.getChildren().add(buildCommentRow(comment, solution, commentsSection));
        }
    }

    private HBox buildCommentRow(Comment comment, UserSolution solution, VBox commentsSection) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("comment-row");

        Circle avatar = new Circle(10);
        avatar.getStyleClass().add("nav-avatar");

        String username = (comment.getUser() != null && comment.getUser().getUsername() != null)
                ? comment.getUser().getUsername()
                : "Anonymous";

        VBox textBox = new VBox(2);
        HBox.setHgrow(textBox, Priority.ALWAYS);
        Label usernameLabel = new Label(username);
        usernameLabel.getStyleClass().add("comment-username");
        Label commentText = new Label(comment.getComment());
        commentText.setWrapText(true);
        commentText.getStyleClass().add("comment-text");
        textBox.getChildren().addAll(usernameLabel, commentText);

        row.getChildren().addAll(avatar, textBox);

        boolean isOwner = App.currentUser != null
                && comment.getUser() != null
                && App.currentUser.getUsername() != null
                && App.currentUser.getUsername().equals(comment.getUser().getUsername());

        if (isOwner) {
            Button removeBtn = new Button("Remove");
            removeBtn.getStyleClass().add("comment-remove-btn");
            removeBtn.setOnAction(e -> {
                solution.getReplies().remove(comment);
                QuestionList.getInstance().saveAll();
                buildCommentsList(solution, commentsSection);
            });
            row.getChildren().add(removeBtn);
        }

        return row;
    }

    private void refreshVoteStyles(Button thumbUp, Button thumbDown, int state) {
        thumbUp.getStyleClass().remove("vote-btn-active");
        thumbDown.getStyleClass().remove("vote-btn-active");
        if (state == 1)
            thumbUp.getStyleClass().add("vote-btn-active");
        else if (state == -1)
            thumbDown.getStyleClass().add("vote-btn-active");
    }

    @FXML
    private void postSolution() {
        String text = (solutionInput == null) ? "" : solutionInput.getText().trim();
        if (text.isEmpty() || App.currentQuestion == null)
            return;

        UserSolution newSolution = new UserSolution(App.currentUser, text);
        App.currentQuestion.getSolutionList().add(newSolution);

        solutionInput.clear();

        int count = App.currentQuestion.getSolutionList().size();
        answersLabel.setText(count + (count == 1 ? " Answer" : " Answers"));
        populateSolutions(App.currentQuestion);
    }

    @FXML
    private void goBack() {
        try {
            App.setRoot("dashboard");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goToCommunity() {
        try {
            App.setRoot("leaderboard");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goToQuestions() throws IOException {
        App.setRoot("dashboard");
    }

    @FXML
    private void goToHome() {
        try {
            App.setRoot("userpage");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goToProfile() {
        try {
            App.setRoot("profile");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
