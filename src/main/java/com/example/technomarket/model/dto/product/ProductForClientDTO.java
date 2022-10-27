package com.example.technomarket.model.dto.product;

import com.example.technomarket.model.pojo.Brand;
import com.example.technomarket.model.pojo.Discount;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
public class ProductForClientDTO {
    private String name;
    private long productId;
    private BigDecimal price;
    private int amountLeft;
    private Brand brandName;

}
