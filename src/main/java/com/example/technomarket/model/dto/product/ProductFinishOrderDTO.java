package com.example.technomarket.model.dto.product;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductFinishOrderDTO {

    private String productName;
    private BigDecimal price;
    private int quantity;

}
