package com.interviews;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

/**
 * MH and SE
 * DataWriter is responsible for saving the current state of users and questions to JSON files.
 * It provides methods to save users and questions, as well as helper methods to convert complex objects into JSON string representations.
 * The saveUsers() method saves the list of users to "data/users.json", while the saveQuestions() method saves the list of questions to "data/questions.json".
 * The helper methods include listIds() for converting a list of questions to a JSON array of their IDs, achList() for converting a list of achievements to a JSON array, solutionList() for converting a list of user solutions to a JSON array, sectionList() for converting a list of sections to a JSON array, enumToArray() for converting an enum value to a JSON array, stringList() for converting a list of strings to a JSON array, and commentList() for converting a list of comments to a JSON array.
 * The class handles exceptions that may occur during file writing and returns a boolean indicating the success of the save operation.
 */
public class DataWriter {

    /**
     * Saves the list of users to a JSON file. It retrieves the list of users from the UserList singleton instance, creates a "data" directory if it doesn't exist, and writes the user data to "data/users.json" in JSON format. Each user is represented as a JSON object with fields such as id, first-name, last-name, username, email, graduation-year, id-usc, starred-questions, answered-questions, and achievements. The method returns true if the save operation is successful and false if an exception occurs.
     * @return boolean indicating the success of the save operation
     * @throws Exception if an error occurs during file writing
     */
    public boolean saveUsers() {
        try {
            List<User> users = UserList.getInstance().getUsers();
            new File("data").mkdirs();

            try (PrintWriter w = new PrintWriter("data/users.json")) {
                w.println("[");

                for (int i = 0; i < users.size(); i++) {
                    User u = users.get(i);

                    w.print("  {"
                            + "\"id\":\"" + u.getId() + "\","
                            + "\"first-name\":\"" + u.getFirstName() + "\","
                            + "\"last-name\":\"" + u.getLastName() + "\","
                            + "\"username\":\"" + u.getUsername() + "\","
                            + "\"email\":\"" + u.getEmail() + "\","
                            + "\"graduation-year\":" + u.getGraduationYear() + ","
                            + "\"id-usc\":\"" + u.getIdUSC() + "\","
                            + "\"starred-questions\":" + listIds(u.getStarredQuestionList()) + ","
                            + "\"answered-questions\":" + listIds(u.getAnsweredQuestions()) + ","
                            + "\"achievements\":" + achList(u.getAchievements())
                            + "}");

                    w.println(i < users.size() - 1 ? "," : "");
                }
                w.println("]");
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Saves the list of questions to a JSON file. It retrieves the list of questions from the QuestionList singleton instance, creates a "data" directory if it doesn't exist, and writes the question data to "data/questions.json" in JSON format. Each question is represented as a JSON object with fields such as id, title, user, description, question-content, hints, difficulty, question-language, solution-list, given-solution-img, and given-solution-text. The method returns true if the save operation is successful and false if an exception occurs.
     * @return boolean indicating the success of the save operation
     * @throws Exception if an error occurs during file writing
     */
    public boolean saveQuestions() {
        try {
            List<Question> questions = QuestionList.getInstance().getQuestions();
            new File("data").mkdirs();

            try (PrintWriter w = new PrintWriter("data/questions.json")) {
                w.println("[");

                for (int i = 0; i < questions.size(); i++) {
                    Question q = questions.get(i);

                    w.print("  {"
                            + "\"id\":\"" + q.getId() + "\","
                            + "\"title\":\"" + q.getTitle() + "\","
                            + "\"user\":\"" + (q.getUser() == null ? "" : q.getUser().getId()) + "\","
                            + "\"description\":\"" + q.getDescription() + "\","
                            + "\"question-content\":" + sectionList(q.getQuestionContent()) + ","
                            + "\"hints\":" + stringList(q.getHints()) + ","
                            + "\"difficulty\":" + enumToArray(q.getDifficulty()) + ","
                            + "\"question-language\":" + enumToArray(q.getLanguage()) + ","
                            + "\"solution-list\":" + solutionList(q.getSolutionList()) + ","
                            + "\"given-solution-img\":" + stringList(q.getGivenSolutionImg()) + ","
                            + "\"given-solution-text\":" + stringList(q.getGivenSolutionText())
                            + "}");

                    w.println(i < questions.size() - 1 ? "," : "");
                }
                w.println("]");
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Converts a list of questions into a JSON array of their unique IDs. If the list is null or empty, it returns an empty JSON array "[]". Otherwise, it constructs a JSON array string by iterating through the list of questions and appending their IDs in the format ["id1", "id2", ...]. The method uses a StringBuilder to efficiently build the resulting string and ensures that commas are correctly placed between the IDs.
     * @param list the list of questions to convert
     * @return a JSON array string representation of the question IDs
     */
    static String listIds(List<Question> list) {
        if (list == null || list.isEmpty())
            return "[]";
        StringBuilder b = new StringBuilder("[");

        for (int i = 0; i < list.size(); i++) {
            b.append("\"").append(list.get(i).getId()).append("\"");
            if (i < list.size() - 1)
                b.append(", ");
        }

        b.append("]");
        return b.toString();
    }

    /**
     * Converts a list of achievements into a JSON array of achievement objects. If the list is null or empty, it returns an empty JSON array "[]". Otherwise, it constructs a JSON array string by iterating through the list of achievements and appending their details in the format [{"leaderboard-place":..,"user-level":..,"all-vote-points":..,"streak":..}, ...]. The method uses a StringBuilder to efficiently build the resulting string and ensures that commas are correctly placed between the achievement objects.
     * @param a the list of achievements to convert
     * @return a JSON array string representation of the achievements
     */
    static String achList(List<Achievement> a) {
        if (a == null || a.isEmpty())
            return "[]";
        StringBuilder b = new StringBuilder("[");

        for (int i = 0; i < a.size(); i++) {
            Achievement x = a.get(i);
            b.append("{\"leaderboard-place\":").append(x.getLeaderboardPlace())
                    .append(",\"user-level\":").append(x.getLevel())
                    .append(",\"all-vote-points\":").append(x.getAllVotePoints())
                    .append(",\"streak\":").append(x.getStreak()).append("}");
            if (i < a.size() - 1)
                b.append(", ");
        }

        b.append("]");
        return b.toString();
    }

    /**
     * Converts a list of user solutions into a JSON array of user solution objects. If the list is null or empty, it returns an empty JSON array "[]". Otherwise, it constructs a JSON array string by iterating through the list of user solutions and appending their details in the format [{"user":"userId","description":"solutionDescription","thread":[...],"user-vote":userVote,"total-vote":totalVote}, ...]. The method uses a StringBuilder to efficiently build the resulting string and ensures that commas are correctly placed between the user solution objects. It also handles null values for user and description by substituting them with empty strings.
     * @param s the list of user solutions to convert
     * @return a JSON array string representation of the user solutions
     */
    static String solutionList(List<UserSolution> s) {
        if (s == null || s.isEmpty())
            return "[]";
        StringBuilder b = new StringBuilder("[");

        for (int i = 0; i < s.size(); i++) {
            UserSolution us = s.get(i);
            String uid = us.getUser() == null ? "" : String.valueOf(us.getUser().getId());
            String desc = us.getDescription() == null ? "" : us.getDescription();
            b.append("{")
                    .append("\"user\":\"").append(uid).append("\",")
                    .append("\"description\":\"").append(desc).append("\",")
                    .append("\"thread\":").append(commentList(us.getThread())).append(",")
                    .append("\"user-vote\":").append(us.getUserVote()).append(",")
                    .append("\"total-vote\":").append(us.getTotalVote())
                    .append("}");
            if (i < s.size() - 1)
                b.append(",");
        }

        b.append("]");
        return b.toString();
    }

    /**
     * Converts a list of sections into a JSON array of section objects. If the list is null or empty, it returns an empty JSON array "[]". Otherwise, it constructs a JSON array string by iterating through the list of sections and appending their details in the format [{"section-title":"...","section-content":[...],"section-text":"...","fileName":"..."}, ...]. The method uses a StringBuilder to efficiently build the resulting string and ensures that commas are correctly placed between the section objects. It also handles null values for section title, section text, and file name by substituting them with empty strings.
     * @param sections the list of sections to convert
     * @return a JSON array string representation of the sections
     */
    static String sectionList(List<Section> sections) {
        if (sections == null || sections.isEmpty())
            return "[]";
        StringBuilder b = new StringBuilder("[");

        for (int i = 0; i < sections.size(); i++) {
            Section s = sections.get(i);
            b.append("{")
                    .append("\"section-title\":\"").append(s.getSectionTitle()).append("\",")
                    .append("\"section-content\":").append(stringList(s.getSectionContent())).append(",")
                    .append("\"section-text\":\"").append(s.getSectionText()).append("\",")
                    .append("\"fileName\":\"").append(s.getFileName()).append("\"")
                    .append("}");

            if (i < sections.size() - 1) {
                b.append(",");
            }
        }

        b.append("]");
        return b.toString();
    }

    /**
     * Converts an enum value into a JSON array containing the enum name. If the enum value is null, it returns an empty JSON array "[]". Otherwise, it returns a JSON array in the format ["ENUM_NAME"]. This method is useful for converting enum values such as difficulty and question language into a JSON format that can be easily parsed when loading data.
     * @param e the enum value to convert
     * @return a JSON array string representation of the enum value
     */
    static String enumToArray(Enum<?> e) {
        if (e == null)
            return "[]";
        return "[\"" + e.name() + "\"]";
    }

    /**
     * Converts a list of strings into a JSON array. If the list is null or empty, it returns an empty JSON array "[]". Otherwise, it constructs a JSON array string by iterating through the list of strings and appending their values in the format ["string1","string2",...]. The method uses a StringBuilder to efficiently build the resulting string and ensures that commas are correctly placed between the string values. It also handles null values for strings by substituting them with empty strings.
     * @param list the list of strings to convert
     * @return a JSON array string representation of the strings
     */
    static String stringList(List<String> list) {
        if (list == null || list.isEmpty())
            return "[]";
        StringBuilder b = new StringBuilder("[");

        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i) == null ? "" : list.get(i);
            b.append("\"").append(s).append("\"");
            if (i < list.size() - 1) {
                b.append(",");
            }
        }

        b.append("]");
        return b.toString();
    }

    // Convert List<Comment> -> [{"user":..,"comment":..,"replies":[...]}]
    /**
     * Converts a list of comments into a JSON array of comment objects. If the list is null or empty, it returns an empty JSON array "[]". Otherwise, it constructs a JSON array string by iterating through the list of comments and appending their details in the format [{"user":"userId","comment":"commentText","replies":[...]}]. The method uses a StringBuilder to efficiently build the resulting string and ensures that commas are correctly placed between the comment objects. It also handles null values for user and comment text by substituting them with empty strings.
     * @param comments the list of comments to convert
     * @return a JSON array string representation of the comments
     */
    static String commentList(List<Comment> comments) {
        if (comments == null || comments.isEmpty())
            return "[]";

        StringBuilder b = new StringBuilder("[");

        for (int i = 0; i < comments.size(); i++) {
            Comment c = comments.get(i);
            String userId;
            if (c.getUser() == null) {
                    userId = "";
                } else {
                    userId = String.valueOf(c.getUser().getId());
                    
                }
            String commentText;
                if (c.getComment() == null) {
                    commentText = "";
                } else {
                    commentText = c.getComment();
                }

            b.append("{")
                    .append("\"user\":\"").append(userId).append("\",")
                    .append("\"comment\":\"").append(commentText).append("\",")
                    .append("\"replies\":").append(commentList(c.getReplies()))
                    .append("}");

            if (i < comments.size() - 1)
                b.append(",");
        }

        b.append("]");
        return b.toString();
    }

}

