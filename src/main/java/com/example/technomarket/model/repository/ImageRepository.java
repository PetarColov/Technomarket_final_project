package com.example.technomarket.model.repository;

import com.example.technomarket.model.pojo.Product;
import com.example.technomarket.model.pojo.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findAllByProduct(Product product);
}
