package com.interviews;

import java.util.ArrayList;

public class Contributor extends User{

    private ArrayList<Question> postedQuestions;


    //should we have another constructor like the user class where it brings more in
    public Contributor(){
        super(null, null, null, null, null, 0, null);
    }

    public Question addQuestion(String title, User user, String description,
         ArrayList<Template> quesitonContent, ArrayList<String> hints, 
         Difficulty difficulty, Language questionLanguage){

    }

    public Question editQuestion(int id, String title, User user, 
        String description, ArrayList<Template>){

            this.id = id;
            this.title = title;
            this.user = user;
            this.difficulty = difficulty;
            this.questionLanguage = questionLanguage;

    }

    public void removeQuestion(int id){

        //I know we are doing question list array list but should we have a hashamp or something

    }



    
}
