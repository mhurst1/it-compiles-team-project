package com.interviews;

import java.util.ArrayList;
import java.util.UUID;

/**
 * 
 * A class that creates a Question like a technical question that 
 * users can browse and add solutions to
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

    public Question(UUID id){
        this.id = UUID.randomUUID();
    }

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

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public Difficulty getDifficulty(){
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty){
        this.difficulty = difficulty;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }


    // THis is the same as questionID
    public UUID getId(){
        return id;
    }
    public void setId(UUID id){
        this.id = id;
    }

    public Language getLanguage(){
        return questionLang;
    }

    public void setLanguage(Language questionLang){
        this.questionLang = questionLang;
    }


    public ArrayList<Section> getSections() {
        return questionContent;
    }

    /* 
    public int selectSolution(){  // ????

    }
    */

    public ArrayList<String> getHints(){
        return hints;
    }

    public ArrayList<Section> getQuestionContent(){
        return questionContent;
    }
    public void setQuestionContent(ArrayList<Section> questionContent){
        this.questionContent = questionContent;
    }

    public UUID getQuestionId(){
        return id;
    }

    public void setHint(ArrayList<String> hints){
        this.hints = hints;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }


    public ArrayList<UserSolution> getSolutionList(){
        return solutionList;
    }
    public void setSolutionList(ArrayList<UserSolution> solutionList){
        this.solutionList = solutionList;
    }

    public ArrayList<String> getGivenSolutionImg(){
        return givenSolutionIMG;
    }
    public ArrayList<String> getGivenSolutionText(){
        return givenSolutionTXT;
    }


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
     * a method that searches through the saved questions 
     * for the given question
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