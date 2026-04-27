package com.interviews;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Writes User and Question data to JSON files.
 * 
 * The @SuppressWarnings was what ChatGPT told me to write in order to fix the yellow lines.
 * The code still functions as it should, but I think I was possibly supposed to use something 
 * other than a HashMap when mapping both the users and the questions.
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
    @SuppressWarnings("unchecked")
    public static boolean saveUsers(ArrayList<User> users) {
        /*COMMENTED OUT BY NICOLE TO FIX JSON SAVING ISSUE, otherwise skips updating any user with existing UUID */
       /* Map<UUID, User> userMap = new HashMap<>();
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
            */

        JSONArray array = new JSONArray();
        for (User u : users) {
            JSONObject obj = new JSONObject();
            obj.put("id", u.getId().toString());
            obj.put("first-name", u.getFirstName());
            obj.put("last-name", u.getLastName());
            obj.put("username", u.getUsername());
            obj.put("password", u.getPassword());
            obj.put("email", u.getEmail());
            obj.put("status", u.getStatus().toString());
            obj.put("contributor-application-pending", u.hasContributorApplicationPending());
            obj.put("contributor-application-experience", u.getContributorApplicationExperience());
            obj.put("contributor-application-motivation", u.getContributorApplicationMotivation());
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
    @SuppressWarnings("unchecked")
    public static boolean saveQuestions(ArrayList<Question> questions) {
        JSONArray array = new JSONArray();
        for (Question q : questions) {
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
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
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
            obj.put("last-active-date", a.getLastActiveDate() != null ? a.getLastActiveDate().toString() : "");
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
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
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
            obj.put("up-voters",   uuidArray(us.getUpVoters()));
            obj.put("down-voters", uuidArray(us.getDownVoters()));
            arr.add(obj);
        }
        return arr;
    }

    @SuppressWarnings("unchecked")
    private static JSONArray uuidArray(ArrayList<UUID> list) {
        JSONArray arr = new JSONArray();
        if (list == null) return arr;
        for (UUID id : list) {
            if (id != null) arr.add(id.toString());
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
    @SuppressWarnings("unchecked")
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
            obj.put("attachment-name", c.getAttachmentName());
            obj.put("attachment-path", c.getAttachmentPath());

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
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
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
            System.err.println("DataWriter: could not write to " + path + e.getMessage());
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
    /*public static void main(String[] args) {
        System.out.println("========= DATA WRITER TESTER =========\n");

        // Fixed UUIDs so re-running this tester is safe — DataWriter skips
        // any entry whose UUID already exists in the file.
        java.util.UUID user1Id = java.util.UUID.fromString("cccccccc-0000-0000-0000-cccccccccccc");
        java.util.UUID user2Id = java.util.UUID.fromString("dddddddd-0000-0000-0000-dddddddddddd");
        java.util.UUID q1Id    = java.util.UUID.fromString("eeeeeeee-0000-0000-0000-eeeeeeeeeeee");
        java.util.UUID q2Id    = java.util.UUID.fromString("ffffffff-0000-0000-0000-ffffffffffff");
        java.util.UUID sol1Id  = java.util.UUID.fromString("11111111-aaaa-aaaa-aaaa-111111111111");
        java.util.UUID sol2Id  = java.util.UUID.fromString("22222222-bbbb-bbbb-bbbb-222222222222");

        // --- Build users ---
        Achievement a1 = new Achievement(3, 2, 40, 5);
        ArrayList<Achievement> achievements1 = new ArrayList<>();
        achievements1.add(a1);

        User user1 = new User(
                user1Id,
                "Carlos", "Rivera", "crivera98", "Secure!99",
                "carlos.rivera@test.com",
                new ArrayList<>(), new ArrayList<>(), achievements1,
                Status.USER, 2028, "M55556666"
        );

        User user2 = new User(
                user2Id,
                "Diana", "Patel", "dpatel99", "Hello!77",
                "diana.patel@test.com",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                Status.CONTRIBUTOR, 2027, "M77778888"
        );

        ArrayList<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        // --- Build question 1: Stack ---
        ArrayList<String> stackHints = new ArrayList<>();
        stackHints.add("Think about LIFO order");
        stackHints.add("Java has a built-in Stack class");
        stackHints.add("Push adds to the top, pop removes from the top");

        ArrayList<String> stackContent = new ArrayList<>();
        stackContent.add("A stack follows Last-In First-Out order.");
        stackContent.add("Elements are pushed onto and popped off the top.");
        stackContent.add("Common uses include undo history and call stacks.");

        ArrayList<Section> stackSections = new ArrayList<>();
        stackSections.add(new Section("Definition", stackContent,
                "A Stack is a linear data structure that follows LIFO ordering."));

        Question q1 = new Question(
                "What is a Stack?",
                user1,
                "Explain what a stack data structure is and how it works.",
                Difficulty.EASY,
                Language.JAVA,
                stackHints,
                stackSections
        );
        q1.setId(q1Id);

        Comment stackReply = new Comment(user1, "Right, it's like a pile of plates.");
        ArrayList<Comment> stackReplies = new ArrayList<>();
        stackReplies.add(stackReply);

        Comment stackComment = new Comment(user2, "Is a stack different from a queue?", stackReplies);
        ArrayList<Comment> stackThread = new ArrayList<>();
        stackThread.add(stackComment);

        UserSolution sol1 = new UserSolution(
                user2,
                "A stack processes elements in LIFO order. Use push() to add and pop() to remove.",
                sol1Id,
                stackThread,
                15
        );
        sol1.setQuestionId(q1Id);
        q1.getSolutionList().add(sol1);

        // --- Build question 2: Polymorphism ---
        ArrayList<String> polyHints = new ArrayList<>();
        polyHints.add("Think about method overriding");
        polyHints.add("A parent reference can point to a child object");
        polyHints.add("This is a core OOP principle");

        ArrayList<String> polyContent = new ArrayList<>();
        polyContent.add("Polymorphism means 'many forms'.");
        polyContent.add("A subclass can override a method from its parent.");
        polyContent.add("The correct method is chosen at runtime.");

        ArrayList<Section> polySections = new ArrayList<>();
        polySections.add(new Section("Concept", polyContent,
                "Polymorphism allows one interface to represent different underlying types."));

        Question q2 = new Question(
                "What is Polymorphism?",
                user2,
                "Describe polymorphism in Java with an example.",
                Difficulty.MEDIUM,
                Language.JAVA,
                polyHints,
                polySections
        );
        q2.setId(q2Id);

        UserSolution sol2 = new UserSolution(
                user1,
                "Polymorphism lets a parent-type variable hold a child object and call overridden methods.",
                sol2Id,
                new ArrayList<>(),
                22
        );
        sol2.setQuestionId(q2Id);
        q2.getSolutionList().add(sol2);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);

        // --- Save ---
        System.out.println("--- Saving ---");
        boolean usersSaved     = saveUsers(users);
        boolean questionsSaved = saveQuestions(questions);
        System.out.println("Users saved:     " + usersSaved);
        System.out.println("Questions saved: " + questionsSaved);

        // --- Reload and verify the two new entries specifically ---
        System.out.println("\n--- Reload Verification ---");
        ArrayList<User> loadedUsers         = DataLoader.getUsers();
        ArrayList<Question> loadedQuestions = DataLoader.getQuestions();
        System.out.println("Total users in file:     " + loadedUsers.size());
        System.out.println("Total questions in file: " + loadedQuestions.size());

        System.out.println("\n--- New Users ---");
        for (User u : loadedUsers) {
            if (u.getId().equals(user1Id) || u.getId().equals(user2Id)) {
                System.out.println("  Username:   " + u.getUsername());
                System.out.println("  Name:       " + u.getFirstName() + " " + u.getLastName());
                System.out.println("  Email:      " + u.getEmail());
                System.out.println("  Grad Year:  " + u.getGraduationYear());
                System.out.println("  USC ID:     " + u.getIdUSC());
                System.out.println("  Status:     " + u.getStatus());
                System.out.println("  Achievements: " + u.getAchievements().size());
                System.out.println();
            }
        }

        System.out.println("--- New Questions ---");
        for (Question q : loadedQuestions) {
            if (q.getId().equals(q1Id) || q.getId().equals(q2Id)) {
                System.out.println("  Title:      " + q.getTitle());
                System.out.println("  Difficulty: " + q.getDifficulty());
                System.out.println("  Language:   " + q.getLanguage());
                System.out.println("  Hints:      " + q.getHints().size());
                System.out.println("  Sections:   " + q.getSections().size());
                System.out.println("  Solutions:  " + q.getSolutionList().size());
                for (UserSolution s : q.getSolutionList()) {
                    System.out.println("    Solution: " + s.getDescription());
                    System.out.println("    Votes:    " + s.getTotalVote());
                    System.out.println("    Comments: " + s.getReplies().size());
                    for (Comment c : s.getReplies()) {
                        System.out.println("      [" + (c.getUser() != null ? c.getUser().getUsername() : "?") + "]: " + c.getComment());
                        for (Comment r : c.getReplies()) {
                            System.out.println("        -> " + r.getComment());
                        }
                    }
                }
                System.out.println();
            }
        }

        System.out.println("========= DONE =========");
    }
    */

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
