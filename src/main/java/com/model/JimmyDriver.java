package com.model;

import com.interviews.Difficulty;
import com.interviews.Language;
import com.interviews.QuestionApplication;


public class JimmyDriver {

    private QuestionApplication questionApplication;

    public JimmyDriver(){
        questionApplication = new QuestionApplication();
    }

    public void run(){
        scenario();
    }

    public void scenario(){

        questionApplication.login("jimmysmith", "imthegoat5");

        if (questionApplication.getCurrentUser() == null) {
            System.out.println("Sorry we couldn't login.");
            return;
        }

        System.out.println("jimmysmith has been successfully logged in");

        //streak 
        System.out.println(questionApplication.getCurrentQuestion().getSolutionList());

        //pick a solution
        //create a user?
        questionApplication.getCurrentUserSolution().addComment("I'm a little confused how you got this", getCurrentUser(), String comment)

    }
}
