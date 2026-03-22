package com.interviews;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Loads user and question data from JSON files and converts that data
 * into Java objects used by the interview application.
 * 
 * This class manually parses JSON text without using an external JSON library.
 * It supports loading:
 * Users
 * Questions
 * Sections inside questions
 * Solutions and nested comment threads
 * 
 * The class also includes a tester in the main method to verify that
 * the JSON data is being loaded correctly.
 */
public class DataLoader{

    /** Base directory containing the JSON data files. */
    private static final String DATA_DIR = "it-compiles-team-project/json";

    /** Path to the users JSON file. */
    private static final String USERS_PATH = DATA_DIR + "/users.json";

    /** Path to the questions JSON file. */
    private static final String QUESTIONS_PATH = DATA_DIR + "/question.json";

    /**
     * Loads all users from the users JSON file.
     * 
     * The method reads the raw JSON, splits the top-level user objects,
     * parses each one, and stores them in an ArrayList.
     * 
     * @return an ArrayList of User objects loaded from the JSON file;
     *         returns an empty list if the file cannot be read
     */
    public static ArrayList<User> getUsers() {
        try {
            String json = Files.readString(Path.of(USERS_PATH));
            List<String> objs = splitTopLevelObjects(json);

            ArrayList<User> users = new ArrayList<>();
            for (String obj : objs) {
                User u = parseUser(obj);
                if (u != null) users.add(u);
            }
            return users;

        } catch (IOException e) {
            System.err.println("Could not read " + USERS_PATH + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Loads all questions from the questions JSON file.
     * 
     * This method first loads users so question authors and solution authors
     * can be connected to the proper User objects through their UUIDs.
     * It then parses the question JSON and builds Question objects.
     * 
     * @return an ArrayList of Question objects loaded from the JSON file;
     *         returns an empty list if the file cannot be read
     */
    public static ArrayList<Question> getQuestions() {
        try {
            ArrayList<User> users = getUsers();
            Map<UUID, User> usersById = new HashMap<>();
            for (User u : users) {
                if (u != null && u.getId() != null) usersById.put(u.getId(), u);
            }

            String json = Files.readString(Path.of(QUESTIONS_PATH));
            List<String> objs = splitTopLevelObjects(json);

            ArrayList<Question> questions = new ArrayList<>();
            for (String obj : objs) {
                Question q = parseQuestion(obj, usersById);
                if (q != null) questions.add(q);
            }

            return questions;

        } catch (IOException e) {
            System.err.println("Could not read " + QUESTIONS_PATH + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Parses a single user JSON object into a User object.
     * 
     * The method extracts user fields such as ID, name, email, graduation year,
     * and achievements. If the user ID is missing or invalid, null is returned.
     * 
     * @param obj the raw JSON substring representing one user object
     * @return the parsed User object, or null if the ID is invalid
     */
    private static User parseUser(String obj) {
        UUID id = asUUID(extractString(obj, "id"));
        if (id == null) return null;

        String first = extractString(obj, "first-name");
        String last = extractString(obj, "last-name");
        String username = extractString(obj, "username");
        String email = extractString(obj, "email");
        int gradYear = extractInt(obj, "graduation-year", 0);
        String idUsc = extractString(obj, "id-usc");

        String password = extractString(obj, "password");
        Status status = Status.USER;

        ArrayList<Question> starred = new ArrayList<>();
        ArrayList<Question> answered = new ArrayList<>();

        ArrayList<Achievement> achievements = new ArrayList<>();
        String achArr = extractArrayRaw(obj, "achievements"); // "[{...},{...}]"
        if (achArr != null) {
            List<String> achObjs = splitTopLevelObjects(achArr);
            for (int i = 0; i < achObjs.size(); i++) achievements.add(new Achievement());
        }

        return new User(
                id, first, last, username, password,
                email, starred, answered, achievements,
                status, gradYear, idUsc
        );
    }

    /**
     * Parses a single question JSON object into a Question object.
     * 
     * This includes the main question data, associated user, difficulty,
     * language, hints, question sections, solutions, and threaded comments.
     * 
     * @param obj the raw JSON substring representing one question object
     * @param usersById a map of user UUIDs to User objects for linking authors
     * @return the parsed Question object
     */
    private static Question parseQuestion(String obj, Map<UUID, User> usersById) {
        UUID id = asUUID(extractString(obj, "id"));
        String title = extractString(obj, "title");
        String description = extractString(obj, "description");

        // "user"
        UUID userId = asUUID(extractString(obj, "user"));
        User user = (userId == null) ? null : usersById.get(userId);

        // difficulty
        String diffStr = firstStringInStringArray(obj, "difficulty");
        Difficulty difficulty = parseDifficulty(diffStr);

        // question-language
        String langStr = firstStringInStringArray(obj, "question-language");
        Language lang = parseLanguage(langStr);

        ArrayList<String> hints = parseStringArray(extractArrayRaw(obj, "hints"));

        // question-content
        ArrayList<Section> sections = new ArrayList<>();
        String qcArr = extractArrayRaw(obj, "question-content");
        if (qcArr != null) {
            List<String> secObjs = splitTopLevelObjects(qcArr);
            for (String secObj : secObjs) {
                String secTitle = extractString(secObj, "section-title");
                ArrayList<String> secContent = parseStringArray(extractArrayRaw(secObj, "section-content"));
                String secText = extractString(secObj, "section-text");
                sections.add(new Section(secTitle, secContent, secText));
            }
        }

        Question q = new Question(title, user, description, difficulty, lang, hints, sections);
        if (id != null){
            q.setId(id);
        }

        ArrayList<UserSolution> solutions = new ArrayList<>();
        String solArr = extractArrayRaw(obj, "solution-list");

        if (solArr != null) {
            List<String> solObjs = splitTopLevelObjects(solArr);

            for (String solObj : solObjs) {
                UUID solutionId = asUUID(extractString(solObj, "id"));
                String solutionDescription = extractString(solObj, "description");
                int totalVote = extractInt(solObj, "total-vote", 0);

                UUID solutionUserId = asUUID(extractString(solObj, "user"));
                User solutionUser = (solutionUserId == null) ? null : usersById.get(solutionUserId);

                ArrayList<Comment> replies = parseComments(extractArrayRaw(solObj, "thread"), usersById);

                UserSolution solution = new UserSolution(
                    solutionUser,
                    solutionDescription,
                    solutionId,
                    replies,
                    totalVote
                );

                solutions.add(solution);
            }
        }

        q.setSolutionList(solutions);

        return q;
    }


    /**
     * Splits a JSON array of objects into a list of raw object strings.
     * 
     * This method is designed to split only top-level JSON objects and
     * correctly handles nested braces and quoted strings.
     * 
     * @param json the raw JSON text containing an array of objects
     * @return a list of individual JSON object substrings
     */
    private static List<String> splitTopLevelObjects(String json) {
        List<String> objects = new ArrayList<>();
        if (json == null) return objects;
        String s = json.trim();
        if (s.isEmpty()) return objects;

        int i = 0;
        int len = s.length();

        if (s.charAt(0) != '[') {
            if (s.startsWith("{") && s.endsWith("}")) objects.add(s);
            return objects;
        }

        i++;
        while (i < len) {
            while (i < len && (Character.isWhitespace(s.charAt(i)) || s.charAt(i) == ',')) i++;
            if (i >= len || s.charAt(i) == ']') break;

            if (s.charAt(i) == '{') {
                int start = i;
                int depth = 0;
                boolean inString = false;

                while (i < len) {
                    char c = s.charAt(i);

                    if (c == '"' && (i == 0 || s.charAt(i - 1) != '\\')) {
                        inString = !inString;
                    } else if (!inString) {
                        if (c == '{') depth++;
                        else if (c == '}') {
                            depth--;
                            if (depth == 0) {
                                objects.add(s.substring(start, i + 1));
                                i++;
                                break;
                            }
                        }
                    }
                    i++;
                }
            } else {
                i++;
            }
        }

        return objects;
    }

    /**
     * Returns an empty string if the key is missing or not a string.
     * 
     * @param obj the raw JSON object string
     * @param key the key whose string value should be extracted
     * @return the extracted string value, or an empty string if not found
     */
    private static String extractString(String obj, String key) {
        if (obj == null){
            return "";
        }
        String pattern = "\"" + key + "\"";
        int k = obj.indexOf(pattern);
        if (k < 0){
            return "";
        }

        int colon = obj.indexOf(':', k + pattern.length());
        if (colon < 0){
            return "";
        }

        int i = colon + 1;
        while (i < obj.length() && Character.isWhitespace(obj.charAt(i))){
            i++;
        }

        if (i >= obj.length() || obj.charAt(i) != '"') {
            // if its not a string (or empty)
            return "";
        }

        i++; // after opening quote
        StringBuilder sb = new StringBuilder();
        boolean esc = false;
        while (i < obj.length()) {
            char c = obj.charAt(i++);
            if (esc) {
                if (c == '"' || c == '\\' || c == '/') sb.append(c);
                else if (c == 'n') sb.append('\n');
                else if (c == 'r') sb.append('\r');
                else if (c == 't') sb.append('\t');
                else sb.append(c);
                esc = false;
            } else {
                if (c == '\\') esc = true;
                else if (c == '"') break;
                else sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Extracts a raw JSON array substring for a key:  Example: "hints": [ ... ]
     * The returned string includes the surrounding square brackets.
     * 
     * @param obj the raw JSON object string
     * @param key the key whose array value should be extracted
     * @return the raw JSON array substring including brackets, or null if not found
     */
    private static String extractArrayRaw(String obj, String key) {
        if (obj == null) return null;
        String pattern = "\"" + key + "\"";
        int k = obj.indexOf(pattern);
        if (k < 0) return null;

        int colon = obj.indexOf(':', k + pattern.length());
        if (colon < 0) return null;

        int i = colon + 1;
        while (i < obj.length() && Character.isWhitespace(obj.charAt(i))) i++;
        if (i >= obj.length() || obj.charAt(i) != '[') return null;

        int end = findMatchingBracket(obj, i, '[', ']');
        if (end < 0) return null;

        return obj.substring(i, end + 1);
    }

    /**
     * Finds the matching closing bracket for a given opening bracket position.
     * 
     * This helper is used to correctly locate the end of nested JSON arrays
     * or objects while ignoring brackets that appear inside quoted strings.
     * 
     * @param s the string being searched
     * @param start the starting index of the opening bracket
     * @param open the opening bracket character
     * @param close the closing bracket character
     * @return the index of the matching closing bracket, or -1 if not found
     */
    private static int findMatchingBracket(String s, int start, char open, char close) {
        int depth = 0;
        boolean inString = false;
        for (int i = start; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"' && (i == 0 || s.charAt(i - 1) != '\\')) {
                inString = !inString;
            }
            if (inString) continue;

            if (c == open) depth++;
            else if (c == close) {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }

    /**
     * Extracts an integer value for a given key from a JSON object substring.
     * 
     * If the key is missing or the value cannot be parsed as an integer,
     * the fallback value is returned.
     * 
     * @param obj the raw JSON object string
     * @param key the key whose integer value should be extracted
     * @param fallback the default value to return if parsing fails
     * @return the parsed integer value, or the fallback value
     */
    private static int extractInt(String obj, String key, int fallback) {
        if (obj == null) return fallback;
        String pattern = "\"" + key + "\"";
        int k = obj.indexOf(pattern);
        if (k < 0) return fallback;

        int colon = obj.indexOf(':', k + pattern.length());
        if (colon < 0) return fallback;

        int i = colon + 1;
        while (i < obj.length() && Character.isWhitespace(obj.charAt(i))) i++;

        int j = i;
        while (j < obj.length() && (Character.isDigit(obj.charAt(j)) || obj.charAt(j) == '-')) j++;

        if (j == i) return fallback;
        try {
            return Integer.parseInt(obj.substring(i, j));
        } catch (Exception e) {
            return fallback;
        }
    }

    /**
     * Parses a raw JSON string array into an ArrayList of Java strings.
     * 
     * This method supports escaped characters and also handles
     * non-string raw values by storing them as trimmed text.
     * 
     * @param arrayRaw the raw JSON array string
     * @return an ArrayList containing the parsed string values
     */
    private static ArrayList<String> parseStringArray(String arrayRaw) {
        ArrayList<String> out = new ArrayList<>();
        if (arrayRaw == null) return out;

        String s = arrayRaw.trim();
        if (s.length() < 2 || s.charAt(0) != '[') return out;

        int i = 1; // after '['
        while (i < s.length()) {
            while (i < s.length() && (Character.isWhitespace(s.charAt(i)) || s.charAt(i) == ',')) i++;
            if (i >= s.length() || s.charAt(i) == ']') break;

            if (s.charAt(i) == '"') {
                i++; // after quote
                StringBuilder sb = new StringBuilder();
                boolean esc = false;
                while (i < s.length()) {
                    char c = s.charAt(i++);
                    if (esc) {
                        if (c == '"' || c == '\\' || c == '/') sb.append(c);
                        else if (c == 'n') sb.append('\n');
                        else if (c == 'r') sb.append('\r');
                        else if (c == 't') sb.append('\t');
                        else sb.append(c);
                        esc = false;
                    } else {
                        if (c == '\\') esc = true;
                        else if (c == '"') break;
                        else sb.append(c);
                    }
                }
                out.add(sb.toString());
            } else {
                // non-string item: read until comma or ]
                int j = i;
                while (j < s.length() && s.charAt(j) != ',' && s.charAt(j) != ']') j++;
                String raw = s.substring(i, j).trim();
                if (!raw.isEmpty()) out.add(raw);
                i = j;
            }
        }
        return out;
    }

    /**
     * Returns the first string stored in a JSON string array for a given key.
     * 
     * If the array is empty or missing, an empty string is returned.
     * 
     * @param obj the raw JSON object string
     * @param key the key whose array should be checked
     * @return the first string in the array, or an empty string if none exists
     */
    private static String firstStringInStringArray(String obj, String key) {
        String rawArray = extractArrayRaw(obj, key);
        ArrayList<String> list = parseStringArray(rawArray);
        if (list.isEmpty()) {
            return "";
        }

        return list.get(0);
    }

    /**
     * Converts a string into a UUID object.
     * 
     * If the string is null, blank, or not a valid UUID format,
     * this method returns null.
     * 
     * @param s the string to convert
     * @return the UUID represented by the string, or null if invalid
     */
    private static UUID asUUID(String s) {
        if (s == null || s.isBlank()){
            return null;
        }
        try {
            return UUID.fromString(s.trim());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts a difficulty string into a Difficulty enum value.
     * 
     * Accepts EASY, MEDIUM, DIFFICULT, and HARD.
     * HARD is treated as DIFFICULT.
     * 
     * @param s the difficulty string
     * @return the matching Difficulty enum value, or null if not recognized
     */
    private static Difficulty parseDifficulty(String s) {
        if (s == null){
            return null;
        }
        s = s.trim().toUpperCase();
        if (s.equals("EASY")){
            return Difficulty.EASY;
        } 
        if (s.equals("MEDIUM")){
            return Difficulty.MEDIUM;
        } 

        // I added hard here just incase we wanted to change what it says to the user.
        // So ither way it will still be classified as Difficult within the enumeration
        if (s.equals("DIFFICULT") || s.equals("HARD")){ 
            return Difficulty.DIFFICULT;
        } 
        return null;
    }

    /**
     * Converts a language string into a Language enum value.
     * 
     * Supports common variations such as JAVASCRIPT and JAVA_SCRIPT.
     * If parsing fails, the method defaults to JAVASCRIPT.
     * 
     * @param s the language string
     * @return the matching Language enum value, or null if the input is null
     */
    private static Language parseLanguage(String s) {
        if (s == null){
            return null;
        }

        s = s.trim().toUpperCase();
        try {
            return Language.valueOf(s);
        } catch (Exception e) {
            if (s.equals("JAVA")){
                return Language.JAVA;
            }
            if (s.equals("JAVASCRIPT") || s.equals("JAVA_SCRIPT")){
                return Language.JAVASCRIPT;
            } return Language.JAVASCRIPT;
        }
    }

    /**
     * Recursively parses a JSON comments array into a list of Comment objects.
     * 
     * Each comment may contain nested replies, which are parsed by calling
     * this same method again.
     * 
     * @param commentsRaw the raw JSON array string containing comment data
     * @param usersById a map of user UUIDs to User objects for linking comment authors
     * @return an ArrayList of parsed Comment objects
     */
    private static ArrayList<Comment> parseComments(String commentsRaw, Map<UUID, User> usersById) {
    ArrayList<Comment> comments = new ArrayList<>();

    if (commentsRaw == null) {
        return comments;
    }

    List<String> commentObjs = splitTopLevelObjects(commentsRaw);

    for (String commentObj : commentObjs) {
        UUID commentUserId = asUUID(extractString(commentObj, "user"));
        User commentUser = (commentUserId == null) ? null : usersById.get(commentUserId);

        String commentText = extractString(commentObj, "comment");

        ArrayList<Comment> nestedReplies = parseComments(
            extractArrayRaw(commentObj, "replies"),
            usersById
        );

        Comment comment = new Comment(commentUser, commentText, nestedReplies);
        comments.add(comment);
    }

    return comments;
    }


    /**
     * Tester method for verifying that users, questions, solutions,
     * and comments load correctly from the JSON files.
     * 
     * It prints the loaded objects in a readable format to the console.
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        // My Tester To See if it loaded from the JSON
        ArrayList<User> users = getUsers();
        ArrayList<Question> qs = getQuestions();
        System.out.println("Users loaded: " + users.size() + " Questions loaded: " + qs.size() + "\n");

        // GPTS TESTER
        System.out.println("========= USERS =========");
        for(User user : users){
            System.out.println("ID: " + user.getId());
            System.out.println("First Name: " + user.getFirstName());
            System.out.println("Last Name: " + user.getLastName());
            System.out.println("Username: " + user.getUsername());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Graduation Year: " + user.getGraduationYear());
            System.out.println("USC ID: " + user.getIdUSC());
            System.out.println("Achievements: " + user.getAchievements().size()); // Error In Testing
            System.out.println();
        }


        System.out.println("========= QUESTIONS =========");

        ArrayList<Question> questions = getQuestions();

        for(Question q : questions){

            System.out.println("Question ID: " + q.getId());
            System.out.println("Title: " + q.getTitle());
            System.out.println("Description: " + q.getDescription());
            System.out.println("Difficulty: " + q.getDifficulty());
            System.out.println("Language: " + q.getLanguage());

            if(q.getUser() != null){
                System.out.println("Posted By: " + q.getUser().getUsername());
            }

            System.out.println("Hints:");
            for(String hint : q.getHints()){
                System.out.println("- " + hint);
            }

            System.out.println("Sections:");
            for(Section s : q.getSections()){
                System.out.println("Section Title: " + s.getSectionTitle());
                System.out.println("Section Text: " + s.getSectionText());

                for(String line : s.getSectionContent()){
                    System.out.println("  " + line);
                }
            }
            System.out.println("Solutions:");
            if (q.getSolutionList() != null && !q.getSolutionList().isEmpty()) {
                for (UserSolution solution : q.getSolutionList()) {
                    System.out.println("  Solution ID: " + solution.getSoulutionId());
                    System.out.println("  Description: " + solution.getDescription());
                    System.out.println("  Total Votes: " + solution.getTotalVote());
                    System.out.println("  User Vote: " + solution.getUserVote());

                    if (solution.getUser() != null) {
                        System.out.println("  Posted By: " + solution.getUser().getUsername());
                    }

                    System.out.println("  Replies:");
                    if (solution.getReplies() != null && !solution.getReplies().isEmpty()) {
                        for (Comment comment : solution.getReplies()) {
                            printComment(comment, "    ");
                        }
                    } 
                    else {
                        System.out.println("    No replies");
                    }

                    System.out.println();
                }
            } 
            else {
                System.out.println("  No solutions");
            }

            System.out.println();
            System.out.println("----------------------------");
        }
    }

    /**
     * Recursively prints a comment and all nested replies with indentation.
     * 
     * This is used by the tester to show threaded comment structures
     * in a readable format.
     * 
     * @param comment the comment to print
     * @param indent the indentation prefix used for formatting nested replies
     */
    private static void printComment(Comment comment, String indent) {
        if (comment == null) {
            return;
        }

        String username = "unknown";
        if (comment.getUser() != null) {
            username = comment.getUser().getUsername();
        }

        System.out.println(indent + "Comment By: " + username);
        System.out.println(indent + "Comment: " + comment.getComment());

        if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            System.out.println(indent + "Replies:");
            for (Comment reply : comment.getReplies()) {
                printComment(reply, indent + "    ");
            }
        }
    }
}
