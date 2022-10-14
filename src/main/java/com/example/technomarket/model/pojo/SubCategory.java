package com.example.technomarket.model.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sub_categories")
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
}
