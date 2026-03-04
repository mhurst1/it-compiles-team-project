package com.interviews;

import java.util.ArrayList;

public class UserList {

    private static UserList userList;
    private ArrayList<User> users;

    public UserList(){

    }

    public UserList getInstance(){


    }

    public User getUser(String username, String password){
        if(User.isMatch(username,password)){
            return this.User;
        }
        return null; 

    }

    public User getUser(String username){
         
    }

    public User getPass(){


    }

    public void deleteUser(){


    }

    // You Cant Use this. if the feild doesn't exist within the class
    public void editUser(String firstName, String lastName, String username, 
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
        DataWriter.saveUsers();
    }




    
}
