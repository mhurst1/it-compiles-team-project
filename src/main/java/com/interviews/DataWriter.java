package com.interviews;

import java.io.FileWriter;
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

/**
 * Writes User and Question data to JSON files.
 */
public class DataWriter {

    private static final String PROJECT_DIR = "it-compiles-team-project";
    
    /** Path to the users JSON file. */
    private static final Path USERS_PATH = resolveDataFile("users.json");

    /** Path to the questions JSON file. */
    private static final Path QUESTIONS_PATH = resolveDataFile("question.json");

    /**
     * Adds new users to users.json. Skips any user whose UUID already exists in the file.
     * Returns true if the file was saved successfully.
     *
     * @param users the list of users to add
     * @return true if saved successfully, false otherwise
     */
    public static boolean saveUsers(ArrayList<User> users) {
        Map<UUID, User> userMap = new HashMap<>();
        for (User u : DataLoader.getUsers()) {
            userMap.put(u.getId(), u);
        }
        for (User u : users) {
            if (userMap.containsKey(u.getId())) {
                System.err.println("DataWriter: user already exists: " + u.getUsername());
            } else {
                userMap.put(u.getId(), u);
            }
        }

        JSONArray array = new JSONArray();
        for (User u : userMap.values()) {
            JSONObject obj = new JSONObject();
            obj.put("id", u.getId().toString());
            obj.put("first-name", u.getFirstName());
            obj.put("last-name", u.getLastName());
            obj.put("username", u.getUsername());
            obj.put("password", u.getPassword());
            obj.put("email", u.getEmail());
            obj.put("status", u.getStatus().toString());
            obj.put("graduation-year", (long) u.getGraduationYear());
            obj.put("id-usc", u.getIdUSC());
            obj.put("starred-questions", questionIdArray(u.getStarredQuestionList()));
            obj.put("answered-questions", questionIdArray(u.getAnsweredQuestions()));
            obj.put("achievements", achievementArray(u.getAchievements()));
            array.add(obj);
        }

        boolean saved = writeToFile(USERS_PATH, array);
        return saved;
    }

    /**
     * Adds new questions to question.json. Skips any question whose UUID already exists in the file.
     * Returns true if the file was saved successfully.
     *
     * @param questions the list of questions to add
     * @return true if saved successfully, false otherwise
     */
    public static boolean saveQuestions(ArrayList<Question> questions) {
        Map<UUID, Question> questionMap = new HashMap<>();
        for (Question q : DataLoader.getQuestions()) {
            questionMap.put(q.getId(), q);
        }
        for (Question q : questions) {
            if (questionMap.containsKey(q.getId())) {
                System.err.println("DataWriter1: question already exists:" + q.getTitle());
            } else {
                questionMap.put(q.getId(), q);
            }
        }

        JSONArray array = new JSONArray();
        for (Question q : questionMap.values()) {
            JSONObject obj = new JSONObject();

            String qId = "";
            if (q.getId() != null) {
                qId = q.getId().toString();
            }
            obj.put("id", qId);

            obj.put("title", q.getTitle());

            String qUserId = "";
            if (q.getUser() != null) {
                qUserId = q.getUser().getId().toString();
            }
            obj.put("user", qUserId);

            obj.put("description", q.getDescription());
            obj.put("question-content", sectionArray(q.getQuestionContent()));
            obj.put("hints", stringArray(q.getHints()));
            obj.put("difficulty", enumArray(q.getDifficulty()));
            obj.put("question-language", enumArray(q.getLanguage()));
            obj.put("solution-list", solutionArray(q.getSolutionList(), q.getId()));
            array.add(obj);
        }

        boolean saved = writeToFile(QUESTIONS_PATH, array);
        return saved;
    }

    /**
     * Converts a list of Questions into a JSONArray of their UUID strings.
     *
     * @param list the list of Questions to convert
     * @return a JSONArray of UUID strings, or an empty array if the list is null
     */
    private static JSONArray questionIdArray(ArrayList<Question> list) {
        JSONArray arr = new JSONArray();
        if (list == null) {
            return arr;
        }
        for (Question q : list) {
            if (q.getId() != null) {
                arr.add(q.getId().toString());
            }
        }
        return arr;
    }

