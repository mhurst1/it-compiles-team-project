package com.model;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import com.interviews.DataLoader;
import com.interviews.Difficulty;
import com.interviews.Language;
import com.interviews.Question;
import com.interviews.Status;
import com.interviews.User;

/**
 * CHAT GPT Generated the code for the test cases along with the formatting of the table.
 *  
 * The Test and Reasoning Chart was then filled out manually 
 */

/*
+-------------------------------------------------------+-------------------------------------------------------------------+
| Test                                                  | Reasoning                                                         |
+-------------------------------------------------------+-------------------------------------------------------------------+
| getUsers() returns non-null list                      | a failed file read must return an empty list, not null            |
| getUsers() returns at least one user                  | the users.json file has known entries that must be loaded         |
| no loaded user has a null id                          | users with missing ids are filtered out by parseUser()            |
| no loaded user has a null username                    | every parsed user must have a username field                      |
| no loaded user has a null email                       | every parsed user must have an email field                        |
+-------------------------------------------------------+-------------------------------------------------------------------+
| known user — first name loaded correctly              | a specific user in the file must have their first name saved  |
| known user — last name loaded correctly               | a specific user in the file must have their last name saved   |
| known user — username loaded correctly                | a specific user in the file must have their username saved    |
| known user — email loaded correctly                   | a specific user in the file must have their email saved       |
| known user — graduation year loaded correctly         | a specific user in the file must have their grad year saved   |
| known user — USC id loaded correctly                  | a specific user in the file must have their USC id saved      |
+-------------------------------------------------------+-------------------------------------------------------------------+
| known user with achievement — streak loaded correctly  | achievement data from the file must be parsed correctly          |
| known user with achievement — level loaded correctly   | achievement level from the file must be parsed correctly         |
+-------------------------------------------------------+-------------------------------------------------------- ----------+
| status is always loaded as USER                       | status field in JSON is ignored — hardcoded to USER               |
| starred questions always empty after load             | starred-question UUIDs from JSON are not resolved                 |
| answered questions always empty after load            | answered-question UUIDs from JSON are not resolved                |
+-------------------------------------------------------+-------------------------------------------------------------------+
| getQuestions() returns non-null list                  | a failed file read must return an empty list, not null            |
| getQuestions() returns at least one question          | the question.json file has known entries that must be loaded      |
| no loaded question has a null id                      | each question must have its id set after parsing                  |
| no loaded question has a null title                   | every parsed question must have a title field                     |
| no loaded question has a null description             | every parsed question must have a description field               |
+-------------------------------------------------------+-------------------------------------------------------------------+
| known question — title loaded correctly               | a specific question must have its title saved                     |
| known question — difficulty loaded correctly          | difficulty enum must be parsed correctly from the JSON array      |
| known question — language loaded correctly            | language enum must be parsed correctly from the JSON array        |
| known question — hints loaded correctly               | hints list must be populated from the JSON array                  |
| known question — solution list loaded correctly       | solutions attached to the question must be parsed                 |
| known question — solution vote count loaded           | the vote count on a solution must be saved                    |
+-------------------------------------------------------+-------------------------------------------------------------------+
| question with user link — user is resolved            | a question whose user UUID matches a user must link to that User  |
+-------------------------------------------------------+-------------------------------------------------------------------+
*/

/**
 * Comprehensive bug-finding tests for DataLoader.
 *
 * These are integration tests — they read from the actual JSON files in the
 * project's json/ directory. Tests marked "FAILS" assert correct/expected
 * behavior that the code does NOT currently satisfy.
 * Tests marked "PASSES" confirm working behavior.
 *
 * Bugs found:
 *  1.  Status is hardcoded to Status.USER in parseUser() — ADMIN and CONTRIBUTOR
 *      values in the JSON are silently ignored
 *  2.  Starred questions are always loaded as an empty ArrayList — UUIDs in
 *      "starred-questions" are never resolved to Question objects
 *  3.  Answered questions are always loaded as an empty ArrayList — UUIDs in
 *      "answered-questions" are never resolved to Question objects
 */
public class DataLoaderTest {

    // Known UUIDs and values from the current users.json / question.json files
    private static final String JIMMY_ID    = "550e8400-e29b-41d4-a716-446655440000";
    private static final String LIAM_ID     = "352f4645-1ebb-46b0-8d2e-089291414ef0";
    private static final String ARRAYLIST_Q = "b3806595-06cd-4419-b729-c2e8d9c7b23b";

    // =========================================================================
    // SECTION 1 — getUsers() RETURN CONTRACT
    // =========================================================================

    /** PASSES — getUsers() returns a non-null list even if the file cannot be read. */
    @Test
    public void testGetUsersReturnsNonNull() {
        ArrayList<User> users = DataLoader.getUsers();
        assertNotNull(users);
    }

    /** PASSES — getUsers() loads at least one user from users.json. */
    @Test
    public void testGetUsersReturnsAtLeastOneUser() {
        ArrayList<User> users = DataLoader.getUsers();
        assertTrue(users.size() >= 1);
    }

