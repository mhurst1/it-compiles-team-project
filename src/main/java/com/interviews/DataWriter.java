package com.interviews;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class DataWriter {

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
                    .append(",\"user-level\":").append(x.getLevel())
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

    // Convert List<Section> ->
    // [{"section-title":..,"section-content":[...],"section-text":..,"fileName":..}]
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
            b.append("\"").append(s).append("\"");
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

