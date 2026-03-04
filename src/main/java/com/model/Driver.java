package com.model;

import java.util.Scanner;

import com.interviews.QuestionApplication;

public class Driver {
    private QuestionApplication questionApplication;

    Driver() {
        questionApplication = new QuestionApplication();
    }

    public void run() {
        scenario1();
        scenario2();
    }

    public void scenario1() {
        System.out.println();

        questionApplication.login("asmith", "12345");

        if (questionApplication.getCurrentUser() == null) {
            System.out.println("Sorry we couldn't login.");
            return;
        }

        System.out.println("asmith is now logged in");
    }

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