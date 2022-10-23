package com.example.technomarket.util;

import com.example.technomarket.model.pojo.User;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Data
@Component
@SessionScope
public class CurrentUser {

    private Long id;

    private String email;
    private boolean isAdmin;

    public void login(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.isAdmin = user.isAdmin();
    }

    public void logout(){
        this.id = null;
        this.email = null;
        this.isAdmin = false;
    }

    public boolean checkAdmin(){
        if (this.isAdmin){
            if (this.id != null){
                return true;
            }
        }
        return false;
    }
}
