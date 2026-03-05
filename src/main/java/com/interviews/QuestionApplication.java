package com.interviews;

import java.util.ArrayList;
import java.util.UUID;

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
        this.currentUser = user;
    }

    public User getCurrentUser(){
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

            User user = new User(firstName, lastName, username, password, email, 
                graduationYear, idUSC);
            return user;

    }

    public void login(String username, String password){
        User user = userList.getUser(username, password);
        if(user != null) {
            currentUser = user;
        }

    }

    public ArrayList<Question> getQuestions(){


    }

    public boolean addQuestion(String title, User user, String description, Difficulty difficulty,
         Language questionLanguage, ArrayList<String> hints, ArrayList<Section> questionContent){

            Question question = new Question(title, user, description, questionContent, hints, difficulty, questionLanguage);
            return question;
    }

    public Question editQuestion(Question question, String title, User user, 
        String description, ArrayList<Section> questionContent, ArrayList<String> hints, 
        Difficulty difficulty, Language questionLanguage){

            if(questionList.contains(question)){
                question.setTitle(title);
                question.setUser(user);
                question.setDescription(description);
                question.setQuestionContent(questionContent);
                question.getHints(hints);
                question.getDifficulty(difficulty);
                question.getQuestionLanguage(questionLanguage);
            }
            
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
