package com.interviews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class DataLoader {

    public static ArrayList<Question> loadQuestion;
    public static ArrayList<User> loadUser;

    // Load users from users.json
    public static boolean loadUsers() {
        try {
            Gson gson = new Gson();

            FileReader reader = new FileReader("data/users.json");

            // Tell Gson we want an ArrayList<User>
            Type userListType = new TypeToken<ArrayList<User>>() {}.getType();

            ArrayList<User> users = gson.fromJson(reader, userListType);

            reader.close();

            // Put loaded users into the singleton
            UserList.getInstance().getUsers().clear();
            UserList.getInstance().getUsers().addAll(users);

            System.out.println("Users loaded successfully.");
            return true;

        } catch (Exception e) {
            System.out.println("Error loading users.");
            e.printStackTrace();
            return false;
        }
    }

    // Load questions from questions.json
    public static boolean loadQuestions() {
        try {
            Gson gson = new Gson();

            FileReader reader = new FileReader("data/questions.json");

            Type questionListType = new TypeToken<ArrayList<Question>>() {}.getType();

            ArrayList<Question> questions = gson.fromJson(reader, questionListType);

            reader.close();

            // Put loaded questions into the singleton
            QuestionList.getInstance().getQuestions().clear();
            QuestionList.getInstance().getQuestions().addAll(questions);

            System.out.println("Questions loaded successfully.");
            return true;

        } catch (Exception e) {
            System.out.println("Error loading questions.");
            e.printStackTrace();
            return false;
        }
    }

    // Optional helper method to load everything at once
    public static void loadAll() {
        loadUsers();
        loadQuestions();
    }
}