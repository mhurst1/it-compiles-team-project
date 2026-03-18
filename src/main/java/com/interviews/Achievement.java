package com.interviews;

import java.time.LocalDate;

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

    public Achievement() {
    }

    // Call this when user receives a vote
    public void addVote() {
        voteCount++;
        allVotePoints = calculateVotePoints();
        levelUp();
    }

    // Call this when user answers a question
    public void addQuestion() {
        questionCount++;
        levelUp();
    }

    public void levelUp() {
        int totalPoints = calculateAllPoints();
        userLevel = (totalPoints / POINTS_PER_LEVEL) + 1;
    }

    public int calculateVotePoints() {
        return voteCount * VOTE_POINT_VALUE;
    }

    public int calculateQuestionPoints() {
        return questionCount * QUESTION_POINT_VALUE;
    }

    public int getStreak() {
        return streak;
    }

    public int getLevel() {
        return userLevel;
    }


    // NEED TO BE MODIFIED FOR DATA WRITER
    public int getLeaderboardPlace(){
        return leaderboardPlace;
    }
    public int getUserLevel(){
        return userLevel;
    }
    public int getAllVotePoints(){
        return allVotePoints;
    }

    // Call this once per day when user is active
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

    public int calculateAllPoints() {
        return calculateVotePoints() + calculateQuestionPoints();
    }
}