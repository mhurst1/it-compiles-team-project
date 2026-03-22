package com.interviews;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DataLoader{

    // PATHS FOR BOTH THE QUESTION AND THE USER
    private static final String DATA_DIR = "it-compiles-team-project/json";
    private static final String USERS_PATH = DATA_DIR + "/users.json";
    private static final String QUESTIONS_PATH = DATA_DIR + "/question.json";

    /**
     * GET USERS SECTION DATA LOADER
     * @return
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
     * GET QUESTIONS SECTION DATA LOADER
     * @return
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

       // We are going to need getters and setters for the achievement class
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


    // Helpers 
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
     * Extracts a JSON string value for a key inside an object substring.
     * 
     * 
     * Example: "title": "What is JSON?" -> returns What is JSON?
     * Returns "" if missing.
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
     * Returns the substring including brackets
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

    private static String firstStringInStringArray(String obj, String key) {
        String rawArray = extractArrayRaw(obj, key);
        ArrayList<String> list = parseStringArray(rawArray);
        if (list.isEmpty()) {
            return "";
        }

        return list.get(0);
    }

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

    // Because in the language tab these all have similar names, we need to differentiate them.
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
     * THIS IS THE TESTER FOR THE DATALOADER
     * 
     * GPT Loaded TESTER based on the code written above
     * 
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
