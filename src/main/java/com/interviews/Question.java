package com.interviews;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Represents a question that users can browse, answer, and discuss.
 */
public class Question {
    
    /** Title of the question */
    private String title;

    /** User who created the question */
    private User user;

    /** Description explaining the question */
    private String description;

    /** Structured content broken into sections */
    private ArrayList<Section> questionContent;

    /** List of hints to help solve the question */
    private ArrayList<String> hints;

    /** Unique identifier for the question */
    private UUID id;

    /** Difficulty level of the question */
    private Difficulty difficulty;

    /** Programming language associated with the question */
    private Language questionLang;

    /** List of user-submitted solutions */
    private ArrayList<UserSolution> solutionList;

    /** List of comments related to the question */
    private ArrayList<Comment> comments;

    /** List of image-based given solutions */
    private ArrayList<String> givenSolutionIMG;

    /** List of text-based given solutions */
    private ArrayList<String> givenSolutionTXT;
    
    /**
     * Constructs a Question with all main attributes.
     * 
     * @param title the title of the question
     * @param user the user who created the question
     * @param description the description of the question
     * @param questionContent the sections of the question
     * @param hints hints related to solving the question
     * @param difficulty difficulty level
     * @param questionLang programming language associated
     */
    public Question(String title, User user, String description, ArrayList<Section> questionContent,
                ArrayList<String> hints, Difficulty difficulty, Language questionLang){
        this.title = title;
        this.user = user;
        this.description = description;
        this.difficulty = difficulty;
        this.questionLang = questionLang;
        this.hints = hints;
        this.questionContent = questionContent;

        this.comments = new ArrayList<>();
        this.solutionList = new ArrayList<>();
    }

    /**
     * Constructs a Question with a generated UUID.
     * 
     * @param id the UUID (not used directly; generates a new one)
     */
    public Question(UUID id){
        this.id = UUID.randomUUID();
    }

    /**
     * Alternate constructor that initializes a question with a new UUID.
     * 
     * @param title the title of the question
     * @param user the user who created the question
     * @param description the description
     * @param difficulty difficulty level
     * @param questionLang programming language
     * @param hints hints for the question
     * @param questionContent structured sections
     */
    public Question(String title, User user, String description, Difficulty difficulty, Language questionLang,
            ArrayList<String> hints, ArrayList<Section> questionContent) {

        this.id = UUID.randomUUID(); // This will create a random question id
        this.title = title;
        this.user = user;
        this.description = description;
        this.difficulty = difficulty;
        this.questionLang = questionLang;
        this.hints = hints;
        this.questionContent = questionContent;

        this.comments = new ArrayList<>();
        this.solutionList = new ArrayList<>();
    }

    /**
     * Gets the question title.
     * @return the title
     */
    public String getTitle(){
        return title;
    }

    /**
     * Sets the question title.
     * @param title the new title
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * Gets the question description.
     * @return the description
     */
    public String getDescription(){
        return description;
    }

    /**
     * Sets the question description.
     * @param description the new description
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * Gets the difficulty level.
     * @return the difficulty
     */
    public Difficulty getDifficulty(){
        return difficulty;
    }

    /**
     * Sets the difficulty level.
     * @param difficulty the new difficulty
     */
    public void setDifficulty(Difficulty difficulty){
        this.difficulty = difficulty;
    }

    /**
     * Gets the user who created the question.
     * @return the user
     */
    public User getUser(){
        return user;
    }

    /**
     * Sets the user who created the question.
     * @param user the new user
     */
    public void setUser(User user){
        this.user = user;
    }


    /**
     * Gets the unique question ID.
     * @return the UUID
     */
    public UUID getId(){
        return id;
    }

    /**
     * Sets the question ID.
     * @param id the new UUID
     */
    public void setId(UUID id){
        this.id = id;
    }

    /**
     * Gets the programming language of the question.
     * @return the language
     */
    public Language getLanguage(){
        return questionLang;
    }

    /**
     * Sets the programming language.
     * @param questionLang the new language
     */
    public void setLanguage(Language questionLang){
        this.questionLang = questionLang;
    }


    /**
     * Gets the sections of the question.
     * @return list of sections
     */

    public ArrayList<Section> getSections() {
        return questionContent;
    }


    /**
     * Gets the list of hints.
     * @return list of hints
     */
    public ArrayList<String> getHints(){
        return hints;
    }

    /**
     * Gets the question content sections.
     * @return list of sections
     */
    public ArrayList<Section> getQuestionContent(){
        return questionContent;
    }
    
    /**
     * Sets the question content sections.
     * @param questionContent the new sections
     */
    public void setQuestionContent(ArrayList<Section> questionContent){
        this.questionContent = questionContent;
    }

    /**
     * Gets the question ID (same as getId).
     * @return UUID of the question
     */
    public UUID getQuestionId(){
        return id;
    }

    /**
     * Sets the hints for the question.
     * @param hints the new hints
     */
    public void setHint(ArrayList<String> hints){
        this.hints = hints;
    }

    /**
     * Gets the list of comments.
     * @return list of comments
     */
    public ArrayList<Comment> getComments() {
        return comments;
    }


    /**
     * Gets the list of solutions.
     * @return list of solutions
     */
    public ArrayList<UserSolution> getSolutionList(){
        syncSolutionQuestionIds();
        return solutionList;
    }

    /**
     * Sets the list of solutions.
     * @param solutionList the new solutions
     */
    public void setSolutionList(ArrayList<UserSolution> solutionList){
        this.solutionList = solutionList;
        syncSolutionQuestionIds();
    }

    /**
     * Gets image-based solutions.
     * @return list of image paths
     */
    public ArrayList<String> getGivenSolutionImg(){
        return givenSolutionIMG;
    }

    /**
     * Gets text-based solutions.
     * @return list of text solutions
     */
    public ArrayList<String> getGivenSolutionText(){
        return givenSolutionTXT;
    }


    /**
     * Returns a string representation of the question.
     * 
     * @return a string (currently placeholder)
     */
    public String toString(){
        return " "; // Edit this
    }

    /* CONTAINTS MIGHT HAVE TO GO WITIN SECTION

    public boolean contains(String keyword){
        if (keyword == null){
            return false;
        }

        for (Section s : questionContent){
            if(s.contains(keyword)){
                return true;
            }
        }
        return false; // If Not Found
    }
        */

    /**
     * Prints the title when it matches the provided search string.
     *
     * @param titleSearch the title of the question
     */
    public void searchQuestions(String titleSearch){
        if (titleSearch == null){
            return;
        } else if (this.title.toLowerCase().contains(titleSearch.toLowerCase())){
            System.out.println(this.title);
        }
    }

    private void syncSolutionQuestionIds() {
        if (solutionList == null || id == null) {
            return;
        }

        for (UserSolution solution : solutionList) {
            if (solution != null) {
                solution.setQuestionId(id);
            }
        }
    }

}
