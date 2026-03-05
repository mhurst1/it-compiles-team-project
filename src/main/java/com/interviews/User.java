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

    public User(UUID id, String firstName, String lastName, String username, String password,  
                    String email, ArrayList<Question> starredQuestions, ArrayList<Question> answeredQuestions, 
                    ArrayList<Achievement> achievements, Status status, int graduationYear, String idUSC){

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.starredQuestions = starredQuestions;
        this.answeredQuestions = answeredQuestions;
        this.achievements = achievements;
        this.status = status;
        this.graduationYear = graduationYear;
        this.idUSC = idUSC;
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
        return(this.username.equals(username)&&this.password.equals(password));
    }

    public UserSolution addsolution(User user, String description, 
        ArrayList<Comment> thread){
            UserSolution solution = new UserSolution(user, description);
            solution.addSolution(user, description, thread);
            return solution;

    }

    public void removeSolution(User user, String description,
         ArrayList<Comment> thread){
            UserSolution solution = getSolution();
            if(solution != null){
                solution.removeSolution(user, description, thread);
            }
         }

    public void starQuestion(){
        if(!starredQuestions.contains(question)){
            starredQuestions.add(question);
        }
    }

    public ArrayList<Question> getStarredQuestionList(){
        return starredQuestions;
    }

    public Comment addComment(User user, String comment){
        Comment newComment = new Comment(user, comment, new ArrayList<>());
        return newComment;
    }

    public void removeComment(String comment){

    }

    public Status getStatus(){
        return this.status;
    }

    public boolean setStatus(boolean Status){
        this.status = status;
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

    public int getGraduationYear(){
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