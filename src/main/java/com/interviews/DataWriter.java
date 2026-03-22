package com.interviews;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class DataWriter {

    /** Base directory containing the JSON data files. */
    private static final String DATA_DIR = "it-compiles-team-project/json";

    /** Path to the users JSON file. */
    private static final String USERS_PATH = DATA_DIR + "/users.json";

    /** Path to the questions JSON file. */
    private static final String QUESTIONS_PATH = DATA_DIR + "/question.json";

    public boolean saveUsers() {
        try {
            List<User> users = UserList.getInstance().getUsers();
            new File(DATA_DIR).mkdirs();

            try (PrintWriter w = new PrintWriter(USERS_PATH)) {
                w.println("[");

                for (int i = 0; i < users.size(); i++) {
                    User u = users.get(i);

                    w.print("  {"
                            + "\"id\":\"" + u.getId() + "\","
                            + "\"first-name\":\"" + escape(u.getFirstName()) + "\","
                            + "\"last-name\":\"" + escape(u.getLastName()) + "\","
                            + "\"username\":\"" + escape(u.getUsername()) + "\","
                            + "\"password\":\"" + escape(u.getPassword()) + "\","
                            + "\"email\":\"" + escape(u.getEmail()) + "\","
                            + "\"graduation-year\":" + u.getGraduationYear() + ","
                            + "\"id-usc\":\"" + escape(u.getIdUSC()) + "\","
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

    public boolean saveQuestions() {
        try {
            List<Question> questions = QuestionList.getInstance().getQuestions();
            new File(DATA_DIR).mkdirs();

            try (PrintWriter w = new PrintWriter(QUESTIONS_PATH)) {
                w.println("[");

                for (int i = 0; i < questions.size(); i++) {
                    Question q = questions.get(i);

                    w.print("  {"
                            + "\"id\":\"" + q.getId() + "\","
                            + "\"title\":\"" + escape(q.getTitle()) + "\","
                            + "\"user\":\"" + (q.getUser() == null ? "" : q.getUser().getId()) + "\","
                            + "\"description\":\"" + escape(q.getDescription()) + "\","
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

    /* --- helpers --- */

    // Convert List<Question> -> ["uuid", "uuid", ...]
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

    // Convert List<Achievement> -> [{..},{..}]
    static String achList(List<Achievement> a) {
        if (a == null || a.isEmpty())
            return "[]";

        StringBuilder b = new StringBuilder("[");

        for (int i = 0; i < a.size(); i++) {
            Achievement x = a.get(i);
            b.append("{\"leaderboard-place\":").append(x.getLeaderboardPlace())
             .append(",\"user-level\":").append(x.getUserLevel())
             .append(",\"all-vote-points\":").append(x.getAllVotePoints())
             .append(",\"streak\":").append(x.getStreak()).append("}");

            if (i < a.size() - 1)
                b.append(", ");
        }

        b.append("]");
        return b.toString();
    }

    // Convert List<UserSolution> ->
    // [{"user":..,"description":..,"thread":[...],"user-vote":..,"total-vote":..}]
    static String solutionList(List<UserSolution> s) {
        if (s == null || s.isEmpty())
            return "[]";

        StringBuilder b = new StringBuilder("[");

        for (int i = 0; i < s.size(); i++) {
            UserSolution us = s.get(i);
            String uid = us.getUser() == null ? "" : String.valueOf(us.getUser().getId());
            String desc = us.getDescription() == null ? "" : us.getDescription();

            b.append("{")
             .append("\"user\":\"").append(escape(uid)).append("\",")
             .append("\"description\":\"").append(escape(desc)).append("\",")
             .append("\"thread\":").append(commentList(us.getReplies())).append(",")
             .append("\"user-vote\":").append(us.getUserVote()).append(",")
             .append("\"total-vote\":").append(us.getTotalVote())
             .append("}");

            if (i < s.size() - 1)
                b.append(",");
        }

        b.append("]");
        return b.toString();
    }

    // Convert List<Section> ->
    // [{"section-title":..,"section-content":[...],"section-text":..,"fileName":..}]
    static String sectionList(List<Section> sections) {
        if (sections == null || sections.isEmpty())
            return "[]";

        StringBuilder b = new StringBuilder("[");

        for (int i = 0; i < sections.size(); i++) {
            Section s = sections.get(i);

            b.append("{")
             .append("\"section-title\":\"").append(escape(s.getSectionTitle())).append("\",")
             .append("\"section-content\":").append(stringList(s.getSectionContent())).append(",")
             .append("\"section-text\":\"").append(escape(s.getSectionText())).append("\",")
             .append("\"fileName\":\"").append(escape(s.getFileName())).append("\"")
             .append("}");

            if (i < sections.size() - 1) {
                b.append(",");
            }
        }

        b.append("]");
        return b.toString();
    }

    // Convert Enum -> ["ENUM_NAME"]
    static String enumToArray(Enum<?> e) {
        if (e == null)
            return "[]";

        return "[\"" + e.name() + "\"]";
    }

    // Convert List<String> -> ["text1","text2",...]
    static String stringList(List<String> list) {
        if (list == null || list.isEmpty())
            return "[]";

        StringBuilder b = new StringBuilder("[");

        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i) == null ? "" : list.get(i);
            b.append("\"").append(escape(s)).append("\"");
            if (i < list.size() - 1) {
                b.append(",");
            }
        }

        b.append("]");
        return b.toString();
    }

    // Convert List<Comment> -> [{"user":..,"comment":..,"replies":[...]}]
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
             .append("\"user\":\"").append(escape(userId)).append("\",")
             .append("\"comment\":\"").append(escape(commentText)).append("\",")
             .append("\"replies\":").append(commentList(c.getReplies()))
             .append("}");

            if (i < comments.size() - 1)
                b.append(",");
        }

        b.append("]");
        return b.toString();
    }

    static String escape(String text) {
        if (text == null) {
            return "";
        }

        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }




    /**
     * THIS IS THE TESTER FOR THE DATAWRITER
     * 
     * GPT Loaded TESTER based on the code written above
     * 
     */
public static void main(String[] args) {

    System.out.println("========= FINAL DATA WRITER TESTER =========");

    UserList userList = UserList.getInstance();
    QuestionList questionList = QuestionList.getInstance();

    // OPTIONAL: Uncomment if you want FULL overwrite
    // userList.getUsers().clear();
    // questionList.getQuestions().clear();

    // -----------------------------
    // USERS
    // -----------------------------
    User user1 = new User(
            UUID.randomUUID(),
            "Liam",
            "Anderson",
            "landerson",
            "secure123",
            "liam.anderson@email.com",
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            Status.USER,
            2025,
            "M11112222"
    );

    User user2 = new User(
            UUID.randomUUID(),
            "Emma",
            "Thompson",
            "ethompson",
            "pass999",
            "emma.thompson@email.com",
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            Status.CONTRIBUTOR,
            2026,
            "M33334444"
    );

    userList.getUsers().add(user1);
    userList.getUsers().add(user2);

    // -----------------------------
    // QUESTION 1: ArrayList
    // -----------------------------
    ArrayList<String> hints1 = new ArrayList<>();
    hints1.add("Think about dynamic arrays");
    hints1.add("Resizable structure");
    hints1.add("Part of Java Collections");

    ArrayList<String> content1 = new ArrayList<>();
    content1.add("ArrayList is a resizable array.");
    content1.add("It allows adding and removing elements.");
    content1.add("It is part of java.util package.");

    ArrayList<Section> sections1 = new ArrayList<>();
    sections1.add(new Section(
            "Overview",
            content1,
            "ArrayList grows automatically when needed."
    ));

    Question q1 = new Question(
            "What is an ArrayList?",
            user1,
            "Explain how ArrayList works in Java.",
            Difficulty.EASY,
            Language.JAVA,
            hints1,
            sections1
    );

    // Comments (thread)
    Comment r1 = new Comment(user1, "Because arrays are fixed size.");
    ArrayList<Comment> replies1 = new ArrayList<>();
    replies1.add(r1);

    Comment c1 = new Comment(user2, "Why not just use arrays?", replies1);

    ArrayList<Comment> thread1 = new ArrayList<>();
    thread1.add(c1);

    UserSolution sol1 = new UserSolution(
            user2,
            "ArrayList allows dynamic resizing unlike normal arrays.",
            UUID.randomUUID(),
            thread1,
            30
    );

    q1.getSolutionList().add(sol1);

    // -----------------------------
    // QUESTION 2: Recursion
    // -----------------------------
    ArrayList<String> hints2 = new ArrayList<>();
    hints2.add("A function calling itself");
    hints2.add("Base case is important");
    hints2.add("Think of factorial");

    ArrayList<String> content2 = new ArrayList<>();
    content2.add("Recursion is when a method calls itself.");
    content2.add("It requires a base case to stop.");
    content2.add("Used in problems like factorial or tree traversal.");

    ArrayList<Section> sections2 = new ArrayList<>();
    sections2.add(new Section(
            "Concept",
            content2,
            "Recursion breaks problems into smaller pieces."
    ));

    Question q2 = new Question(
            "What is Recursion?",
            user2,
            "Explain recursion with an example.",
            Difficulty.MEDIUM,
            Language.JAVA,
            hints2,
            sections2
    );

    // Comments (thread)
    Comment r2 = new Comment(user2, "Yes, factorial is a great example.");
    ArrayList<Comment> replies2 = new ArrayList<>();
    replies2.add(r2);

    Comment c2 = new Comment(user1, "Is recursion efficient?", replies2);

    ArrayList<Comment> thread2 = new ArrayList<>();
    thread2.add(c2);

    UserSolution sol2 = new UserSolution(
            user1,
            "Recursion solves problems by calling the same function with smaller inputs.",
            UUID.randomUUID(),
            thread2,
            45
    );

    q2.getSolutionList().add(sol2);

    // -----------------------------
    // ADD QUESTIONS
    // -----------------------------
    questionList.getQuestions().add(q1);
    questionList.getQuestions().add(q2);

    // -----------------------------
    // SAVE
    // -----------------------------
    DataWriter writer = new DataWriter();

    boolean usersSaved = writer.saveUsers();
    boolean questionsSaved = writer.saveQuestions();

    System.out.println("Users saved: " + usersSaved);
    System.out.println("Questions saved: " + questionsSaved);

    // -----------------------------
    // VERIFY
    // -----------------------------
    System.out.println("\n========= VERIFY =========");

    ArrayList<User> users = DataLoader.getUsers();
    ArrayList<Question> questions = DataLoader.getQuestions();

    System.out.println("Users loaded: " + users.size());
    System.out.println("Questions loaded: " + questions.size());

    for (Question q : questions) {
        System.out.println("\n" + q.getTitle());
        System.out.println("Solutions: " + q.getSolutionList().size());
    }
}
}
