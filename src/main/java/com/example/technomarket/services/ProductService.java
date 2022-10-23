package com.example.technomarket.services;


import com.example.technomarket.model.dto.product.ProductDTO;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.exceptions.UnauthorizedException;
import com.example.technomarket.model.pojo.Brand;
import com.example.technomarket.model.pojo.Product;
import com.example.technomarket.model.pojo.SubCategory;
import com.example.technomarket.model.repository.BrandRepository;
import com.example.technomarket.model.repository.ProductRepository;
import com.example.technomarket.model.repository.SubcategoryRepository;
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
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CurrentUser currentUser;
    @Autowired
    private BrandRepository brandRepository;

    public ProductDTO addProduct(ProductDTO productDTO) {

        if (!validAmount(productDTO.getAmountLeft()) || !validPrice(productDTO.getPrice())) {
            throw new BadRequestException("Enter valid product data!");
        }
        if (!currentUser.checkAdmin()) {
            throw new UnauthorizedException("Method not allowed!");
        }

        if(!validBrandName(productDTO.getBrandName())){
            throw new BadRequestException("No such brand!");
        }

        if (!validSubCategoryName(productDTO.getSubcategoryName())){
            throw new BadRequestException("No such subcategory!");
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

            SubCategory subCategory = getSubCategoryFromDB(productDTO);
            existingProduct.setSubcategory(subCategory);

            Brand brand = getBrandFromDB(productDTO);
            existingProduct.setBrandName(brand);

            productRepository.save(existingProduct);
            p = mapper.map(existingProduct, ProductDTO.class);
        } else {
            Product newProduct = mapper.map(productDTO, Product.class);

            SubCategory subCategory = getSubCategoryFromDB(productDTO);
            newProduct.setSubcategory(subCategory);

            Brand brand = getBrandFromDB(productDTO);
            newProduct.setBrandName(brand);

            productRepository.save(newProduct);
            p = mapper.map(newProduct, ProductDTO.class);
        }
        return p;
    }

    private Brand getBrandFromDB(ProductDTO productDTO) {
        Brand brand = brandRepository.findByBrandName(productDTO.getBrandName()).get();
        return brand;
    }

    private SubCategory getSubCategoryFromDB(ProductDTO productDTO) {
        return subcategoryRepository.findSubCategoryBySubcategoryName(productDTO.getSubcategoryName()).get();
    }

    private boolean validSubCategoryName(String subcategoryName) {
        Optional<SubCategory> byName = subcategoryRepository.findSubCategoryBySubcategoryName(subcategoryName);
        return byName.isPresent();
    }

    private boolean validBrandName(String brandName) {
        Optional<Brand> byName = brandRepository.findByBrandName(brandName);
        return byName.isPresent();
    }


    public void deleteProduct(long pid) {
        if (!currentUser.checkAdmin()) {
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
