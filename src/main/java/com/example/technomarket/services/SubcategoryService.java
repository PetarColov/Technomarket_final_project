package com.example.technomarket.services;

import com.example.technomarket.model.dto.subcategoryDTOs.SubcategoryWithNameOnly;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.pojo.Category;
import com.example.technomarket.model.pojo.SubCategory;
import com.example.technomarket.model.repository.CategoryRepository;
import com.example.technomarket.model.repository.SubcategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubcategoryService {
    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<SubcategoryWithNameOnly> showSubcategory(long cid) {
        Optional<Category> categoryOptional = categoryRepository.findCategoryByCategoryId(cid);
        if(categoryOptional.isPresent()){
            List<SubCategory> subCategoryOptional = subcategoryRepository.findAllSubCategoryByCategory(categoryOptional.get());
            return subCategoryOptional.stream().map(s -> modelMapper.map(s, SubcategoryWithNameOnly.class)).collect(Collectors.toList());
        }
        throw new BadRequestException("There is no such category!");
    }

    public void addSubcategory(long cid, SubcategoryWithNameOnly subcategory) {
        Optional<Category> categoryOptional = categoryRepository.findCategoryByCategoryId(cid);
        if(categoryOptional.isPresent()){
            Optional<SubCategory> subCategoryOptional = subcategoryRepository.findSubCategoryBySubcategoryName(subcategory.getSubcategoryName());
            if(subCategoryOptional.isEmpty()){
                SubCategory subCategory = modelMapper.map(subcategory,SubCategory.class);
                subCategory.setCategory(categoryOptional.get());
                subcategoryRepository.save(subCategory);
            }
            else{
                throw new BadRequestException("Such subcategory already exists");
            }
        }
        else{
            throw new BadRequestException("No such category exists so that you can add this subcategory");
        }
    }
}
