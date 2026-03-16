package com.interviews;

import java.util.ArrayList;

public class QuestionList {
    private static QuestionList questionList;
    private ArrayList<Question> questions;

    private QuestionList() {
        questions = new ArrayList<>();
    }

    public static QuestionList getInstance() {
        if (questionList == null) {
            questionList = new QuestionList();
        }
        return questionList;
    }

    /**
     * Used as a default to retrun questions
     * @return
     */
    public ArrayList<Question> getQuestions(){
        return questions;
    }

    /**
     * ??? I am assuming this is used to search for a question
     * If it is we need to change the name
     * @param keyword
     * @return
     */
    public ArrayList<Question> getQuestions(String keyword) {
        ArrayList<Question> result = new ArrayList<>();
        for (Question question : questions) {
            if (question.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(question);
            }
        }
        return result;
    }

    public void addQuestion(String title, User user, String description, Difficulty difficulty, 
                        Language questionLang, ArrayList<String> hints, ArrayList<Section> questionContent) {
        Question question = new Question(title, user, description, difficulty, questionLang, hints, questionContent);
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
        questions.getLanguage();
        questions.getUser();
        questions.getId();
    }

    public void save() {

        DataWriter writer = new DataWriter();
        writer.saveQuestions();
        
    }
}
