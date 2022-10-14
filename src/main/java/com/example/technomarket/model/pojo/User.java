package com.example.technomarket.model.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "user")
    private Set<Cart> cartUser;

    @ManyToOne
    private Role role;


    @ManyToMany(mappedBy = "usersSubscribed")
    private Set<Product> subscriptions;
}
