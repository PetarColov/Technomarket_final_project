package com.example.technomarket.model.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "product_characteristics")
public class Characteristic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String characteristicName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "characteristic", cascade = CascadeType.ALL)
    private Set<Chars> characteristics;
}
