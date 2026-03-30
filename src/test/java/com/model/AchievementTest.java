package com.model;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.interviews.Achievement;


/**
 * CHAT GPT Generated the code for the test cases along with the formatting of the table.
 *  
 * The Test and Reasoning Chart was then filled out manually 
 */

/*
+-------------------------------------------------------+-------------------------------------------------------------------+
| Test                                                  | Reasoning                                                         |
+-------------------------------------------------------+-------------------------------------------------------------------+
| leaderboardPlace = -1                                 | leaderboard placement cannot be negative                          |
| userLevel = -1                                        | a user's level cannot be negative                                 |
| streak = -5                                           | a streak cannot be negative                                       |
| allVotePoints = -100                                  | vote points cannot be negative                                    |
+-------------------------------------------------------+-------------------------------------------------------------------+
| leaderboardPlace stored correctly                     | constructor must store the given leaderboard place                |
| userLevel stored correctly                            | constructor must store the given user level                       |
| allVotePoints stored correctly                        | constructor must store the given vote points                      |
| streak stored correctly                               | constructor must store the given streak                           |
+-------------------------------------------------------+-------------------------------------------------------------------+
| addVote() increases allVotePoints by 10               | each vote is worth 10 points                                      |
| addVote() called 3x gives 30 vote points              | vote points must scale with number of votes                       |
| addVote() causes a level check                        | gaining votes must recalculate the user's level                   |
| calculateVotePoints() returns voteCount * 10          | vote points formula must be correct                               |
+-------------------------------------------------------+-------------------------------------------------------------------+
| addQuestion() increases question count                | answering a question must be counted                              |
| addQuestion() causes a level check                    | answering questions must recalculate the user's level             |
| calculateQuestionPoints() returns questionCount * 20  | question points formula must be correct                           |
+-------------------------------------------------------+-------------------------------------------------------------------+
| 10 votes reaches level 2                              | 100 points must advance the user from level 1 to level 2          |
| 5 votes + 3 questions reaches level 2                 | mixed points must combine correctly for leveling                  |
| 20 votes reaches level 3                              | 200 points must place the user at level 3                         |
| below 100 points stays at level 1                     | insufficient points must not advance the level                    |
| calculateAllPoints returns sum of vote + question pts | total points must be the sum of both categories                   |
+-------------------------------------------------------+-------------------------------------------------------------------+
| streakCounter — first call increments streak          | calling with no previous date must add 1 to the streak            |
| streakCounter — same day twice does not double streak | two calls on the same day must only count once                    |
| streakCounter — consecutive day increments streak     | activity on the next day must extend the streak                   |
| streakCounter — gap in days resets streak to 1        | missing a day must reset the streak back to 1                     |
+-------------------------------------------------------+-------------------------------------------------------------------+
| setStreak(5) stores correctly                         | setStreak must update the streak value                            |
| setStreak(-1) stores negative value                   | negative streak values should be rejected                         |
+-------------------------------------------------------+-------------------------------------------------------------------+
| getLeaderboardPlace returns correct value             | getter must return what the constructor stored                     |
| getUserLevel returns correct value                    | getter must return the current level                              |
| getAllVotePoints returns correct value after addVote   | getter must reflect updated vote points                           |
+-------------------------------------------------------+-------------------------------------------------------------------+
*/

/**
 * Comprehensive bug-finding tests for Achievement.
 *
 * Tests are grouped into sections. Every test marked "FAILS" asserts
 * correct/expected behavior that the code does NOT currently satisfy.
 * Tests marked "PASSES" confirm working behavior.
 *
 * Bugs found:
 *  1.  Negative leaderboardPlace is accepted with no validation
 *  2.  Negative userLevel is accepted with no validation
 *  3.  Negative streak is accepted with no validation
 *  4.  Negative allVotePoints is accepted with no validation
 *  5.  setStreak() accepts negative values
 */
public class AchievementTest {

    private Achievement achievement;

    @Before
    public void setUp() {
        achievement = new Achievement(1, 1, 0, 0);
    }

    // =========================================================================
    // SECTION 1 — CONSTRUCTOR NEGATIVE VALUE VALIDATION (all FAIL — no guards)
    // =========================================================================

