package com.model;

import com.interviews.Contributor;
import com.interviews.Difficulty;
import com.interviews.Language;
import com.interviews.Question;
import com.interviews.QuestionList;
import com.interviews.Section;
import com.interviews.Status;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;

/*
+-------------------------------------------+--------------------------------------------------------------+
| Test                                      | Reasoning                                                    |
+-------------------------------------------+--------------------------------------------------------------+
| contributor stores first name             | contributor should keep the same user identity fields        |
| contributor stores username               | login-related data should carry over from User               |
| contributor starts with user permissions  | constructor should preserve the current default status       |
| addQuestion returns a question            | contributors need a created question object back to use      |
| addQuestion stores title                  | the created question should keep the entered title           |
| addQuestion stores description            | the created question should keep its description             |
| addQuestion stores difficulty             | the created question should preserve selected difficulty     |
| addQuestion stores language               | the created question should preserve selected language       |
| addQuestion stores hints                  | question hints should remain attached after creation         |
| editQuestion updates title                | contributors should be able to revise a posted question      |
| editQuestion updates description          | question details should be editable after posting            |
| editQuestion updates user                 | ownership data should change when editing says to change it  |
| editQuestion updates difficulty           | difficulty should stay editable for corrections              |
| editQuestion updates language             | language should stay editable for corrections                |
| editQuestion updates hints                | contributors should be able to replace hint lists            |
| editQuestion returns null                 | this method currently signals completion with null           |
| editQuestion bad id does nothing          | editing the wrong question should not change another one     |
| removeQuestion removes from question list | deleting a posted question should also remove shared copy    |
| removeQuestion bad id does nothing        | bad ids should not remove unrelated questions                |
+-------------------------------------------+--------------------------------------------------------------+
*/
public class ContributorTest {

    private Contributor contributor;
    private ArrayList<Section> sections;
    private ArrayList<String> hints;

    @Before
    public void setUp() {
        contributor = new Contributor("Jane", "Smith", "jsmith", "pass123", "jsmith@email.com", 2026, "USC999");
        sections = new ArrayList<>();
        hints = new ArrayList<>();
        hints.add("Try a loop");

        QuestionList.getInstance().getQuestions().clear();
    }

    @Test
    public void testConstructorSetsFirstName() {
        assertEquals("Jane", contributor.getFirstName());
    }

    @Test
    public void testConstructorSetsUsername() {
        assertEquals("jsmith", contributor.getUsername());
    }

    @Test
    public void testConstructorDefaultStatusIsUser() {
        assertEquals(Status.USER, contributor.getStatus());
    }

    @Test
    public void testAddQuestionReturnsQuestion() {
        Question question = contributor.addQuestion("Two Sum", contributor, "Find two numbers", sections, hints,
                Difficulty.EASY, Language.JAVA);

        assertNotNull(question);
    }

    @Test
    public void testAddQuestionSetsTitle() {
        Question question = contributor.addQuestion("Two Sum", contributor, "Find two numbers", sections, hints,
                Difficulty.EASY, Language.JAVA);

        assertEquals("Two Sum", question.getTitle());
    }

    @Test
    public void testAddQuestionSetsDescription() {
        Question question = contributor.addQuestion("Two Sum", contributor, "Find two numbers", sections, hints,
                Difficulty.EASY, Language.JAVA);

        assertEquals("Find two numbers", question.getDescription());
    }

    @Test
    public void testAddQuestionSetsDifficulty() {
        Question question = contributor.addQuestion("Two Sum", contributor, "Find two numbers", sections, hints,
                Difficulty.EASY, Language.JAVA);

        assertEquals(Difficulty.EASY, question.getDifficulty());
    }

    @Test
    public void testAddQuestionSetsLanguage() {
        Question question = contributor.addQuestion("Two Sum", contributor, "Find two numbers", sections, hints,
                Difficulty.EASY, Language.JAVA);

        assertEquals(Language.JAVA, question.getLanguage());
    }

    @Test
    public void testAddQuestionSetsHints() {
        Question question = contributor.addQuestion("Two Sum", contributor, "Find two numbers", sections, hints,
                Difficulty.EASY, Language.JAVA);

        assertEquals(hints, question.getHints());
    }

    @Test
    public void testEditQuestionUpdatesTitle() {
        Question question = contributor.addQuestion("Old Title", contributor, "Old description", sections, hints,
                Difficulty.EASY, Language.JAVA);
        UUID id = question.getId();

        contributor.editQuestion(id, "New Title", contributor, "Old description", sections, hints,
                Difficulty.EASY, Language.JAVA);

        assertEquals("New Title", question.getTitle());
    }

