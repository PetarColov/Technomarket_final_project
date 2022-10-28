package com.example.technomarket.model.dto.orders;

import com.example.technomarket.model.dto.product.ProductFinishOrderDTO;
import com.example.technomarket.model.dto.product.ProductWithNameDTO;
import com.example.technomarket.model.dto.user.UserWithoutPasswordDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FinishOrderDTO {

    private List<ProductFinishOrderDTO> products;

    private UserWithoutPasswordDTO userWithoutPasswordDTO;

    private BigDecimal priceSum;

}
