package com.interviews;

import java.util.ArrayList;

public class UserList {

    private static UserList userList;
    private ArrayList<User> users;

    public UserList(){
        users = DataLoader.getUsers();
    }

    public static UserList getInstance(){
        if(userList == null){
            userList = new UserList();
        }
        return userList;

    }

    public User getUserByID(UUID id){
        for(User user : users){
            if(user.getID().equals(id)){
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public User getUser(String username, String password){
        for(User user : users){
            if(user.isMatch(username,password)){
                return user;
            }
        }
        return null; 
    }

    public User getUser(String username){
         for(User user : users){
            if(user.getUsername().equals(username)){
                return user;
            }
         }
         return null;
    }

    /*
    public User getPass(){

    }
    */

    public void deleteUser(User user){
        users.remove(user);
    }

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

    public void save(){
        DataWriter writer = new DataWriter();
        writer.saveUsers();
    }
    
}