    /** BUG: negative leaderboardPlace is accepted. FAILS — -1 stored. */
    @Test
    public void testLeaderboardPlaceCannotBeNegative() {
        Achievement a = new Achievement(-1, 1, 0, 0);
        assertTrue(a.getLeaderboardPlace() >= 0); // FAILS — -1 stored
    }

    /** BUG: negative userLevel is accepted. FAILS — -1 stored. */
    @Test
    public void testUserLevelCannotBeNegative() {
        Achievement a = new Achievement(1, -1, 0, 0);
        assertTrue(a.getUserLevel() >= 1); // FAILS — -1 stored
    }

    /** BUG: negative streak is accepted. FAILS — -5 stored. */
    @Test
    public void testStreakCannotBeNegative() {
        Achievement a = new Achievement(1, 1, 0, -5);
        assertTrue(a.getStreak() >= 0); // FAILS — -5 stored
    }

    /** BUG: negative allVotePoints is accepted. FAILS — -100 stored. */
    @Test
    public void testAllVotePointsCannotBeNegative() {
        Achievement a = new Achievement(1, 1, -100, 0);
        assertTrue(a.getAllVotePoints() >= 0); // FAILS — -100 stored
    }

    // =========================================================================
    // SECTION 2 — CONSTRUCTOR HAPPY PATH (all PASS)
    // =========================================================================

    /** PASSES — leaderboardPlace stored correctly. */
    @Test
    public void testConstructorStoresLeaderboardPlace() {
        Achievement a = new Achievement(5, 1, 0, 0);
        assertEquals(5, a.getLeaderboardPlace());
    }

    /** PASSES — userLevel stored correctly. */
    @Test
    public void testConstructorStoresUserLevel() {
        Achievement a = new Achievement(1, 3, 0, 0);
        assertEquals(3, a.getUserLevel());
    }

    /** PASSES — allVotePoints stored correctly. */
    @Test
    public void testConstructorStoresAllVotePoints() {
        Achievement a = new Achievement(1, 1, 50, 0);
        assertEquals(50, a.getAllVotePoints());
    }

    /** PASSES — streak stored correctly. */
    @Test
    public void testConstructorStoresStreak() {
        Achievement a = new Achievement(1, 1, 0, 7);
        assertEquals(7, a.getStreak());
    }

    // =========================================================================
    // SECTION 3 — addVote() AND calculateVotePoints()
    // =========================================================================

    /** PASSES — one addVote increases allVotePoints by 10. */
    @Test
    public void testAddVoteIncreasesAllVotePointsByTen() {
        achievement.addVote();
        assertEquals(10, achievement.getAllVotePoints());
    }

    /** PASSES — three addVote calls give 30 vote points. */
    @Test
    public void testAddVoteThreeTimesGivesThirtyPoints() {
        achievement.addVote();
        achievement.addVote();
        achievement.addVote();
        assertEquals(30, achievement.getAllVotePoints());
    }

    /** PASSES — addVote triggers a level recalculation. */
    @Test
    public void testAddVoteTriggersLevelCheck() {
        for (int i = 0; i < 10; i++) achievement.addVote(); // 100 points
        assertEquals(2, achievement.getLevel());
    }

    /** PASSES — calculateVotePoints returns voteCount multiplied by 10. */
    @Test
    public void testCalculateVotePointsFormula() {
        achievement.addVote();
        achievement.addVote();
        assertEquals(20, achievement.calculateVotePoints());
    }

    // =========================================================================
    // SECTION 4 — addQuestion() AND calculateQuestionPoints()
    // =========================================================================

    /** PASSES — addQuestion increases question points by 20. */
    @Test
    public void testAddQuestionIncreasesPointsByTwenty() {
        achievement.addQuestion();
        assertEquals(20, achievement.calculateQuestionPoints());
    }

    /** PASSES — addQuestion triggers level recalculation. */
    @Test
    public void testAddQuestionTriggersLevelCheck() {
        for (int i = 0; i < 5; i++) achievement.addQuestion(); // 100 points
        assertEquals(2, achievement.getLevel());
    }

    /** PASSES — calculateQuestionPoints returns questionCount multiplied by 20. */
    @Test
    public void testCalculateQuestionPointsFormula() {
        achievement.addQuestion();
        achievement.addQuestion();
        achievement.addQuestion();
        assertEquals(60, achievement.calculateQuestionPoints());
    }

