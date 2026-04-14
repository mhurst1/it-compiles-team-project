package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class AddQuestionController {

    @FXML private TextField txt_title;
    @FXML private TextField txt_section;
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

    // tracks how many hint rows are currently visible (1 or 2 are always shown)
    private int visibleHints = 2;

    @FXML
    public void initialize() {
        cb_difficulty.getItems().addAll("Easy", "Medium", "Hard");
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
        feedbackLabel.setText("Draft saved.");
        feedbackLabel.setStyle("-fx-text-fill: #7734ED;");
    }

    @FXML
    private void submitQuestion() {
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
    private void addAnswer() {
        // TODO: open answer entry panel
    }

    @FXML
    private void goToDashboard() {
        // TODO: navigate back to admin dashboard
    }
}
