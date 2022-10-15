package com.example.technomarket.model.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "characteristics")
public class Characteristic {

    @EmbeddedId
    private CharacteristicKey id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @MapsId("characteristicId")
    @JoinColumn(name = "characteristic_id")
    private Characteristic characteristic;

    @Column(nullable = false)
    private String characteristicValue;
}
