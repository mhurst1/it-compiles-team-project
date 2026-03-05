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
    private ArrayList<Section> qustionContent;
    private ArrayList<String> hints;
    private UUID id;
    private Difficulty difficulty;
    private Language questionLang;
    private ArrayList<String> solutionList; // Check if the ArrayList value should be a string

    private ArrayList<String> givenSolutionIMG;
    private ArrayList<String> givenSolutionTXT;

    public Question(String title, User user, String description, Difficulty difficulty, 
                        Language questionLang, ArrayList<String> hints, ArrayList<Section> questionContent){
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.questionLang = questionLang;
        this.hints = hints;
        this.qustionContent = questionContent;
    }

    public Question(UUID id){
        this.id = id;
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

    public Language getQuestionLanguage(){
        return questionLang;
    }

    public void setQuestionLanguage(Language questionLang){
        this.questionLang = questionLang;
    }

    public int selectSolution(){  // ????

    }

    public ArrayList<String> getHints(){
        return hints;
    }

    public ArrayList<Section> getQuestionContent(){
        return qustionContent;
    }

    public UUID getQuestionId(){ // Not UUID
        return id;
    }

    public void setHint(ArrayList<String> hints){
        this.hints = hints;
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