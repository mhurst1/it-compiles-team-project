package com.model;

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
    }


     /**
     * Scenario 1 shows a successful login followed by a logout.
     * 
     * The method attempts to login a valid user ("asmith"). If the login
     * succeeds, the user is then logged out and the logout is verified
     * by checking if the current user becomes null.
     */
    public void scenario1() {
        System.out.println();

        questionApplication.login("asmith", "12345");

        if (questionApplication.getCurrentUser() == null) {
            System.out.println("Sorry we couldn't login.");
            return;
        }

        System.out.println("asmith is now logged in");

        questionApplication.logout();

        //check
        if (questionApplication.getCurrentUser() == null) {
            System.out.println("asmith is now logged out");
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

    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.run();
    }
}