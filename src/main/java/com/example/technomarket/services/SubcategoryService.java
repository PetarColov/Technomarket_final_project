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
        if(categoryOptional.isPresent()){
            List<SubCategory> subCategoryOptional = subcategoryRepository.findAllSubCategoryByCategory(categoryOptional.get());
            return subCategoryOptional.stream().map(s -> modelMapper.map(s, SubcategoryWithNameOnly.class)).collect(Collectors.toList());
        }
        throw new BadRequestException("There is no such category!");
    }

    public ResponseSubcategoryDTO addSubcategory(long cid, SubcategoryWithNameOnly subcategory) {
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
                Optional<SubCategory> s = subcategoryRepository.findSubCategoryBySubcategoryName(subcategory.getSubcategoryName());
                ResponseSubcategoryDTO subcategoryDTO = null;
                if(s.isPresent()){
                    subcategoryDTO = modelMapper.map(s.get(), ResponseSubcategoryDTO.class);
                }
                return subcategoryDTO;
            }
            else{
                throw new BadRequestException("Such subcategory already exists");
            }
        }
        else{
            throw new BadRequestException("No such category exists so that you can add this subcategory");
        }
    }

    public ResponseSubcategoryDTO editSubcategory(long cid, SubcategoryWithNewName subcategoryWithNewName){
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
                ResponseSubcategoryDTO subcategoryDTO = modelMapper.map(subCategory1,ResponseSubcategoryDTO.class);
                subcategoryRepository.save(subCategory1);
                return subcategoryDTO;
            }
            else{
                throw new BadRequestException("No such subcategory exists so that you can edit this subcategory");
            }
        }
        else{
            throw new BadRequestException("No such category exists so that you can edit this subcategory");
        }
    }

    public ResponseSubcategoryDTO deleteSubcategory(long cid, SubcategoryWithNameOnly subcategoryWithNameOnly) {
        if(!currentUser.isAdmin()){
            throw new UnauthorizedException("Method not allowed!");
        }
        Optional<Category> category = categoryRepository.findCategoryByCategoryId(cid);
        if (category.isPresent()) {
            Optional<SubCategory> subCategoryOptional = subcategoryRepository.findSubCategoryBySubcategoryName(subcategoryWithNameOnly.getSubcategoryName());
            if(subCategoryOptional.isPresent()) {
                SubCategory subCategory = modelMapper.map(subCategoryOptional, SubCategory.class);
                ResponseSubcategoryDTO subcategoryDTO = modelMapper.map(subCategory,ResponseSubcategoryDTO.class);
                subcategoryRepository.delete(subCategory);
                return subcategoryDTO;
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
