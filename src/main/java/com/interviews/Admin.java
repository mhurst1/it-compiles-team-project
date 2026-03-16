package com.interviews;

import java.util.UUID;

/**
 * A class that is just like a User and Contributor, but has more advanced 
 * administrative features and acessibility 
 */
public class Admin extends Contributor {

    public Admin(){
        super("Admin", "Admin", "Admin", "Admin", "Admin", 2024, "Admin");
    }

    /**
     * A method to make a regular user a contributor
     * @param id the unique UUID of the user
     */
    public void addContributor(UUID id){
        User user = UserList.getInstance().getUserByID(id);
        if(user!=null)
            user.setStatus(Status.CONTRIBUTOR);
    }

    /**
     * Method that removes the status of the contributor
     * @param id the unique UUID of the user
     */
    public void removeContributor(UUID id){
        User user = UserList.getInstance().getUserByID(id);
        if(user!=null)
            user.setStatus(Status.USER);
    }


    
}
