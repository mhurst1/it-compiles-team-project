package com.interviews;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Loads User and Question data from JSON files.
 */
public class DataLoader {

    private static final String PROJECT_DIR = "it-compiles-team-project";

    /** Path to the users JSON file. */
    private static final Path USERS_PATH = resolveDataFile("users.json");

    /** Path to the questions JSON file. */
    private static final Path QUESTIONS_PATH = resolveDataFile("question.json");

    /**
     * Returns all users from users.json, or an empty list if the file can't be read.
     */
    public static ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(USERS_PATH.toFile())) {

            JSONArray array = (JSONArray) parser.parse(reader);

            for (Object obj : array) {
                User u = parseUser((JSONObject) obj);
                if (u != null) {
                    users.add(u);
                }
            }

        } catch (IOException | ParseException e) {
            System.err.println("DataLoader: could not load users:" + e.getMessage());
        }

        return users;
    }

    /**
     * Returns all questions from question.json, or an empty list if the file can't be read.
     * Loads users first so question and solution authors can be linked by UUID.
     */
    public static ArrayList<Question> getQuestions() {
        ArrayList<Question> questions = new ArrayList<>();

        ArrayList<User> userList = getUsers();
        Map<UUID, User> usersById = new HashMap<>();
        for (User u : userList) {
            usersById.put(u.getId(), u);
        }

        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(QUESTIONS_PATH.toFile())) {
            JSONArray array = (JSONArray) parser.parse(reader);

            for (Object obj : array) {
                Question q = parseQuestion((JSONObject) obj, usersById);
                if (q != null) {
                    questions.add(q);
                }
            }

        } catch (IOException | ParseException e) {
            System.err.println("DataLoader: could not load questions: " + e.getMessage());
        }

        return questions;
    }

    /**
     * Builds a User from a JSON object. Returns null if the id field is missing or invalid.
     *
     * @param obj the JSONObject representing one user entry
     * @return the constructed User, or null if the id is invalid
     */
    private static User parseUser(JSONObject obj) {
        UUID id = toUUID(getString(obj, "id"));
        if (id == null) {
            return null;
        }

        String firstName = getString(obj, "first-name");
        String lastName = getString(obj, "last-name");
        String username = getString(obj, "username");
        String password = getString(obj, "password");
        String email = getString(obj, "email");
        int gradYear = getInt(obj, "graduation-year");
        String idUSC = getString(obj, "id-usc");

        ArrayList<Achievement> achievements = new ArrayList<>();
        JSONArray achArray = (JSONArray) obj.get("achievements");
        if (achArray != null) {
            for (Object achObj : achArray) {
                JSONObject ach = (JSONObject) achObj;
                achievements.add(new Achievement(
                        getInt(ach, "leaderboard-place"),
                        getInt(ach, "user-level"),
                        getInt(ach, "all-vote-points"),
                        getInt(ach, "streak")
                ));
            }
        }

        return new User(id, firstName, lastName, username, password,
                email, new ArrayList<>(), new ArrayList<>(),
                achievements, parseStatus(getString(obj, "status")), gradYear, idUSC);
    }

    /**
     * Builds a Question from a JSON object and links the author and solutions to User objects.
     *
     * @param obj the JSONObject representing one question entry
     * @param usersById a map of user UUIDs to User objects used to resolve authors
     * @return the constructed Question
     */
    private static Question parseQuestion(JSONObject obj, Map<UUID, User> usersById) {
        UUID id = toUUID(getString(obj, "id"));
        String title = getString(obj, "title");
        String description = getString(obj, "description");

        UUID userId = toUUID(getString(obj, "user"));
        User user = null;
        if (userId != null) {
            user = usersById.get(userId);
        }

        Difficulty difficulty = parseDifficulty(firstInArray(obj, "difficulty"));
        Language language = parseLanguage(firstInArray(obj, "question-language"));

        ArrayList<String> hints = toStringList((JSONArray) obj.get("hints"));

        ArrayList<Section> sections = new ArrayList<>();
        JSONArray contentArr = (JSONArray) obj.get("question-content");
        if (contentArr != null) {
            for (Object secObj : contentArr) {
                JSONObject sec = (JSONObject) secObj;
                String secTitle = getString(sec, "section-title");
                ArrayList<String> secContent = toStringList((JSONArray) sec.get("section-content"));
                String secText = getString(sec, "section-text");
                sections.add(new Section(secTitle, secContent, secText));
            }
        }

        Question q = new Question(title, user, description, difficulty, language, hints, sections);
        if (id != null) {
            q.setId(id);
        }

        ArrayList<UserSolution> solutions = new ArrayList<>();
        JSONArray solutionArr = (JSONArray) obj.get("solution-list");
        if (solutionArr != null) {
            for (Object solObj : solutionArr) {
                JSONObject sol = (JSONObject) solObj;
                UUID solId = toUUID(getString(sol, "id"));
                UUID qId = toUUID(getString(sol, "question-id"));
                String desc = getString(sol, "description");
                int totalVote = getInt(sol, "total-vote");
                UUID solUserId = toUUID(getString(sol, "user"));
                User solUser = null;
                if (solUserId != null) {
                    solUser = usersById.get(solUserId);
                }
                ArrayList<Comment> thread = parseComments((JSONArray) sol.get("thread"), usersById);

                UserSolution solution = new UserSolution(solUser, desc, solId, thread, totalVote);
                if (qId != null) {
                    solution.setQuestionId(qId);
                } else {
                    solution.setQuestionId(q.getId());
                }
                solutions.add(solution);
            }
        }

        q.setSolutionList(solutions);
        return q;
    }

    /**
     * Recursively parses a JSON array of comment objects.
     * Each comment can have a "replies" array that is parsed the same way.
     *
     * @param arr the JSONArray of comment objects, or null if there are none
     * @param usersById a map of user UUIDs to User objects used to resolve comment authors
     * @return an ArrayList of Comment objects, or an empty list if arr is null
     */
    private static ArrayList<Comment> parseComments(JSONArray arr, Map<UUID, User> usersById) {
        ArrayList<Comment> comments = new ArrayList<>();
        if (arr == null) {
            return comments;
        }

        for (Object obj : arr) {
            JSONObject c = (JSONObject) obj;
            UUID userId = toUUID(getString(c, "user"));
            User user = null;
            if (userId != null) {
                user = usersById.get(userId);
            }
            String text = getString(c, "comment");
            ArrayList<Comment> replies = parseComments((JSONArray) c.get("replies"), usersById);
            comments.add(new Comment(user, text, replies));
        }
        return comments;
    }


     /**
     * Returns the int value for a key, or 0 if the key is missing.
     * JSON.simple stores numbers as Long, so we handle both Long and Integer here.
     *
     * GPT HELP, Had trouble storing integers initially and by using "Long"
     * it allowed for the loader to function properly
     * 
     * @param obj the JSONObject to read from
     * @param key the key whose value should be retrieved
     * @return the int value, or 0 if not found
     */
    private static int getInt(JSONObject obj, String key) {
        Object val = obj.get(key);
        if (val instanceof Long) {
            return ((Long) val).intValue();
        }
        if (val instanceof Integer) {
            return (Integer) val;
        }
        return 0;
    }

    /**
     * Returns the String value for a key, or "" if the key is missing.
     *
     * 
     * @param obj the JSONObject to read from
     * @param key the key whose value should be retrieved
     * @return the String value, or "" if not found
     */
    private static String getString(JSONObject obj, String key) {
        Object val = obj.get(key);
        if (val instanceof String) {
            return (String) val;
        }
        return "";
    }

    /**
     * Converts a JSONArray into an ArrayList of Strings.
     *
     * @param arr the JSONArray to convert
     * @return an ArrayList of strings, or an empty list if arr is null
     */
    private static ArrayList<String> toStringList(JSONArray arr) {
        ArrayList<String> list = new ArrayList<>();
        if (arr == null) {
            return list;
        }
        for (Object o : arr) {
            if (o != null) {
                list.add(o.toString());
            }
        }
        return list;
    }

    /**
     * Returns the first String in a JSON array stored under the given key, or "" if empty.
     * Used for fields like "difficulty" and "question-language" stored as single-element arrays.
     *
     * @param obj the JSONObject to read from
     * @param key the key whose array value should be checked
     * @return the first element as a String, or "" if the array is missing or empty
     */
    private static String firstInArray(JSONObject obj, String key) {
        JSONArray arr = (JSONArray) obj.get(key);
        if (arr == null || arr.isEmpty()) {
            return "";
        }
        Object first = arr.get(0);
        if (first != null) { 
            return first.toString();
        }
        return "";
    }

    /**
     * Parses a String into a UUID, or returns null if invalid.
     *
     * @param s the string to parse
     * @return the parsed UUID, or null if the string is invalid
     */
    private static UUID toUUID(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(s.trim());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts a difficulty string to the matching Difficulty enum value.
     * Returns null if the string doesn't match a known value.
     *
     * @param s the difficulty string read from JSON
     * @return the matching Difficulty enum value, or null if unrecognized
     */
    private static Difficulty parseDifficulty(String s) {
        if (s == null) {
            return null;
        }
        switch (s.trim().toUpperCase()) {
            case "EASY":
                return Difficulty.EASY;
            case "MEDIUM":
                return Difficulty.MEDIUM;
            case "DIFFICULT":
                return Difficulty.DIFFICULT;
            default:
                return null;
        }
    }

    /**
     * Converts a language string to the matching Language enum value.
     * Defaults to JAVASCRIPT if the string doesn't match any known value.
     *
     * @param s the language string read from JSON
     * @return the matching Language enum value, or Language.UNKNOWN if not found
     */
    private static Language parseLanguage(String s) {
        if (s == null) {
            return null;
        }
        try {
            return Language.valueOf(s.trim().toUpperCase());
        } catch (Exception e) {
            return Language.UNKNOWN;
        }
    }

    /**
     * Converts a user status string to the matching Status enum value.
     * Defaults to USER when the provided value is missing or invalid.
     *
     * @param s the status string read from JSON
     * @return the matching status, or USER when unavailable
     */
    private static Status parseStatus(String s) {
        if (s == null || s.isBlank()) {
            return Status.USER;
        }
        try {
            return Status.valueOf(s.trim().toUpperCase());
        } catch (Exception e) {
            return Status.USER;
        }
    }






    /* THIS IS A CHAT GPT LOADED TESTER TO TEST THE NEW CONTENT
    *  OF THE NEW FORMATTING USING JSON.SIMPLE
    *
    *  THIS WAS USED TO CHECK AND MAKE SURE ALL CONTENT WAS CORRECTLY PASSED THROUGH THE JSON
    */
    // -------------------------------------------------------------------------
    // Tester
    // -------------------------------------------------------------------------

    /**
     * Loads and prints all users and questions from the JSON files to verify parsing.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        ArrayList<User> users = getUsers();
        ArrayList<Question> questions = getQuestions();
        System.out.println("Users loaded: " + users.size() + "  Questions loaded: " + questions.size() + "\n");

        System.out.println("========= USERS =========");
        for (User u : users) {
            System.out.println("ID: " + u.getId());
            System.out.println("Name: " + u.getFirstName() + " " + u.getLastName());
            System.out.println("Username: " + u.getUsername());
            System.out.println("Email: " + u.getEmail());
            System.out.println("Status: " + u.getStatus());
            System.out.println("Grad Year: " + u.getGraduationYear());
            System.out.println("USC ID: " + u.getIdUSC());
            System.out.println("Achievements: " + u.getAchievements().size());
            System.out.println();
        }

        System.out.println("========= QUESTIONS =========");
        for (Question q : questions) {
            System.out.println("ID: " + q.getId());
            System.out.println("Title: " + q.getTitle());
            System.out.println("Description: " + q.getDescription());
            System.out.println("Difficulty: " + q.getDifficulty());
            System.out.println("Language: " + q.getLanguage());
            if (q.getUser() != null) {
                System.out.println("Posted By: " + q.getUser().getUsername());
            }
            System.out.println("Hints:");
            for (String hint : q.getHints()) {
                System.out.println("  - " + hint);
            }
            System.out.println("Sections:");
            for (Section s : q.getSections()) {
                System.out.println("  Title: " + s.getSectionTitle());
                System.out.println("  Text: " + s.getSectionText());
                for (String line : s.getSectionContent()) {
                    System.out.println("    " + line);
                }
            }
            System.out.println("Solutions: " + q.getSolutionList().size());
            for (UserSolution sol : q.getSolutionList()) {
                System.out.println("  Solution ID: " + sol.getSoulutionId());
                System.out.println("  Description: " + sol.getDescription());
                System.out.println("  Total Votes: " + sol.getTotalVote());
                if (sol.getUser() != null) {
                    System.out.println("  Posted By: " + sol.getUser().getUsername());
                }
                System.out.println("  Comments: " + sol.getReplies().size());
                for (Comment c : sol.getReplies()) {
                    printComment(c, "    ");
                }
            }
            System.out.println("----------------------------");
        }
    }

    /**
     * Recursively prints a comment and its nested replies with indentation.
     *
     * @param comment the Comment to print
     * @param indent the indentation string applied to this comment's output
     */
    private static void printComment(Comment comment, String indent) {
        if (comment == null) {
            return;
        }
        String username = "unknown";
        if (comment.getUser() != null) {
            username = comment.getUser().getUsername();
        }
        System.out.println(indent + "By: " + username);
        System.out.println(indent + "Comment: " + comment.getComment());
        if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            for (Comment reply : comment.getReplies()) {
                printComment(reply, indent + "  ");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Path resolution — walks up the directory tree to find the json/ folder
    // -------------------------------------------------------------------------

    /**
     * Walks up the directory tree to find the json/ folder containing the given file.
     * Falls back to a default path if not found.
     *
     * @param fileName the name of the JSON file to locate (e.g. "users.json")
     * @return the resolved Path to the file
     */
    private static Path resolveDataFile(String fileName) {
        Path start = Paths.get("").toAbsolutePath().normalize();
        for (Path cur = start; cur != null; cur = cur.getParent()) {
            Path direct = cur.resolve("json").resolve(fileName);
            if (Files.exists(direct)) {
                return direct;
            }
            Path project = cur.resolve(PROJECT_DIR).resolve("json").resolve(fileName);
            if (Files.exists(project)) {
                return project;
            }
        }
        return start.resolve("json").resolve(fileName);
    }
}
