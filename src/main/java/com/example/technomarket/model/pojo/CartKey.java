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
public class CartKey implements Serializable {

    @Column(name = "user_id")
    private long userId;

    @Column(name = "product_id")
    private long productId;
}
