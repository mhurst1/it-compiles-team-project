package com.model;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.interviews.Difficulty;
import com.interviews.Language;
import com.interviews.Question;
import com.interviews.User;
import com.interviews.UserSolution;

/*
+-------------------------------------------------------+-------------------------------------------------------------------+
| Test                                                  | Reasoning                                                         |
+-------------------------------------------------------+-------------------------------------------------------------------+
| constructor 1 — id is null                            | first must set the id and not leave it null         |
| constructor 1 — title stored correctly                | constructor must store the title                                  |
| constructor 1 — user stored correctly                 | constructor must store the user                                   |
| constructor 1 — description stored correctly          | constructor must store the description                             |
| constructor 1 — comments initialized to empty list    | comments list must be ready to use, not null                      |
| constructor 1 — solutionList initialized to empty     | solution list must be ready to use, not null                      |
+-------------------------------------------------------+-------------------------------------------------------------------+
| constructor 2 — passed UUID is ignored                | constructor(UUID) ignores its argument and generates a new one    |
+-------------------------------------------------------+-------------------------------------------------------------------+
| constructor 3 — id is not null                        | this constructor must assign a UUID                               |
| constructor 3 — title stored correctly                | constructor must store the title                                  |
| constructor 3 — null title stored without error       | null title should be rejected                                     |
| constructor 3 — null user stored without error        | null user should be rejected                                      |
+-------------------------------------------------------+-------------------------------------------------------------------+
| setTitle updates the title                            | setter must change the stored title                               |
| setTitle(null) title = null                           | null title should be rejected                                     |
| setDescription updates the description                | setter must change the stored description                         |
| setDescription(null) description = null               | null description should be rejected                               |
| setUser updates the user                              | setter must change the stored user                                |
| setUser(null) user = null                             | null user should be rejected                                      |
| setDifficulty updates the difficulty                  | setter must change the stored difficulty                          |
| setLanguage updates the language                      | setter must change the stored language                            |
| setHint updates the hints list                        | setter must replace the stored hints                              |
| setQuestionContent updates the content list           | setter must replace the stored sections                           |
| setId updates the id                                  | setter must change the stored UUID                                |
+-------------------------------------------------------+-------------------------------------------------------------------+
| getSolutionList returns empty list                    | no solutions should exist on a fresh question                     |
| getSolutionList syncs question id into solutions      | each solution's questionId must match the question's id           |
| getComments returns empty list initially              | no comments should exist on a fresh question                      |
+-------------------------------------------------------+-------------------------------------------------------------------+
| getGivenSolutionImg returns null     (NO DATA YET)    | field is never initialized (No Data)            |
| getGivenSolutionText returns null   (NO DATA YET)     | field is never initialized (No Data)           |
+-------------------------------------------------------+-------------------------------------------------------------------+
| searchQuestions — null keyword                        | null must be handled to not throw a Null Pointer                                   |
| searchQuestions — matching title                      | a match must not cause an error                                   |
| searchQuestions — non-matching title                  | a tite that does not match must not cause an error                                    |
| searchQuestions — null title in question causes NPE   | null mudy br handled to not throw a Null Pointer          |
+-------------------------------------------------------+-------------------------------------------------------------------+
| toString returns a non-empty string                   | toString should return what the user should see                   |
+-------------------------------------------------------+-------------------------------------------------------------------+
*/

/**
 * Comprehensive bug-finding tests for Question.
 *
 * Tests are grouped into sections. Every test marked "FAILS" asserts
 * correct/expected behavior that the code does NOT currently satisfy.
 * Tests marked "PASSES" confirm working behavior.
 *
 * Bugs found:
 *  1.  Constructor 1 (Section-first) never assigns id — id is null after construction
 *  2.  Constructor 2 (UUID) ignores the passed UUID and generates a new one
 *  3.  Constructor 3 accepts null title and null user with no validation
 *  4.  setTitle(null), setDescription(null), and setUser(null) all silently store null
 *  5.  givenSolutionIMG and givenSolutionTXT are never initialized — always null
 *  6.  searchQuestions() will throw NullPointerException if the question's title is null
 *  7.  toString() returns " " instead of meaningful content
 */
public class QuestionTest {

    private User alice;
    private Question q;

