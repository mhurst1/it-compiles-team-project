package com.interviews;

import java.time.LocalDate;

/**
 * a class of an achievement used in things like leveling up and increase
 * the gamification of an application
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

    public Achievement(int leaderboardPlace, int userLevel, int allVotePoints, int streak) {

        // This Method Should Get the Lowest Leaderboard Place and Place the user there
        // Set to 0 for now until I find a way to get all the users from json and set it as an int
        if (leaderboardPlace < 0) {
            System.out.println("Invalid leaderboard place: " + leaderboardPlace + ". Setting to lowest leaderboard place."); 
            leaderboardPlace = 0; 
        }

        if (userLevel < 1) {
            System.out.println("Invalid user level: " + userLevel + ". Setting User Level to 1.");
            userLevel = 1;
        }
        if (allVotePoints < 0) {
            System.out.println("Invalid vote points: " + allVotePoints + ". Setting Vote Points to 0.");
            allVotePoints = 0;
        }
        if (streak < 0) {
            System.out.println("Invalid streak: " + streak + ". Setting Streak to 0.");
            streak = 0;
        }
        this.leaderboardPlace = leaderboardPlace;
        this.userLevel = userLevel;
        this.allVotePoints = allVotePoints;
        this.streak = streak;
    }

    // Call this when user receives a vote
    /**
     * adds a vote (an integer) to the solution
     */
    public void addVote() {
        voteCount++;
        allVotePoints = calculateVotePoints();
        levelUp();
    }

    // Call this when user answers a question
    /**
     * when a user adds a question, it is tallied and tied into the users level status 
     */
    public void addQuestion() {
        questionCount++;
        levelUp();
    }

    /**
     * a method to increase a users level (leveling up)
     */
    public void levelUp() {
        int totalPoints = calculateAllPoints();
        userLevel = (totalPoints / POINTS_PER_LEVEL) + 1;
    }

    /**
     * calculates the number of votes 
     * @return a int of the number of votes
     */
    public int calculateVotePoints() {
        return voteCount * VOTE_POINT_VALUE;
    }

    /**
     * method to calculate the number of points a question has
     * @return an int of the points
     */
    public int calculateQuestionPoints() {
        return questionCount * QUESTION_POINT_VALUE;
    }

    public int getStreak() {
        return streak;
    }

    /**
     * Sets the user's current streak value.
     *
     * @param streak the streak to store
     */
    public void setStreak(int streak) {
        if (streak < 0) { 
            System.err.println("Invalid streak: " + streak + ". Your streak is a negative number. \n"); 
            System.out.println("Setting streak to 0");
            streak = 0; 
        }
        this.streak = streak;
    }

    public int getLevel() {
        return userLevel;
    }

    /**
     * Sets the user's last active date used by the streak counter.
     *
     * @param lastActiveDate the date the user was last active
     */
    public void setLastActiveDate(LocalDate lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public LocalDate getLastActiveDate() {
        return lastActiveDate;
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

    /**
     * Updates the user's streak based on the current date and the stored
     * last active date.
     *
     * @return the updated streak value
     */
    public int streakCounter() {
        LocalDate today = LocalDate.now();

        if (lastActiveDate == null) {
            streak = 1;
        } else if (lastActiveDate.equals(today)) {
            // already logged in today — no change
        } else if (lastActiveDate.plusDays(1).equals(today)) {
            streak++;
        } else {
            // missed at least one day — streak resets
            streak = 1;
        }

        lastActiveDate = today;
        return streak;
    }

    /**
     * Calculates the total number of points for a __
     * @return int of the total points
     */
    public int calculateAllPoints() {
        return calculateVotePoints() + calculateQuestionPoints();
    }
}
