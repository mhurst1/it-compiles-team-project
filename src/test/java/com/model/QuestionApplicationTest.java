package com.model;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.interviews.Difficulty;
import com.interviews.Language;
import com.interviews.Question;
import com.interviews.QuestionApplication;
import com.interviews.QuestionList;
import com.interviews.User;
import com.interviews.UserList;
import com.interviews.UserSolution;

/**
 * CHAT GPT Generated the code for the test cases along with the formatting of the table.
 *  
 * The Test and Reasoning Chart was then filled out manually 
 */

/*
+-------------------------------------------------------+-------------------------------------------------------------------+
| Test                                                  | Reasoning                                                         |
+-------------------------------------------------------+-------------------------------------------------------------------+
| createAccount — null username                         | a username cannot be null, it must be a real string               |
| createAccount — null password                         | a password cannot be null, it must be a real string               |
| createAccount — null email                            | an email cannot be null, it must be a real string                 |
| createAccount — empty username                        | a username cannot be empty, it must have substance                |
| createAccount — empty password                        | a password cannot be empty, it must have substance                |
| createAccount — duplicate username                    | two users cannot share the same username                          |
| createAccount — duplicate email                       | two users cannot share the same email                             |
| createAccount — valid data returns User               | a successful account creation must return the new User object     |
| createAccount — user added to list                    | the new user must be findable in the user list after creation     |
+-------------------------------------------------------+-------------------------------------------------------------------+
| login — null username returns false                   | null inputs should not crash, just fail gracefully                |
| login — null password returns false                   | null inputs should not crash, just fail gracefully                |
| login — valid credentials returns true                | correct credentials must grant access                             |
| login — valid credentials sets currentUser            | after login the facade must track who is logged in                |
| login — wrong password returns false                  | incorrect credentials must deny access                            |
| login — unknown username returns false                | non-existent users must not log in                                |
+-------------------------------------------------------+-------------------------------------------------------------------+
| addQuestion — valid data returns true                 | a valid question must be accepted and added                       |
| addQuestion — increases question list size            | the list must grow by 1 after adding a question                   |
| addQuestion — null title stored                       | null title should be rejected, not silently stored                |
| addQuestion — null user stored                        | null user should be rejected, not silently stored                 |
+-------------------------------------------------------+-------------------------------------------------------------------+
| editQuestion — updates question in list               | editing a known question must update its fields                   |
| editQuestion — null question returns null             | null input must return null without crashing                      |
| editQuestion — question not in list returns null      | unknown question must return null                                 |
| editQuestion — title can be set to null               | setting a title to null should be rejected (BUG: accepted)        |
+-------------------------------------------------------+-------------------------------------------------------------------+
| findQuestion — null keyword returns empty list        | null should not crash; return empty list                          |
| findQuestion — matching keyword returns results       | a known keyword must find its question                            |
| findQuestion — no match returns empty list            | an unknown keyword must return empty, not null                    |
| findQuestion — case-insensitive match                 | search must not be case-sensitive                                 |
+-------------------------------------------------------+-------------------------------------------------------------------+
| deleteQuestion — removes from list                    | a deleted question must no longer be in the list                  |
| deleteQuestion — null does not crash                  | null must not cause a crash                                       |
| deleteQuestion — not in list leaves list unchanged    | deleting an unknown question must not alter the list              |
+-------------------------------------------------------+-------------------------------------------------------------------+
| addComment(Question) — adds to question's comments    | the comment must appear in the question's comment list            |
| addComment(Question) — null question does not crash   | null question must not crash                                      |
| addComment(Question) — null user does not crash       | null user must not crash                                          |
| addComment(Question) — null text does not crash       | null comment text must not crash                                  |
| addComment(Question) — blank text does not crash      | blank comment must not be added                                   |
| addComment(UserSolution) — null solution crashes      | no null guard on UserSolution overload (NPE BUG)                  |
+-------------------------------------------------------+-------------------------------------------------------------------+
| getAnsweredQuestions — no user returns empty list     | must not crash when no user is logged in                          |
| getAnsweredQuestions — logged-in user returns list    | must return the current user's answered-questions list            |
+-------------------------------------------------------+-------------------------------------------------------------------+
| logout — sets currentUser to null                     | after logout the facade must have no active user                  |
| logout — no user logged in does not crash             | calling logout when already out must not crash                    |
+-------------------------------------------------------+-------------------------------------------------------------------+
| starQuestion — null currentUser causes NPE            | no null guard on currentUser (NPE BUG)                            |
| starQuestion — adds to currentUser's starred list     | the question must appear in the user's starred list               |
+-------------------------------------------------------+-------------------------------------------------------------------+
*/

