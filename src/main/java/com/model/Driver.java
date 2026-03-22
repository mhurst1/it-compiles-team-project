package com.model;

import com.interviews.Difficulty;
import com.interviews.Language;
import com.interviews.QuestionApplication;

/**
 * Driver class used to test the login and logout functionality
 * of the QuestionApplication system.
 * 
 * It runs two scenarios:
 * 1. A successful login followed by a logout.
 * 2. An unsuccessful login attempt.
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
     * Scenario 2 shows an unsuccessful login attempt.
     * 
     * The method attempts to log in using invalid credentials.
     * If authentication fails, an error message is displayed.
     */
    public void scenario2() {
        System.out.println();

        questionApplication.login("wronguser", "wrongpass");

        if (questionApplication.getCurrentUser() == null) {
            System.out.println("Sorry we couldn't login.");
            return;
        }

        System.out.println("User is now logged in");
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

    //Scenario 4: Sally successfully creates an account, sets contributor status, and logs in
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


    

    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.run();
    }
}
