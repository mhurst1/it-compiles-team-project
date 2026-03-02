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
    public int totalVote;


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

    public void addSolution(){

    }

    public void removeSolution(){

    }

    public void getInstance(){ // IS IT VOID OR WHAT??

    }
    public Comment accessComment(){

        
    }

    public int calculateVotes(){

        return 0; // PlaceHolder
    }
    public void setUserVote(boolean userVote){

    }

}
