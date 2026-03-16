package com.interviews;
import java.util.ArrayList;
import java.util.UUID;

/**
 * 
 * MH
 */
public class UserSolution {
    private User user;
    private String description;
    private ArrayList<Comment> thread;
    private UUID solutionID;
    public boolean userVote;
    public int totalVote; // I think this can be replaced with upVotes and downVotes


    public UserSolution(User user, String description, UUID solutionID, ArrayList<Comment> thread, int totalVote){
        this.user = user;
        this.description = description;
        this.solutionID = solutionID;
        this.thread = thread;
        this.totalVote = totalVote;
    }

    public UserSolution(User user, String description){
        this.user = user;
        this.description = description;
    }

    // GETTERS 
    public User getUser(){
        return user;
    }
    public String getDescription(){
        return description;
    }
    public ArrayList<Comment> getThread(){
        return thread;
    }
    public boolean getUserVote(){
        return userVote;
    }
    public int getTotalVote(){
        return totalVote;
    }



    public void addSolution(){

    }

    public void removeSolution(){

    }

    public void getInstance(){ // IS IT VOID OR WHAT??

    }
    public Comment accessComment(){

        
    }

    public int calculateVotes(User user, int upVotes, int downVotes){

        int totalVote = upVotes - downVotes;
        if(totalVote < 0){
            return 0; // Does not allow to have a negative amount of votes (Lowest is 0)
        }

        return totalVote;
    }
    public void setUserVote(boolean userVote){

    }

}
