package com.example.technomarket.model.dto.product;

import com.example.technomarket.model.pojo.Brand;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductForClientDTO {
    private String name;
    private long productId;
    private BigDecimal price;
    private int amountLeft;
    private Brand brandName;
}
