package com.controllers;

import java.util.Locale;

import com.interviews.Difficulty;
import com.interviews.Status;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * Controller for the admin dashboard view.
 */
public class AdminDashboardController {

    @FXML
    private Label profileInitialsLabel;

    @FXML
    private Label totalUsersLabel;

    @FXML
    private Label totalUsersTrendLabel;

    @FXML
    private Label questionsCountLabel;

    @FXML
    private Label questionsTrendLabel;

    @FXML
    private Label upvotesCountLabel;

    @FXML
    private Label upvotesTrendLabel;

    @FXML
    private Label solutionsCountLabel;

    @FXML
    private Label solutionsTrendLabel;

    @FXML
    private TableView<UserRow> userTable;

    @FXML
    private TableColumn<UserRow, String> usernameColumn;

    @FXML
    private TableColumn<UserRow, Status> roleColumn;

    @FXML
    private TableColumn<UserRow, String> solvedColumn;

    @FXML
    private TableColumn<UserRow, String> votesColumn;

    @FXML
    private TableColumn<UserRow, UserRow> userActionsColumn;

    @FXML
    private TableView<QuestionRow> questionTable;

    @FXML
    private TableColumn<QuestionRow, String> questionTitleColumn;

    @FXML
    private TableColumn<QuestionRow, Difficulty> difficultyColumn;

    @FXML
    private TableColumn<QuestionRow, String> attemptsColumn;

    @FXML
    private TableColumn<QuestionRow, String> authorColumn;

    @FXML
    private TableColumn<QuestionRow, QuestionRow> questionActionsColumn;

    @FXML
    private TableView<LeaderboardRow> leaderboardTable;

    @FXML
    private TableColumn<LeaderboardRow, String> rankColumn;

    @FXML
    private TableColumn<LeaderboardRow, String> leaderboardUsernameColumn;

    @FXML
    private TableColumn<LeaderboardRow, Status> leaderboardRoleColumn;

    @FXML
    private TableColumn<LeaderboardRow, String> leaderboardUpvotesColumn;

    @FXML
    private TableView<PendingReviewRow> pendingReviewTable;

    @FXML
    private TableColumn<PendingReviewRow, String> reviewTitleColumn;

    @FXML
    private TableColumn<PendingReviewRow, String> submittedByColumn;

    @FXML
    private TableColumn<PendingReviewRow, PendingReviewRow> reviewActionsColumn;

    @FXML
    private void initialize() {
        configureSummaryCards();
        configureUserTable();
        configureQuestionTable();
        configureLeaderboardTable();
        configurePendingReviewTable();
        loadSampleData();
    }

    private void configureSummaryCards() {
        totalUsersLabel.setText("248");
        totalUsersTrendLabel.setText("\u2191 12 this week");
        questionsCountLabel.setText("128");
        questionsTrendLabel.setText("\u2191 5 new");
        upvotesCountLabel.setText("5,240");
        upvotesTrendLabel.setText("\u2191 312 this week");
        solutionsCountLabel.setText("3,816");
        solutionsTrendLabel.setText("\u2191 204 this week");
        profileInitialsLabel.setText("AD");
    }

