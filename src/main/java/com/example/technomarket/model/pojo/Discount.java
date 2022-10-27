package com.example.technomarket.model.pojo;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "discounts")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long discountId;

    @Column(nullable = false)
    private String discountDescription;

    @Column(nullable = false)
    private int discountPercent;

    @Column(nullable = false)
    private LocalDate startedAt;

    @Column(nullable = false)
    private LocalDate endedAt;

    @OneToMany(mappedBy = "discount")
    private Set<Product> products;
}
