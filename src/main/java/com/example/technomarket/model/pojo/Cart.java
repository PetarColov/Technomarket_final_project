package com.example.technomarket.model.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "carts")
public class Cart {

    @EmbeddedId
    private CartKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
}
