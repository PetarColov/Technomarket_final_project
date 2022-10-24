package com.example.technomarket.util;

import com.example.technomarket.model.pojo.Cart;
import com.example.technomarket.model.pojo.User;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Set;

@Data
@Component
@SessionScope
public class CurrentUser {

    private Long id;

    private String email;

    private boolean isAdmin;

    private Set<Cart> cartUser;


    public void login(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.isAdmin = user.isAdmin();
        this.cartUser = user.getCartUser();
    }

    public void logout(){
        this.id = null;
        this.email = null;
        this.isAdmin = false;
        this.cartUser = null;
    }

    public boolean checkAdmin(){
        if (this.isAdmin){
            if (this.id != null){
                return true;
            }
        }
        return false;
    }

    public void addToCart(Cart cart){
        this.cartUser.add(cart);
    }
}
