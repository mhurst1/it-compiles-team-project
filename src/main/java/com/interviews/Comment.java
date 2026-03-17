package com.interviews;
import java.util.ArrayList;

/**
 * A class that creates a comment that can be 
 * attributed to a solution
 */
public class Comment {
    private User user;
    private String comment;
    private ArrayList<Comment> replies;

    public Comment(User user, String comment){
        this.user = user;
        this.comment = comment;
        this.replies = new ArrayList<>();
    }


    /**
     * method to retrieve the current user
     * @return the current user
     */
    public User getUser(){
        return user;
    }

    /**
     * method to retrieve the current comment
     * @return current comment
     */
    public String getComment(){
        return comment;
    }

    /**
     * A method to return the list of commnet replies 
     * @return the ArrayList<Comment> of comments
     */
    public ArrayList<Comment> getReplies(){
        return replies;
    }


    /**
     * Method to a add a new comment before there is a chain
     * @param user the current user
     * @param commentText the text the user wants to comment
     */
    public void addComment(User user, String commentText){
        comment = new Comment(user, commentText, new ArrayList<Comments>());
        replies.add(comment);
    }

    /**
     * A method to add a comment to the replies
     * @param user the current user
     * @param comment the current comment
     * @param replies the list of comments
     */
    public void addComment(User user, String comment, ArrayList<Comment> replies){
        comment = new Comment(user, commentText, replies);
        this.replies.add(comment);
    }

    /**
     * a method to delete the current comment
     * @param comment the current comment 
     */
    public void deleteComment(Comment comment){
        replies.remove(comment);
    }

    /**
     * A method to add a reply to the comment
     * @param reply the string they want to comment 
     */
    public void addReply(Comment reply){
        if(reply != null){
            replies.add(reply);
        }
    }

    /**
     * Removes Comment Based on the reply
     * @param reply the reply the user wants to delete
     */
    public void removeReply(Comment reply){
        replies.remove(reply);
    }

    /**
     * Removes Comment at Index Given
     * @param index the index where the comment is found
     */
    public void removeReply(int index){
        if (index >= 0 && index <replies.size()){
            replies.remove(index);
        }
    }

}
