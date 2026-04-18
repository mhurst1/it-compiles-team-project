package com.interviews;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A class that creates a list of all of the users
 */
public class UserList {

    private static UserList userList;
    private ArrayList<User> users;

    /**
     * Creates a user list by loading users from persistent storage.
     */
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
     * Returns the user with the given username.
     *
     * @param username the username to search for
     * @return the User that matches that username
     */
    public User getUser(String username){
         for(User user : users){
            if(user.getUsername() != null && user.getUsername().equals(username)){
                return user;
            }
         }
         return null;
    }

    /**
     * Adds a new user to the in-memory list.
     *
     * @param user the user to add
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Removes a user from the in-memory list.
     *
     * @param user the current user
     */
    public void deleteUser(User user){
        if (user == null || user.getId() == null) return;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.remove(i);
                return;
            }
        }
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
        user.setFirstName(firstName);
        user.setLastName(lastName);

        // Only update username if it is not null/blank and not already taken by another user
        if (username != null && !username.isBlank()) {
            boolean isDuplicate = false;
            for (User u : users) {
                if (u != user && username.equals(u.getUsername())) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                user.setUsername(username);
            }
        }

        // setPassword and setEmail have built-in validation that rejects null/blank/invalid
        user.setPassword(password);
        user.setEmail(email);
        user.setGraduationYear(graduationYear);
        user.setIdUSC(idUSC);
    }

    /**
     * Persists the current user list through the data writer.
     */
    public void save(){
        DataWriter.saveUsers(users);
    }
}