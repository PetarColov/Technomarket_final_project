package com.example.technomarket.model.repository;

import com.example.technomarket.model.pojo.Cart;
import com.example.technomarket.model.pojo.CartKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, CartKey> {

    Optional<Cart> removeAllById(CartKey cartKey);

}
