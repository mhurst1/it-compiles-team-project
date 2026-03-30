package com.model;

import com.interviews.Comment;
import com.interviews.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/*
+--------------------------------------+--------------------------------------------------------------+
| Test                                 | Reasoning                                                    |
+--------------------------------------+--------------------------------------------------------------+
| comment user is stored               | every comment should keep track of who wrote it              |
| comment text is stored               | the comment needs to save the message content                |
| replies list starts empty            | a new comment should not have replies unless added           |
| constructor with replies stores them | a comment should support preexisting reply threads           |
| null replies creates empty list      | reply storage should still work even if null is passed in    |
| addComment adds a reply              | users need to be able to respond to a comment                |
| addComment with nested replies       | replies may already have their own reply chain               |
| deleteComment removes reply          | users should be able to remove a reply from a thread         |
| addReply adds existing reply object  | the class should allow attaching an already-created reply    |
| addReply ignores null                | null should not create a broken reply entry                  |
| removeReply by object works          | a specific reply object should be removable                  |
| removeReply by index works           | replies should also be removable by position in the list     |
| invalid index does nothing           | bad indexes should not crash or remove the wrong reply       |
+--------------------------------------+--------------------------------------------------------------+
*/

public class CommentTest {

    private User user;
    private Comment comment;

    @Before
    public void setUp() {
        user = new User("John", "Doe", "jdoe", "pass123", "jdoe@email.com", 2025, "ABC123");
        comment = new Comment(user, "Original comment");
    }

    @Test
    public void testConstructorSetsUser() {
        assertEquals(user, comment.getUser());
    }

    @Test
    public void testConstructorSetsComment() {
        assertEquals("Original comment", comment.getComment());
    }

    @Test
    public void testConstructorInitializesEmptyReplies() {
        assertNotNull(comment.getReplies());
        assertTrue(comment.getReplies().isEmpty());
    }

    @Test
    public void testFullConstructorUsesProvidedReplies() {
        ArrayList<Comment> replies = new ArrayList<>();
        Comment reply = new Comment(user, "Reply");
        replies.add(reply);

        Comment fullComment = new Comment(user, "Original comment", replies);

        assertEquals(replies, fullComment.getReplies());
        assertEquals(1, fullComment.getReplies().size());
        assertTrue(fullComment.getReplies().contains(reply));
    }

    @Test
    public void testFullConstructorWithNullRepliesInitializesEmptyList() {
        Comment fullComment = new Comment(user, "Original comment", null);

        assertNotNull(fullComment.getReplies());
        assertTrue(fullComment.getReplies().isEmpty());
    }

    @Test
    public void testAddCommentAddsReply() {
        comment.addComment(user, "New reply");

        assertEquals(1, comment.getReplies().size());
        assertEquals("New reply", comment.getReplies().get(0).getComment());
        assertEquals(user, comment.getReplies().get(0).getUser());
    }

    @Test
    public void testAddCommentWithRepliesAddsNestedReply() {
        ArrayList<Comment> nestedReplies = new ArrayList<>();
        Comment nestedReply = new Comment(user, "Nested reply");
        nestedReplies.add(nestedReply);

        comment.addComment(user, "Reply with nested", nestedReplies);

        assertEquals(1, comment.getReplies().size());
        assertEquals("Reply with nested", comment.getReplies().get(0).getComment());
        assertEquals(nestedReplies, comment.getReplies().get(0).getReplies());
    }

    @Test
    public void testDeleteCommentRemovesReply() {
        Comment reply = new Comment(user, "Reply");
        comment.addReply(reply);

        comment.deleteComment(reply);

        assertTrue(comment.getReplies().isEmpty());
    }

    @Test
    public void testAddReplyAddsExistingReply() {
        Comment reply = new Comment(user, "Reply");

        comment.addReply(reply);

        assertEquals(1, comment.getReplies().size());
        assertTrue(comment.getReplies().contains(reply));
    }

    @Test
    public void testAddReplyIgnoresNull() {
        comment.addReply(null);

        assertTrue(comment.getReplies().isEmpty());
    }

    @Test
    public void testRemoveReplyByObjectRemovesReply() {
        Comment reply = new Comment(user, "Reply");
        comment.addReply(reply);

        comment.removeReply(reply);

        assertTrue(comment.getReplies().isEmpty());
    }

    @Test
    public void testRemoveReplyByIndexRemovesReply() {
        Comment reply = new Comment(user, "Reply");
        comment.addReply(reply);

        comment.removeReply(0);

        assertTrue(comment.getReplies().isEmpty());
    }

    @Test
    public void testRemoveReplyByInvalidIndexDoesNothing() {
        Comment reply = new Comment(user, "Reply");
        comment.addReply(reply);

        comment.removeReply(5);

        assertEquals(1, comment.getReplies().size());
        assertTrue(comment.getReplies().contains(reply));
    }
}
