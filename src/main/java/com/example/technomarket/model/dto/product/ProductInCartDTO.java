package com.example.technomarket.model.dto.product;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductInCartDTO {

    private String productName;

    private int quantity;
}
