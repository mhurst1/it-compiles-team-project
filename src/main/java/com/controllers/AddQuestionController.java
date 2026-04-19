package com.controllers;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import com.interviews.App;

public class AddQuestionController {

    @FXML private TextField txt_title;
    @FXML private ComboBox<String> cb_section;
    @FXML private ComboBox<String> cb_difficulty;
    @FXML private TextArea txt_description;
    @FXML private TextField txt_hint1;
    @FXML private TextField txt_hint2;
    @FXML private TextField txt_hint3;
    @FXML private HBox hint3Row;
    @FXML private Button addHintBtn;
    @FXML private TableView<?> publishedTable;
    @FXML private TableColumn<?, ?> col_title;
    @FXML private TableColumn<?, ?> col_section;
    @FXML private TableColumn<?, ?> col_difficulty;
    @FXML private TableColumn<?, ?> col_status;
    @FXML private TableColumn<?, ?> col_actions;
    @FXML private Label feedbackLabel;
    @FXML private Label attachmentLabel;
    @FXML private Label welcomeLabel;
    @FXML private VBox formCard;
    @FXML private Button saveDraftBtn;
    @FXML private Button submitBtn;

    private File selectedAttachment;

    private int visibleHints = 2;

    @FXML
    public void initialize() {
        cb_section.getItems().addAll(
            "Arrays & Strings",
            "Trees & Graphs",
            "Dynamic Programming",
            "Searching & Sorting",
            "Stacks & Queues",
            "Linked Lists",
            "System Design",
            "OOP Concepts",
            "Databases"
        );
        cb_difficulty.getItems().addAll("Easy", "Medium", "Hard");

        if (App.currentUser != null) {
            welcomeLabel.setText(App.currentUser.getFirstName());
        }

        if (App.currentUser == null || !App.currentUser.canModifyQuestions()) {
            formCard.setDisable(true);
            feedbackLabel.setText("Access denied: only contributors can add questions.");
            feedbackLabel.setStyle("-fx-text-fill: #e53e3e;");
        }
    }

    @FXML
    private void addHint() {
        if (visibleHints < 3) {
            hint3Row.setVisible(true);
            hint3Row.setManaged(true);
            visibleHints = 3;
            addHintBtn.setVisible(false);
            addHintBtn.setManaged(false);
        }
    }

    @FXML
    private void removeHint1() {
        txt_hint1.clear();
    }

    @FXML
    private void removeHint2() {
        txt_hint2.clear();
    }

    @FXML
    private void removeHint3() {
        txt_hint3.clear();
        hint3Row.setVisible(false);
        hint3Row.setManaged(false);
        visibleHints = 2;
        addHintBtn.setVisible(true);
        addHintBtn.setManaged(true);
    }

    @FXML
    private void saveDraft() {
        if (App.currentUser == null || !App.currentUser.canModifyQuestions()) {
            feedbackLabel.setText("Access denied: only contributors can add questions.");
            feedbackLabel.setStyle("-fx-text-fill: #e53e3e;");
            return;
        }
        feedbackLabel.setText("Draft saved.");
        feedbackLabel.setStyle("-fx-text-fill: #7734ED;");
    }

    @FXML
    private void submitQuestion() {
        if (App.currentUser == null || !App.currentUser.canModifyQuestions()) {
            feedbackLabel.setText("Access denied: only contributors can add questions.");
            feedbackLabel.setStyle("-fx-text-fill: #e53e3e;");
            return;
        }
        if (txt_title.getText().isBlank()) {
            feedbackLabel.setText("Question title is required.");
            feedbackLabel.setStyle("-fx-text-fill: #e53e3e;");
            return;
        }
        if (cb_difficulty.getValue() == null) {
            feedbackLabel.setText("Please select a difficulty.");
            feedbackLabel.setStyle("-fx-text-fill: #e53e3e;");
            return;
        }
        feedbackLabel.setText("Question submitted!");
        feedbackLabel.setStyle("-fx-text-fill: #22c55e;");
    }

    @FXML
    private void addAttachment() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Attachment");
        fileChooser.getExtensionFilters().add(
            new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(formCard.getScene().getWindow());
        if (file != null) {
            selectedAttachment = file;
            attachmentLabel.setText(file.getName());
            attachmentLabel.setStyle("-fx-text-fill: #7734ED;");
        }
    }

    @FXML
    private void goToHome() {
        try {
            App.setRoot("dashboard");
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
    private void goToDashboard() {
        try {
            App.setRoot("dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
