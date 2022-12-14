package com.example.technomarket.model.repository;

import com.example.technomarket.model.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
     Optional<Category> findCategoryByName(String categoryName);
     Optional<Category> findCategoryByCategoryId(long cid);
}
