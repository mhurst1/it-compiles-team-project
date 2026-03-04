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

    /* --- helpers --- */

    // Convert List<Question> -> ["uuid", "uuid", ...]
    static String listIds(List<Question> list) {
        if (list == null || list.isEmpty()) return "[]";
        StringBuilder b = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            b.append("\"").append(list.get(i).getId()).append("\"");
            if (i < list.size() - 1) b.append(", ");
        }
        b.append("]");
        return b.toString();
    }

    // Convert List<Achievement> -> [{..},{..}]
    static String achList(List<Achievement> a) {
        if (a == null || a.isEmpty()) return "[]";
        StringBuilder b = new StringBuilder("[");
        for (int i = 0; i < a.size(); i++) {
            Achievement x = a.get(i);
            b.append("{\"leaderboard-place\":").append(x.getLeaderboardPlace())
             .append(",\"user-level\":").append(x.getUserLevel())
             .append(",\"all-vote-points\":").append(x.getAllVotePoints())
             .append(",\"streak\":").append(x.getStreak()).append("}");
            if (i < a.size() - 1) b.append(", ");
        }
        b.append("]");
        return b.toString();
    }

}