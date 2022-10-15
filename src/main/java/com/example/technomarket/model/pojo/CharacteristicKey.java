package com.example.technomarket.model.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class CharacteristicKey implements Serializable {
    @Column(name = "product_id")
    private long productId;

    @Column(name = "characteristic_id")
    private long characteristicId;

//    @Column
//    private String characteristicValue;
}
