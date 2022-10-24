package com.example.technomarket.contoller;

import com.example.technomarket.model.dto.product.ProductInCartDTO;
import com.example.technomarket.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/remove/{pid}")
    public ProductInCartDTO removeFromCart(@PathVariable long pid){
       return cartService.removeFromCart(pid);
    }

}