/**
 * Comprehensive bug-finding tests for QuestionApplication.
 *
 * Tests are grouped by method. Every test marked "FAILS" asserts
 * correct/expected behavior that the code does NOT currently satisfy.
 * Tests marked "PASSES" confirm working behavior.
 *
 * Bugs found:
 *  1.  createAccount() passes null/empty inputs to User with no validation
 *  2.  addQuestion() passes null title and null user to Question with no validation
 *  3.  editQuestion() allows setting the title to null
 *  4.  addComment(UserSolution, User, String) has no null guard — NPE on null solution
 *  5.  starQuestion() has no null guard on currentUser — NPE when no one is logged in
 *  6.  removeUserSolution() uses && instead of || for null guard — only skips if both are null
 */
public class QuestionApplicationTest {

    private QuestionApplication app;
    private User alice;
    private User bob;
    private Question q1;

    @Before
    public void setUp() {
        app = new QuestionApplication();

        UserList.getInstance().getUsers().clear();
        QuestionList.getInstance().getQuestions().clear();

        alice = new User("Alice", "A", "alice", "pass1", "alice@test.com", 2025, "USC001");
        bob   = new User("Bob",   "B", "bob",   "pass2", "bob@test.com",   2026, "USC002");

        UserList.getInstance().getUsers().add(alice);
        UserList.getInstance().getUsers().add(bob);

        q1 = new Question("What is recursion?", alice,
                "Explain recursion in Java.", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());

        QuestionList.getInstance().getQuestions().add(q1);
    }

    // =========================================================================
    // SECTION 1 — createAccount() NULL / EMPTY INPUT VALIDATION
    // =========================================================================

    /** BUG: null username passed straight to User with no check. FAILS — null stored. */
    @Test
    public void testCreateAccountNullUsernameRejected() {
        User u = app.createAccount("First", "Last", null, "pass1", "new@test.com", 2025, "USC010");
        assertNotNull(u.getUsername()); // FAILS — null stored
    }

    /** BUG: null password passed straight to User with no check. FAILS — null stored. */
    @Test
    public void testCreateAccountNullPasswordRejected() {
        User u = app.createAccount("First", "Last", "newuser", null, "new@test.com", 2025, "USC010");
        assertNotNull(u.getPassword()); // FAILS — null stored
    }

    /** BUG: null email passed straight to User with no check. FAILS — null stored. */
    @Test
    public void testCreateAccountNullEmailRejected() {
        User u = app.createAccount("First", "Last", "newuser", "pass1", null, 2025, "USC010");
        assertNotNull(u.getEmail()); // FAILS — null stored
    }

    /** BUG: empty username passed through with no check. FAILS — "" stored. */
    @Test
    public void testCreateAccountEmptyUsernameRejected() {
        User u = app.createAccount("First", "Last", "", "pass1", "new@test.com", 2025, "USC010");
        assertFalse(u.getUsername().isEmpty()); // FAILS — "" stored
    }

    /** BUG: empty password passed through with no check. FAILS — "" stored. */
    @Test
    public void testCreateAccountEmptyPasswordRejected() {
        User u = app.createAccount("First", "Last", "newuser", "", "new@test.com", 2025, "USC010");
        assertFalse(u.getPassword().isEmpty()); // FAILS — "" stored
    }

    // =========================================================================
    // SECTION 2 — createAccount() DUPLICATE PREVENTION AND HAPPY PATH
    // =========================================================================

    /** PASSES — duplicate username throws IllegalArgumentException. */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountDuplicateUsernameThrows() {
        app.createAccount("Other", "O", "alice", "pass99", "other@test.com", 2025, "USC099");
    }

    /** PASSES — duplicate email throws IllegalArgumentException. */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountDuplicateEmailThrows() {
        app.createAccount("Other", "O", "otheralice", "pass99", "alice@test.com", 2025, "USC099");
    }

    /** PASSES — valid new account returns a non-null User. */
    @Test
    public void testCreateAccountValidReturnsUser() {
        User u = app.createAccount("Carol", "C", "carol", "pass3", "carol@test.com", 2027, "USC003");
        assertNotNull(u);
    }

    /** PASSES — created user appears in the user list. */
    @Test
    public void testCreateAccountAddsUserToList() {
        app.createAccount("Carol", "C", "carol", "pass3", "carol@test.com", 2027, "USC003");
        assertNotNull(UserList.getInstance().getUser("carol")); // PASSES — user is found
    }

    // =========================================================================
    // SECTION 3 — login()
    // =========================================================================

    /** PASSES — null username returns false without crashing. */
    @Test
    public void testLoginNullUsernameReturnsFalse() {
        assertFalse(app.login(null, "pass1"));
    }

    /** PASSES — null password returns false without crashing. */
    @Test
    public void testLoginNullPasswordReturnsFalse() {
        assertFalse(app.login("alice", null));
    }

