package com.interviews;

import java.util.ArrayList;

public class Contributor extends User{

    private ArrayList<Question> postedQuestions;

    public Contributor(String firstName, String lastName, String username, 
                       String password, String email, int graduationYear, String idUSC){
        super(firstName, lastName, username, password, email, graduationYear, idUSC);

        this.postedQuestions = new ArrayList<>(); // Should I be initializing this?? 
    }

    public Question addQuestion(String title, User user, String description,
                        ArrayList<Section> quesitonContent, ArrayList<String> hints, 
                        Difficulty difficulty, Language questionLanguage){

            Question question = new Question(title, user, description, difficulty, questionLanguage, hints, quesitonContent);
            postedQuestions.add(question); // Adds question to the posted questions list
            return question;
    }

    public Question editQuestion(UUID id, String title, User user, String description, 
        ArrayList<Section> questionContent, ArrayList<String> hints, 
        Difficulty difficulty, Language questionLanguage){

            if(question.getID().equals(id)){
                question.setTitle(title);
                question.setDescription(description);
                question.setUser(user);
                question.setLanguage(questionLanguage);
                question.setDifficulty(difficulty);
                question.setHint(hints);
            }
    }

    public void removeQuestion(UUID id){
        for(Question question : questions){
            if(question.getId.equals(id)){
                postedQuestions.remove(question);
                QuestionList.getInstance().deleteQuestion(question);
                return;
            }
        }
    }
    
}
