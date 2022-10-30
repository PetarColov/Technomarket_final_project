package com.example.technomarket.services;

import com.example.technomarket.model.pojo.Cart;
import com.example.technomarket.model.pojo.CartKey;
import com.example.technomarket.model.repository.CartRepository;
import com.example.technomarket.util.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CurrentUser currentUser;



    @Transactional
    public void removeFromCart(long pid) {

        CartKey cartKey = new CartKey(currentUser.getId(), pid);

        cartRepository.removeProductFromCart(cartKey);

        Iterator<Cart> iterator = currentUser.getCartUser().iterator();
        while (iterator.hasNext()){
            Cart next = iterator.next();
            iterator.remove();
            System.out.println(next.getId().getProductId() + " " + next.getId().getUserId());
        }

    }
}
