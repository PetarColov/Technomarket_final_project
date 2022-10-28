package com.example.technomarket.contoller;

import com.example.technomarket.model.dto.orders.FinishOrderDTO;
import com.example.technomarket.model.dto.orders.OrderCredentialsDTO;
import com.example.technomarket.services.CartService;
import com.example.technomarket.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/remove/{pid}")
    public void removeFromCart(@PathVariable long pid){
       cartService.removeFromCart(pid);
    }

    @PostMapping("/finish")
    public FinishOrderDTO finishOrder(@RequestBody OrderCredentialsDTO credentialsDTO){
        return orderService.finishOrder(credentialsDTO);
    }

}
