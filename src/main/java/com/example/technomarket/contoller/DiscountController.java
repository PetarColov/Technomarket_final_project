package com.example.technomarket.contoller;

import com.example.technomarket.model.dto.discounts.DiscountProductsDTO;
import com.example.technomarket.model.dto.discounts.RequestDiscountDTO;
import com.example.technomarket.model.dto.discounts.ResponseDiscountDTO;
import com.example.technomarket.model.pojo.Discount;
import com.example.technomarket.services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @PostMapping("/discount/add")
    public ResponseDiscountDTO addDiscount(@RequestBody RequestDiscountDTO requestDiscountDTO){
        return discountService.addDiscount(requestDiscountDTO);
    }

    @PostMapping("/discount/products")
    public ResponseDiscountDTO addProductsForDiscount(@RequestBody DiscountProductsDTO discountProductsDTO){
        return discountService.addProductsForDiscount(discountProductsDTO);
    }
}
