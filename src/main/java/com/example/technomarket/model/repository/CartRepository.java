package com.example.technomarket.model.repository;

import com.example.technomarket.model.pojo.Cart;
import com.example.technomarket.model.pojo.CartKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, CartKey> {

    @Modifying
    @Query("DELETE FROM Cart c where c.id = :cart_id")
    void removeProductFromCart(@Param("cart_id") CartKey cartKey);


    @Query("select c FROM Cart c where c.id.userId = :id")
    List<Cart> findAllByUserId(@Param("id")Long id);
}
