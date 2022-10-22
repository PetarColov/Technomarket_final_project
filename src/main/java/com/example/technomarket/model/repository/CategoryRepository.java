package com.example.technomarket.model.repository;

import com.example.technomarket.model.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    public Optional<Category> findCategoryByName(String categoryName);
    public Optional<Category> findCategoryByCategoryId(long cid);

}
