package com.interviews;
import java.util.ArrayList;

/**
 * A class that creates a comment that can be 
 * attributed to a solution
 */
public class Comment {
    private User user;
    private String comment;
    private String attachmentName;
    private String attachmentPath;
    private ArrayList<Comment> replies;

    /**
     * Creates a comment with no replies.
     *
     * @param user the author of the comment
     * @param comment the comment text
     */
    public Comment(User user, String comment){
        this.user = user;
        this.comment = comment;
        this.attachmentName = "";
        this.attachmentPath = "";
        this.replies = new ArrayList<>();
    }

    /**
     * Creates a comment with an optional attachment.
     *
     * @param user the author of the comment
     * @param comment the comment text
     * @param attachmentName the selected attachment file name
     * @param attachmentPath the selected attachment file path
     */
    public Comment(User user, String comment, String attachmentName, String attachmentPath){
        this.user = user;
        this.comment = comment;
        this.attachmentName = attachmentName != null ? attachmentName : "";
        this.attachmentPath = attachmentPath != null ? attachmentPath : "";
        this.replies = new ArrayList<>();
    }

    /**
     * Creates a comment with an explicit reply list.
     *
     * @param user the author of the comment
     * @param comment the comment text
     * @param replies the existing replies, or {@code null} to start empty
     */
    public Comment(User user, String comment, ArrayList<Comment> replies){
        this(user, comment, replies, "", "");
    }

    /**
     * Creates a comment with replies and optional attachment metadata.
     *
     * @param user the author of the comment
     * @param comment the comment text
     * @param replies the existing replies, or {@code null} to start empty
     * @param attachmentName the selected attachment file name
     * @param attachmentPath the selected attachment file path
     */
    public Comment(User user, String comment, ArrayList<Comment> replies,
            String attachmentName, String attachmentPath){
        this.user = user;
        this.comment = comment;
        this.attachmentName = attachmentName != null ? attachmentName : "";
        this.attachmentPath = attachmentPath != null ? attachmentPath : "";
        if(replies == null){
            this.replies = new ArrayList<>();
        }
        else{
            this.replies = replies;
        }
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
     * @return the attached file name, or an empty string when no file is attached
     */
    public String getAttachmentName(){
        return attachmentName != null ? attachmentName : "";
    }

    /**
     * @return the attached file path, or an empty string when no file is attached
     */
    public String getAttachmentPath(){
        return attachmentPath != null ? attachmentPath : "";
    }

    /**
     * @return true when this comment includes attachment metadata
     */
    public boolean hasAttachment(){
        return !getAttachmentName().isBlank() || !getAttachmentPath().isBlank();
    }

    /**
     * A method to return the list of commnet replies 
     * @return the ArrayList<Comment> of comments
     */
    public ArrayList<Comment> getReplies(){
        return replies;
    }


    /**
     * Adds a reply comment to this comment.
     *
     * @param user the current user
     * @param commentText the text the user wants to comment
     */
    public void addComment(User user, String commentText){
        Comment newComment = new Comment(user, commentText);
        replies.add(newComment);
    }

    /**
     * Adds a reply comment with a predefined nested reply list.
     *
     * @param user the current user
     * @param commentText the comment text to add
     * @param replies the list of comments
     */
    public void addComment(User user, String commentText, ArrayList<Comment> replies){
        Comment newComment = new Comment(user, commentText, replies);
        this.replies.add(newComment);
    }

    /**
     * Removes a reply comment from this comment.
     *
     * @param comment the comment to remove
     */
    public void deleteComment(Comment comment){
        replies.remove(comment);
    }

    /**
     * Adds an existing reply object to this comment.
     *
     * @param reply the reply to attach
     */
    public void addReply(Comment reply){
        if(reply != null){
            replies.add(reply);
        }
    }

    /**
     * Removes a reply object from this comment.
     *
     * @param reply the reply the user wants to delete
     */
    public void removeReply(Comment reply){
        replies.remove(reply);
    }

    /**
     * Removes the reply at the given index when the index is valid.
     *
     * @param index the index where the comment is found
     */
    public void removeReply(int index){
        if (index >= 0 && index <replies.size()){
            replies.remove(index);
        }
    }

}
