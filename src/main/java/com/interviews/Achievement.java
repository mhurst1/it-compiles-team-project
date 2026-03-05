package com.interviews;

import java.time.LocalDate;

/**
 * This class is responsible for tracking user achievements, including their level, vote count, question count, and streaks. It calculates points based on user activity and determines when a user levels up. The class also manages the user's activity streak by tracking the last active date.
 * @author Sydney ernst
 */
public class Achievement {
    private int leaderboardPlace;
    private int userLevel = 1;
    private int voteCount;
    private int questionCount;
    private int allVotePoints;
    private int streak;

    private LocalDate lastActiveDate;

    private static final int VOTE_POINT_VALUE = 10;
    private static final int QUESTION_POINT_VALUE = 20;
    private static final int POINTS_PER_LEVEL = 100;

    /**
     * Constructor for the Achievement class. Initializes the user's achievement tracking with default values. The user starts at level 1 with zero votes, questions, and points. The last active date is set to null until the user becomes active.
     */
    public Achievement() {
    }

    /**
     * Call this when user receives a vote on their solution. Increments the vote count, recalculates the total points, and checks if the user has leveled up. Each vote contributes a certain number of points to the user's total, which can lead to leveling up when enough points are accumulated.
     */
    public void addVote() {
        voteCount++;
        allVotePoints = calculateVotePoints();
        levelUp();
    }

    /**
     * Call this when user answers a question. Increments the question count and checks if the user has leveled up.
     */
    public void addQuestion() {
        questionCount++;
        levelUp();
    }

    /**
     * Checks if the user has accumulated enough points to level up. The user's level is determined by dividing the total points by a predefined number of points required per level. If the user has enough points, their level is increased accordingly.
     */
    public void levelUp() {
        int totalPoints = calculateAllPoints();
        userLevel = (totalPoints / POINTS_PER_LEVEL) + 1;
    }

    /**
     * Calculates the total points from votes. Each vote contributes a fixed number of points to the user's total. This method multiplies the vote count by the predefined point value for each vote to determine the total vote points.
     */
    public int calculateVotePoints() {
        return voteCount * VOTE_POINT_VALUE;
    }

    /**
     * Calculates the total points from questions answered. Each question contributes a fixed number of points to the user's total. This method multiplies the question count by the predefined point value for each question to determine the total question points.
     */
    public int calculateQuestionPoints() {
        return questionCount * QUESTION_POINT_VALUE;
    }

    /**
     * Getters for the Achievement class. These methods return the current state of the user's achievements, including their leaderboard place, total vote points, total question points, total points, streak count, and user level. These getters allow other parts of the application to access the user's achievement information as needed.
     */
    public int getStreak() {
        return streak;
    }

    /**
     * Returns the user's current level. The level is determined by the total points accumulated from votes and questions. As the user earns more points, their level increases, reflecting their progress and activity within the application.
     */
    public int getLevel() {
        return userLevel;
    }

    /**
     * Updates the user's activity streak. If the user is active on consecutive days, their streak increases. If they miss a day, the streak resets to 1. This method checks the last active date and compares it to the current date to determine whether to increment the streak or reset it. The last active date is updated each time this method is called to ensure accurate tracking of the user's activity over time.
     */
    public int streakCounter() {
        LocalDate today = LocalDate.now();

        if (lastActiveDate == null) {
            streak = 1;
        } else if (lastActiveDate.plusDays(1).equals(today)) {
            streak++;
        } else if (!lastActiveDate.equals(today)) {
            streak = 1;
        }

        lastActiveDate = today;
        return streak;
    }

    /**
     * Calculates the user's total points by summing the points from votes and questions. This method calls the individual point calculation methods for votes and questions and adds their results together to get the overall point total for the user. The total points are used to determine the user's level and can also be displayed in the user's profile or leaderboard standings.
     */
    public int calculateAllPoints() {
        return calculateVotePoints() + calculateQuestionPoints();
    }
}