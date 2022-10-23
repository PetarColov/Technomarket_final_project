package com.example.technomarket.services;

import com.example.technomarket.model.dto.subcategoryDTOs.SubcategoryWithNameOnly;
import com.example.technomarket.model.dto.subcategoryDTOs.SubcategoryWithNewName;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.exceptions.UnauthorizedException;
import com.example.technomarket.model.pojo.Category;
import com.example.technomarket.model.pojo.SubCategory;
import com.example.technomarket.model.repository.CategoryRepository;
import com.example.technomarket.model.repository.SubcategoryRepository;
import com.example.technomarket.util.CurrentUser;
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
    @Autowired
    private CurrentUser currentUser;

    public List<SubcategoryWithNameOnly> showSubcategory(long cid) {
        Optional<Category> categoryOptional = categoryRepository.findCategoryByCategoryId(cid);
        if(categoryOptional.isPresent()){
            List<SubCategory> subCategoryOptional = subcategoryRepository.findAllSubCategoryByCategory(categoryOptional.get());
            return subCategoryOptional.stream().map(s -> modelMapper.map(s, SubcategoryWithNameOnly.class)).collect(Collectors.toList());
        }
        throw new BadRequestException("There is no such category!");
    }

    public void addSubcategory(long cid, SubcategoryWithNameOnly subcategory) {
        if(!currentUser.isAdmin()){
            throw new UnauthorizedException("Method not allowed!");
        }
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

    public void editSubcategory(long cid, SubcategoryWithNewName subcategoryWithNewName){
        if(!currentUser.isAdmin()){
            throw new UnauthorizedException("Method not allowed!");
        }
        Optional<Category> category = categoryRepository.findCategoryByCategoryId(cid);
        if(category.isPresent()){
            Optional<SubCategory> subCategoryOptional = subcategoryRepository.findSubCategoryBySubcategoryName(subcategoryWithNewName.getSubcategoryName());
            if(subCategoryOptional.isPresent()){
                SubCategory subCategory = subCategoryOptional.get();
                subCategory.setSubcategoryName(subcategoryWithNewName.getNewSubcategoryName());
                SubCategory subCategory1 = modelMapper.map(subCategory,SubCategory.class);
                subcategoryRepository.save(subCategory1);
            }
        }
        else{
            throw new BadRequestException("No such category exists so that you can edit this subcategory");
        }

    }

    public void deleteSubcategory(long cid, SubcategoryWithNameOnly subcategoryWithNameOnly) {
        if(!currentUser.isAdmin()){
            throw new UnauthorizedException("Method not allowed!");
        }
        Optional<Category> category = categoryRepository.findCategoryByCategoryId(cid);
        if (category.isPresent()) {
            Optional<SubCategory> subCategoryOptional = subcategoryRepository.findSubCategoryBySubcategoryName(subcategoryWithNameOnly.getSubcategoryName());
            if(subCategoryOptional.isPresent()) {
                SubCategory subCategory = modelMapper.map(subCategoryOptional, SubCategory.class);
                subcategoryRepository.delete(subCategory);
            }
            else{
                throw new BadRequestException("No such category exists!");
            }
        }
        else{
            throw new BadRequestException("No such category exists so that you can delete this subcategory");
        }
    }

}
