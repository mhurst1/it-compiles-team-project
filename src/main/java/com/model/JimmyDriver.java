package com.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.interviews.Achievement;
import com.interviews.Difficulty;
import com.interviews.Language;
import com.interviews.Question;

import java.util.ArrayList;
import java.util.UUID;
import com.interviews.QuestionApplication;
import com.interviews.Status;
import com.interviews.User;


public class JimmyDriver {

    QuestionApplication app = new QuestionApplication();

    //cant be tailored to 8 days, it needs to be applicable to other accounts
    Achievement jimmyAchievement = new Achievement();

    for(int i = 0; i < 8; i++){
        jimmyAchievement.streakCounter();
    }

    ArrayList<Question> starredQ = new ArrayList<>();
    ArrayList<Question> answeredQ = new ArrayList<>();
    ArrayList<Achievement> achievements = new ArrayList<>();
    achievements.add(JimmyAchievement);

    User jimmy = new User(UUID.randomUUID(), "Jimmy", "Cricket", "jcricket", 
    "ilovecrickets", "jcricker@email.com", starredQ, answeredQ, achievements, Status.USER, 
    2026, "X12345678");

    app.createAccount("Jimmy", "Cricket", "jcricket", "ilovecrickets",
     "jcricker@email.com", 2026, "X12345678");
    }



}
