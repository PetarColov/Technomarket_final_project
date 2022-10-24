package com.example.technomarket.contoller;

import com.example.technomarket.model.dto.product.AddProductDTO;
import com.example.technomarket.model.dto.product.AddProductToCartDTO;
import com.example.technomarket.model.dto.product.ProductInCartDTO;
import com.example.technomarket.model.dto.product.ProductResponseDTO;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.pojo.Product;
import com.example.technomarket.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/product")
public class ProductController extends AbstractController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ProductResponseDTO addProduct(@RequestBody AddProductDTO productDTO) {
        return productService.addProduct(productDTO);
    }

    @PostMapping("/delete/{pid}")
    public ProductResponseDTO deleteProduct(@PathVariable long pid){
        return productService.deleteProduct(pid);
    }

    @PostMapping("/addToCart/{pid}")
    public ProductInCartDTO addToCart(@PathVariable long pid, @RequestBody AddProductToCartDTO addProductToCartDTO){
        return productService.addToCart(addProductToCartDTO, pid);
    }
}
