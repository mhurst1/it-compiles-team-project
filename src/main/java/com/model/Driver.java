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
        scenario4();
        scenario5();
        scenario6();
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

        questionApplication.login("asmith", "");

        if (questionApplication.getCurrentUser() == null) {
            System.out.println("Sorry we couldn't login.");
            return;
        }

        System.out.println("asmith is now logged in");

        questionApplication.logout();

        // check
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

    //Scenario 4 shows a user logging in, adding a question, and then logging out.
    public void scenario4() {
        System.out.println();

        questionApplication.login("asmith", "");

        if (questionApplication.getCurrentUser() == null) {
            System.out.println("Sorry we couldn't login.");
            return;
        }

        System.out.println("asmith is now logged in");

        // Add a question
        questionApplication.addQuestion(
                "How to implement a linked list in Java?",
                null, 
                "What is the process of creating and using a LinkedList?",
                Difficulty.MEDIUM,
                Language.JAVA, null, null);

        System.out.println("asmith has added a question.");

        questionApplication.logout();

        if (questionApplication.getCurrentUser() == null) {
            System.out.println("asmith is now logged out");
        }
    }

    //Scenario 5 shows a user logging in, accessing a question, uploading a solution, and then logging out.
    public void scenario5() {
        System.out.println();

        questionApplication.login("asmith", "");

        if (questionApplication.getCurrentUser() == null) {
            System.out.println("Sorry we couldn't login.");
            return;
        }

        System.out.println("asmith is now logged in");

        // Access a question
        questionApplication.findQuestion("How to implement a linked list in Java?");

        // Upload a solution
        questionApplication.addUserSolution(
                null,
                 "Here is a simple implementation of a linked list in Java...",
                null, 
                null,
                 0);

        System.out.println("asmith has uploaded a solution.");

        questionApplication.logout();

        if (questionApplication.getCurrentUser() == null) {
            System.out.println("asmith is now logged out");
        }
    }

    //Scenario 6 shows a user logging in, accessing a question uploaded by asmith, commenting on the solution, and then logging out.
    public void scenario6() {
        System.out.println();

        questionApplication.login("jbrown1", "");

        if (questionApplication.getCurrentUser() == null) {
            System.out.println("Sorry we couldn't login.");
            return;
        }

        System.out.println("jbrown1 is now logged in");

        // Access a question
        questionApplication.findQuestion("How to implement a linked list in Java?");

        // Comment on the solution
        questionApplication.addComment(
                "This is a great question! I have a question about the time complexity.",
                null, 
                null);

        System.out.println("jbrown1 has commented on the solution.");

        questionApplication.logout();

        if (questionApplication.getCurrentUser() == null) {
            System.out.println("jbrown1 is now logged out");
        }
    }
    


    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.run();
    }
}