package com.example.technomarket.model.repository;

import com.example.technomarket.model.dto.subcategoryDTOs.SubcategoryWithNameOnly;
import com.example.technomarket.model.pojo.Category;
import com.example.technomarket.model.pojo.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubcategoryRepository extends JpaRepository<SubCategory,Long> {
     List<SubCategory> findAllSubCategoryByCategory(Category category);
     Optional<SubCategory> findSubCategoryBySubcategoryName(String name);
}
