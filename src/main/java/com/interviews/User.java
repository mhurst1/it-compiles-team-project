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

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String firstName){
        this.lastName = lastName;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getIdUSC(){
        return idUSC;
    }

    public void setIdUSC(String idUSC){
        this.idUSC = idUSC;
    }

    public int getGraudationYear(){
        return graduationYear;
    }

    public void setGraudationYear(int graduationYear){
        this.graduationYear = graduationYear;
    }
    
    public UUID getId(){
        return id;
    }

    public void setId(UUID id){
        this.id = id;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public ArrayList<Question> getStarredQuestions(){
        return starredQuestions;
    }

    public void setStarredQuestions(ArrayList<Question> starredQuestions){
        this.starredQuestions = starredQuestions;
    }

    public ArrayList<Question> getAnsweredQuestions(){
        return answeredQuestions;
    }

    public void setAnsweredQuestions(ArrayList<Question> answeredQuestions){
        this.answeredQuestions = answeredQuestions;
    }

    public ArrayList<Achievement> getAchievements(){
        return answeredQuestions;
    }

    public void setAchievements(ArrayList<Achievement> achievements){
        this.achievements = achievements;
    }


}