package com.interviews;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A class of a Solution that can be posted to a Question
 * Solution contains descriptions and has replies tied to it
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
    //yes and we already have this method above
    /*
    public ArrayList<Comment> getReplies(){
        return replies;
    }
    */

    // Setters 
    public void setUserVote(boolean userVote){
        this.userVote = userVote;
    }

    /* I DONT THINK WE NEED THESE MH

    public void addSolution(User user, String description, ArrayList<Comment> replies){

    }

    public void removeSolution(User user, String description, ArrayList<Comment> replies){

    }

    public void getInstance(){ // IS IT VOID OR WHAT??

    } */

    // Fix Method Later
    public Comment accessComment(Comment comment, User user){
        return comment;
    }

    /**
     * a method to calculate the number of votes on a solution
     * @param user current user 
     * @param upVotes the number of up votes (likes)
     * @param downVotes the number of down votes (dislikes)
     * @return and integer of the sum of the "positive" and 
     * "negative" votes
     */
    public int calculateVotes(User user, int upVotes, int downVotes){

        int totalVote = upVotes - downVotes;
        if(totalVote < 0){
            return 0; // Does not allow to have a negative amount of votes (Lowest is 0)
        }
        return totalVote;
    }
}
