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
    private UUID questionID;
    public boolean userVote;
    private ArrayList<UUID> upVoters;
    private ArrayList<UUID> downVoters;

    /**
     * Creates a fully populated solution object.
     *
     * @param user the user who posted the solution
     * @param description the solution description
     * @param solutionID the unique solution identifier
     * @param replies the comments attached to the solution
     * @param totalVote the current total vote count
     */
    public UserSolution(User user, String description, UUID solutionID, ArrayList<Comment> replies,
                        ArrayList<UUID> upVoters, ArrayList<UUID> downVoters) {
        this.user = user;
        this.description = description;
        this.solutionID = (solutionID == null) ? UUID.randomUUID() : solutionID;
        this.replies = (replies != null) ? replies : new ArrayList<>();
        this.upVoters = (upVoters != null) ? upVoters : new ArrayList<>();
        this.downVoters = (downVoters != null) ? downVoters : new ArrayList<>();
    }

    /**
     * Creates a new solution with a generated ID and no replies.
     *
     * @param user the user who posted the solution
     * @param description the solution description
     */
    public UserSolution(User user, String description){
        this.user = user;
        this.description = description;
        this.solutionID = UUID.randomUUID();
        this.replies = new ArrayList<>();
        this.upVoters = new ArrayList<>();
        this.downVoters = new ArrayList<>();
        this.userVote = false;
    }

    /**
     * Returns the user who posted the solution.
     *
     * @return the solution author
     */
    public User getUser(){
        return user;
    }

    /**
     * Returns the solution description.
     *
     * @return the solution text
     */
    public String getDescription(){
        return description;
    }

    /**
     * Returns the reply comments for this solution.
     *
     * @return the solution replies
     */
    public ArrayList<Comment> getReplies(){
        return replies;
    }

    /**
     * Returns whether the current user has voted on this solution.
     *
     * @return {@code true} if the current user has voted, otherwise {@code false}
     */
    public boolean getUserVote(){
        return userVote;
    }

    /**
     * Returns the total vote count for this solution.
     *
     * @return the total vote count
     */
    public int getUpVotes() { return upVoters.size(); }
    public int getDownVotes() { return downVoters.size(); }
    public int getTotalVote() { return Math.max(0, upVoters.size() - downVoters.size()); }
    public ArrayList<UUID> getUpVoters() { return upVoters; }
    public ArrayList<UUID> getDownVoters() { return downVoters; }

    public int getVoteStateForUser(UUID userId) {
        if (userId == null) return 0;
        if (upVoters.contains(userId)) return 1;
        if (downVoters.contains(userId)) return -1;
        return 0;
    }

    public void castVote(UUID userId, int direction) {
        if (userId == null) return;
        upVoters.remove(userId);
        downVoters.remove(userId);
        if (direction == 1) upVoters.add(userId);
        else if (direction == -1) downVoters.add(userId);
    }

    /**
     * Returns the unique solution identifier.
     *
     * @return the solution ID
     */
    public UUID getSoulutionId(){
        return solutionID;
    }

    /**
     * Returns the owning question identifier for this solution.
     *
     * @return the question ID
     */
    public UUID getQuestionId() {
        return questionID;
    }

    // ?? Are we just using thread as a mean to return replies
    //yes and we already have this method above
    /*
    public ArrayList<Comment> getReplies(){
        return replies;
    }
    */

    /**
     * Sets whether the current user has voted on this solution.
     *
     * @param userVote the current-user vote flag
     */
    public void setUserVote(boolean userVote){
        this.userVote = userVote;
    }

    /**
     * Stores the owning question identifier for this solution.
     *
     * @param questionID the parent question ID
     */
    public void setQuestionId(UUID questionID) {
        this.questionID = questionID;
    }

    /* I DONT THINK WE NEED THESE MH

    public void addSolution(User user, String description, ArrayList<Comment> replies){

    }

    public void removeSolution(User user, String description, ArrayList<Comment> replies){

    }

    public void getInstance(){ // IS IT VOID OR WHAT??

    } */

    /**
     * Returns the provided comment reference.
     *
     * @param comment the comment to access
     * @param user the user requesting access
     * @return the provided comment
     */
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
