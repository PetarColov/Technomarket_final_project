package com.example.technomarket.model.dto.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {

    private int amountLeft;
    private String name;
    private BigDecimal price;

}
