package com.interviews;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Represents a question that users can browse, answer, and discuss.
 */
public class Question {
    
    private String title;
    private User user;
    private String description;
    private ArrayList<Section> questionContent;
    private ArrayList<String> hints;
    private UUID id;
    private Difficulty difficulty;
    private Language questionLang;
    private ArrayList<UserSolution> solutionList; // Check if the ArrayList value should be a string

    private ArrayList<Comment> comments;

    private ArrayList<String> givenSolutionIMG;
    private ArrayList<String> givenSolutionTXT;
    
    /**
     * Creates a question with the provided content and metadata.
     *
     * @param title the question title
     * @param user the user who created the question
     * @param description the question description
     * @param questionContent the section content for the question
     * @param hints the hints associated with the question
     * @param difficulty the difficulty level
     * @param questionLang the primary language for the question
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
     * Creates a question placeholder with a generated ID.
     *
     * @param id ignored placeholder parameter retained for compatibility
     */
    public Question(UUID id){
        this.id = UUID.randomUUID();
    }

    /**
     * Creates a question with a generated ID and the provided metadata.
     *
     * @param title the question title
     * @param user the user who created the question
     * @param description the question description
     * @param difficulty the difficulty level
     * @param questionLang the primary language for the question
     * @param hints the hints associated with the question
     * @param questionContent the section content for the question
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
     * Returns the question title.
     *
     * @return the title
     */
    public String getTitle(){
        return title;
    }

    /**
     * Sets the question title.
     *
     * @param title the new title
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * Returns the question description.
     *
     * @return the description
     */
    public String getDescription(){
        return description;
    }

    /**
     * Sets the question description.
     *
     * @param description the new description
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * Returns the question difficulty.
     *
     * @return the difficulty
     */
    public Difficulty getDifficulty(){
        return difficulty;
    }

    /**
     * Sets the question difficulty.
     *
     * @param difficulty the new difficulty
     */
    public void setDifficulty(Difficulty difficulty){
        this.difficulty = difficulty;
    }

    /**
     * Returns the user who posted the question.
     *
     * @return the question author
     */
    public User getUser(){
        return user;
    }

    /**
     * Sets the user who posted the question.
     *
     * @param user the new question author
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * Returns the question ID.
     *
     * @return the question ID
     */
    public UUID getId(){
        return id;
    }

    /**
     * Sets the question ID.
     *
     * @param id the new question ID
     */
    public void setId(UUID id){
        this.id = id;
    }

    /**
     * Returns the primary language for the question.
     *
     * @return the question language
     */
    public Language getLanguage(){
        return questionLang;
    }

    /**
     * Sets the primary language for the question.
     *
     * @param questionLang the new question language
     */
    public void setLanguage(Language questionLang){
        this.questionLang = questionLang;
    }

    /**
     * Returns the question sections.
     *
     * @return the question sections
     */
    public ArrayList<Section> getSections() {
        return questionContent;
    }

    /* 
    public int selectSolution(){  // ????

    }
    */
    /**
     * Returns the question hints.
     *
     * @return the question hints
     */
    public ArrayList<String> getHints(){
        return hints;
    }

    /**
     * Returns the structured content of the question.
     *
     * @return the question content
     */
    public ArrayList<Section> getQuestionContent(){
        return questionContent;
    }

    /**
     * Sets the structured content of the question.
     *
     * @param questionContent the new question content
     */
    public void setQuestionContent(ArrayList<Section> questionContent){
        this.questionContent = questionContent;
    }

    /**
     * Returns the question ID.
     *
     * @return the question ID
     */
    public UUID getQuestionId(){
        return id;
    }

    /**
     * Sets the hints for the question.
     *
     * @param hints the new hints
     */
    public void setHint(ArrayList<String> hints){
        this.hints = hints;
    }

    /**
     * Returns the comments attached directly to the question.
     *
     * @return the question comments
     */
    public ArrayList<Comment> getComments() {
        return comments;
    }

    /**
     * Returns the list of solutions attached to the question.
     *
     * @return the solution list
     */
    public ArrayList<UserSolution> getSolutionList(){
        return solutionList;
    }

    /**
     * Sets the list of solutions attached to the question.
     *
     * @param solutionList the new solution list
     */
    public void setSolutionList(ArrayList<UserSolution> solutionList){
        this.solutionList = solutionList;
    }

    /**
     * Returns the image-based given solutions.
     *
     * @return the image solution list
     */
    public ArrayList<String> getGivenSolutionImg(){
        return givenSolutionIMG;
    }

    /**
     * Returns the text-based given solutions.
     *
     * @return the text solution list
     */
    public ArrayList<String> getGivenSolutionText(){
        return givenSolutionTXT;
    }

    /**
     * Returns a simple string representation of the question.
     *
     * @return a placeholder string representation
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

}