    /**
     * Converts a list of Achievements into a JSONArray.
     * 
     * This class had help with ChatGPT where it typecasts them as (long),
     * as i couldn't get the achievements to write properly and that was the only fix it presented
     *
     * @param list the list of Achievements to convert
     * @return a JSONArray of achievement objects, or an empty array if the list is null
     */
    private static JSONArray achievementArray(ArrayList<Achievement> list) {
        JSONArray arr = new JSONArray();
        if (list == null) {
            return arr;
        }
        for (Achievement a : list) {
            JSONObject obj = new JSONObject();
            obj.put("leaderboard-place", (long) a.getLeaderboardPlace());
            obj.put("user-level", (long) a.getUserLevel());
            obj.put("all-vote-points", (long) a.getAllVotePoints());
            obj.put("streak", (long) a.getStreak());
            arr.add(obj);
        }
        return arr;
    }

    /**
     * Converts a list of Sections into a JSONArray.
     *
     * @param sections the list of Sections to convert
     * @return a JSONArray of section objects, or an empty array if the list is null
     */
    private static JSONArray sectionArray(ArrayList<Section> sections) {
        JSONArray arr = new JSONArray();
        if (sections == null) {
            return arr;
        }
        for (Section s : sections) {
            JSONObject obj = new JSONObject();

            String sTitle = "";
            if (s.getSectionTitle() != null) {
                sTitle = s.getSectionTitle();
            }
            obj.put("section-title", sTitle);

            obj.put("section-content", stringArray(s.getSectionContent()));

            String sText = "";
            if (s.getSectionText() != null) {
                sText = s.getSectionText();
            }
            obj.put("section-text", sText);

            String sFileName = "";
            if (s.getFileName() != null) {
                sFileName = s.getFileName();
            }
            obj.put("fileName", sFileName);

            arr.add(obj);
        }
        return arr;
    }

    /**
     * Converts a list of UserSolutions into a JSONArray.
     *
     * @param solutions the list of solutions to convert
     * @param questionId the UUID of the parent question
     * @return a JSONArray of solution objects, or an empty array if the list is null
     */
    private static JSONArray solutionArray(ArrayList<UserSolution> solutions, UUID questionId) {
        JSONArray arr = new JSONArray();
        if (solutions == null) {
            return arr;
        }
        for (UserSolution us : solutions) {
            JSONObject obj = new JSONObject();

            String solId = "";
            if (us.getSoulutionId() != null) {
                solId = us.getSoulutionId().toString();
            }
            obj.put("id", solId);

            String qId = "";
            if (questionId != null) {
                qId = questionId.toString();
            }
            obj.put("question-id", qId);

            String solUserId = "";
            if (us.getUser() != null) {
                solUserId = us.getUser().getId().toString();
            }
            obj.put("user", solUserId);

            String solDesc = "";
            if (us.getDescription() != null) {
                solDesc = us.getDescription();
            }
            obj.put("description", solDesc);

            obj.put("thread", commentArray(us.getReplies()));
            obj.put("user-vote", us.getUserVote());
            obj.put("total-vote", (long) us.getTotalVote());
            arr.add(obj);
        }
        return arr;
    }

    /**
     * converts a list of Comments into a JSONArray.
     * Each comment's replies are converted the same way.
     *
     * @param comments the list of Comments to convert
     * @return a JSONArray of comment objects, or an empty array if the list is null
     */
    private static JSONArray commentArray(ArrayList<Comment> comments) {
        JSONArray arr = new JSONArray();
        if (comments == null) {
            return arr;
        }
        for (Comment c : comments) {
            JSONObject obj = new JSONObject();

            String commentUserId = "";
            if (c.getUser() != null) {
                commentUserId = c.getUser().getId().toString();
            }
            obj.put("user", commentUserId);

            String commentText = "";
            if (c.getComment() != null) {
                commentText = c.getComment();
            }
            obj.put("comment", commentText);

            obj.put("replies", commentArray(c.getReplies()));
            arr.add(obj);
        }
        return arr;
    }

    /**
     * Converts a list of Strings into a JSONArray.
     *
     * @param list the list of Strings to convert
     * @return a JSONArray of string values, or an empty array if the list is null
     */
    private static JSONArray stringArray(ArrayList<String> list) {
        JSONArray arr = new JSONArray();
        if (list == null) {
            return arr;
        }
        for (String s : list) {
            if (s != null) {
                arr.add(s);
            } else {
                arr.add("");
            }
        }
        return arr;
    }

