package com.interviews;

import java.util.ArrayList;

public class QuestionList {
    private static QuestionList questionList;
    private ArrayList<Question> questions;

    private QuestionList() {
        questions = new ArrayList<>();
    }

    public static Question getInstance() {
        if (questionList == null) {
            questionList = new QuestionList();
        }
        return questionList;
    }

    public ArrayList<Question> getQuestions(String keyword) {
        ArrayList<Question> result = new ArrayList<>();
        for (Question question : questions) {
            if (question.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(question);
            }
        }
        return result;
    }

    public void addQuestion(String title, User user, String description, ArrayList<Section> questionContent, ArrayList<String> hints, Difficulty difficulty, Language questionLanguage) {
        Question question = new Question(title, user, description, questionContent, hints, difficulty, questionLanguage);
        questions.add(question);
    }

    public void deleteQuestion(Question question) {
        questions.remove(question);
    }

    public void getQuestion(Question questions) {
        questions.getTitle();
        questions.getDescription();
        questions.getQuestionContent();
        questions.getHints();
        questions.getDifficulty();
        questions.getQuestionLanguage();
        questions.getUser();
        questions.getId();
    }

    public void save() {
        
    }
}
