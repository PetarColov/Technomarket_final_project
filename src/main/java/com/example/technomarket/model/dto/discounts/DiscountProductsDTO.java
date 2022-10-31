package com.example.technomarket.model.dto.discounts;

import lombok.Data;

import java.util.List;

@Data
public class DiscountProductsDTO {
    private Long discountId;
    private List<Long> products;
}
