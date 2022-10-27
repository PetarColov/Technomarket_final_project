package com.example.technomarket.model.dto.user;

import com.example.technomarket.model.dto.product.ProductWithNameDTO;
import com.example.technomarket.model.pojo.Product;
import lombok.Data;

import java.util.List;

@Data
public class SubscriptionsDTO {

    private List<ProductWithNameDTO> subscriptions;

}
