package com.example.technomarket.contoller;

import com.example.technomarket.model.dto.characteristicDTOs.CharacteristicWithValueDTO;
import com.example.technomarket.model.dto.product.*;
import com.example.technomarket.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @GetMapping("/search/{product}")
    public ProductForClientDTO searchForProductByName(@PathVariable String product){
        return productService.searchForProductByName(product);
    }

    @GetMapping("/sortAsc/{subcategory}")
    public List<ProductForClientDTO> sortProductsAscending(@PathVariable String subcategory){
        return productService.sortProductsAscending(subcategory);
    }

    @GetMapping("/sortDesc/{subcategory}")
    public List<ProductForClientDTO> sortProductsDescending(@PathVariable String subcategory){
        return productService.sortProductsDescending(subcategory);
    }

    @PostMapping("/{pid}/addchar")
    public ProductWithCharacteristicsDTO addCharacteristic(@PathVariable long pid, @RequestBody CharacteristicWithValueDTO characteristic){
        return productService.addCharacteristic(pid, characteristic);
    }

    @GetMapping("/get/{subcategory}")
    public List<ProductForClientDTO> getProductBySubcategory(@PathVariable String subcategory){
       return productService.getProductBySubcategory(subcategory);
    }

    @GetMapping("/{pid}")
    public ProductForClientDTO getProductById(@PathVariable long pid){
        return productService.getProduct(pid);
    }



}