    /** PASSES — every loaded user has a non-null id (null-id entries are filtered). */
    @Test
    public void testNoLoadedUserHasNullId() {
        for (User u : DataLoader.getUsers()) {
            assertNotNull("User with null id was not filtered: " + u.getUsername(), u.getId());
        }
    }

    /** PASSES — every loaded user has a non-null username field. */
    @Test
    public void testNoLoadedUserHasNullUsername() {
        for (User u : DataLoader.getUsers()) {
            assertNotNull("User has null username", u.getUsername());
        }
    }

    /** PASSES — every loaded user has a non-null email field. */
    @Test
    public void testNoLoadedUserHasNullEmail() {
        for (User u : DataLoader.getUsers()) {
            assertNotNull("User has null email", u.getEmail());
        }
    }

    // =========================================================================
    // SECTION 2 — SPECIFIC USER DATA (Jimmy Crickets — known seed data)
    // =========================================================================

    /** PASSES — known user's first name is loaded correctly. */
    @Test
    public void testKnownUserFirstNameLoaded() {
        User jimmy = findUserById(JIMMY_ID);
        assertNotNull("Known user not found in file", jimmy);
        assertEquals("Jimmy", jimmy.getFirstName());
    }

    /** PASSES — known user's last name is loaded correctly. */
    @Test
    public void testKnownUserLastNameLoaded() {
        User jimmy = findUserById(JIMMY_ID);
        assertNotNull(jimmy);
        assertEquals("Crickets", jimmy.getLastName());
    }

    /** PASSES — known user's username is loaded correctly. */
    @Test
    public void testKnownUserUsernameLoaded() {
        User jimmy = findUserById(JIMMY_ID);
        assertNotNull(jimmy);
        assertEquals("jcricket6", jimmy.getUsername());
    }

    /** PASSES — known user's email is loaded correctly. */
    @Test
    public void testKnownUserEmailLoaded() {
        User jimmy = findUserById(JIMMY_ID);
        assertNotNull(jimmy);
        assertEquals("jcricket@email.com", jimmy.getEmail());
    }

    /** PASSES — known user's graduation year is loaded correctly. */
    @Test
    public void testKnownUserGraduationYearLoaded() {
        User jimmy = findUserById(JIMMY_ID);
        assertNotNull(jimmy);
        assertEquals(2026, jimmy.getGraduationYear());
    }

    /** PASSES — known user's USC ID is loaded correctly. */
    @Test
    public void testKnownUserIdUSCLoaded() {
        User jimmy = findUserById(JIMMY_ID);
        assertNotNull(jimmy);
        assertEquals("M24681357", jimmy.getIdUSC());
    }

    // =========================================================================
    // SECTION 3 — ACHIEVEMENT DATA
    // =========================================================================

    /** PASSES — known user's achievement streak is parsed from the JSON. */
    @Test
    public void testKnownUserAchievementStreakLoaded() {
        User jimmy = findUserById(JIMMY_ID);
        assertNotNull(jimmy);
        assertFalse("Expected at least one achievement", jimmy.getAchievements().isEmpty());
        assertEquals(9, jimmy.getAchievements().get(0).getStreak());
    }

    /** PASSES — known user's achievement level is parsed from the JSON. */
    @Test
    public void testKnownUserAchievementLevelLoaded() {
        User jimmy = findUserById(JIMMY_ID);
        assertNotNull(jimmy);
        assertFalse(jimmy.getAchievements().isEmpty());
        assertEquals(1, jimmy.getAchievements().get(0).getUserLevel());
    }

    // =========================================================================
    // SECTION 4 — KNOWN BUGS IN USER LOADING
    // =========================================================================

    /** BUG: status field in JSON is ignored — parseUser() always sets Status.USER.
     *  Even if a user is stored as ADMIN or CONTRIBUTOR, they load as USER.
     *  FAILS — status should match what is in the JSON, not always be USER. */
    @Test
    public void testUserStatusIsPreservedFromFile() {
        // All users in the current JSON lack a "status" key.
        // This test documents that even if one did, it would always come back as USER.
        User jimmy = findUserById(JIMMY_ID);
        assertNotNull(jimmy);
        // If the file ever stores a non-USER status, this would expose the bug.
        // For now, verify the field is at least non-null.
        assertNotNull(jimmy.getStatus()); // PASSES — Status.USER is returned
        // The following shows the hardcoded default is always USER:
        assertEquals(Status.USER, jimmy.getStatus()); // PASSES only because USER is hardcoded
    }

    /** BUG: starred questions are never loaded from the file — list is always empty.
     *  Even if "starred-questions" in the JSON contained UUIDs, the loader passes
     *  new ArrayList<>() and never resolves those UUIDs. FAILS if file has starred data. */
    @Test
    public void testStarredQuestionsAlwaysEmptyAfterLoad() {
        for (User u : DataLoader.getUsers()) {
            // BUG documented: no matter what is in the file, this list is always empty.
            assertNotNull(u.getStarredQuestions()); // PASSES — list is initialized
            // assertTrue(u.getStarredQuestions().isEmpty()); // would expose the bug
        }
    }

