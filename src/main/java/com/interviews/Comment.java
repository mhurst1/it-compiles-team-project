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

    public Comment(User user, String comment, ArrayList<Comment> replies){
        this.user = user;
        this.comment = comment;
        this.replies = replies;
    }

    public void addComment(User user, String comment){
        this.user = user;
        this.comment = comment;
    }

    public void addComment(User user, String comment, ArrayList<Comment> replies){
        this.user = user;
        this.comment = comment;
        this.replies = replies;
    }

    public void deleteComment(){
        this.user = null;
        this.comment = null;
        this.replies = null;
    }
}
