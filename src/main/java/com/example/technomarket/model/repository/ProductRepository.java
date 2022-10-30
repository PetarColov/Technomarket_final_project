package com.example.technomarket.model.repository;

import com.example.technomarket.model.pojo.Product;
import com.example.technomarket.model.pojo.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);
    List<Product> findAllBySubcategoryOrderByPriceDesc(SubCategory subCategory);
    List<Product> findAllBySubcategoryOrderByPriceAsc(SubCategory subCategory);
    List<Product> findAllBySubcategory(SubCategory subcategory);
}
