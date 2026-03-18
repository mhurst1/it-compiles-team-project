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
    private ArrayList<Comment> replies;
    private UUID solutionID;
    public boolean userVote;
    public int totalVote; // I think this can be replaced with upVotes and downVotes


    public UserSolution(User user, String description, UUID solutionID, ArrayList<Comment> replies, int totalVote){
        this.user = user;
        this.description = description;
        this.solutionID = solutionID;
        this.replies = replies;
        this.totalVote = totalVote;
    }

    public UserSolution(User user, String description){
        this.user = user;
        this.description = description;
        this.solutionID = UUID.randomUUID();
        this.replies = new ArrayList<>();
        this.totalVote = 0;
        this.userVote = false;
    }

    // GETTERS 
    public User getUser(){
        return user;
    }
    public String getDescription(){
        return description;
    }
    public ArrayList<Comment> getReplies(){
        return replies;
    }
    public boolean getUserVote(){
        return userVote;
    }
    public int getTotalVote(){
        return totalVote;
    }
    public UUID getSoulutionId(){
        return solutionID;
    }

    // ?? Are we just using thread as a mean to return replies
    public ArrayList<Comment> getThread(){
        return replies;
    }

    // Setters 
    public void setUserVote(boolean userVote){
        this.userVote = userVote;
    }

    /* I DONT THINK WE NEED THESE MH

    public void addSolution(User user, String description, ArrayList<Comment> thread){

    }

    public void removeSolution(User user, String description, ArrayList<Comment> thread){

    }

    public void getInstance(){ // IS IT VOID OR WHAT??

    } */

    // Fix Method Later
    public Comment accessComment(Comment comment, User user){
        return comment;
    }

    public int calculateVotes(User user, int upVotes, int downVotes){

        int totalVote = upVotes - downVotes;
        if(totalVote < 0){
            return 0; // Does not allow to have a negative amount of votes (Lowest is 0)
        }
        return totalVote;
    }
}
