package com.example.technomarket.model.pojo;

import javax.persistence.*;

@Entity
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private  String name;
}
