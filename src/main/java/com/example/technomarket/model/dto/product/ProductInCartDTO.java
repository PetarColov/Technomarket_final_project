package com.example.technomarket.model.dto.product;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductInCartDTO {

    //TODO add product id
    private String productName;

    private int quantity;
}
