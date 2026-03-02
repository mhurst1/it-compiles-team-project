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

    public void editUser(String firstName, String lastName, String username, 
        String password, String email, int graduationYear, String idUSC){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.graduationYear = graduationYear;
        this.idUSC = idUSC;
    }

    public void save(){

        
    }




    
}