    private void configureUserTable() {
        usernameColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getUsername()));
        roleColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getRole()));
        solvedColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getSolvedDisplay()));
        votesColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getVotesDisplay()));
        userActionsColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue()));

        roleColumn.setCellFactory(column -> createTagCell());
        userActionsColumn.setCellFactory(column -> new TableCell<UserRow, UserRow>() {
            @Override
            protected void updateItem(UserRow item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }

                Button editButton = createActionButton("Edit", "#175CD3", "#EFF8FF", event ->
                    showActionMessage("Edit User", "Editing user " + item.getUsername())
                );
                Button removeButton = createActionButton("Remove", "#B42318", "#FEF3F2", event ->
                    showActionMessage("Remove User", "Removing user " + item.getUsername())
                );

                HBox actionBox = new HBox(8, editButton, removeButton);
                setGraphic(actionBox);
            }
        });

        alignTableColumns(usernameColumn, solvedColumn, votesColumn);
    }

    private void configureQuestionTable() {
        questionTitleColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getTitle()));
        difficultyColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getDifficulty()));
        attemptsColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAttemptsDisplay()));
        authorColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAuthor()));
        questionActionsColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue()));

        difficultyColumn.setCellFactory(column -> createTagCell());
        questionActionsColumn.setCellFactory(column -> new TableCell<QuestionRow, QuestionRow>() {
            @Override
            protected void updateItem(QuestionRow item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }

                Button editButton = createActionButton("Edit", "#175CD3", "#EFF8FF", event ->
                    showActionMessage("Edit Question", "Editing question " + item.getTitle())
                );
                Button removeButton = createActionButton("Remove", "#B42318", "#FEF3F2", event ->
                    showActionMessage("Remove Question", "Removing question " + item.getTitle())
                );

                HBox actionBox = new HBox(8, editButton, removeButton);
                setGraphic(actionBox);
            }
        });

        alignTableColumns(questionTitleColumn, attemptsColumn, authorColumn);
    }

    private void configureLeaderboardTable() {
        rankColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getRank()));
        leaderboardUsernameColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getUsername()));
        leaderboardRoleColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getRole()));
        leaderboardUpvotesColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getUpvotesDisplay()));

        leaderboardRoleColumn.setCellFactory(column -> createTagCell());
        alignTableColumns(rankColumn, leaderboardUsernameColumn, leaderboardUpvotesColumn);
    }

    private void configurePendingReviewTable() {
        reviewTitleColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getTitle()));
        submittedByColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getSubmittedBy()));
        reviewActionsColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue()));

        reviewActionsColumn.setCellFactory(column -> new TableCell<PendingReviewRow, PendingReviewRow>() {
            @Override
            protected void updateItem(PendingReviewRow item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }

                Button approveButton = createActionButton("Approve", "#027A48", "#ECFDF3", event ->
                    showActionMessage("Approve Review", "Approved \"" + item.getTitle() + "\"")
                );
                Button rejectButton = createActionButton("Reject", "#B42318", "#FEF3F2", event ->
                    showActionMessage("Reject Review", "Rejected \"" + item.getTitle() + "\"")
                );

                HBox actionBox = new HBox(8, approveButton, rejectButton);
                setGraphic(actionBox);
            }
        });

        alignTableColumns(reviewTitleColumn, submittedByColumn);
    }

    private void loadSampleData() {
        userTable.setItems(FXCollections.observableArrayList(
            new UserRow("jimmy", Status.USER, 77, 156)
        ));

        questionTable.setItems(FXCollections.observableArrayList(
            new QuestionRow("Two Sum", Difficulty.EASY, 39, "john_dev")
        ));

        leaderboardTable.setItems(FXCollections.observableArrayList(
            new LeaderboardRow("#1", "alex_l", Status.ADMIN, 621)
        ));

        pendingReviewTable.setItems(FXCollections.observableArrayList(
            new PendingReviewRow("Design a Rate Limiter", "jimmy")
        ));
    }

    @FXML
    private void handleHomeNav() {
        showActionMessage("Navigation", "Home tab selected.");
    }

    @FXML
    private void handleQuestionsNav() {
        showActionMessage("Navigation", "Questions tab selected.");
    }

    @FXML
    private void handleCommunityNav() {
        showActionMessage("Navigation", "Community tab selected.");
    }

    @FXML
    private void handleAddUser() {
        showActionMessage("Add User", "Add user action will be connected to form or ArrayList workflow later.");
    }

    @FXML
    private void handleAddQuestion() {
        showActionMessage("Add Question", "Add question action will be connected to form or ArrayList workflow later.");
    }

    private <S, T> TableCell<S, T> createTagCell() {
        return new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                Label tag = new Label(formatTagText(item));
                tag.setStyle(resolveTagStyle(item));
                tag.setTextFill(Color.web(resolveTagTextColor(item)));
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(tag);
            }
        };
    }

    private String formatTagText(Object item) {
        if (item instanceof Difficulty) {
            Difficulty difficulty = (Difficulty) item;
            String lower = difficulty.name().toLowerCase(Locale.ROOT);
            return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
        }
        return item.toString();
    }

    private String resolveTagStyle(Object item) {
        return "-fx-background-color: " + resolveTagBackground(item)
            + "; -fx-background-radius: 999; -fx-padding: 6 12 6 12; -fx-font-size: 11px; -fx-font-weight: bold;";
    }

    private String resolveTagBackground(Object item) {
        if (item == Status.ADMIN) {
            return "#EEF4FF";
        }
        if (item == Status.CONTRIBUTOR) {
            return "#EFF8FF";
        }
        if (item == Status.USER) {
            return "#F2F4F7";
        }
        if (item == Difficulty.EASY) {
            return "#ECFDF3";
        }
        if (item == Difficulty.MEDIUM) {
            return "#FFF7ED";
        }
        if (item == Difficulty.DIFFICULT) {
            return "#FEF3F2";
        }
        return "#F2F4F7";
    }

    private String resolveTagTextColor(Object item) {
        if (item == Status.ADMIN) {
            return "#3538CD";
        }
        if (item == Status.CONTRIBUTOR) {
            return "#175CD3";
        }
        if (item == Status.USER) {
            return "#344054";
        }
        if (item == Difficulty.EASY) {
            return "#027A48";
        }
        if (item == Difficulty.MEDIUM) {
            return "#B54708";
        }
        if (item == Difficulty.DIFFICULT) {
            return "#B42318";
        }
        return "#344054";
    }

    private Button createActionButton(String text, String textColor, String backgroundColor,
            javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setOnAction(handler);
        button.setStyle("-fx-background-color: " + backgroundColor
            + "; -fx-background-radius: 999; -fx-text-fill: " + textColor
            + "; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 6 12 6 12; -fx-cursor: hand;");
        return button;
    }

    @SafeVarargs
    private final <S> void alignTableColumns(TableColumn<S, ?>... columns) {
        for (TableColumn<S, ?> column : columns) {
            column.setStyle("-fx-alignment: CENTER-LEFT;");
        }
    }

    private void showActionMessage(String title, String message) {
        System.out.println(title + ": " + message);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public static class UserRow {
        private final String username;
        private final Status role;
        private final int solvedCount;
        private final int votes;

        public UserRow(String username, Status role, int solvedCount, int votes) {
            this.username = username;
            this.role = role;
            this.solvedCount = solvedCount;
            this.votes = votes;
        }

        public String getUsername() {
            return username;
        }

        public Status getRole() {
            return role;
        }

        public String getSolvedDisplay() {
            return solvedCount + " solved";
        }

        public String getVotesDisplay() {
            return String.valueOf(votes);
        }
    }

    public static class QuestionRow {
        private final String title;
        private final Difficulty difficulty;
        private final int attempts;
        private final String author;

        public QuestionRow(String title, Difficulty difficulty, int attempts, String author) {
            this.title = title;
            this.difficulty = difficulty;
            this.attempts = attempts;
            this.author = author;
        }

        public String getTitle() {
            return title;
        }

        public Difficulty getDifficulty() {
            return difficulty;
        }

        public String getAttemptsDisplay() {
            return String.valueOf(attempts);
        }

        public String getAuthor() {
            return author;
        }
    }

    public static class LeaderboardRow {
        private final String rank;
        private final String username;
        private final Status role;
        private final int upvotes;

        public LeaderboardRow(String rank, String username, Status role, int upvotes) {
            this.rank = rank;
            this.username = username;
            this.role = role;
            this.upvotes = upvotes;
        }

        public String getRank() {
            return rank;
        }

        public String getUsername() {
            return username;
        }

        public Status getRole() {
            return role;
        }

        public String getUpvotesDisplay() {
            return String.valueOf(upvotes);
        }
    }

    public static class PendingReviewRow {
        private final String title;
        private final String submittedBy;

        public PendingReviewRow(String title, String submittedBy) {
            this.title = title;
            this.submittedBy = submittedBy;
        }

        public String getTitle() {
            return title;
        }

        public String getSubmittedBy() {
            return submittedBy;
        }
    }
}
