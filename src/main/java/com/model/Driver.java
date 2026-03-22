package com.model;

import com.interviews.Difficulty;
import com.interviews.Language;
import com.interviews.QuestionApplication;
import com.interviews.Status;
import com.interviews.User;

/**
 * Driver class used to test account creation and login flows
 * in the QuestionApplication system.
 *
 * It runs three scenarios:
 * 1. Duplicate account creation is rejected.
 * 2. A new contributor/publisher-style account is created and logged in.
 * 3. A new standard account is created, logged in, and logged out.
 */

public class Driver {
    private QuestionApplication questionApplication;

    Driver() {
        questionApplication = new QuestionApplication();
    }

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
     * Scenario 3 shows creating an account, logging in with that account,
     * and then logging out.
     *
     * The method creates a new user account, attempts to log in using the
     * new credentials, and verifies that logout works properly.
     */
    public void scenario3() {
        System.out.println();

        // Create a new account
        questionApplication.createAccount(
                "New", "User",
                "newuser",
                "newpass",
                "newuser@email.com",
                2026,
                "U12345678");

        questionApplication.login("newuser", "newpass");

        if (questionApplication.getCurrentUser() == null) {
            System.out.println("Sorry we couldn't login the new account.");
            return;
        }

        System.out.println("newuser is now logged in");

        questionApplication.logout();

        if (questionApplication.getCurrentUser() == null) {
            System.out.println("newuser is now logged out");
        }
    }
    

    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.run();
    }
}
