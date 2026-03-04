package com.interviews;

/**
 * This class is responsible for calculating the achievements of a user, such as their level, vote points, question points, and streak.
 */
public class Achievement {
    private int leaderboardPlace;
    private int userLevel;
    private int allVotePoints;
    private int streak;

    public Achievement() {

    }

    public void levelUp() {

    }

    public int calculateVotePoints() {

    }

    public int calculateQuestionPoints() {
        
    }

    public int getStreak() {
        return streak;
    }

    public int getLevel() {
        return userLevel;
    }

    public int streakCounter() {

    }

    public int calculateAllPoints() {
        return allVotePoints + calculateQuestionPoints();
    }
}
