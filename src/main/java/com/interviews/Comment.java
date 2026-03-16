package com.interviews;
import java.util.ArrayList;

/**
 * 
 * MH and SE
 */
public class Comment {
    private User user;
    private ArrayList<Section> comments;
    private ArrayList<Comment> replies;

    public Comment(User user, ArrayList<Section> comments){
        this.user = user;
        if(comments != null){
            this.comments = comments;
        }
        else{
            this.comments = new ArrayList<>();
        }
        this.replies = new ArrayList<>();
    }


    // Getters
    public User getUser(){
        return user;
    }
    public ArrayList<Section> getComments(){
        return comments;
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

    public void addReply(){

    }
    public void removeReply(){

    }
}
