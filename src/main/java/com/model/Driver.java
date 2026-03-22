package com.model;

import java.util.ArrayList;

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
 * It runs three scenarios:
 * 1. Duplicate account creation is rejected.
 * 2. A new contributor/publisher-style account is created and logged in.
 * 3. Sally creates a new question and two solutions.
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
        scenario1();
        scenario2();
        scenario3();
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
    public void scenario4() {
        System.out.println();

        // Sally creates an account
        questionApplication.createAccount(
                 "Sally", "Sparrow",
                    "ssparrow",
                    "sparrowpass2",
                    "sally.sparrow@email.com",
                    2028,
                    "U33334444");

        
        // Sally sets contributor status
        questionApplication.setStatus("ssparrow", "contributor");

        // Sally logs in
        questionApplication.login("ssparrow", "sparrowpass2");
    
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