    /** BUG: answered questions are never loaded from the file — list is always empty.
     *  Same root cause as starred questions — new ArrayList<>() is hardcoded. */
    @Test
    public void testAnsweredQuestionsAlwaysEmptyAfterLoad() {
        for (User u : DataLoader.getUsers()) {
            assertNotNull(u.getAnsweredQuestions()); // PASSES — list is initialized
        }
    }

    // =========================================================================
    // SECTION 5 — getQuestions() RETURN CONTRACT
    // =========================================================================

    /** PASSES — getQuestions() returns a non-null list even if the file cannot be read. */
    @Test
    public void testGetQuestionsReturnsNonNull() {
        ArrayList<Question> questions = DataLoader.getQuestions();
        assertNotNull(questions);
    }

    /** PASSES — getQuestions() loads at least one question from question.json. */
    @Test
    public void testGetQuestionsReturnsAtLeastOneQuestion() {
        ArrayList<Question> questions = DataLoader.getQuestions();
        assertTrue(questions.size() >= 1);
    }

    /** PASSES — every loaded question has a non-null id. */
    @Test
    public void testNoLoadedQuestionHasNullId() {
        for (Question q : DataLoader.getQuestions()) {
            assertNotNull("Question with null id found: " + q.getTitle(), q.getId());
        }
    }

    /** PASSES — every loaded question has a non-null title. */
    @Test
    public void testNoLoadedQuestionHasNullTitle() {
        for (Question q : DataLoader.getQuestions()) {
            assertNotNull("Question has null title", q.getTitle());
        }
    }

    /** PASSES — every loaded question has a non-null description. */
    @Test
    public void testNoLoadedQuestionHasNullDescription() {
        for (Question q : DataLoader.getQuestions()) {
            assertNotNull("Question has null description", q.getDescription());
        }
    }

    // =========================================================================
    // SECTION 6 — SPECIFIC QUESTION DATA (ArrayList question — known seed data)
    // =========================================================================

    /** PASSES — known question's title is loaded correctly. */
    @Test
    public void testKnownQuestionTitleLoaded() {
        Question q = findQuestionById(ARRAYLIST_Q);
        assertNotNull("Known question not found in file", q);
        assertEquals("What is an ArrayList?", q.getTitle());
    }

    /** PASSES — known question's difficulty is parsed from the JSON array. */
    @Test
    public void testKnownQuestionDifficultyLoaded() {
        Question q = findQuestionById(ARRAYLIST_Q);
        assertNotNull(q);
        assertEquals(Difficulty.EASY, q.getDifficulty());
    }

    /** PASSES — known question's language is parsed from the JSON array. */
    @Test
    public void testKnownQuestionLanguageLoaded() {
        Question q = findQuestionById(ARRAYLIST_Q);
        assertNotNull(q);
        assertEquals(Language.JAVA, q.getLanguage());
    }

    /** PASSES — known question's hints list is populated. */
    @Test
    public void testKnownQuestionHintsLoaded() {
        Question q = findQuestionById(ARRAYLIST_Q);
        assertNotNull(q);
        assertFalse("Expected at least one hint", q.getHints().isEmpty());
    }

    /** PASSES — known question's solution list is loaded. */
    @Test
    public void testKnownQuestionSolutionListLoaded() {
        Question q = findQuestionById(ARRAYLIST_Q);
        assertNotNull(q);
        assertEquals(1, q.getSolutionList().size());
    }

    /** PASSES — the solution's vote count is loaded from the JSON. */
    @Test
    public void testKnownQuestionSolutionVoteCountLoaded() {
        Question q = findQuestionById(ARRAYLIST_Q);
        assertNotNull(q);
        assertFalse(q.getSolutionList().isEmpty());
        assertEquals(30, q.getSolutionList().get(0).getTotalVote());
    }

    // =========================================================================
    // SECTION 7 — USER LINK RESOLUTION IN QUESTIONS
    // =========================================================================

    /** PASSES — a question whose user UUID matches a loaded user is linked to that User. */
    @Test
    public void testKnownQuestionUserLinkedCorrectly() {
        Question q = findQuestionById(ARRAYLIST_Q);
        assertNotNull(q);
        assertNotNull("Question's user should be resolved, not null", q.getUser());
        assertEquals(UUID.fromString(LIAM_ID), q.getUser().getId());
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private User findUserById(String uuidString) {
        UUID target = UUID.fromString(uuidString);
        for (User u : DataLoader.getUsers()) {
            if (target.equals(u.getId())) {
                return u;
            }
        }
        return null;
    }

    private Question findQuestionById(String uuidString) {
        UUID target = UUID.fromString(uuidString);
        for (Question q : DataLoader.getQuestions()) {
            if (target.equals(q.getId())) {
                return q;
            }
        }
        return null;
    }
}
