package com.interviews;

import java.util.ArrayList;
import java.util.UUID;

/**
 * 
 * MH
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

    private ArrayList<String> givenSolutionIMG;
    private ArrayList<String> givenSolutionTXT;

    private ArrayList<Section> sections; // Added Section for the question
    // Question Should have a section correct??
    // Because we are sectioning off comments but also using it for the questions as well

    
    public Question(String title, User user, String description, ArrayList<Section> questionContent2,
                ArrayList<String> hints2,
                Difficulty difficulty2,
                Language questionLang2){
        this.title = title;
        this.user = user;
        this.description = description;
        this.difficulty = difficulty2;
        this.questionLang = questionLang;
        this.hints = hints2;
        this.questionContent = questionContent2;
    }

    public Question(UUID id){
        this.id = id;
    }
    public Question(String title2, User user2, String description2, Difficulty difficulty2, Language lang,
            ArrayList<String> hints2, ArrayList<Section> sections2) {
        //TODO Auto-generated constructor stub
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

    public UUID getQuestionId(){ // Not UUID
        return id;
    }

    public void setHint(ArrayList<String> hints){
        this.hints = hints;
    }

    // CHECK IF IT SHOULD HAVE TYPE USERSOLUTION
    public ArrayList<UserSolution> getSolutionList(){
        return solutionList;
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

    public void searchQuestions(String titleSearch){
        if (titleSearch == null){
            return;
        } else if (this.title.toLowerCase().contains(titleSearch.toLowerCase())){
            System.out.println(this.title);
        }
    }

}