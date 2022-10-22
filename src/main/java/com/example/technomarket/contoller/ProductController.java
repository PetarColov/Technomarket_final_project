package com.example.technomarket.contoller;

import com.example.technomarket.model.dto.product.ProductDTO;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.pojo.Product;
import com.example.technomarket.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController extends AbstractController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ProductDTO addProduct(@RequestBody ProductDTO productDTO) {
        return productService.addProduct(productDTO);
    }



}