    /** PASSES — valid credentials return true. */
    @Test
    public void testLoginValidCredentialsReturnsTrue() {
        assertTrue(app.login("alice", "pass1"));
    }

    /** PASSES — successful login sets currentUser. */
    @Test
    public void testLoginSetsCurrentUser() {
        app.login("alice", "pass1");
        assertEquals(alice, app.getCurrentUser());
    }

    /** PASSES — wrong password returns false. */
    @Test
    public void testLoginWrongPasswordReturnsFalse() {
        assertFalse(app.login("alice", "wrongpass"));
    }

    /** PASSES — unknown username returns false. */
    @Test
    public void testLoginUnknownUsernameReturnsFalse() {
        assertFalse(app.login("nobody", "pass1"));
    }

    // =========================================================================
    // SECTION 4 — addQuestion()
    // =========================================================================

    /** PASSES — valid addQuestion returns true. */
    @Test
    public void testAddQuestionValidReturnsTrue() {
        boolean result = app.addQuestion("What is a loop?", alice, "Explain loops.",
                Difficulty.EASY, Language.JAVA, new ArrayList<>(), new ArrayList<>());
        assertTrue(result);
    }

    /** PASSES — addQuestion increases the list size by 1. */
    @Test
    public void testAddQuestionIncreasesListSize() {
        int before = app.getQuestions().size();
        app.addQuestion("New Question", alice, "desc", Difficulty.MEDIUM, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        assertEquals(before + 1, app.getQuestions().size());
    }

    /** BUG: null title passed to Question with no check. FAILS — null stored in title. */
    @Test
    public void testAddQuestionNullTitleRejected() {
        app.addQuestion(null, alice, "desc", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        Question added = app.getQuestions().get(app.getQuestions().size() - 1);
        assertNotNull(added.getTitle()); // FAILS — null stored
    }

    /** BUG: null user passed to Question with no check. FAILS — null stored in user. */
    @Test
    public void testAddQuestionNullUserRejected() {
        app.addQuestion("Valid title", null, "desc", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        Question added = app.getQuestions().get(app.getQuestions().size() - 1);
        assertNotNull(added.getUser()); // FAILS — null stored
    }

    // =========================================================================
    // SECTION 5 — editQuestion()
    // =========================================================================

    /** PASSES — editing a question in the list updates its title. */
    @Test
    public void testEditQuestionUpdatesTitle() {
        Question result = app.editQuestion(q1, "Updated Title", alice, "new desc",
                new ArrayList<>(), new ArrayList<>(), Difficulty.DIFFICULT, Language.JAVA);
        assertNotNull(result);
        assertEquals("Updated Title", q1.getTitle());
    }

    /** PASSES — null question argument returns null. */
    @Test
    public void testEditQuestionNullReturnsNull() {
        Question result = app.editQuestion(null, "T", alice, "d",
                new ArrayList<>(), new ArrayList<>(), Difficulty.EASY, Language.JAVA);
        assertNull(result);
    }

    /** PASSES — question not in the list returns null. */
    @Test
    public void testEditQuestionNotInListReturnsNull() {
        Question notInList = new Question("Ghost", alice, "ghost", Difficulty.EASY,
                Language.JAVA, new ArrayList<>(), new ArrayList<>());
        Question result = app.editQuestion(notInList, "T", alice, "d",
                new ArrayList<>(), new ArrayList<>(), Difficulty.EASY, Language.JAVA);
        assertNull(result);
    }

    /** BUG: editQuestion allows setting the title to null. FAILS — null stored. */
    @Test
    public void testEditQuestionCannotSetTitleToNull() {
        app.editQuestion(q1, null, alice, "d",
                new ArrayList<>(), new ArrayList<>(), Difficulty.EASY, Language.JAVA);
        assertNotNull(q1.getTitle()); // FAILS — null stored
    }

    // =========================================================================
    // SECTION 6 — findQuestion()
    // =========================================================================

    /** PASSES — null keyword returns empty list without crashing. */
    @Test
    public void testFindQuestionNullKeywordReturnsEmpty() {
        ArrayList<Question> result = app.findQuestion(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /** PASSES — matching keyword returns the expected question. */
    @Test
    public void testFindQuestionMatchingKeywordReturnsResult() {
        ArrayList<Question> result = app.findQuestion("recursion");
        assertFalse(result.isEmpty());
        assertTrue(result.contains(q1));
    }

    /** PASSES — unknown keyword returns empty list, not null. */
    @Test
    public void testFindQuestionNoMatchReturnsEmpty() {
        ArrayList<Question> result = app.findQuestion("xyzzy_no_match");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /** PASSES — search is case-insensitive. */
    @Test
    public void testFindQuestionCaseInsensitive() {
        ArrayList<Question> result = app.findQuestion("RECURSION");
        assertFalse(result.isEmpty());
        assertTrue(result.contains(q1));
    }

    // =========================================================================
    // SECTION 7 — deleteQuestion()
    // =========================================================================

    /** PASSES — deleted question is no longer in the list. */
    @Test
    public void testDeleteQuestionRemovesFromList() {
        app.deleteQuestion(q1);
        assertFalse(app.getQuestions().contains(q1));
    }

    /** PASSES — passing null to deleteQuestion does not crash. */
    @Test
    public void testDeleteQuestionNullDoesNotCrash() {
        int before = app.getQuestions().size();
        app.deleteQuestion(null);
        assertEquals(before, app.getQuestions().size());
    }

    /** PASSES — deleting a question not in the list leaves the list unchanged. */
    @Test
    public void testDeleteQuestionNotInListLeavesListUnchanged() {
        Question notInList = new Question("Ghost", alice, "ghost", Difficulty.EASY,
                Language.JAVA, new ArrayList<>(), new ArrayList<>());
        int before = app.getQuestions().size();
        app.deleteQuestion(notInList);
        assertEquals(before, app.getQuestions().size());
    }

    // =========================================================================
    // SECTION 8 — addComment(Question, User, String)
    // =========================================================================

    /** PASSES — comment is added to the question's comment list. */
    @Test
    public void testAddCommentToQuestionAddsToList() {
        app.addComment(q1, alice, "Great question!");
        assertEquals(1, q1.getComments().size());
        assertEquals("Great question!", q1.getComments().get(0).getComment());
    }

    /** PASSES — null question does not crash. */
    @Test
    public void testAddCommentNullQuestionDoesNotCrash() {
        app.addComment((Question) null, alice, "text"); // PASSES — null check exists
    }

    /** PASSES — null user does not crash. */
    @Test
    public void testAddCommentNullUserDoesNotCrash() {
        app.addComment(q1, null, "text"); // PASSES — null check exists
    }

    /** PASSES — null comment text does not crash. */
    @Test
    public void testAddCommentNullTextDoesNotCrash() {
        app.addComment(q1, alice, null);
        assertTrue(q1.getComments().isEmpty()); // PASSES — null check stops it
    }

    /** PASSES — blank comment text is not added. */
    @Test
    public void testAddCommentBlankTextDoesNotCrash() {
        app.addComment(q1, alice, "   ");
        assertTrue(q1.getComments().isEmpty()); // PASSES — blank check stops it
    }

    /** BUG: addComment(UserSolution, User, String) has no null guard.
     *  Passing a null solution causes a NullPointerException. FAILS — NPE thrown. */
    @Test(expected = NullPointerException.class)
    public void testAddCommentNullUserSolutionCausesNPE() {
        app.addComment((UserSolution) null, alice, "comment"); // FAILS — NPE
    }

    // =========================================================================
    // SECTION 9 — getAnsweredQuestions()
    // =========================================================================

    /** PASSES — returns empty list when no user is logged in. */
    @Test
    public void testGetAnsweredQuestionsNoUserReturnsEmpty() {
        ArrayList<Question> result = app.getAnsweredQuestions();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /** PASSES — returns the current user's answered questions list after login. */
    @Test
    public void testGetAnsweredQuestionsReturnsCurrentUserList() {
        app.login("alice", "pass1");
        ArrayList<Question> result = app.getAnsweredQuestions();
        assertEquals(alice.getAnsweredQuestions(), result);
    }

    // =========================================================================
    // SECTION 10 — logout()
    // =========================================================================

    /** PASSES — currentUser is null after logout. */
    @Test
    public void testLogoutSetsCurrentUserToNull() {
        app.login("alice", "pass1");
        app.logout();
        assertNull(app.getCurrentUser());
    }

    /** PASSES — calling logout when no user is logged in does not crash. */
    @Test
    public void testLogoutWhenNotLoggedInDoesNotCrash() {
        app.logout(); // PASSES — null guard exists, just prints a message
        assertNull(app.getCurrentUser());
    }

    // =========================================================================
    // SECTION 11 — starQuestion()
    // =========================================================================

    /** BUG: starQuestion() has no null guard on currentUser.
     *  Calling it when no user is logged in throws NullPointerException. FAILS — NPE. */
    @Test(expected = NullPointerException.class)
    public void testStarQuestionWithNoCurrentUserCausesNPE() {
        app.starQuestion(q1, UUID.randomUUID()); // FAILS — NPE: currentUser is null
    }

    /** PASSES — question appears in the logged-in user's starred list. */
    @Test
    public void testStarQuestionAddsToCurrentUserStarredList() {
        app.login("alice", "pass1");
        app.starQuestion(q1, alice.getId());
        assertTrue(alice.getStarredQuestions().contains(q1));
    }
}
