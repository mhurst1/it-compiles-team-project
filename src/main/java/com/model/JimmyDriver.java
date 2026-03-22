package com.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.interviews.Achievement;
import com.interviews.Comment;
import com.interviews.Difficulty;
import com.interviews.Language;
import com.interviews.Question;

import java.util.ArrayList;
import java.util.UUID;
import com.interviews.QuestionApplication;
import com.interviews.Section;
import com.interviews.Status;
import com.interviews.User;
import com.interviews.UserSolution;


/**
 * JimmyDriver class simulates a user named "Jimmy Cricket" interacting with the QuestionApplication system.
 * The driver performs the following steps:
 * 1. Logs in with valid credentials.
 * 2. Checks and prints the user's current daily streak and achievement level.
 * 3. Retrieves and prints the daily challenge question.
 * 4. Reviews the solutions for the daily challenge question.
 * 5. Posts a comment on the second solution of the daily challenge question.
 * 6. Prints the daily challenge question and its details to a text file.
 * 7. Searches for questions related to "Binary Search Tree" and prints the results.
 * 8. Logs out of the application.
 */
public class JimmyDriver {

    public static void main(String [] args) {

QuestionApplication app = new QuestionApplication();

    //Step 1: LOGIN 
    boolean loggedIn = app.login("jcricket6", "ilovecrickets3");

    if(!loggedIn){
        System.out.println(" Login failed. Check username and password.");
        return;
    }

    User currentUser = app.getCurrentUser();
    System.out.println(" Login successful! Welcome back, " + currentUser.getFirstName() 
    + " " + currentUser.getLastName() + "!");

    //Step 2: STREAK

    Achievement achievement = currentUser.getAchievements().get(0);
    System.out.println(" Daily Streak : " + achievement.getStreak() + " days");
    System.out.println(" Current Level: " + achievement.getLevel());

    //Step 3: DAILY CHALLENGE

    ArrayList<Question> allQuestions = app.getQuestions();

    if(allQuestions.isEmpty()){
        System.out.println(" No questions available");
        return;
    }

    Question dailyChallenge = allQuestions.get(0);

    System.out.println(" Today's challenge: ");
    System.out.println(" --------------------------------------------");
    System.out.printf(" Title    : %-38s|%n", dailyChallenge.getTitle());
    System.out.printf(" Difficulty       : %-38s|%n", dailyChallenge.getDifficulty());
    System.out.printf( " Language     :%-38s|%n", dailyChallenge.getLanguage());
    System.out.println(" --------------------------------------------");

    //Step 4: CLICK ON QUESTION AND REVIEW SOLUTIONS
    //method below

    printQuestion(dailyChallenge);

    //Step 5 and 6: JIMMY COMMENTS ON SOLUTION 2
    
    ArrayList<UserSolution> solutions = dailyChallenge.getSolutionList();

    if(solutions != null && solutions.size() >= 2){
        UserSolution solutionTwo = solutions.get(1);

        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));

        String commentText = "[" + currentUser.getUsername() + " - " + dateStr + "]" 
        + "I am confused about this solution, can you explain?";

        app.addComment(solutionTwo, currentUser, commentText);

        System.out.println(" Comment posted succesfully");
        System.out.println(" " + "-".repeat(56));
        for(Comment c : solutionTwo.getReplies()){
            System.out.println(" " + c.getComment());
        }
        System.out.println(" "+ "-".repeat(56));


        } else{ 
            System.out.println(" Solution not availble on this question");
        }

        //Step 7: JIMMY PRINTS QUESTION TO A TEXT FILE
        //method below

        String filename = dailyChallenge.getTitle().replace(" ", "_") + "_Review.txt";
        printQuestionToFile(dailyChallenge, filename);
        System.out.println(" Exported to: " + filename);

        //Step 8: JIMMY SEARCHES FOR BINARY SEARCH TREE

        ArrayList<Question> searchResults = app.findQuestion("Binary Search Tree");
        System.out.printf(" Found %d result(s):%n", searchResults.size());
        for(Question q : searchResults){
            System.out.printf(" [%-10s] %s%n", q.getDifficulty(), q.getTitle());
        }

        //Step 9: JIMMY GOES THROUGH BOTH RESULTS

        for (int i = 0; i < searchResults.size(); i++){
            System.out.println("\n - result " + (i+1));
            printQuestion(searchResults.get(i));
        }

        //Step 10: DAILY STREAK INCREMENTS

        int oldStreak = achievement.getStreak();
        achievement.streakCounter();
        System.out.printf(" Streak updated: %d -> %d days%n", oldStreak, achievement.getStreak());

        //Step 11: JIMMY LOGS OUT

        app.logout();
        System.out.println(" Jimmy has been logged out. See you next time!");

    }

    /**
     * Helper method to print the details of a question in a formatted manner.
     * @param q The Question object to be printed.
     */
    private static void printQuestion(Question q) {

        String bar = "   " + "-".repeat(56);
        System.out.println(bar);
        System.out.printf("   Title       : %s%n", q.getTitle());
        System.out.printf("   Difficulty       : %-12s    Language: %s%n", q.getDifficulty(), q.getLanguage());
        System.out.println(bar);
        System.out.println("     Description: ");
        System.out.println("      " + q.getDescription());

        if(q.getQuestionContent() != null && !q.getQuestionContent().isEmpty()) { 
            System.out.println(bar);
            for(Section s : q.getQuestionContent()) { 
                System.out.println(" [" + s.getSectionTitle() + "]");
                System.out.println("    " + s.getSectionText() + "    ");
                for(String line : s.getSectionContent()) { 
                    System.out.println("     - " + line);
                }

            }

        }
        if (q.getHints() != null && !q.getHints().isEmpty()) {
            System.out.println(bar);
            System.out.println(" Hints: ");
            for(int i = 0; i < q.getHints().size(); i++){
                System.out.printf(".     %d. %s%n", i+1, q.getHints().get(i));
            }
        }

        ArrayList<UserSolution> sols = q.getSolutionList();
        if(sols != null && !sols.isEmpty()){
                        System.out.printf("  Solutions (%d):%n", sols.size());
            for (int i = 0; i < sols.size(); i++) {
                System.out.println("\n  ── Solution " + (i + 1) + " ──");
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
     * Helper method to print the details of a question to a text file in a formatted manner.
     * @param q The Question object to be printed.
     * @param filename The name of the file to which the question details will be written.
     */
private static void printQuestionToFile(Question q, String filename){

            try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
 
            String thick = "=".repeat(70);
            String thin  = "-".repeat(70);
 
            pw.println(thick);
            pw.printf("  QUESTION: %s%n", q.getTitle());
            pw.println(thick);
            pw.printf("  Difficulty  : %s%n", q.getDifficulty());
            pw.printf("  Language    : %s%n", q.getLanguage());
            pw.printf("  Printed on  : %s%n",
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
                    for (String line : s.getSectionContent()) {
                        pw.println("  - " + line);
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
                    pw.println("── Solution " + (i + 1) + " " + "─".repeat(57));
                    pw.println(sols.get(i).getDescription());
                    pw.println("Votes: " + sols.get(i).getTotalVote());
                    ArrayList<Comment> replies = sols.get(i).getReplies();
                    if (replies != null && !replies.isEmpty()) {
                        pw.println();
                        pw.println("  COMMENTS:");
                        for (Comment c : replies) {
                            pw.println("    " + c.getComment());
                        }
                    }
                    pw.println(thin);
                }
            }
 
        } catch (IOException e) {
            System.err.println("  Could not write file: " + e.getMessage());
        }
}

}




    
