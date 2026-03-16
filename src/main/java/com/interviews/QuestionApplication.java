package com.interviews;

import java.util.ArrayList;
import java.util.UUID;

public class QuestionApplication {
    private QuestionList questionList;
    private UserList userList;
    private User currentUser;
    private Question currentQuestion;
    private UserSolution currentUserSolution;
    

    public QuestionApplication() {
        this.currentQuestion = null;
        this.currentUser = null;
        this.currentUserSolution = null;
        this.questionList = new QuestionList();
        this.userList = new UserList();

    }

    public QuestionList questionList() {
        return questionList;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Question currentQuestion() {
        return currentQuestion;
    }

    public UserSolution currentUserSolution() {
        return currentUserSolution();
    }

    public User createAccount(String firstName, String lastName, String username,
            String password, String email, int graduationYear, String idUSC) {

        User user = new User(firstName, lastName, username, password, email,
                graduationYear, idUSC);
        return user;

    }

    public void login(String username, String password) {
        User user = userList.getUser(username, password);
        if (user != null) {
            currentUser = user;
        }

    }

    public ArrayList<Question> getQuestions() {
        return new ArrayList<>();
    }

    public boolean addQuestion(String title, User user, String description, Difficulty difficulty,
            Language questionLanguage, ArrayList<String> hints, ArrayList<Section> questionContent) {

         Question question = new Question(title, user, description, difficulty, questionLanguage, hints,
                questionContent);
        return true;
    }

   public Question editQuestion(Question question, String title, User user,
                             String description, ArrayList<Section> questionContent,
                             ArrayList<String> hints, Difficulty difficulty,
                             Language questionLanguage) {

    if (questionList.getQuestions().contains(question)) {
        question.setTitle(title);
        question.setUser(user);
        question.setDescription(description);
        question.setQuestionContent(questionContent);
        question.setHint(hints);
        question.setDifficulty(difficulty);
        question.setLanguage(questionLanguage);
        return question;
    }

    // not found or not editable
    return null;
}

    public ArrayList<Question> findQuestion(String keyword) {
        return new ArrayList<>();
    }

    public void deleteQuestion(Question question, int id) {

    }

    public void addUserSolution(User user, String description, UUID solutionID,
            ArrayList<Comment> thread, int totalVote) {

    }

    public UserSolution findSolution() {
        return null;
    }

    public ArrayList<Question> getAnsweredQuestions() {
        return new ArrayList<>();
    }

    public void removeUserSolution(UserSolution userSolution) {

    }

    public void addComment(Question question, User user, String comment) {

    }

    public void addComment(UserSolution userSolution, User user, String comment) {

    }

    public void deleteCommnet(Comment comment, int id) {

    }

    public void starQuestion(Question question, UUID id) {

    }

    public void editUser(User user, UUID id) {

    }

    public void logout(){

        if (currentUser == null) {
            System.out.println("No user is currently logged in.");
            return;
        }
        
        currentUser = null;
        
    }

}
