package com.example.technomarket.model.pojo;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "characteristic_pairs")
public class Chars {

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
