package com.example.technomarket.contoller;

import com.example.technomarket.model.dto.characteristicDTOs.CharacteristicWithValueDTO;
import com.example.technomarket.model.dto.product.*;
import com.example.technomarket.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController extends AbstractController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ProductResponseDTO addProduct(@RequestBody ProductDTO productDTO) {
        return productService.addProduct(productDTO);
    }

    @PostMapping("/delete/{pid}")
    public ProductResponseDTO deleteProduct(@PathVariable long pid){
        return productService.deleteProduct(pid);
    }

    @PostMapping("/addToCart/{pid}")
    public ProductInCartDTO addToCart(@PathVariable long pid, @RequestBody ProductForCartDTO productForCartDTO){
        return productService.addToCart(productForCartDTO, pid);
    }

    @GetMapping("/search")
    public ProductResponseDTO searchForProductByName(@RequestParam(name = "name") String productName){
        return productService.searchForProductByName(productName);
    }


    @PostMapping("/{pid}/addchar")
    public ProductWithCharacteristicsDTO addCharacteristic(@PathVariable long pid, @RequestBody CharacteristicWithValueDTO characteristic){
        return productService.addCharacteristic(pid, characteristic);
    }

    @GetMapping("/find/subcat")
    public List<ProductResponseDTO> getProductBySubcategory(@RequestParam(name = "name") String subcategory){
       return productService.getProductBySubcategory(subcategory);
    }

    @GetMapping("/{pid}")
    public ProductForClientDTO getProductById(@PathVariable long pid){
        return productService.getProduct(pid);
    }

    @PostMapping("/{pid}/subscribe")
    public ProductWithNameDTO subscribeForProduct(@PathVariable long pid){
        return productService.subscribeForProduct(pid);
    }

    @GetMapping()
    public List<ProductResponseDTO> sortProducts(@RequestParam(name = "subcat_id") Long subcategory, @RequestParam(name = "sort") String sort){
        return productService.sortProducts(subcategory, sort);
    }
}
