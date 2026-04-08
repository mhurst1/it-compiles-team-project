package com.interviews;

import java.util.ArrayList;
import java.util.UUID;


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

    private ArrayList<Comment> comments;
    private ArrayList<UserSolution> solutions;

    /**
     * Creates a fully populated user from stored data.
     *
     * @param id the user ID
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param username the username
     * @param password the password
     * @param email the email address
     * @param starredQuestions the starred questions
     * @param answeredQuestions the answered questions
     * @param achievements the user's achievements
     * @param status the user status
     * @param graduationYear the graduation year
     * @param idUSC the USC ID
     */
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

        this.comments = new ArrayList<>();
        this.solutions = new ArrayList<>();

    }

    /**
     * Creates a new standard user with a generated UUID.
     *
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param username the username
     * @param password the password
     * @param email the email address
     * @param graduationYear the graduation year
     * @param idUSC the USC ID
     */
    public User(String firstName, String lastName, String username,
                    String password, String email, int graduationYear, String idUSC){

        this.id = UUID.randomUUID();

        if (firstName == null || firstName.isBlank()) {
            System.out.println("Invalid firstName: cannot be null or blank. Setting to 'unknown'.");
            firstName = "unknown";
        }
        if (lastName == null || lastName.isBlank()) {
            System.out.println("Invalid lastName: cannot be null or blank. Setting to 'unknown'.");
            lastName = "unknown";
        }
        if (username == null || username.isBlank()) {
            System.out.println("Invalid username: cannot be null or blank. Setting to 'unknown'.");
            username = "unknown";
        }
        if (password == null || password.isBlank()) {
            System.out.println("Invalid password: cannot be null or blank. Setting to default.");
            password = "Unknown!1";
        }
        if (email == null || !email.contains("@") || email.indexOf("@") == 0 || email.indexOf("@") == email.length() - 1) {
            System.out.println("Invalid email format. Setting to default.");
            email = "unknown@placeholder.com";
        }
        if (graduationYear < 2000 || graduationYear > 2099) {
            System.out.println("Invalid graduation year. Setting to 2000.");
            graduationYear = 2000;
        }
        if (idUSC == null || idUSC.isBlank()) {
            System.out.println("Invalid idUSC: cannot be null or blank. Setting to 'unknown'.");
            idUSC = "unknown";
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.graduationYear = graduationYear;
        this.idUSC = idUSC;
        this.status = Status.USER;

        this.starredQuestions = new ArrayList<>();
        this.answeredQuestions = new ArrayList<>();
        this.achievements = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.solutions = new ArrayList<>();

    }

    /**
     * Checks whether the provided credentials match this user.
     *
     * @param username the users public username 
     * @param password the users password
     * @return {@code true} when the username and password match
     */
    public boolean isMatch(String username, String password){
        return this.username != null && this.username.equals(username)
                && this.password != null && this.password.equals(password);
    }

    /**
     * Method so the user can add a solution
     * @param user the current user
     * @param description what the solution description says
     * @param replies any comments attached to the solution
     * @return returns the new solution just created by the user with all of the 
     * information just supplied
     */
    public UserSolution addsolution(User user, String description, 
        ArrayList<Comment> replies){
            UserSolution solution = new UserSolution(user, description);
            solutions.add(solution);
            return solution;

    }

    /**
     * A method that removes a posted solution by the current user
     * @param user the current user
     * @param description the description string that makes up the solution
     * @param replies the comments attached to the solution
     */
    public void removeSolution(User user, String description,
         ArrayList<Comment> replies){
           for(int i = 0; i < solutions.size(); i++){
            UserSolution solution = solutions.get(i);
            if(solution.getUser().equals(user) && solution.getDescription().equals(description)){
                solutions.remove(i);
            } 
           }
         }
    
    /**
     * method that adds the selected question to the users stored list of questions
     * called starredQuestions
     */
    public void starQuestion(Question question){
        if(starredQuestions == null){
            starredQuestions = new ArrayList<>();
        }

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
        for(Comment com: comments){
            if(com.getComment().equals(comment)){
                comments.remove(com);
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
        if (username == null || username.isBlank()) {
            System.out.println("Invalid username: cannot be null or blank. Username not updated.");
            return;
        }
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
        if (password == null || password.isBlank()) {
            System.out.println("Invalid password: cannot be null or blank. Password not updated.");
            return;
        }
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
        if (email == null || !email.contains("@") || email.indexOf("@") == 0 || email.indexOf("@") == email.length() - 1) {
            System.out.println("Invalid email format. Email not updated.");
            return;
        }
        this.email = email;
    }

    /**
     * Returns the user's USC ID.
     *
     * @return the USC ID
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
    public void setGraduationYear(int graduationYear){
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
     * Generates and stores a new UUID for the user.
     */
    public void setId(){
        this.id = UUID.randomUUID();
    }

    /**
     * method to set the current status to the parameter
     * @param Status the status the user inputs 
     */
    public void setStatus(Status status){
        this.status = status;
    }

    /**
     * method to see of the user has access to modify questions
     * @return true if the user can modify questions and false if they cant
     */
    public boolean canModifyQuestions(){
        return status == Status.CONTRIBUTOR || status == Status.ADMIN;
    }

    /**
     * method to see if the user has admin permissons
     * @return true if they are admin and false if they are not
     */
    public boolean canModifyContributors(){
        return status == Status.ADMIN;
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
        return achievements;
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
