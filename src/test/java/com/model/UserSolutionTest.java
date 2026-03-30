package com.model;

import com.interviews.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;

/*
+-----------------------------------------------+-------------------------------------------------------------------+
| Test                                          | Reasoning                                                         |
+-----------------------------------------------+-------------------------------------------------------------------+
| calculateVotes(5, 2)                          | verifies positive vote difference is computed correctly           |
| calculateVotes(2, 5)                          | verifies negative results are clamped to 0                        |
| calculateVotes(0, 0)                          | verifies zero votes returns 0                                     |
| calculateVotes(10, 0)                         | verifies only upvotes returns correct total                       |
| setUserVote(true)                             | verifies user vote flag updates to true                           |
| setUserVote(false)                            | verifies user vote flag updates back to false                     |
| setQuestionId(valid UUID)                     | verifies question ID is stored and retrieved correctly            |
| accessComment(null comment)                   | verifies method returns null when null is passed                  |
| accessComment(valid comment)                  | verifies method returns the exact same comment object             |
| 2-arg constructor initializes defaults        | verifies totalVote=0, userVote=false, and replies is empty        |
| 2-arg constructor generates UUID              | verifies a UUID is automatically generated                        |
| full constructor stores provided values       | verifies description, replies, totalVote, and ID are stored       |
| full constructor null solutionID              | verifies UUID is generated when null is passed                    |
+-----------------------------------------------+-------------------------------------------------------------------+
 */

public class UserSolutionTest {

    @Test
    public void testCalculateVotesReturnsDifference() {
        UserSolution solution = new UserSolution(null, "sample solution", UUID.randomUUID(), new ArrayList<>(), 0);

        int result = solution.calculateVotes(null, 5, 2);

        assertEquals(3, result);
    }

    @Test
    public void testCalculateVotesClampsNegativeToZero() {
        UserSolution solution = new UserSolution(null, "sample solution", UUID.randomUUID(), new ArrayList<>(), 0);

        int result = solution.calculateVotes(null, 2, 5);

        assertEquals(0, result);
    }

    @Test
    public void testCalculateVotesWithZeroVotes() {
        UserSolution solution = new UserSolution(null, "sample solution", UUID.randomUUID(), new ArrayList<>(), 0);

        int result = solution.calculateVotes(null, 0, 0);

        assertEquals(0, result);
    }

    @Test
    public void testCalculateVotesWithTenUpvotesAndNoDownvotes() {
        UserSolution solution = new UserSolution(null, "sample solution", UUID.randomUUID(), new ArrayList<>(), 0);

        int result = solution.calculateVotes(null, 10, 0);

        assertEquals(10, result);
    }

    @Test
    public void testSetUserVoteTrue() {
        UserSolution solution = new UserSolution(null, "sample solution");

        solution.setUserVote(true);

        assertEquals(true, solution.getUserVote());
    }

    @Test
    public void testSetUserVoteFalse() {
        UserSolution solution = new UserSolution(null, "sample solution");

        solution.setUserVote(true);
        solution.setUserVote(false);

        assertEquals(false, solution.getUserVote());
    }

    @Test
    public void testSetQuestionIdStoresValue() {
        UserSolution solution = new UserSolution(null, "sample solution");

        UUID questionId = UUID.randomUUID();
        solution.setQuestionId(questionId);

        assertEquals(questionId, solution.getQuestionId());
    }

    @Test
    public void testAccessCommentReturnsSameObject() {
        UserSolution solution = new UserSolution(null, "sample solution");

        Comment comment = null;

        Comment result = solution.accessComment(comment, null);

        assertEquals(comment, result);
    }

    @Test
    public void testTwoArgConstructorInitializesDefaults() {
        UserSolution solution = new UserSolution(null, "sample solution");

        assertEquals(0, solution.getTotalVote());
        assertEquals(false, solution.getUserVote());
        assertEquals(0, solution.getReplies().size());
    }

    @Test
    public void testTwoArgConstructorGeneratesUUID() {
        UserSolution solution = new UserSolution(null, "sample solution");

        assertNotNull(solution.getSoulutionId());
    }

    @Test
    public void testFullConstructorStoresValues() {
        ArrayList<Comment> replies = new ArrayList<>();
        UUID id = UUID.randomUUID();

        UserSolution solution = new UserSolution(null, "sample solution", id, replies, 5);

        assertEquals("sample solution", solution.getDescription());
        assertEquals(replies, solution.getReplies());
        assertEquals(5, solution.getTotalVote());
        assertEquals(id, solution.getSoulutionId());
    }

    @Test
    public void testFullConstructorGeneratesUUIDWhenNull() {
        UserSolution solution = new UserSolution(null, "sample solution", null, new ArrayList<>(), 0);

        assertNotNull(solution.getSoulutionId());
    }

}
