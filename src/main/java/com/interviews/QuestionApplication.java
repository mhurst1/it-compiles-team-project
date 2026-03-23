package com.interviews;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A facade class that manages interactions between users, questions,
 * solutions, and comments within the application.
 * 
 * This class acts as the main controller for:
 * User account creation and authentication
 * Question creation, editing, searching, and deletion
 * Solution submission and management
 * Commenting and discussion threads
 */
public class QuestionApplication {

    /** List of all questions in the application */
    private QuestionList questionList;

    /** List of all users in the application */
    private UserList userList;

    /** Currently logged-in user */
    private User currentUser;

    /** Currently selected question */
    private Question currentQuestion;

    /** Currently selected user solution */
    private UserSolution currentUserSolution;
    

    /**
     * Constructs a new QuestionApplication and initializes all fields.
     */
    public QuestionApplication() {
        this.currentQuestion = null;
        this.currentUser = null;
        this.currentUserSolution = null;
        this.questionList = new QuestionList();
        this.userList = new UserList();

    }

    /**
     * Gets the question list.
     * @return the QuestionList object
     */
    public QuestionList getQuestionList() {
        return questionList;
    }

    /**
     * Sets the currently logged-in user.
     * @param user the user to set as current
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Gets the currently logged-in user.
     * @return the current user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Gets the currently selected question.
     * @return the current question
     */
    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    /**
     * Gets the currently selected user solution.
     * @return the current user solution
     */
    public UserSolution getCurrentUserSolution() {
        return currentUserSolution;
    }


    /**
     * Creates a new user account and adds it to the user list.
     *
     * This method checks that the username and email are unique before creating
     * the account. If either already exists, an IllegalArgumentException is thrown.
     * The new user is then added to the user list and saved.
     *
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param username the desired username (must be unique)
     * @param password the user's password
     * @param email the user's email address (must be unique)
     * @param graduationYear the user's graduation year
     * @param idUSC the user's USC ID
     * @return the newly created User object
     * @throws IllegalArgumentException if the username or email already exists
    */
    public User createAccount(String firstName, String lastName, String username,
            String password, String email, int graduationYear, String idUSC) {

        for (User existing : this.userList.getUsers()) {
        if (existing.getUsername().equalsIgnoreCase(username)) {
            // Username already used
            throw new IllegalArgumentException("Username already exists: " + username);
        }
        if (existing.getEmail().equalsIgnoreCase(email)) {
            // Email already used 
            throw new IllegalArgumentException("Email already exists: " + email);
        }
        }
        
        User user = new User(firstName, lastName, username, password, email,
                graduationYear, idUSC);        
        this.userList.getUsers().add(user);
        this.userList.save();
        return user;

    }

