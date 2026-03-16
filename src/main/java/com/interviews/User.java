package com.interviews;

import java.util.ArrayList;
import java.util.UUID;

import main.java.com.interviews.Achievement;

/**
 * User class that creates a user profile (like an account), storing 
 * crucial information about the user
 */
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

    /**
     * Method to see if the given username and password match 
     * @param username the users public username 
     * @param password the users password
     * @return a boolean eturns true if the username 
     * and password match the current users information
     */
    public boolean isMatch(String username, String password){
        return(this.username.equals(username)&&this.password.equals(password));
    }

    /**
     * Method so the user can add a solution
     * @param user the current user
     * @param description what the solution description says
     * @param thread any comments attached to the solution
     * @return returns the new solution just created by the user with all of the 
     * information just supplied
     */
    public UserSolution addsolution(User user, String description, 
        ArrayList<Comment> thread){
            UserSolution solution = new UserSolution(user, description);
            solution.addSolution(user, description, thread);
            return solution;

    }

    /**
     * A method that removes a posted solution by the current user
     * @param user the current user
     * @param description the description string that makes up the solution
     * @param thread the comments attached to the solution
     */
    public void removeSolution(User user, String description,
         ArrayList<Comment> thread){
            UserSolution solution = getSolution();
            if(solution != null){
                solution.removeSolution(user, description, thread);
            }
         }
    
    /**
     * method that adds the selected question to the users stored list of questions
     * called starredQuestions
     */
    public void starQuestion(){
        if(!starredQuestions.contains(question)){
            starredQuestions.add(question);
        }
    }

    /**
     * Method that returns the starred questions list from the user
     * @return ArrayList<Question> which is of all the starred questions
     */
    public ArrayList<Question> getStarredQuestionList(){
        return starredQuestions;
    }

    /**
     * A method that adds a comment to a solution
     * @param user the curruent user
     * @param comment the string comment that the user wants to add 
     * @return Comment the new comment
     */
    public Comment addComment(User user, String comment){
        Comment newComment = new Comment(user, comment, new ArrayList<>());
        return newComment;
    }

    /**
     * A method to remove the comment 
     * @param comment the comment the user wants to delete
     */
    public void removeComment(String comment){
        for(Comment com: user.getComments()){
            if(com.getComment().equals(comment)){
                user.getComments().remove(com);
                break;
            }
        }
    }

    /**
     * A method to get the current satus of the user
     * @return Status of the current user
     */
    public Status getStatus(){
        return this.status;
    }
    
    /**
     * A method that sets the status of the current user
     * @param status is the status you want to be or update to
     * @return true if the user can has the ability to post a question
     * (if they are a contributor or admin) or false if they are just a user
     */
    public boolean setStatus(boolean status){
        this.userStatus = status;
        if(this.userStatus == Status.CONTRIBUTOR || this.userStatus == Status.ADMIN){
            return true;
        }
        return false;
    }

    /**
     * A method to get the last name stored for the user
     * @return lastName the last name of the user
     */
    public String getLastName(){
        return lastName;
    }

    /**
     * A method to set the lastname of the user
     * @param lastName the lastName that the user wants to be 
     * their last name
     */
    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    /**
     * A method to get the first name stored for the user
     * @return firstName the first name of the user
     */
    public String getFirstName(){
        return firstName;
    }

        /**
     * A method to set the firstname of the user
     * @param firstName the firstName that the user wants to be 
     * their first name
     */
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    /**
     * method to get the username of the current user
     * @return String of the username
     */
    public String getUsername(){
        return username;
    }

    /**
     * method to set the current username to the parameter
     * @param username the username the user inputs 
     */
    public void setUsername(String username){
        this.username = username;
    }

        /**
     * method to get the password of the current user
     * @return String of the password
     */
    public String getPassword(){
        return password;
    }

    /**
     * method to set the current password to the parameter
     * @param password the password the user inputs 
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * method to get the email of the current user
     * @return String of the email
     */
    public String getEmail(){
        return email;
    }

    /**
     * method to set the current email to the parameter
     * @param email the email the user inputs 
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * method to get the id of the current user
     * @return String of their id
     */
    public String getIdUSC(){
        return idUSC;
    }

    /**
     * method to set the current USC id to the parameter
     * @param idUSC the USC id the user inputs 
     */
    public void setIdUSC(String idUSC){
        this.idUSC = idUSC;
    }

    /**
     * method to get the graduation year of the current user
     * @return int of the grad year
     */
    public int getGraduationYear(){
        return graduationYear;
    }

    /**
     * method to set the current graduation year to the parameter
     * @param graduationYear the year the user inputs they are graduating 
     */
    public void setGraudationYear(int graduationYear){
        this.graduationYear = graduationYear;
    }
    
    /**
     * method to get the UUID of the current user
     * @return UUID of the id
     */
    public UUID getId(){
        return id;
    }

    /**
     * method to set the current id to the parameter
     * @param UUID the id the user inputs 
     */
    public void setId(UUID id){
        this.id = id;
    }

    /**
     * method to set the current status to the parameter
     * @param Status the status the user inputs 
     */
    public void setStatus(Status status){
        this.status = status;
    }

    /**
     * a method that returns the list of the users starred questions
     * @return ArrayList<Question> an array list of Questions the user 
     * has starred
     */
    public ArrayList<Question> getStarredQuestions(){
        return starredQuestions;
    }

    /**
     * A method to set the starredQuestions list to the new/updated starredQuestion List
     * being brought in as a parameter
     * @param starredQuestions the new or updated starredQuestions list
     */
    public void setStarredQuestions(ArrayList<Question> starredQuestions){
        this.starredQuestions = starredQuestions;
    }


    /**
     * A method that gets the list of the users answered questions 
     * @return ArrayList<Question> an array list of questions answered by 
     * the current user
     */
    public ArrayList<Question> getAnsweredQuestions(){
        return answeredQuestions;
    }

    /**
     * A method to set the answered questions list to the new/updated 
     * answered question list being brought in as a parameter
     * @param answeredQuestions the new or updated answeredQuestions list
     */
    public void setAnsweredQuestions(ArrayList<Question> answeredQuestions){
        this.answeredQuestions = answeredQuestions;
    }

    /**
     * A mehthod that gets the users achievements
     * @return ArrayList<Achievement> an array list of the users achievements 
     */
    public ArrayList<Achievement> getAchievements(){
        return answeredQuestions;
    }

    /**
     * A method that sets the users achievements to the newer or updated list of 
     * achievements
     * @param achievements the list of user achievements
     */
    public void setAchievements(ArrayList<Achievement> achievements){
        this.achievements = achievements;
    }

}