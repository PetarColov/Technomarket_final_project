package com.example.technomarket.contoller;

import com.example.technomarket.model.dto.product.ProductDTO;
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
    public ProductDTO addProduct(@RequestBody ProductDTO productDTO) {
        return productService.addProduct(productDTO);
    }

    @PostMapping("/delete/{pid}")
    public void deleteProduct(@PathVariable long pid, HttpServletResponse response){
        productService.deleteProduct(pid);
        try {
            response.getWriter().println("Product was successfully deleted");
        } catch (IOException e) {
            System.out.println("Issue with deleting product " + e.getMessage());
        }
    }
}
