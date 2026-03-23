package com.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.interviews.Achievement;
import com.interviews.Comment;
import com.interviews.DataWriter;
import com.interviews.Difficulty;
import com.interviews.Language;
import com.interviews.Question;
import com.interviews.QuestionApplication;
import com.interviews.Section;
import com.interviews.Status;
import com.interviews.User;
import com.interviews.UserSolution;

/**
 * Driver class used to test account creation and login flows
 * in the QuestionApplication system.
 *
 * It runs five scenarios:
 * 1. Duplicate account creation is rejected.
 * 2. A new contributor/publisher-style account is created and logged in.
 * 3. Sally creates a new question and two solutions.
 * 4. Sally uses the original project flow.
 * 5. Jimmy logs in, reviews a daily challenge, comments on a solution,
 *    exports the question, searches for Binary Search Tree questions,
 *    updates his streak, and logs out.
 */
public class Driver {
    private QuestionApplication questionApplication;

    /**
     * Creates the driver and initializes the shared application facade.
     */
    Driver() {
        questionApplication = new QuestionApplication();
    }

    /**
     * Runs the configured driver scenarios in order.
     */
    public void run() {
        //scenario1();
        //scenario2();
        //scenario3();
        //scenario4();
    }

    /**
     * Scenario 1 shows a duplicate account creation being rejected.
     *
     * Sullivan Sparrow creates an account first. Sally Sparrow then attempts
     * to create an account using the same username and is rejected.
     */
    public void scenario1() {
        System.out.println();

        try {
            questionApplication.createAccount(
                    "Sullivan", "Sparrow",
                    "ssparrow",
                    "sparrowpass1",
                    "sullivan.sparrow@email.com",
                    2027,
                    "U11112222");

            System.out.println("Sullivan Sparrow's account was created.");

            questionApplication.createAccount(
                    "Sally", "Sparrow",
                    "ssparrow",
                    "sparrowpass2",
                    "sally.sparrow@email.com",
                    2028,
                    "U33334444");

            System.out.println("Sally Sparrow's account was created.");
        } catch (IllegalArgumentException e) {
            System.out.println("Sally Sparrow's account was rejected: " + e.getMessage());
        }
    }

    /**
     * Scenario 2 shows successful account creation followed by a valid login.
     *
     * Sally changes her credentials so they no longer match Sullivan's, creates
     * an account, is marked as a publisher/contributor, and then logs in.
     */
    public void scenario2() {
        System.out.println();

        try {
            User sally = questionApplication.createAccount(
                    "Sally", "Sparrow",
                    "sallysparrow",
                    "sparrowpass2",
                    "sally.sparrow@email.com",
                    2028,
                    "U33334444");

            sally.setStatus(Status.CONTRIBUTOR);
            System.out.println("Sally Sparrow created a publisher account.");

            questionApplication.login("sallysparrow", "sparrowpass2");

            if (questionApplication.getCurrentUser() == null) {
                System.out.println("Sorry we couldn't login Sally Sparrow.");
                return;
            }

            System.out.println("Sally Sparrow validly logged in.");
        } catch (IllegalArgumentException e) {
            System.out.println("Sally Sparrow's account could not be created: " + e.getMessage());
        }
    }

