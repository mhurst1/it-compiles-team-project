package com.model;

import com.interviews.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * CHAT GPT Generated the code for the test cases along with the formatting of the table.
 *  
 * The Test and Reasoning Chart was then filled out manually 
 */

/*
+-------------------------------------------------------+-------------------------------------------------------------------+
| Test                                                  |Reasoning                                                          |
+-------------------------------------------------------+-------------------------------------------------------------------+
| getInstance() called twice                            | a user should not be able to get an instance twice
+-------------------------------------------------------+-------------------------------------------------------------------+
| getQuestions() on initialized list                    | should return a non-null list as getInstance should initialize it
| getQuestions() after clear()                          | should return an empty list as the list of questions is cleared
+-------------------------------------------------------+-------------------------------------------------------------------+
| addQuestion("T", user, "d", EASY, JAVA, [], [])       | Base Case should store all information from a question correctly
| addQuestion("My Question", user, ...)                 | Should store the title within a question correctly
| addQuestion(..., "My Desc", ...)                      | Should store the description within a question correcty
| addQuestion(..., testUser, ...)                       | Should store the user within a question correcty
| addQuestion(..., MEDIUM, ...)                         | Should store the difficulty within a question correcty
| addQuestion(..., PYTHON, ...)                         | Should store the language within a question correcty
| addQuestion() called twice                            | Should store each question that is added independently and not merge the two
| addQuestion(..., hints=["Think about loops"], ...)    | Should store all hints within a question correctly
| addQuestion(...) → getId()                            | Null Id should not throw a null pointer exception
+-------------------------------------------------------+-------------------------------------------------------------------+
| getQuestions("Loops"), title = "Java Loops"           | questions should still return even if the title is 2 parts and it is reading the second part
| getQuestions("loops"), title = "Java Loops"           | The Search should ignore case sensitivity when returning the questions
| getQuestions("Python"), title = "Java Loops"          | Any questions with Python in the title should return.
| getQuestions("Java"),                                 | Titles that have Java in them should return and anything else shouldnt
| getQuestions("Script"), title = "JavaScript"          | when part of the name is within the question it should still return the question
| getQuestions(null)                                    | when getting a null for a question the code shouldn't break with a null pointer
+-------------------------------------------------------+-------------------------------------------------------------------+
| deleteQuestion(q), q is in the list                   | question is successfully removed
| deleteQuestion(q), list has 2 questions               | only the targeted question is removed
| deleteQuestion(q), q not in list                      | nothing should be removed if the question is not within the list
| deleteQuestion(copy), copy has same UUID as original  | only the copy is removed and the original is still within the questionList
+-------------------------------------------------------+-------------------------------------------------------------------+
*/

/**
 * Bug-finding tests for QuestionList.
 *
 * Tests marked FAILS assert correct/expected behavior the code does NOT currently satisfy.
 * Tests marked PASSES confirm working behavior.
 *
 * Bugs found:
 *  1. addQuestion() the Question constructor that never sets id —
 *     getId() returns null on every question added through this method
 * 
 *  2. getQuestions(String keyword) has no null case written and will throw
 *     a null pointer exception
 * 
 *  3. deleteQuestion() when deleting a question and trying to recreate the question with the same
 *     ID it fails because the original is never deleted
 */

public class QuestionListTest {

    private QuestionList questionList;
    private User testUser;

    @Before
    public void setUp() {
        questionList = QuestionList.getInstance();
        questionList.getQuestions().clear();

        testUser = new User("Test", "User", "testuser", "pass123", "test@email.com", 2025, "USC001");
    }

    // =========================================================================
    // SECTION 1 — SINGLETON BEHAVIOR
    // =========================================================================

    /** PASSES — getInstance() always returns the exact same object */
    @Test
    public void testGetInstanceReturnsSameObject() {
        QuestionList first = QuestionList.getInstance();
        QuestionList second = QuestionList.getInstance();
        assertTrue(first == second);
    }

    // =========================================================================
    // SECTION 2 — getQuestions() (no args)
    // =========================================================================

    /** PASSES — getQuestions() is never null */
    @Test
    public void testGetQuestionsReturnsNonNull() {
        assertNotNull(questionList.getQuestions());
    }

