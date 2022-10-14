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

    @Column
    private String name;

    @Column
    private BigDecimal price;

    @OneToMany(mappedBy = "product")
    private Set<Cart> cartProduct;

    @ManyToMany
    private Set<User> usersSubscribed;

    @ManyToOne
    private ProductCategory category;
}