    // =========================================================================
    // SECTION 5 — levelUp() AND calculateAllPoints()
    // =========================================================================

    /** PASSES — 10 votes (100 pts) advances user to level 2. */
    @Test
    public void testTenVotesReachesLevelTwo() {
        for (int i = 0; i < 10; i++) achievement.addVote();
        assertEquals(2, achievement.getLevel());
    }

    /** PASSES — 5 votes (50) + 3 questions (60) = 110 pts → level 2. */
    @Test
    public void testMixedPointsReachLevelTwo() {
        for (int i = 0; i < 5; i++) achievement.addVote();
        for (int i = 0; i < 3; i++) achievement.addQuestion();
        assertEquals(2, achievement.getLevel());
    }

    /** PASSES — 20 votes (200 pts) advances user to level 3. */
    @Test
    public void testTwentyVotesReachesLevelThree() {
        for (int i = 0; i < 20; i++) achievement.addVote();
        assertEquals(3, achievement.getLevel());
    }

    /** PASSES — fewer than 100 total points stays at level 1. */
    @Test
    public void testBelowHundredPointsStaysLevelOne() {
        for (int i = 0; i < 4; i++) achievement.addVote();  // 40 pts
        for (int i = 0; i < 2; i++) achievement.addQuestion(); // 40 pts
        assertEquals(1, achievement.getLevel());
    }

    /** PASSES — calculateAllPoints returns the sum of vote and question points. */
    @Test
    public void testCalculateAllPointsReturnsCombinedTotal() {
        achievement.addVote();       // 10 pts
        achievement.addQuestion();   // 20 pts
        assertEquals(30, achievement.calculateAllPoints());
    }

    // =========================================================================
    // SECTION 6 — streakCounter()
    // =========================================================================

    /** PASSES — first call with no prior date increments the streak. */
    @Test
    public void testStreakCounterFirstCallIncrementsStreak() {
        int result = achievement.streakCounter();
        assertEquals(1, result);
    }

    /** PASSES — calling streakCounter twice on the same day does not double the streak. */
    @Test
    public void testStreakCounterSameDayDoesNotDouble() {
        achievement.streakCounter(); // first call sets lastActiveDate = today
        int result = achievement.streakCounter(); // same day — no change
        assertEquals(1, result);
    }

    /** PASSES — calling on a consecutive day increments the streak. */
    @Test
    public void testStreakCounterConsecutiveDayIncrementsStreak() {
        achievement.setLastActiveDate(LocalDate.now().minusDays(1));
        achievement.setStreak(3);
        int result = achievement.streakCounter();
        assertEquals(4, result);
    }

    /** PASSES — a gap of two or more days resets the streak to 1. */
    @Test
    public void testStreakCounterGapResetsStreakToOne() {
        achievement.setLastActiveDate(LocalDate.now().minusDays(3));
        achievement.setStreak(5);
        int result = achievement.streakCounter();
        assertEquals(1, result);
    }

    // =========================================================================
    // SECTION 7 — setStreak() / getStreak()
    // =========================================================================

    /** PASSES — setStreak stores the correct value. */
    @Test
    public void testSetStreakStoresCorrectly() {
        achievement.setStreak(5);
        assertEquals(5, achievement.getStreak());
    }

    /** BUG: setStreak accepts a negative value. FAILS — -1 stored. */
    @Test
    public void testSetStreakCannotBeNegative() {
        achievement.setStreak(-1);
        assertTrue(achievement.getStreak() >= 0); // FAILS — -1 stored
    }

    // =========================================================================
    // SECTION 8 — GETTERS
    // =========================================================================

    /** PASSES — getLeaderboardPlace returns what was passed to the constructor. */
    @Test
    public void testGetLeaderboardPlaceReturnsCorrectValue() {
        Achievement a = new Achievement(10, 1, 0, 0);
        assertEquals(10, a.getLeaderboardPlace());
    }

    /** PASSES — getUserLevel returns the current level. */
    @Test
    public void testGetUserLevelReturnsCorrectValue() {
        assertEquals(1, achievement.getUserLevel());
    }

    /** PASSES — getAllVotePoints reflects the updated value after addVote. */
    @Test
    public void testGetAllVotePointsUpdatesAfterAddVote() {
        achievement.addVote();
        achievement.addVote();
        assertEquals(20, achievement.getAllVotePoints());
    }
}
