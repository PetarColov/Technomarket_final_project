package com.example.technomarket.model.pojo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class CharacteristicKey implements Serializable {

    @Column(name = "product_id")
    private long productId;

    @Column(name = "characteristic_id")
    private long characteristicId;
}
