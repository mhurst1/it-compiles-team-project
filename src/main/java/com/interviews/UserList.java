package com.interviews;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A class that creates a list of all of the users
 */
public class UserList {

    private static UserList userList;
    private ArrayList<User> users;

    public UserList(){
        users = DataLoader.getUsers();
    }


    /**
     * A method to return a specific instance of a user
     * @return the UserList of users
     */
    public static UserList getInstance(){
        if(userList == null){
            userList = new UserList();
        }
        return userList;

    }

    /**
     * A method that grabs the user using their ID
     * @param id the users unique ID
     * @return the User that matches that ID
     */
    public User getUserByID(UUID id){
        for(User user : users){
            if(user.getId().equals(id)){
                return user;
            }
        }
        return null;
    }

    /**
     * A method that gets all the users
     * @return an ArrayList<User> of users
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * A method that returns the user by their username and password 
     * @param username the username of the current user
     * @param password the password of the current user
     * @return the user that matches those parameters
     */
    public User getUser(String username, String password){
        for(User user : users){
            if(user.isMatch(username,password)){
                return user;
            }
        }
        return null; 
    }

    /**
     * Method that grabs the user by their username
     * @param username
     * @return the User that matches that username
     */
    public User getUser(String username){
         for(User user : users){
            if(user.getUsername().equals(username)){
                return user;
            }
         }
         return null;
    }

    /**
     * a method that deletes the user
     * @param user the current user
     */
    public void deleteUser(User user){
        users.remove(user);
    }

    /**
     * A method that allows the user to be edited
     * @param user the current user
     * @param firstName the users first name
     * @param lastName the users last name
     * @param username the users username
     * @param password the users password
     * @param email the users email
     * @param graduationYear the users graduation year
     * @param idUSC the users USC id
     */
    public void editUser(User user, String firstName, String lastName, String username, 
        String password, String email, int graduationYear, String idUSC){
        if(users.contains(user)){
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setGraduationYear(graduationYear);
            user.setIdUSC(idUSC);
        }
    }

    /**
     * a method that saves the users to the data writer
     */
    public void save(){
        DataWriter writer = new DataWriter();
        writer.saveUsers();
    }
    
}
