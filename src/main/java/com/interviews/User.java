package com.interviews;

import java.util.ArrayList;
import java.util.UUID;

public class User {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private int graduationYear;
    private String idUSC;
    private UUID id;
    private Status status;
    private ArrayList<Question> starredQuestions;
    private ArrayList<Question> answeredQuestions;
    private ArrayList<Achievement> achievements;


    // Since this is the overall Constructor check if we should add these 
    // int graduationYear 
    // String idUSC
    public User(UUID id, String firstName, String lastName, String username, String password,  
                    String email, ArrayList<Question> starredQuestions, ArrayList<Question> answeredQuestions, 
                    ArrayList<Achievement> achievements, Status userStatus){

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.starredQuestions = starredQuestions;
        this.answeredQuestions = answeredQuestions;
        this.achievements = achievements;
        this.userStatus = userStatus;
    }

    public User(String firstName, String lastName, String username, 
                    String password, String email, int graduationYear, String idUSC){

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.graduationYear = graduationYear;
        this.idUSC = idUSC;

    }

    public boolean isMatch(String username, String password){


    }

    public UserSolution addsolution(User user, String description, 
        ArrayList<Comment> thread){

    }

    public void removeSolution(User user, String description,
         ArrayList<Comment> thread){


         }

    public UserSolution getSolution(){

    }

    public void starQuestion(){


    }

    public ArrayList<Question> getStarredQuestionList(){


    }

    public Question getQuestion(){


    }

    public Comment addComment(User user, String comment){


    }

    public void removeComment(User user, String comment){


    }

    public Status getStatus(){
        return this.status;
    }

    public boolean setStatus(boolean Status){

    }



}
