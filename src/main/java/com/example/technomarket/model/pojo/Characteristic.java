package com.example.technomarket.model.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "characteristics_main")
public class Characteristic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String characteristicName;

    @OneToMany(mappedBy = "characteristic")
    private Set<Chars> characteristics;
}