    /**
     * Attempts to log in a user with the given username and password.
     *
     * This method searches through the user list and uses the User.isMatch(...)
     * method to verify credentials. If a match is found, the current user is set.
     *
     * @param username the username of the account
     * @param password the password of the account
     * @return true if login is successful, false otherwise
     */
    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        for (User u : this.userList.getUsers()) {
        if (u.isMatch(username, password)) {
            setCurrentUser(u);
            return true;
        }
     }
        return false;

    }


    /**
    * Returns the list of all questions in the application.
    *
    * @return an ArrayList containing all Question objects
    */
    public ArrayList<Question> getQuestions() {
        return questionList.getQuestions();
    }


    /**
    * Creates a new Question and adds it to the application's question list.
    *
    * @param title the title of the question
    * @param user the user creating the question
    * @param description the question description
    * @param difficulty the difficulty level
    * @param questionLanguage the programming language associated with the question
    * @param hints a list of hints for the question
    * @param questionContent the structured content of the question
    * @return true if the question was successfully added
    */
    public boolean addQuestion(String title, User user, String description, Difficulty difficulty,
        Language questionLanguage, ArrayList<String> hints, ArrayList<Section> questionContent) {

        Question question = new Question(title, user, description, difficulty, questionLanguage, hints,
            questionContent);
        
        this.questionList.getQuestions().add(question);
        this.questionList.save();

        return true;
    }

    /**
    * Edits an existing Question if it exists in the question list.
    *
    * @param question the Question to edit
    * @param title the new title
    * @param user the user editing the question
    * @param description the new description
    * @param questionContent the updated content sections
    * @param hints the updated list of hints
    * @param difficulty the updated difficulty level
    * @param questionLanguage the updated programming language
    * @return the updated Question if found, or null if the question does not exist
    */
   public Question editQuestion(Question question, String title, User user,
        String description, ArrayList<Section> questionContent, ArrayList<String> hints, Difficulty difficulty, Language questionLanguage) {
    

    if (question !=null && questionList.getQuestions().contains(question)) {

        question.setTitle(title);
        question.setUser(user);
        question.setDescription(description);
        question.setQuestionContent(questionContent);
        question.setHint(hints);
        question.setDifficulty(difficulty);
        question.setLanguage(questionLanguage);

        this.questionList.save();
        return question;
     }

        // not found or not editable
        return null;
    }



    /**
    * Searches for questions that contain the given keyword in their title or description.
    *
    * @param keyword the keyword to search for
    * @return a list of matching Question objects (empty if none found)
    */
    public ArrayList<Question> findQuestion(String keyword) {
        ArrayList<Question> matchingQuestions = new ArrayList<>();

        if(keyword == null) {
            return matchingQuestions; // Return empty list if keyword is null
        }

        String lowerKeyword = keyword.toLowerCase();
        for (Question q : questionList.getQuestions()) {
            if (q.getTitle().toLowerCase().contains(lowerKeyword) ||
                q.getDescription().toLowerCase().contains(lowerKeyword)) {
                matchingQuestions.add(q);
            }
        }
        return matchingQuestions;
    }

    /**
    * Removes a question from the application if it exists.
    *
    * @param question the Question to be deleted
    */
    public void deleteQuestion(Question question) {
        if (question != null && questionList.getQuestions().contains(question)) {
            this.questionList.getQuestions().remove(question);
            this.questionList.save();
        }
    }

    /**
    * Creates a new UserSolution and sets it as the current user solution.
    *
    * @param user the user who created the solution
    * @param description the solution description/content
    * @param solutionID the unique identifier for the solution
    * @param replies the list of comments associated with the solution
    * @param totalVote the total vote count for the solution
    */
    public void addUserSolution(User user, String description, UUID solutionID,
            ArrayList<Comment> replies, int totalVote) {
        
        UserSolution newSolu = new UserSolution(user, description, solutionID, replies, totalVote);
        currentQuestion.getSolutionList().add(newSolu);

    }

    /**
     * Returns the list of questions that the current user has answered.
     *
    * @return a list of answered questions, or an empty list if no user is logged in
    */
    public ArrayList<Question> getAnsweredQuestions() {
        if (currentUser == null) {
            return new ArrayList<>();
        }
        return currentUser.getAnsweredQuestions();
    }

    /**
    * Remove a user solution from its question 
    * @param question the question from which to remove the solution
    * @param userSolution the solution to remove (may be null)
    */
    public void removeUserSolution(Question question, UserSolution userSolution) {
        if (question == null && userSolution == null) 
            return;
        currentQuestion.getSolutionList().remove(userSolution);
        this.questionList.save();
    }

    //comment on a question?
    public void addComment(Question question, User user, String comment) {
        if (question == null || user == null || comment == null || comment.isBlank()) {
            return;
        }
        Comment newComment = new Comment(user, comment, new ArrayList<>());
        question.getComments().add(newComment);
    }

    /**
     * Method to create and add a comment 
     * @param userSolution the solution the user wants to comment on
     * @param user the user commenting 
     * @param comment the comment the user wants to publish
     */
    public void addComment(UserSolution userSolution, User user, String comment) {
        Comment newComment = new Comment(user, comment, new ArrayList<>());
        userSolution.getReplies().add(newComment);
    }

    /**
     * A method that deletes a comment from the list of replies
     * @param comment the comment the user wants to delete
     * @param id the id of the comment
     */
    public void deleteComment(Comment comment, int id) {
        currentUserSolution.getReplies().remove(comment);
    }

    /**
     * Method to add a question to the users personal list of starred questions
     * @param question the question the user wants to star
     * @param id the id of the user
     */
    public void starQuestion(Question question, UUID id) {
        currentUser.getStarredQuestions().add(question);
    }

    /**
     * a method that logs the user out of their account
     * This method checks if there is a currently logged-in user. If there is, it saves the current state of users and questions using a DataWriter, and then sets the current user to null, effectively logging them out. If no user is currently logged in, it simply prints a message indicating that there is no user to log out.
     */
    public void logout(){
        if (currentUser == null) {
            System.out.println("No user is currently logged in.");
            return;
        }

        DataWriter writer = new DataWriter();
        writer.saveUsers();
        writer.saveQuestions();

        currentUser = null;
    }

    /**
     * A method that sets the status of a user. This method takes a username and a Status enum value as parameters. It retrieves the User object associated with the given username from the user list. If the user exists, it updates their status to the provided Status value and saves the updated user list. This allows users to change their status (e.g., active, inactive, banned) within the application.
      * @param username the username of the user whose status is to be updated
     */
     public void setStatus(String username, Status status) {
        User user = userList.getUser(username);
        if (user != null) {
            user.setStatus(status);
            userList.save();
        }
    }

    /**
     * A method that adds a solution to a question. This method takes a string representing the solution, an object representing the user who created the solution, and another object representing the question to which the solution is being added. It creates a new UserSolution object using these parameters and adds it to the list of solutions for the specified question. This allows users to contribute solutions to questions within the application.
       * @param solutionDescription the description of the solution being added
     */
    public void addSolution(String string, Object object, Object object2) {
        throw new UnsupportedOperationException("Unimplemented method 'addSolution'");
    }

    /**
     * A method that adds a comment to a question or solution. This method takes a string representing the comment, an object representing the user who created the comment, and another object representing the question or solution to which the comment is being added. It creates a new Comment object using these parameters and adds it to the list of comments for the specified question or solution. This allows users to engage in discussions and provide feedback on questions and solutions within the application.
       * @param commentDescription the description of the comment being added
     */
    public void addComment(String question, Object user, Object comment) {
        throw new UnsupportedOperationException("Unimplemented method 'addComment'");
    }

    /**
     * A method that sets the status of a user. This method takes a string representing the username and another string representing the new status. It retrieves the User object associated with the given username from the user list, updates its status to the new value, and saves the updated user list. This allows users to change their status (e.g., active, inactive, banned) within the application.
       * @param username the username of the user whose status is to be updated
      * @param newStatus the new status to be set for the user
     */
    public void setStatus(String string, String string2) {
        throw new UnsupportedOperationException("Unimplemented method 'setStatus'");
    }

}