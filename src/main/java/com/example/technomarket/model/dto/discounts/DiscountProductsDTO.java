package com.example.technomarket.model.dto.discounts;

import lombok.Data;

import java.util.List;

@Data
public class DiscountProductsDTO {
    private List<Long> products;
    private String discountDescription;
    private int discountPercent;
}
