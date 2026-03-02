package com.interviews;
import java.util.ArrayList;

/**
 * 
 * MH
 */
public class Comment {
    private User user;
    private String comment;
    private ArrayList<Comment> replies;

    public Comment(User user, String comment, ArrayList<Comment> replies){
        this.user = user;
        this.comment = comment;
        this.replies = replies;
    }

    public void addComment(User user, String comment){
        
    }

    public void addComment(User user, String comment, ArrayList<Comment> replies){

    }

    public void deleteComment(){

    }

}
