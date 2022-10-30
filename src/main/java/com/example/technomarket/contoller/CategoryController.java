package com.example.technomarket.contoller;

import com.example.technomarket.model.dto.categoryDTOs.RequestCategoryDTO;
import com.example.technomarket.model.dto.categoryDTOs.CategoryWithNewNameDTO;
import com.example.technomarket.model.dto.categoryDTOs.ResponseCategoryDTO;
import com.example.technomarket.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class CategoryController extends AbstractController{

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public List<ResponseCategoryDTO> showAllCategories(){
        return categoryService.showAllCategories();
    }

    @PostMapping("/category")
    public ResponseCategoryDTO addCategory(@RequestBody RequestCategoryDTO category){
        return categoryService.addCategoryToList(category);
    }

    @PutMapping("/category/{cid}")
    public ResponseCategoryDTO updateCategory(@PathVariable long cid, @RequestBody CategoryWithNewNameDTO category, HttpServletResponse response){
        return categoryService.updateCategory(cid, category);
    }

    @DeleteMapping("/category/{cid}")
    public ResponseCategoryDTO deleteCategory(@PathVariable long cid, HttpServletResponse response){
        return categoryService.deleteCategory(cid);
    }
}
