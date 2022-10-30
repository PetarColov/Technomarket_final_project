package com.example.technomarket.services;

import com.example.technomarket.model.dto.categoryDTOs.CategoryWithNewNameDTO;
import com.example.technomarket.model.dto.categoryDTOs.RequestCategoryDTO;
import com.example.technomarket.model.dto.categoryDTOs.ResponseCategoryDTO;
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
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CurrentUser currentUser;


    public List<ResponseCategoryDTO> showAllCategories(){
        List<Category> category = categoryRepository.findAll();
        return category.stream().map(c -> modelMapper.map(c, ResponseCategoryDTO.class)).collect(Collectors.toList());
    }

    public ResponseCategoryDTO addCategoryToList(RequestCategoryDTO category) {
        if(!currentUser.isAdmin()){
            throw new UnauthorizedException("You don`t have permission for this operation!");
        }
        Optional<Category> optionalCategory = categoryRepository.findCategoryByName(category.getCategoryName());
        if(category.getCategoryName() != null && optionalCategory.isEmpty()){
            categoryRepository.save(modelMapper.map(category, Category.class));
            Optional<Category> c1 = categoryRepository.findCategoryByName(category.getCategoryName());
            return modelMapper.map(c1.get(),ResponseCategoryDTO.class);
        }
        else{
            throw new BadRequestException("Invalid name of the category");
        }
    }

    public ResponseCategoryDTO deleteCategory(long id) {
        if(!currentUser.isAdmin()){
            throw new UnauthorizedException("You don`t have permission for this operation!");
        }
        Optional<Category> categoryOptional = categoryRepository.findCategoryByCategoryId(id);
        if(categoryOptional.isPresent()){
            List<SubCategory> subCategories = subcategoryRepository.findAllSubCategoryByCategory(categoryOptional.get());
            subcategoryRepository.deleteAll(subCategories);
            Category category = modelMapper.map(categoryOptional, Category.class);
            ResponseCategoryDTO responseCategoryDTO = modelMapper.map(categoryOptional,ResponseCategoryDTO.class);
            categoryRepository.delete(category);
            return responseCategoryDTO;
        }
        else{
            throw new BadRequestException("This category does not exist!");
        }
    }

    public ResponseCategoryDTO updateCategory(long cid, CategoryWithNewNameDTO category) {
        if(!currentUser.isAdmin()){
            throw new UnauthorizedException("You don`t have permission for this operation!");
        }
        Optional<Category> categoryOptional= categoryRepository.findCategoryByCategoryId(cid);
        if(categoryOptional.isPresent()){
            Category category1 = categoryOptional.get();
            category1.setName(category.getNewCategoryName());
            Category c = modelMapper.map(category1, Category.class);
            ResponseCategoryDTO responseCategoryDTO = modelMapper.map(category1,ResponseCategoryDTO.class);
            categoryRepository.save(c);
            return responseCategoryDTO;
        }
        else{
            throw new BadRequestException("No such category!");
        }
    }
}
