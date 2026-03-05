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

    public Question editQuestion(int id, String title, User user, 
        String description, ArrayList<Section> questionContent){

        
    }

    // We will stick to an ArrayList for now with this and if we want to implement a hashmap later we can do that
    public void removeQuestion(int id){


    }
    
}
