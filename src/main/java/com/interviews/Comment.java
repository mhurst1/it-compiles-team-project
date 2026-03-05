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

    public Comment(User user, ArrayList<Section> comments, ArrayList<Comment> replies){
        this.user = user;
        this.comments = comments;
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