    @Test
    public void testEditQuestionUpdatesDescription() {
        Question question = contributor.addQuestion("Old Title", contributor, "Old description", sections, hints,
                Difficulty.EASY, Language.JAVA);
        UUID id = question.getId();

        contributor.editQuestion(id, "Old Title", contributor, "New description", sections, hints,
                Difficulty.EASY, Language.JAVA);

        assertEquals("New description", question.getDescription());
    }

    @Test
    public void testEditQuestionUpdatesUser() {
        Question question = contributor.addQuestion("Old Title", contributor, "Old description", sections, hints,
                Difficulty.EASY, Language.JAVA);
        Contributor updatedContributor = new Contributor("Alex", "Brown", "abrown", "pass456", "abrown@email.com",
                2027, "USC888");

        contributor.editQuestion(question.getId(), "Old Title", updatedContributor, "Old description", sections, hints,
                Difficulty.EASY, Language.JAVA);

        assertEquals(updatedContributor, question.getUser());
    }

    @Test
    public void testEditQuestionUpdatesDifficulty() {
        Question question = contributor.addQuestion("Old Title", contributor, "Old description", sections, hints,
                Difficulty.EASY, Language.JAVA);

        contributor.editQuestion(question.getId(), "Old Title", contributor, "Old description", sections, hints,
                Difficulty.HARD, Language.JAVA);

        assertEquals(Difficulty.HARD, question.getDifficulty());
    }

    @Test
    public void testEditQuestionUpdatesLanguage() {
        Question question = contributor.addQuestion("Old Title", contributor, "Old description", sections, hints,
                Difficulty.EASY, Language.JAVA);

        contributor.editQuestion(question.getId(), "Old Title", contributor, "Old description", sections, hints,
                Difficulty.EASY, Language.PYTHON);

        assertEquals(Language.PYTHON, question.getLanguage());
    }

    @Test
    public void testEditQuestionUpdatesHints() {
        Question question = contributor.addQuestion("Old Title", contributor, "Old description", sections, hints,
                Difficulty.EASY, Language.JAVA);
        ArrayList<String> newHints = new ArrayList<>();
        newHints.add("Use a hashmap");

        contributor.editQuestion(question.getId(), "Old Title", contributor, "Old description", sections, newHints,
                Difficulty.EASY, Language.JAVA);

        assertEquals(newHints, question.getHints());
    }

    @Test
    public void testEditQuestionReturnsNull() {
        Question question = contributor.addQuestion("Old Title", contributor, "Old description", sections, hints,
                Difficulty.EASY, Language.JAVA);

        Question result = contributor.editQuestion(question.getId(), "New Title", contributor, "New description",
                sections, hints, Difficulty.MEDIUM, Language.PYTHON);

        assertNull(result);
    }

    @Test
    public void testEditQuestionWithInvalidIdDoesNothing() {
        Question question = contributor.addQuestion("Old Title", contributor, "Old description", sections, hints,
                Difficulty.EASY, Language.JAVA);

        contributor.editQuestion(UUID.randomUUID(), "New Title", contributor, "New description", sections, hints,
                Difficulty.MEDIUM, Language.PYTHON);

        assertEquals("Old Title", question.getTitle());
        assertEquals("Old description", question.getDescription());
        assertEquals(Difficulty.EASY, question.getDifficulty());
        assertEquals(Language.JAVA, question.getLanguage());
    }

    @Test
    public void testRemoveQuestionRemovesFromQuestionList() {
        Question question = contributor.addQuestion("Two Sum", contributor, "Find two numbers", sections, hints,
                Difficulty.EASY, Language.JAVA);
        QuestionList.getInstance().getQuestions().add(question);

        contributor.removeQuestion(question.getId());

        assertFalse(QuestionList.getInstance().getQuestions().contains(question));
    }

    @Test
    public void testRemoveQuestionWithInvalidIdDoesNothing() {
        Question question = contributor.addQuestion("Two Sum", contributor, "Find two numbers", sections, hints,
                Difficulty.EASY, Language.JAVA);
        QuestionList.getInstance().getQuestions().add(question);

        contributor.removeQuestion(UUID.randomUUID());

        assertTrue(QuestionList.getInstance().getQuestions().contains(question));
    }
}
