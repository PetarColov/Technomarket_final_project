package com.example.technomarket.services;

import com.example.technomarket.model.dto.subcategoryDTOs.ResponseSubcategoryDTO;
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

        if(categoryOptional.isEmpty()) {
            throw new BadRequestException("There is no such category!");
        }

        List<SubCategory> subCategoryOptional = subcategoryRepository.findAllSubCategoryByCategory(categoryOptional.get());

        return subCategoryOptional.stream()
                .map(subCategory -> modelMapper
                .map(subCategory, SubcategoryWithNameOnly.class))
                .collect(Collectors.toList());
    }

    public ResponseSubcategoryDTO addSubcategory(long cid, SubcategoryWithNameOnly subcategory) {
        if(!currentUser.isAdmin()){
            throw new UnauthorizedException("You don`t have permission for this operation!");
        }

        Optional<Category> categoryOptional = categoryRepository.findCategoryByCategoryId(cid);
        if(categoryOptional.isEmpty()) {
            throw new BadRequestException("No such category exists so that you can add this subcategory");
        }

        String subcategoryName = subcategory.getSubcategoryName();
        if(subcategoryName == null || subcategoryName.length() == 0){
            throw new BadRequestException("No empty name is allowed!");
        }

        Optional<SubCategory> subCategoryOptional = subcategoryRepository.findSubCategoryBySubcategoryName(subcategoryName);
        if(subCategoryOptional.isPresent()) {
            throw new BadRequestException("Such subcategory already exists");
        }

        SubCategory subCategory = modelMapper.map(subcategory,SubCategory.class);
        subCategory.setCategory(categoryOptional.get());
        SubCategory savedSubcategory = subcategoryRepository.save(subCategory);

        return modelMapper.map(savedSubcategory, ResponseSubcategoryDTO.class);
    }

    public ResponseSubcategoryDTO editSubcategory(SubcategoryWithNewName subcategoryWithNewName){
        if(!currentUser.isAdmin()){
            throw new UnauthorizedException("You don`t have permission for this operation!");
        }

        String subcategoryName = subcategoryWithNewName.getSubcategoryName();
        if(subcategoryName == null || subcategoryName.length() == 0){
            throw new BadRequestException("No empty name is allowed!");
        }

        SubCategory subCategory = findSubcategoryByName(subcategoryName);
        subCategory.setSubcategoryName(subcategoryWithNewName.getNewSubcategoryName());
        subcategoryRepository.save(subCategory);

        return modelMapper.map(subCategory,ResponseSubcategoryDTO.class);
    }

    public ResponseSubcategoryDTO deleteSubcategory(SubcategoryWithNameOnly subcategoryWithNameOnly) {
        if(!currentUser.isAdmin()){
            throw new UnauthorizedException("You don`t have permission for this operation!");
        }

        String subcategoryName = subcategoryWithNameOnly.getSubcategoryName();
        SubCategory subCategory = findSubcategoryByName(subcategoryName);
        ResponseSubcategoryDTO subcategoryDTO = modelMapper.map(subCategory,ResponseSubcategoryDTO.class);
        subcategoryRepository.delete(subCategory);

        return subcategoryDTO;
    }

    private SubCategory findSubcategoryByName(String subcategoryName){
        Optional<SubCategory> subcategory = subcategoryRepository.findSubCategoryBySubcategoryName(subcategoryName);
        if(subcategory.isEmpty()) {
            throw new BadRequestException("No such subcategory exists!");
        }
        return subcategory.get();
    }
}
