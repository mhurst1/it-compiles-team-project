package com.interviews;

import java.util.ArrayList;

public class QuestionApplication {
    private QuestionList questionList;
    private UserList userList;
    private User currentUser;
    Question currentQuestion;

    public QuestionApplication(){
        this.currentQuestion = currentQuestion;
        this.currentUser = currentUser;
        questionList = new QuestionList();
        userList = new UserList();

    }

    public QuestionList getInstance(){
        return questionList.getInstance();
    }

    public User currentUser(){
        return currentUser;
    }

    public Question currentQuestion(){
        return currentQuestion;
    }

    public UserSolution currentUserSolution(){
        return currentUserSolution();
    }

    public User createAccount(String firstName, String lastName, String username, 
        String password, String email, int graduationYear, String idUSC){
    }

    public void login(String username, String password){
        User user = userList.getUser(username, password);
        if(user != null) {
            currentUser = user;
        }

    }

    public ArrayList<Question> getQuestions(){


    }

    public boolean addQuestion(String title, User user, String description,
         ArrayList<Section> questionContent, ArrayList<String> hints, 
         Difficulty difficulty, Language questionLanguage){


    }

    public Question editQuestion(Question question, String title, User user, 
        String description, ArrayList<Section> questionContent, ArrayList<String> hints, 
        Difficulty difficulty, Language questionLanguage){


    }

    public ArrayList<Question> findQuestion(String keyword){


    }

    public void deleteQuestion(Question question, int id){


    }

    public void addUserSolution(User user, String description, UUID solutionID, 
        ArrayList<Comment> thread, int totalVote){


    }

    public UserSolution findSolution(){


    }

    public ArrayList<Question> getAnsweredQuestions(){


    }

    public void removeUserSolution(UserSolution userSolution){


    }

    public void addComment(Question question, User user, String comment){


    }

    public void addComment(UserSolution userSolution, User user, String comment){


    }

    public void deleteCommnet(Comment comment, int id){


    }

    public void starQuestion(Question question, UUID id){


    }

    public void editUser(User user, UUID id){


    }

    public void logout(){

        
    }
    



}
