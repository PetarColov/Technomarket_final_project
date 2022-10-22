package com.example.technomarket.services;


import com.example.technomarket.model.dto.product.ProductDTO;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.exceptions.UnauthorizedException;
import com.example.technomarket.model.pojo.Product;
import com.example.technomarket.model.repository.CategoryRepository;
import com.example.technomarket.model.repository.ProductRepository;
import com.example.technomarket.util.CurrentUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CurrentUser currentUser;

    public ProductDTO addProduct(ProductDTO productDTO) {

        if (!validAmount(productDTO.getAmountLeft()) || !validPrice(productDTO.getPrice())) {
            throw new BadRequestException("Enter valid product data!");
        }
        if (!currentUser.isAdmin() || currentUser.getId() == null) {
            throw new UnauthorizedException("Method not allowed!");
        }

        Optional<Product> byName = productRepository.findByName(productDTO.getName());

        ProductDTO p;

        if (byName.isPresent()) {
            Product existingProduct = byName.get();
            existingProduct.setAmountLeft(existingProduct.getAmountLeft() + productDTO.getAmountLeft());

            double newPrice = productDTO.getPrice().doubleValue();
            double oldPrice = existingProduct.getPrice().doubleValue();

            if (newPrice != oldPrice) {
                existingProduct.setPrice(BigDecimal.valueOf(newPrice));
            }

            productRepository.save(existingProduct);
            p = mapper.map(existingProduct, ProductDTO.class);
        } else {
            Product newProduct = mapper.map(productDTO, Product.class);
            productRepository.save(newProduct);
            p = mapper.map(newProduct, ProductDTO.class);
        }
        return p;
    }


    public void deleteProduct(long pid) {
        if (!currentUser.isAdmin() && currentUser.getId() != null) {
            throw new UnauthorizedException("Method not allowed!");
        }
        Product byId = productRepository.findById(pid).orElseThrow(() -> new BadRequestException("Product not found!"));
        productRepository.delete(byId);
    }


    private boolean validPrice(BigDecimal price) {
        return price.doubleValue() > 0;
    }

    private boolean validAmount(int amountLeft) {
        return amountLeft > 0;
    }
}
