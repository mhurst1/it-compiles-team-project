package com.interviews;

import java.util.UUID;

public class Admin extends Contributor {

    public Admin(){
        super("Admin", "Admin", "Admin", "Admin", "Admin", 2024, "Admin");
    }

    public void addContributor(UUID id){
        User user = UserList.getInstance().getUserByID(id);
        if(user!=null)
            user.setStatus(Status.CONTRIBUTOR);
    }

    public void removeContributor(UUID id){
        User user = UserList.getInstance().getUserByID(id);
        if(user!=null)
            user.setStatus(Status.USER);
    }


    
}
