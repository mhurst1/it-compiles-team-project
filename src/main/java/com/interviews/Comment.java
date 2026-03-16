package com.interviews;
import java.util.ArrayList;

/**
 * 
 * MH and SE
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


    // Getters
    public User getUser(){
        return user;
    }
    public String getComment(){
        return comment;
    }

    public ArrayList<Comment> getReplies(){
        return replies;
    }


    // Methods
    public void addComment(User user, String commentText){
        
    }

    public void addComment(User user, String comment, ArrayList<Comment> replies){
        
    }

    public void deleteComment(){
        
    }

    public void addReply(Comment reply){
        if(reply != null){
            replies.add(reply);
        }
    }

    /**
     * Removes Comment Based on the reply
     * @param reply
     */
    public void removeReply(Comment reply){
        replies.remove(reply);
    }

    /**
     * Removes Comment at Index Given
     * @param index
     */
    public void removeReply(int index){
        if (index >= 0 && index <replies.size()){
            replies.remove(index);
        }
    }

}