    /** PASSES — list is empty after setUp clears it */
    @Test
    public void testGetQuestionsIsEmptyAfterClear() {
        assertTrue(questionList.getQuestions().isEmpty());
    }

    // =========================================================================
    // SECTION 3 — addQuestion()
    // =========================================================================

    /** PASSES — adding one question grows the list to size 1 */
    @Test
    public void testAddQuestionIncreasesSize() {
        questionList.addQuestion("Title", testUser, "desc", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        assertEquals(1, questionList.getQuestions().size());
    }

    /** PASSES — title passed to addQuestion is stored on the question */
    @Test
    public void testAddQuestionStoresCorrectTitle() {
        questionList.addQuestion("My Question", testUser, "desc", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        assertEquals("My Question", questionList.getQuestions().get(0).getTitle());
    }

    /** PASSES — description passed to addQuestion is stored on the question */
    @Test
    public void testAddQuestionStoresCorrectDescription() {
        questionList.addQuestion("Title", testUser, "My Desc", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        assertEquals("My Desc", questionList.getQuestions().get(0).getDescription());
    }

    /** PASSES — user passed to addQuestion is stored as the question creator */
    @Test
    public void testAddQuestionStoresCorrectUser() {
        questionList.addQuestion("Title", testUser, "desc", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        assertEquals(testUser, questionList.getQuestions().get(0).getUser());
    }

    /** PASSES — difficulty enum passed to addQuestion is stored on the question */
    @Test
    public void testAddQuestionStoresCorrectDifficulty() {
        questionList.addQuestion("Title", testUser, "desc", Difficulty.MEDIUM, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        assertEquals(Difficulty.MEDIUM, questionList.getQuestions().get(0).getDifficulty());
    }

    /** PASSES — language enum passed to addQuestion is stored on the question */
    @Test
    public void testAddQuestionStoresCorrectLanguage() {
        questionList.addQuestion("Title", testUser, "desc", Difficulty.EASY, Language.PYTHON,
                new ArrayList<>(), new ArrayList<>());
        assertEquals(Language.PYTHON, questionList.getQuestions().get(0).getLanguage());
    }

    /** PASSES — adding two questions grows the list to size 2 */
    @Test
    public void testAddMultipleQuestionsIncreasesSize() {
        questionList.addQuestion("Q1", testUser, "d1", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        questionList.addQuestion("Q2", testUser, "d2", Difficulty.MEDIUM, Language.PYTHON,
                new ArrayList<>(), new ArrayList<>());
        assertEquals(2, questionList.getQuestions().size());
    }

    /** PASSES — hints list passed to addQuestion is stored on the question */
    @Test
    public void testAddQuestionStoresHints() {
        ArrayList<String> hints = new ArrayList<>();
        hints.add("Think about loops");
        questionList.addQuestion("Title", testUser, "desc", Difficulty.EASY, Language.JAVA,
                hints, new ArrayList<>());
        assertEquals(1, questionList.getQuestions().get(0).getHints().size());
        assertEquals("Think about loops", questionList.getQuestions().get(0).getHints().get(0));
    }

    /**
     * BUG: addQuestion() internally uses the Question constructor that never assigns id.
     * Every question added through this method has getId() == null.
     * FAILS — assertNotNull fails because id is null.
     */
    @Test
    public void testAddedQuestionHasNonNullId() {
        questionList.addQuestion("Title", testUser, "desc", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        assertNotNull(questionList.getQuestions().get(0).getId()); // FAILS — id is null
    }

    // =========================================================================
    // SECTION 4 — getQuestions(String keyword)
    // =========================================================================

    /** PASSES — keyword found in a title returns that question */
    @Test
    public void testGetQuestionsKeywordReturnsMatch() {
        questionList.addQuestion("Java Loops", testUser, "desc", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        ArrayList<Question> results = questionList.getQuestions("Loops");
        assertEquals(1, results.size());
    }

    /** PASSES — keyword search is case-insensitive */
    @Test
    public void testGetQuestionsKeywordIsCaseInsensitive() {
        questionList.addQuestion("Java Loops", testUser, "desc", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        ArrayList<Question> results = questionList.getQuestions("loops");
        assertEquals(1, results.size());
    }

    /** PASSES — keyword not present in any title returns an empty list */
    @Test
    public void testGetQuestionsKeywordNoMatchReturnsEmptyList() {
        questionList.addQuestion("Java Loops", testUser, "desc", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        ArrayList<Question> results = questionList.getQuestions("Python");
        assertTrue(results.isEmpty());
    }

    /** PASSES — keyword shared by multiple titles returns all matching questions */
    @Test
    public void testGetQuestionsKeywordReturnsMultipleMatches() {
        questionList.addQuestion("Java Loops", testUser, "desc", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        questionList.addQuestion("Java Methods", testUser, "desc", Difficulty.MEDIUM, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        questionList.addQuestion("Python Basics", testUser, "desc", Difficulty.EASY, Language.PYTHON,
                new ArrayList<>(), new ArrayList<>());
        ArrayList<Question> results = questionList.getQuestions("Java");
        assertEquals(2, results.size());
    }

    /** PASSES — a substring of a title still counts as a match */
    @Test
    public void testGetQuestionsKeywordPartialMatch() {
        questionList.addQuestion("JavaScript Closures", testUser, "desc", Difficulty.HARD,
                Language.JAVASCRIPT, new ArrayList<>(), new ArrayList<>());
        ArrayList<Question> results = questionList.getQuestions("Script");
        assertEquals(1, results.size());
    }

    /**
     * BUG: getQuestions(null) throws NullPointerException.
     * keyword.toLowerCase() is called unconditionally inside the for-each loop.
     * FAILS — NPE is thrown instead of returning an empty list gracefully.
     */
    @Test
    public void testGetQuestionsNullKeywordDoesNotCrash() {
        questionList.addQuestion("Java Loops", testUser, "desc", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        ArrayList<Question> results = questionList.getQuestions(null); // FAILS — NPE
        assertNotNull(results);
    }

    // =========================================================================
    // SECTION 5 — deleteQuestion()
    // =========================================================================

    /** PASSES — deleting an existing question removes it from the list */
    @Test
    public void testDeleteQuestionRemovesFromList() {
        questionList.addQuestion("To Delete", testUser, "desc", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        Question q = questionList.getQuestions().get(0);
        questionList.deleteQuestion(q);
        assertFalse(questionList.getQuestions().contains(q));
    }

    /** PASSES — after deleting one of two questions, one remains */
    @Test
    public void testDeleteQuestionDecreasesSize() {
        questionList.addQuestion("Q1", testUser, "d1", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        questionList.addQuestion("Q2", testUser, "d2", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        Question q = questionList.getQuestions().get(0);
        questionList.deleteQuestion(q);
        assertEquals(1, questionList.getQuestions().size());
    }

    /** PASSES — deleting a question not in the list does not throw or change size */
    @Test
    public void testDeleteQuestionNotInListIsNoOp() {
        Question q = new Question("Not In List", testUser, "desc",
                new ArrayList<>(), new ArrayList<>(), Difficulty.EASY, Language.JAVA);
        questionList.deleteQuestion(q);
        assertEquals(0, questionList.getQuestions().size());
    }

    /**
     * BUG: deleteQuestion() uses ArrayList.remove() which checks reference equality.
     * A new Question object with the same UUID is a different reference and is NOT removed.
     * FAILS — original question is still present in the list after deleteQuestion(copy).
     */
    @Test
    public void testDeleteQuestionByEquivalentObjectRemovesQuestion() {
        // Use constructor that assigns a UUID so we can set the same id on the copy
        Question original = new Question("Same Title", testUser, "desc", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        questionList.getQuestions().add(original);

        // Build a second object and give it the same UUID — logically the same question
        Question copy = new Question("Same Title", testUser, "desc",
                new ArrayList<>(), new ArrayList<>(), Difficulty.EASY, Language.JAVA);
        copy.setId(original.getId());

        questionList.deleteQuestion(copy);
        assertFalse(questionList.getQuestions().contains(original)); // FAILS — original still present
    }
}
