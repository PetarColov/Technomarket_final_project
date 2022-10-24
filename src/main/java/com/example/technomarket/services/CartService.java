package com.example.technomarket.services;

import com.example.technomarket.model.dto.product.ProductInCartDTO;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.pojo.Cart;
import com.example.technomarket.model.pojo.CartKey;
import com.example.technomarket.model.repository.CartRepository;
import com.example.technomarket.util.CurrentUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CurrentUser currentUser;


    //TODO fix this
    public ProductInCartDTO removeFromCart(long pid) {

        CartKey cartKey = new CartKey(currentUser.getId(), pid);

        Cart cart = cartRepository.removeAllById(cartKey).orElseThrow(() ->
                new BadRequestException("Product does not exist in cart!"));

        currentUser.getCartUser().remove(cart);

        return new ProductInCartDTO(cart.getProduct().getName(), cart.getQuantity());
    }
}
