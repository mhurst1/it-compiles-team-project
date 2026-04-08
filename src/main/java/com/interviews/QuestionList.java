package com.interviews;

import java.util.ArrayList;

/**
 * A class to manage a list of questions.
 */
public class QuestionList {
    private static QuestionList questionList;
    private ArrayList<Question> questions;

    /**
     * Initializes the QuestionList with an empty list of questions. This constructor is private to enforce the singleton pattern, ensuring that only one instance of QuestionList exists throughout the application. The getInstance() method is used to access the single instance of QuestionList, and it will create the instance if it does not already exist.
     */
    public QuestionList() {
        questions = DataLoader.getQuestions();
    }

    /**
     * Returns the single instance of QuestionList. If the instance does not already exist, it creates a new instance before returning it. This method ensures that there is only one instance of QuestionList in the application, following the singleton design pattern.
     * @return the single instance of QuestionList
     */
    public static QuestionList getInstance() {
        if (questionList == null) {
            questionList = new QuestionList();
        }
        return questionList;
    }

    /**
     * Used as a default to retrun questions
     * 
     * @return
     */
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    /**
     * Searches for questions that contain the specified keyword in their title. 
     * This method iterates through the list of questions and checks if the title of each question 
     * contains the given keyword (case-insensitive). If a question's title matches the keyword, 
     * it is added to the result list, which is then returned at the end. This allows users to 
     * search for questions based on specific keywords in their titles.
     * 
     * @param keyword
     * @return
     */
    public ArrayList<Question> getQuestions(String keyword) {
        ArrayList<Question> result = new ArrayList<>();
        if (keyword == null) {
            return result;
        }
        for (Question question : questions) {
            if (question.getTitle() == null) continue;
            if (question.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(question);
            }
        }
        return result;
    }

    /**
     * Adds a new question to the list of questions. This method takes various parameters 
     * that define the properties of the question, such as title, user, description, 
     * difficulty, language, hints, and question content. It creates a new Question object using 
     * these parameters and adds it to the questions list. This allows users to contribute new 
     * questions to the application.
      * @param title the title of the question
      * @param user the user who created the question
      * @param description the description of the question
      * @param difficulty the difficulty level of the question
      * @param questionLang the programming language associated with the question
      * @param hints a list of hints for solving the question
      * @param questionContent a list of sections that make up the content of the question
      */
    public void addQuestion(String title, User user, String description, Difficulty difficulty,
            Language questionLang, ArrayList<String> hints, ArrayList<Section> questionContent) {
        Question question = new Question(title, user, description, questionContent, hints, difficulty, questionLang);
        questions.add(question);
    }

    /**
     * Removes a question from the list of questions. This method takes a 
     * Question object as a parameter and removes it from the questions list. 
     * It also calls the deleteQuestion method to ensure that the question is 
     * properly removed from any persistent storage or related data structures. 
     * This allows users to delete questions that they have created or that are no longer relevant.
      * @param question the Question object to be removed from the list
     */
    public void deleteQuestion(Question question) {
        if (question == null || question.getId() == null) {
            return;
        }
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getId().equals(question.getId())) {
                questions.remove(i);
                System.out.println("Question " + question + " has successfully been removed.");
                return;
            }
        }
        System.err.println("There was an error removing " + question + ". Please Try Again.");
    }

    /**
     * Retrieves the details of a question. This method takes a Question object 
     * as a parameter and accesses its various properties such as title, description, 
     * content, hints, difficulty, language, user, and ID. This method can be used to 
     * display the details of a question to the user or to perform operations that 
     * require access to the question's properties. It does not return any value but 
     * allows access to the question's information through its getter methods.
      * @param questions the Question object whose details are to be retrieved
     */
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

    /**
     * Saves the current list of questions to persistent storage. This method creates an instance of DataWriter and calls its saveQuestions() method to write the current state of the questions list to a file or database. This allows the application to maintain data persistence, ensuring that any changes made to the questions list are saved and can be retrieved later when the application is restarted.
     */
    public void save() {
        DataWriter.saveQuestions(questions);
    }
}