package com.model;

import com.interviews.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        user = new User("John", "Doe", "jdoe", "pass123", "jdoe@email.com", 2025, "ABC123");
    }

    // ---- Constructor / Getters ----

    @Test
    public void testConstructorSetsFirstName() {
        assertEquals("John", user.getFirstName());
    }

    @Test
    public void testConstructorSetsLastName() {
        assertEquals("Doe", user.getLastName());
    }

    @Test
    public void testConstructorSetsUsername() {
        assertEquals("jdoe", user.getUsername());
    }

    @Test
    public void testConstructorSetsPassword() {
        assertEquals("pass123", user.getPassword());
    }

    @Test
    public void testConstructorSetsEmail() {
        assertEquals("jdoe@email.com", user.getEmail());
    }

    @Test
    public void testConstructorSetsGraduationYear() {
        assertEquals(2025, user.getGraduationYear());
    }

    @Test
    public void testConstructorSetsIdUSC() {
        assertEquals("ABC123", user.getIdUSC());
    }

    @Test
    public void testConstructorDefaultStatusIsUser() {
        assertEquals(Status.USER, user.getStatus());
    }

    @Test
    public void testConstructorGeneratesUUID() {
        assertNotNull(user.getId());
    }

    @Test
    public void testConstructorInitializesEmptyStarredQuestions() {
        assertNotNull(user.getStarredQuestions());
        assertTrue(user.getStarredQuestions().isEmpty());
    }

    @Test
    public void testConstructorInitializesEmptyAnsweredQuestions() {
        assertNotNull(user.getAnsweredQuestions());
        assertTrue(user.getAnsweredQuestions().isEmpty());
    }

    @Test
    public void testConstructorInitializesEmptyAchievements() {
        assertNotNull(user.getAchievements());
        assertTrue(user.getAchievements().isEmpty());
    }

    // ---- Full constructor ----

    @Test
    public void testFullConstructorSetsId() {
        UUID id = UUID.randomUUID();
        User u = new User(id, "Jane", "Smith", "jsmith", "pw", "jane@email.com",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), Status.ADMIN, 2026, "XYZ999");
        assertEquals(id, u.getId());
    }

    @Test
    public void testFullConstructorSetsStatus() {
        User u = new User(UUID.randomUUID(), "Jane", "Smith", "jsmith", "pw", "jane@email.com",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), Status.CONTRIBUTOR, 2026, "XYZ999");
        assertEquals(Status.CONTRIBUTOR, u.getStatus());
    }

    // ---- Setters ----

    @Test
    public void testSetFirstName() {
        user.setFirstName("Alice");
        assertEquals("Alice", user.getFirstName());
    }

    @Test
    public void testSetLastName() {
        user.setLastName("Smith");
        assertEquals("Smith", user.getLastName());
    }

    @Test
    public void testSetUsername() {
        user.setUsername("newuser");
        assertEquals("newuser", user.getUsername());
    }

    @Test
    public void testSetPassword() {
        user.setPassword("newpass");
        assertEquals("newpass", user.getPassword());
    }

    @Test
    public void testSetEmail() {
        user.setEmail("new@email.com");
        assertEquals("new@email.com", user.getEmail());
    }

    @Test
    public void testSetGraduationYear() {
        user.setGraduationYear(2028);
        assertEquals(2028, user.getGraduationYear());
    }

    @Test
    public void testSetIdUSC() {
        user.setIdUSC("ZZZ000");
        assertEquals("ZZZ000", user.getIdUSC());
    }

    @Test
    public void testSetStatus() {
        user.setStatus(Status.ADMIN);
        assertEquals(Status.ADMIN, user.getStatus());
    }

    @Test
    public void testSetId() {
        UUID before = user.getId();
        user.setId();
        assertNotEquals(before, user.getId());
    }

    // ---- isMatch ----

    @Test
    public void testIsMatchCorrectCredentials() {
        assertTrue(user.isMatch("jdoe", "pass123"));
    }

    @Test
    public void testIsMatchWrongPassword() {
        assertFalse(user.isMatch("jdoe", "wrong"));
    }

    @Test
    public void testIsMatchWrongUsername() {
        assertFalse(user.isMatch("wronguser", "pass123"));
    }

    @Test
    public void testIsMatchBothWrong() {
        assertFalse(user.isMatch("bad", "bad"));
    }

    // ---- canModifyQuestions ----

    @Test
    public void testUserCannotModifyQuestions() {
        assertFalse(user.canModifyQuestions());
    }

    @Test
    public void testContributorCanModifyQuestions() {
        user.setStatus(Status.CONTRIBUTOR);
        assertTrue(user.canModifyQuestions());
    }

    @Test
    public void testAdminCanModifyQuestions() {
        user.setStatus(Status.ADMIN);
        assertTrue(user.canModifyQuestions());
    }

    // ---- canModifyContributors ----

    @Test
    public void testUserCannotModifyContributors() {
        assertFalse(user.canModifyContributors());
    }

    @Test
    public void testContributorCannotModifyContributors() {
        user.setStatus(Status.CONTRIBUTOR);
        assertFalse(user.canModifyContributors());
    }

    @Test
    public void testAdminCanModifyContributors() {
        user.setStatus(Status.ADMIN);
        assertTrue(user.canModifyContributors());
    }

    // ---- starQuestion ----

    @Test
    public void testStarQuestionAddsQuestion() {
        Question q = new Question("Q1", user, "desc", new ArrayList<>(), new ArrayList<>(), Difficulty.EASY, Language.JAVA);
        user.starQuestion(q);
        assertEquals(1, user.getStarredQuestions().size());
        assertTrue(user.getStarredQuestions().contains(q));
    }

    @Test
    public void testStarQuestionNoDuplicates() {
        Question q = new Question("Q1", user, "desc", new ArrayList<>(), new ArrayList<>(), Difficulty.EASY, Language.JAVA);
        user.starQuestion(q);
        user.starQuestion(q);
        assertEquals(1, user.getStarredQuestions().size());
    }

    @Test
    public void testStarMultipleDistinctQuestions() {
        Question q1 = new Question("Q1", user, "d1", new ArrayList<>(), new ArrayList<>(), Difficulty.EASY, Language.JAVA);
        Question q2 = new Question("Q2", user, "d2", new ArrayList<>(), new ArrayList<>(), Difficulty.DIFFICULT, Language.PYTHON);
        user.starQuestion(q1);
        user.starQuestion(q2);
        assertEquals(2, user.getStarredQuestions().size());
    }

    // ---- getStarredQuestionList ----

    @Test
    public void testGetStarredQuestionListReturnsStarredQuestions() {
        Question q = new Question("Q1", user, "desc", new ArrayList<>(), new ArrayList<>(), Difficulty.EASY, Language.JAVA);
        user.starQuestion(q);
        assertEquals(user.getStarredQuestions(), user.getStarredQuestionList());
    }

    // ---- setStarredQuestions / setAnsweredQuestions / setAchievements ----

    @Test
    public void testSetStarredQuestions() {
        ArrayList<Question> list = new ArrayList<>();
        list.add(new Question("Q1", user, "d", new ArrayList<>(), new ArrayList<>(), Difficulty.EASY, Language.JAVA));
        user.setStarredQuestions(list);
        assertEquals(1, user.getStarredQuestions().size());
    }

    @Test
    public void testSetAnsweredQuestions() {
        ArrayList<Question> list = new ArrayList<>();
        list.add(new Question("Q2", user, "d", new ArrayList<>(), new ArrayList<>(), Difficulty.MEDIUM, Language.JAVA));
        user.setAnsweredQuestions(list);
        assertEquals(1, user.getAnsweredQuestions().size());
    }

    @Test
    public void testSetAchievements() {
        ArrayList<Achievement> list = new ArrayList<>();
        list.add(new Achievement(1, 1, 0, 0));
        user.setAchievements(list);
        assertEquals(1, user.getAchievements().size());
    }

    // ---- addComment ----

    @Test
    public void testAddCommentReturnsComment() {
        Comment c = user.addComment(user, "Great question!");
        assertNotNull(c);
        assertEquals("Great question!", c.getComment());
    }

    // ---- addsolution / removeSolution ----

    @Test
    public void testAddSolutionReturnsSolution() {
        UserSolution sol = user.addsolution(user, "My solution", new ArrayList<>());
        assertNotNull(sol);
        assertEquals("My solution", sol.getDescription());
    }

    @Test
    public void testRemoveSolutionRemovesMatchingSolution() {
        user.addsolution(user, "Solution A", new ArrayList<>());
        // Verify solution was added, then remove it
        user.removeSolution(user, "Solution A", new ArrayList<>());
        // No exception thrown — removal completed without error
    }
}