    @Before
    public void setUp() {
        alice = new User("Alice", "A", "alice", "pass1", "alice@test.com", 2025, "USC001");
        q = new Question("What is recursion?", alice,
                "Explain recursion in Java.", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
    }

    // =========================================================================
    // SECTION 1 — CONSTRUCTOR 1 (Section-first parameter order)
    // =========================================================================

    /** BUG: constructor 1 never sets id — id is null. FAILS — assertNotNull on null. */
    @Test
    public void testConstructorOneIdIsNull() {
        Question q1 = new Question("Title", alice, "desc",
                new ArrayList<>(), new ArrayList<>(), Difficulty.EASY, Language.JAVA);
        assertNotNull(q1.getId()); // FAILS — id is null
    }

    /** PASSES — constructor 1 stores the title. */
    @Test
    public void testConstructorOneTitleStored() {
        Question q1 = new Question("My Title", alice, "desc",
                new ArrayList<>(), new ArrayList<>(), Difficulty.EASY, Language.JAVA);
        assertEquals("My Title", q1.getTitle());
    }

    /** PASSES — constructor 1 stores the user. */
    @Test
    public void testConstructorOneUserStored() {
        Question q1 = new Question("Title", alice, "desc",
                new ArrayList<>(), new ArrayList<>(), Difficulty.EASY, Language.JAVA);
        assertEquals(alice, q1.getUser());
    }

    /** PASSES — constructor 1 stores the description. */
    @Test
    public void testConstructorOneDescriptionStored() {
        Question q1 = new Question("Title", alice, "My description",
                new ArrayList<>(), new ArrayList<>(), Difficulty.EASY, Language.JAVA);
        assertEquals("My description", q1.getDescription());
    }

    /** PASSES — comments list is initialized to an empty list, not null. */
    @Test
    public void testConstructorOneCommentsInitialized() {
        Question q1 = new Question("Title", alice, "desc",
                new ArrayList<>(), new ArrayList<>(), Difficulty.EASY, Language.JAVA);
        assertNotNull(q1.getComments());
        assertTrue(q1.getComments().isEmpty());
    }

    /** PASSES — solutionList is initialized to an empty list, not null. */
    @Test
    public void testConstructorOneSolutionListInitialized() {
        Question q1 = new Question("Title", alice, "desc",
                new ArrayList<>(), new ArrayList<>(), Difficulty.EASY, Language.JAVA);
        assertNotNull(q1.getSolutionList());
        assertTrue(q1.getSolutionList().isEmpty());
    }

    // =========================================================================
    // SECTION 2 — CONSTRUCTOR 2 (UUID parameter)
    // =========================================================================

    /** BUG: the passed UUID is ignored — a new one is generated instead. FAILS. */
    @Test
    public void testConstructorTwoUsesPassedUUID() {
        UUID fixed = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Question q2 = new Question(fixed);
        assertEquals(fixed, q2.getId()); // FAILS — a different UUID is generated
    }

    // =========================================================================
    // SECTION 3 — CONSTRUCTOR 3 (Difficulty-first parameter order)
    // =========================================================================

    /** PASSES — constructor 3 generates a non-null id. */
    @Test
    public void testConstructorThreeIdGenerated() {
        assertNotNull(q.getId());
    }

    /** PASSES — constructor 3 stores the title. */
    @Test
    public void testConstructorThreeTitleStored() {
        assertEquals("What is recursion?", q.getTitle());
    }

    /** BUG: null title is accepted with no validation. FAILS — null stored. */
    @Test
    public void testConstructorThreeNullTitleRejected() {
        Question q3 = new Question(null, alice, "desc", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        assertNotNull(q3.getTitle()); // FAILS — null stored
    }

    /** BUG: null user is accepted with no validation. FAILS — null stored. */
    @Test
    public void testConstructorThreeNullUserRejected() {
        Question q3 = new Question("Title", null, "desc", Difficulty.EASY, Language.JAVA,
                new ArrayList<>(), new ArrayList<>());
        assertNotNull(q3.getUser()); // FAILS — null stored
    }

    // =========================================================================
    // SECTION 4 — SETTERS AND GETTERS
    // =========================================================================

    /** PASSES — setTitle updates the stored title. */
    @Test
    public void testSetTitleUpdatesTitle() {
        q.setTitle("New Title");
        assertEquals("New Title", q.getTitle());
    }

    /** BUG: setTitle(null) silently stores null. FAILS — null stored. */
    @Test
    public void testSetTitleCannotBeNull() {
        q.setTitle(null);
        assertNotNull(q.getTitle()); // FAILS — null stored
    }

    /** PASSES — setDescription updates the stored description. */
    @Test
    public void testSetDescriptionUpdatesDescription() {
        q.setDescription("Updated desc");
        assertEquals("Updated desc", q.getDescription());
    }

    /** BUG: setDescription(null) silently stores null. FAILS — null stored. */
    @Test
    public void testSetDescriptionCannotBeNull() {
        q.setDescription(null);
        assertNotNull(q.getDescription()); // FAILS — null stored
    }

    /** PASSES — setUser updates the stored user. */
    @Test
    public void testSetUserUpdatesUser() {
        User bob = new User("Bob", "B", "bob", "pass2", "bob@test.com", 2026, "USC002");
        q.setUser(bob);
        assertEquals(bob, q.getUser());
    }

    /** BUG: setUser(null) silently stores null. FAILS — null stored. */
    @Test
    public void testSetUserCannotBeNull() {
        q.setUser(null);
        assertNotNull(q.getUser()); // FAILS — null stored
    }

    /** PASSES — setDifficulty updates the stored difficulty. */
    @Test
    public void testSetDifficultyUpdatesDifficulty() {
        q.setDifficulty(Difficulty.DIFFICULT);
        assertEquals(Difficulty.DIFFICULT, q.getDifficulty());
    }

    /** PASSES — setLanguage updates the stored language. */
    @Test
    public void testSetLanguageUpdatesLanguage() {
        q.setLanguage(Language.PYTHON);
        assertEquals(Language.PYTHON, q.getLanguage());
    }

    /** PASSES — setHint replaces the hints list. */
    @Test
    public void testSetHintUpdatesHints() {
        ArrayList<String> hints = new ArrayList<>();
        hints.add("Think about the base case");
        q.setHint(hints);
        assertEquals(hints, q.getHints());
    }

    /** PASSES — setQuestionContent replaces the sections list. */
    @Test
    public void testSetQuestionContentUpdatesContent() {
        ArrayList<com.interviews.Section> sections = new ArrayList<>();
        q.setQuestionContent(sections);
        assertEquals(sections, q.getQuestionContent());
    }

    /** PASSES — setId updates the stored UUID. */
    @Test
    public void testSetIdUpdatesId() {
        UUID newId = UUID.randomUUID();
        q.setId(newId);
        assertEquals(newId, q.getId());
    }

    // =========================================================================
    // SECTION 5 — getSolutionList() AND syncSolutionQuestionIds()
    // =========================================================================

    /** PASSES — solutionList is empty on a fresh question. */
    @Test
    public void testGetSolutionListInitiallyEmpty() {
        assertTrue(q.getSolutionList().isEmpty());
    }

    /** PASSES — getSolutionList syncs the question's id into each solution. */
    @Test
    public void testGetSolutionListSyncsQuestionId() {
        UserSolution sol = new UserSolution(alice, "My solution", UUID.randomUUID(),
                new ArrayList<>(), 0);
        q.getSolutionList().add(sol);
        q.getSolutionList(); // triggers sync
        assertEquals(q.getId(), sol.getQuestionId());
    }

    /** PASSES — getComments returns an empty list on a fresh question. */
    @Test
    public void testGetCommentsInitiallyEmpty() {
        assertNotNull(q.getComments());
        assertTrue(q.getComments().isEmpty());
    }

    // =========================================================================
    // SECTION 6 — givenSolutionIMG AND givenSolutionTXT (never initialized)
    // =========================================================================

    /** BUG: givenSolutionIMG is never initialized — getGivenSolutionImg() returns null.
     *  Iterating it would cause a NullPointerException. FAILS — assertNotNull fails. */
    @Test
    public void testGivenSolutionImgNotNull() {
        assertNotNull(q.getGivenSolutionImg()); // FAILS — null returned
    }

    /** BUG: givenSolutionTXT is never initialized — getGivenSolutionText() returns null.
     *  Iterating it would cause a NullPointerException. FAILS — assertNotNull fails. */
    @Test
    public void testGivenSolutionTextNotNull() {
        assertNotNull(q.getGivenSolutionText()); // FAILS — null returned
    }

    // =========================================================================
    // SECTION 7 — searchQuestions()
    // =========================================================================

    /** PASSES — null keyword does not crash. */
    @Test
    public void testSearchQuestionsNullKeywordDoesNotCrash() {
        q.searchQuestions(null); // PASSES — null check exists
    }

    /** PASSES — a matching title does not crash. */
    @Test
    public void testSearchQuestionsMatchingTitleDoesNotCrash() {
        q.searchQuestions("recursion"); // PASSES — prints title
    }

    /** PASSES — a non-matching keyword does not crash. */
    @Test
    public void testSearchQuestionsNonMatchingKeywordDoesNotCrash() {
        q.searchQuestions("xyzzy"); // PASSES — no match, no print
    }

    /** BUG: searchQuestions() calls this.title.toLowerCase() without a null guard.
     *  If the title is null, a NullPointerException is thrown. FAILS — NPE. */
    @Test(expected = NullPointerException.class)
    public void testSearchQuestionsNullTitleCausesNPE() {
        q.setTitle(null);
        q.searchQuestions("anything"); // FAILS — NPE: null.toLowerCase()
    }

    // =========================================================================
    // SECTION 8 — toString()
    // =========================================================================

    /** BUG: toString() returns " " instead of meaningful content. FAILS. */
    @Test
    public void testToStringReturnsMeaningfulContent() {
        String result = q.toString();
        assertNotNull(result);
        assertFalse(result.trim().isEmpty()); // FAILS — returns " " (just a space)
    }
}
