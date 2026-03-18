package com.interviews;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A class that is like the regular User but can add Questions
 */
public class Contributor extends User{

    private ArrayList<Question> postedQuestions;

    public Contributor(String firstName, String lastName, String username, 
                       String password, String email, int graduationYear, String idUSC){
        super(firstName, lastName, username, password, email, graduationYear, idUSC);

        this.postedQuestions = new ArrayList<>(); // Should I be initializing this?? 
    }

    /**
     * Method that adds a user created question 
     * @param title the title of the question
     * @param user the user who created the question
     * @param description the decription associated with the question
     * @param questionContent the content in the question
     * @param hints the hints given to the user 
     * @param difficulty the difficulty of the question
     * @param questionLanguage the language the question is written in
     * @return the newly created question
     */
    public Question addQuestion(String title, User user, String description,
                        ArrayList<Section> questionContent, ArrayList<String> hints, 
                        Difficulty difficulty, Language questionLanguage){

            Question question = new Question(title, user, description, difficulty, questionLanguage, hints, questionContent);
            postedQuestions.add(question); // Adds question to the posted questions list
            return question;
    }

    /**
     * A method to update/edit the currently selected question
     * @param id the questions unique id
     * @param title the title of the question
     * @param user the user who created the question
     * @param description the decription associated with the question
     * @param questionContent the content in the question
     * @param hints the hints given to the user 
     * @param difficulty the difficulty of the question
     * @param questionLanguage the language the question is written in
     * @return the newly updated question
     */
    public Question editQuestion(UUID id, String title, User user, String description, 
        ArrayList<Section> quesionContent, ArrayList<String> hints, 
        Difficulty difficulty, Language questionLanguage){

            for(Question question : postedQuestions){
                if(question.getId().equals(id)){
                    question.setTitle(title);
                    question.setDescription(description);
                    question.setUser(user);
                    question.setLanguage(questionLanguage);
                    question.setDifficulty(difficulty);
                    question.setHint(hints);
                }
            }
        return null;
    }

    /**
     * A method to delete the current question
     * @param id the unique id of the current question
     */
    public void removeQuestion(UUID id){
        for(int i = 0; i < postedQuestions.size(); i++){
            Question question = postedQuestions.get(i);
            if(question.getId().equals(id)){
                postedQuestions.remove(question);
                QuestionList.getInstance().deleteQuestion(question);
                return;
            }
        }
    }
    
}
