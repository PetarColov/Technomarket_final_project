package com.example.technomarket.model.pojo;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int amountLeft;

    @ManyToOne
    private Brand brandName;

    @OneToMany(mappedBy = "product")
    private Set<Cart> cartProduct;

    @ManyToMany
    private Set<User> usersSubscribed;

    @ManyToOne
    @JoinColumn(name = "subcategory_id",nullable = false)
    private SubCategory subcategory;

    @OneToMany(mappedBy = "product")
    private Set<Chars> characteristics;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;
}