    /**
     * Scenario 3 shows Sally creating a new question and adding two solutions.
     */
    public void scenario3() {
        System.out.println();

        if (!questionApplication.login("sallysparrow", "sparrowpass2")
                && questionApplication.getCurrentUser() == null) {
            System.out.println("Sally Sparrow must be logged in to create a question.");
            return;
        }

        User sally = questionApplication.getCurrentUser();
        ArrayList<String> hints = new ArrayList<>();
        hints.add("Think about prefix sums.");
        hints.add("The array can contain negative numbers, so a sliding window is not enough.");
        hints.add("Store the first occurrence of each prefix sum.");

        ArrayList<Section> questionContent = new ArrayList<>();

        ArrayList<String> promptLines = new ArrayList<>();
        promptLines.add("Given an integer array nums and an integer sum, return the length of the longest contiguous subarray whose total equals k.");
        promptLines.add("The array can contain negative numbers.");
        questionContent.add(new Section(
                "Problem Statement",
                promptLines,
                "Find the length of the longest contiguous subarray whose sum is k."));

        ArrayList<String> exampleOneLines = new ArrayList<>();
        exampleOneLines.add("Input: nums = [1, -1, 5, -2, 3], k = 3");
        exampleOneLines.add("Output: 4");
        exampleOneLines.add("Explanation: The subarray [1, -1, 5, -2] sums to 3 and has length 4.");
        questionContent.add(new Section(
                "Example 1",
                exampleOneLines,
                "Longest valid subarray length is 4."));

        ArrayList<String> exampleTwoLines = new ArrayList<>();
        exampleTwoLines.add("Input: nums = [-2, -1, 2, 1], k = 3");
        exampleTwoLines.add("Output: 2");
        questionContent.add(new Section(
                "Example 2",
                exampleTwoLines,
                "A second example with a different longest length."));

        ArrayList<String> followUpLines = new ArrayList<>();
        followUpLines.add("What is the time complexity of your algorithm?");
        followUpLines.add("Can you find a way to make your algorithm faster?");
        questionContent.add(new Section(
                "Follow-up Questions",
                followUpLines,
                "Compare the brute force and optimized approaches."));

        questionApplication.addQuestion(
                "Longest Subarray with given Sum",
                sally,
                "Return the length of the longest contiguous subarray whose sum equals k.",
                Difficulty.MEDIUM,
                Language.JAVA,
                hints,
                questionContent);

        Question question = questionApplication.getQuestions()
                .get(questionApplication.getQuestions().size() - 1);

        UserSolution bruteForceSolution = new UserSolution(
                sally,
                "Solution 1 - Brute Force Approach: Try every possible subarray and compute its sum. "
                        + "Time Complexity: O(n^2). Java file: LongestSubarrayBruteForce.java");

        UserSolution hashMapSolution = new UserSolution(
                sally,
                "Solution 2 - HashMap Version: Keep track of the running prefix sum in a HashMap and "
                        + "store the first occurrence of each prefix sum. If (prefixSum - k) exists, "
                        + "we found a valid subarray. Time Complexity: O(n). Java file: LongestSubarrayHashMap.java");

        question.getSolutionList().add(bruteForceSolution);
        question.getSolutionList().add(hashMapSolution);
        questionApplication.getQuestionList().save();

        System.out.println("Sally Sparrow created the question: " + question.getTitle());
        System.out.println("Sally Sparrow added 2 solutions to the question.");
    }

    /**
     * Scenario 4 shows Sally creating an account, setting contributor status,
     * and logging in using the original project flow.
     */


    /**
     * Scenario 4 merges JimmyDriver into this shared driver.
     *
     * Jimmy logs in, checks his streak, opens the daily challenge,
     * comments on solution 2, exports the question to a text file,
     * searches for Binary Search Tree questions, updates his streak,
     * and logs out.
     */
    public void scenario4() {
        System.out.println();

        boolean loggedIn = questionApplication.login("jcricket6", "ilovecrickets3");

        if (!loggedIn || questionApplication.getCurrentUser() == null) {
            System.out.println("Jimmy login failed. Check username and password.");
            return;
        }

        User currentUser = questionApplication.getCurrentUser();
        System.out.println("Login successful! Welcome back, "
                + currentUser.getFirstName() + " " + currentUser.getLastName() + "!");

        Achievement achievement = null;
        if (currentUser.getAchievements() != null && !currentUser.getAchievements().isEmpty()) {
            achievement = currentUser.getAchievements().get(0);
            System.out.println("Daily Streak: " + achievement.getStreak() + " days");
            System.out.println("Current Level: " + achievement.getLevel());
        } else {
            System.out.println("No achievements yet!");
        }

        ArrayList<Question> allQuestions = questionApplication.getQuestions();
        if (allQuestions == null || allQuestions.isEmpty()) {
            System.out.println("No questions available.");
            return;
        }

        Question dailyChallenge = allQuestions.get(0);

        System.out.println("Today's challenge:");
        printQuestion(dailyChallenge);

        ArrayList<UserSolution> solutions = dailyChallenge.getSolutionList();

        if (solutions != null && solutions.size() >= 2) {
            UserSolution solutionTwo = solutions.get(1);

            String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
            String commentText = "[" + currentUser.getUsername() + " - " + dateStr + "] "
                    + "I am confused about this solution, can you explain?";

            questionApplication.addComment(solutionTwo, currentUser, commentText);

            System.out.println("Comment posted successfully");
            System.out.println("-".repeat(56));
            if (solutionTwo.getReplies() != null) {
                for (Comment c : solutionTwo.getReplies()) {
                    System.out.println(c.getComment());
                }
            }
            System.out.println("-".repeat(56));
        } else {
            System.out.println("Solution 2 is not available on this question.");
        }

        String filename = dailyChallenge.getTitle().replace(" ", "_") + "_Review.txt";
        printQuestionToFile(dailyChallenge, filename);
        System.out.println("Exported to: " + filename);

        ArrayList<Question> searchResults = questionApplication.findQuestion("Binary Search Tree");
        System.out.printf("Found %d result(s):%n", searchResults.size());
        for (Question q : searchResults) {
            System.out.printf("[%-10s] %s%n", q.getDifficulty(), q.getTitle());
        }

        for (int i = 0; i < searchResults.size(); i++) {
            System.out.println("\nResult " + (i + 1));
            printQuestion(searchResults.get(i));
        }

        if (achievement != null) {
            int oldStreak = achievement.getStreak();
            achievement.streakCounter();
            System.out.printf("Streak updated: %d -> %d days%n", oldStreak, achievement.getStreak());
        }

        DataWriter writer = new DataWriter();
        writer.saveUsers();
        writer.saveQuestions();

        questionApplication.logout();
        System.out.println("Jimmy has been logged out. See you next time!");
        
    }