    /**
     * Wraps a single enum value into a JSONArray as its name string.
     * Difficulty and language are stored as single-element arrays like ["EASY"].
     *
     * This class had help with CHATGPT, where it says Enum<?> e
     * by using this instead of a string, it allows Enumerations like difficulty, status, and language
     * to all use this method to help write to the json
     * 
     * @param e the enum value to wrap
     * @return a JSONArray containing the enum's name, or an empty array if null
     */
    private static JSONArray enumArray(Enum<?> e) {
        JSONArray arr = new JSONArray();
        if (e != null) {
            arr.add(e.name());
        }
        return arr;
    }

    /**
     * Writes a JSONArray to the given path, which is used within saveUsers and saveQuestions
     * to write to its specfic json file
     * 
     * @param path the file path to write to
     * @param array the JSONArray to write
     * @return true if the write succeeded, false if an IOException occurred
     */
    private static boolean writeToFile(Path path, JSONArray array) {
        try {
            Files.createDirectories(path.getParent());
            try (FileWriter writer = new FileWriter(path.toFile())) {
                array.writeJSONString(writer);
            }
            return true;
        } catch (IOException e) {
            System.err.println("DataWriter1: could not write to " + path + e.getMessage());
            return false;
        }
    }


    /* THIS IS a CHAT GPT LOADED TESTER TO TEST THE NEW CONTENT
    *  OF THE NEW FORMATTING USING JSON.SIMPLE
    *
    *  THIS WAS USED TO CHECK AND MAKE SURE ALL CONTENT WAS CORRECTLY PASSED THROUGH THE JSON
    */
    // -------------------------------------------------------------------------
    // Tester
    // -------------------------------------------------------------------------

    /**
     * Builds sample users and questions, saves them, then reloads to verify the round trip works.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("========= DATA WRITER 1 TESTER =========");

        User user1 = new User(
                java.util.UUID.randomUUID(),
                "Liam", "Anderson", "landerson", "secure123",
                "liam.anderson@email.com",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                Status.USER, 2025, "M11112222"
        );
        User user2 = new User(
                java.util.UUID.randomUUID(),
                "Emma", "Thompson", "ethompson", "pass999",
                "emma.thompson@email.com",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                Status.CONTRIBUTOR, 2026, "M33334444"
        );

        ArrayList<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        ArrayList<String> hints = new ArrayList<>();
        hints.add("Think about dynamic arrays");
        hints.add("Part of java.util");

        ArrayList<String> content = new ArrayList<>();
        content.add("ArrayList is a resizable array.");
        content.add("It allows adding and removing elements.");

        ArrayList<Section> sections = new ArrayList<>();
        sections.add(new Section("Overview", content, "ArrayList grows automatically when needed."));

        Question q1 = new Question(
                "What is an ArrayList?",
                user1,
                "Explain how ArrayList works in Java.",
                Difficulty.EASY,
                Language.JAVA,
                hints,
                sections
        );

        Comment reply = new Comment(user1, "Because arrays are fixed size.");
        ArrayList<Comment> replies = new ArrayList<>();
        replies.add(reply);

        Comment comment = new Comment(user2, "Why not just use arrays?", replies);
        ArrayList<Comment> thread = new ArrayList<>();
        thread.add(comment);

        UserSolution sol = new UserSolution(
                user2,
                "ArrayList allows dynamic resizing unlike normal arrays.",
                java.util.UUID.randomUUID(),
                thread,
                30
        );
        q1.getSolutionList().add(sol);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(q1);

        boolean usersSaved = saveUsers(users);
        boolean questionsSaved = saveQuestions(questions);
        System.out.println("Users saved: " + usersSaved);
        System.out.println("Questions saved: " + questionsSaved);

        System.out.println("\n========= VERIFY RELOAD =========");
        ArrayList<User> loadedUsers = DataLoader.getUsers();
        ArrayList<Question> loadedQuestions = DataLoader.getQuestions();
        System.out.println("Users reloaded: " + loadedUsers.size());
        System.out.println("Questions reloaded: " + loadedQuestions.size());

        for (Question q : loadedQuestions) {
            System.out.println("\nTitle: " + q.getTitle());
            System.out.println("Solutions: " + q.getSolutionList().size());
            for (UserSolution s : q.getSolutionList()) {
                System.out.println("  Description: " + s.getDescription());
                System.out.println("  Votes: " + s.getTotalVote());
                System.out.println("  Comments: " + s.getReplies().size());
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
            if (direct.toFile().exists()) {
                return direct;
            }
            Path project = cur.resolve(PROJECT_DIR).resolve("json").resolve(fileName);
            if (project.toFile().exists()) {
                return project;
            }
        }
        return start.resolve("json").resolve(fileName);
    }
}
