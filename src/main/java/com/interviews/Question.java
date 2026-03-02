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
    private ArrayList<String> hints; // Check if the ArrayList value should be a string
    private UUID id;
    private Difficulty difficulty;
    private Language questionLang;
    private ArrayList<String> solutionList; // Check if the ArrayList value should be a string

    private ArrayList<String> givenSolutionIMG; // Check if the ArrayList value should be a string
    private ArrayList<String> givenSolutionTXT; // Check if the ArrayList value should be a string

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

    public Language getQuestionLanguage(){
        return questionLang;
    }

    public void setQuestionLanguage(Language questionLang){
        this.questionLang = questionLang;
    }

    public int selectSolution(){  // ????

    }
    public void setHint(ArrayList<String> hints){
        this.hints = hints;
    }

    public String toString(){
        return " "; // Edit this
    }

    public boolean contains(String keyword){

    }

    public void searchQuestions(String titleSearch){

    }

}