    /**
     * Prints a question and its related data to the console.
     *
     * @param q the question to print
     */
    private static void printQuestion(Question q) {
        String bar = "   " + "-".repeat(56);
        System.out.println(bar);
        System.out.println("   Title      : " + q.getTitle());
        System.out.println("   Difficulty : " + q.getDifficulty() + "    Language: " + q.getLanguage());
        System.out.println(bar);
        System.out.println("   Description:");
        System.out.println("   " + q.getDescription());

        if (q.getQuestionContent() != null && !q.getQuestionContent().isEmpty()) {
            System.out.println(bar);
            for (Section s : q.getQuestionContent()) {
                System.out.println("   [" + s.getSectionTitle() + "]");
                System.out.println("    " + s.getSectionText());
                if (s.getSectionContent() != null) {
                    for (String line : s.getSectionContent()) {
                        System.out.println("     - " + line);
                    }
                }
            }
        }

        if (q.getHints() != null && !q.getHints().isEmpty()) {
            System.out.println(bar);
            System.out.println("   Hints:");
            for (int i = 0; i < q.getHints().size(); i++) {
                System.out.printf("     %d. %s%n", i + 1, q.getHints().get(i));
            }
        }

        ArrayList<UserSolution> sols = q.getSolutionList();
        if (sols != null && !sols.isEmpty()) {
            System.out.printf("   Solutions (%d):%n", sols.size());
            for (int i = 0; i < sols.size(); i++) {
                System.out.println("\n   -- Solution " + (i + 1) + " --");
                System.out.println("    " + sols.get(i).getDescription());
                System.out.println("    Votes: " + sols.get(i).getTotalVote());

                ArrayList<Comment> replies = sols.get(i).getReplies();
                if (replies != null && !replies.isEmpty()) {
                    System.out.println("    Comments:");
                    for (Comment c : replies) {
                        System.out.println("      " + c.getComment());
                    }
                }
            }
        }
        System.out.println(bar);
    }

    /**
     * Writes a formatted version of the question to a text file.
     *
     * @param q the question to export
     * @param filename the name of the output text file
     */
    private static void printQuestionToFile(Question q, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {

            String thick = "=".repeat(70);
            String thin = "-".repeat(70);

            pw.println(thick);
            pw.println("QUESTION: " + q.getTitle());
            pw.println(thick);
            pw.println("Difficulty : " + q.getDifficulty());
            pw.println("Language   : " + q.getLanguage());
            pw.printf("Printed on : %s%n",
                    LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
            pw.println();

            pw.println("DESCRIPTION");
            pw.println(thin);
            pw.println("  " + q.getDescription());
            pw.println();

            if (q.getQuestionContent() != null) {
                for (Section s : q.getQuestionContent()) {
                    pw.println(s.getSectionTitle().toUpperCase());
                    pw.println(thin);
                    pw.println("  " + s.getSectionText());
                    if (s.getSectionContent() != null) {
                        for (String line : s.getSectionContent()) {
                            pw.println("  - " + line);
                        }
                    }
                    pw.println();
                }
            }

            if (q.getHints() != null && !q.getHints().isEmpty()) {
                pw.println("HINTS");
                pw.println(thin);
                for (int i = 0; i < q.getHints().size(); i++) {
                    pw.printf("  %d. %s%n", i + 1, q.getHints().get(i));
                }
                pw.println();
            }

            ArrayList<UserSolution> sols = q.getSolutionList();
            if (sols != null && !sols.isEmpty()) {
                pw.println("SOLUTIONS");
                pw.println(thick);
                for (int i = 0; i < sols.size(); i++) {
                    pw.println();
                    pw.println("Solution " + (i + 1));
                    pw.println(thin);
                    pw.println(sols.get(i).getDescription());
                    pw.println("Votes: " + sols.get(i).getTotalVote());

                    ArrayList<Comment> replies = sols.get(i).getReplies();
                    if (replies != null && !replies.isEmpty()) {
                        pw.println();
                        pw.println("COMMENTS:");
                        for (Comment c : replies) {
                            pw.println("  " + c.getComment());
                        }
                    }
                    pw.println(thin);
                }
            }

        } catch (IOException e) {
            System.out.println("Could not write file: " + filename);
            e.printStackTrace();
        }
    }

    /**
     * Launches the driver scenarios from the command line.
     *
     * @param args command-line arguments, unused
     */
    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.run();
    }
}
