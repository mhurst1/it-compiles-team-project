package com.controllers;

import com.interviews.Status;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import com.interviews.App;
import com.interviews.DataLoader;
import com.interviews.DataWriter;
import com.interviews.Difficulty;
import com.interviews.Language;
import com.interviews.Question;
import com.interviews.QuestionList;
import com.interviews.Section;

public class AddQuestionController {

    @FXML private Button adminDashboardButton;
    @FXML private Label navAvatarLetter;

    @FXML private TextField txt_title;
    @FXML private ComboBox<String> cb_language;
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
    @FXML private Label formTitle;
    @FXML private VBox formCard;
    @FXML private Button saveDraftBtn;
    @FXML private Button submitBtn;

    private File selectedAttachment;
    private Question questionToEdit;

    private int visibleHints = 2;

    @FXML
    public void initialize() {
        questionToEdit = App.editingQuestion;
        App.editingQuestion = null;

        if (App.currentUser != null && App.currentUser.getFirstName() != null && !App.currentUser.getFirstName().isEmpty()) {
            String firstLetter = App.currentUser.getFirstName().substring(0, 1).toUpperCase();
            navAvatarLetter.setText(firstLetter);
        } else {
            navAvatarLetter.setText("U");
        }

        cb_language.getItems().addAll(
            "JAVA",
            "JAVASCRIPT",
            "PYTHON",
            "HTML",
            "CSS",
            "LINUX",
            "UNKNOWN"
        );
        cb_difficulty.getItems().addAll("Easy", "Medium", "Hard");

        if (App.currentUser != null) {
            welcomeLabel.setText(App.currentUser.getFirstName());
        } else {
            welcomeLabel.setText("Unknown User");
        }

        if (App.currentUser == null || App.currentUser.getStatus() != Status.ADMIN) {
            adminDashboardButton.setVisible(false);
            adminDashboardButton.setManaged(false);
        }

        if (App.currentUser == null || !App.currentUser.canModifyQuestions()) {
            formCard.setDisable(true);
            feedbackLabel.setText("Access denied: only contributors can add questions.");
            feedbackLabel.setStyle("-fx-text-fill: #e53e3e;");
        }

        if (questionToEdit != null) {
            formTitle.setText("Edit Question");
            submitBtn.setText("Update");

            txt_title.setText(questionToEdit.getTitle() != null ? questionToEdit.getTitle() : "");
            txt_description.setText(questionToEdit.getDescription() != null ? questionToEdit.getDescription() : "");

            if (questionToEdit.getDifficulty() != null) {
                switch (questionToEdit.getDifficulty()) {
                    case EASY: cb_difficulty.setValue("Easy"); break;
                    case MEDIUM: cb_difficulty.setValue("Medium"); break;
                    case HARD: cb_difficulty.setValue("Hard"); break;
                }
            }

            if (questionToEdit.getLanguage() != null) {
                cb_language.setValue(questionToEdit.getLanguage().name());
            }

            ArrayList<String> hints = questionToEdit.getHints();
            if (hints != null) {
                if (hints.size() >= 1) txt_hint1.setText(hints.get(0));
                if (hints.size() >= 2) txt_hint2.setText(hints.get(1));
                if (hints.size() >= 3) {
                    txt_hint3.setText(hints.get(2));
                    hint3Row.setVisible(true);
                    hint3Row.setManaged(true);
                    visibleHints = 3;
                    addHintBtn.setVisible(false);
                    addHintBtn.setManaged(false);
                }
            }
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
    private void addAttachment() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Attachment");
        fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("All Files", "*.*"),
            new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
            new ExtensionFilter("PDF Files", "*.pdf"),
            new ExtensionFilter("Text Files", "*.txt", "*.md")
        );
        javafx.stage.Window window = (formCard.getScene() != null) ? formCard.getScene().getWindow() : null;
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            selectedAttachment = file;
            attachmentLabel.setText(file.getName());
            attachmentLabel.setStyle("-fx-text-fill: #7734ED;");
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
    private void goToDashboard() {
        try {
            App.setRoot("dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToProfile() {
        try {
            if (App.currentUser != null) {
                App.setRoot("login");
            } else {
                App.setRoot("profile");
            }
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

  private void clearForm() {
    txt_title.clear();
    txt_description.clear();

    txt_hint1.clear();
    txt_hint2.clear();
    txt_hint3.clear();

    cb_language.setValue(null);
    cb_difficulty.setValue(null);

    hint3Row.setVisible(false);
    hint3Row.setManaged(false);
    visibleHints = 2;

    addHintBtn.setVisible(true);
    addHintBtn.setManaged(true);

    selectedAttachment = null;
    attachmentLabel.setText("No file chosen");
    attachmentLabel.setStyle("-fx-text-fill: #6b7280;");
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

        if (cb_language.getValue() == null) {
            feedbackLabel.setText("Please select a language.");
            feedbackLabel.setStyle("-fx-text-fill: #e53e3e;");
            return;
        }

        Difficulty difficulty = Difficulty.valueOf(cb_difficulty.getValue().toUpperCase());
        Language language = Language.valueOf(cb_language.getValue().toUpperCase());

        ArrayList<String> hints = new ArrayList<>();
        if (!txt_hint1.getText().isBlank()) hints.add(txt_hint1.getText().trim());
        if (!txt_hint2.getText().isBlank()) hints.add(txt_hint2.getText().trim());
        if (!txt_hint3.getText().isBlank()) hints.add(txt_hint3.getText().trim());

        ArrayList<Section> sections = new ArrayList<>();
        ArrayList<String> sectionContent = new ArrayList<>();
        sectionContent.add(txt_description.getText().trim());
        sections.add(new Section("Description", sectionContent, txt_description.getText().trim()));

        if (questionToEdit != null) {
            questionToEdit.setTitle(txt_title.getText().trim());
            questionToEdit.setDescription(txt_description.getText().trim());
            questionToEdit.setDifficulty(difficulty);
            questionToEdit.setLanguage(language);
            questionToEdit.setHint(hints);
            questionToEdit.setQuestionContent(sections);
        } else {
            Question q = new Question(
                txt_title.getText().trim(),
                App.currentUser,
                txt_description.getText().trim(),
                difficulty,
                language,
                hints,
                sections
            );
            QuestionList.getInstance().getQuestions().add(q);
        }

        boolean saved = DataWriter.saveQuestions(QuestionList.getInstance().getQuestions());
        if (saved) {
            feedbackLabel.setText(questionToEdit != null ? "Question updated!" : "Question submitted!");
            feedbackLabel.setStyle("-fx-text-fill: #22c55e;");
            questionToEdit = null;
            clearForm();
            formTitle.setText("Add New Question");
            submitBtn.setText("Submit");
        } else {
            feedbackLabel.setText("Could not save question.");
            feedbackLabel.setStyle("-fx-text-fill: #e53e3e;");
        }
    }
}
