package com.example.technomarket.contoller;

import com.example.technomarket.model.dto.categoryDTOs.CategoryWithNameOnlyDTO;
import com.example.technomarket.model.dto.categoryDTOs.CategoryWithNewNameDTO;
import com.example.technomarket.model.pojo.Category;
import com.example.technomarket.model.repository.CategoryRepository;
import com.example.technomarket.services.CategoryService;
import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class CategoryController extends AbstractController{

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryWithNameOnlyDTO> showAllCategories(){
        return categoryService.showAllCategories();
    }

    @PostMapping("/category")
    public void addCategory(@RequestBody CategoryWithNameOnlyDTO category, HttpServletResponse response){
        categoryService.addCategoryToList(category);
        try {
            response.getWriter().println("Category was successfully added");
        } catch (IOException e) {
            System.out.println("Issue with adding category response" + e.getMessage());
        }
    }

    @PutMapping("/category/{cid}")
    public void updateCategory(@PathVariable long cid, @RequestBody CategoryWithNewNameDTO category, HttpServletResponse response){
        categoryService.updateCategory(cid, category);
        try {
            response.getWriter().println("Category was successfully updated");
        } catch (IOException e) {
            System.out.println("Issue with adding category response" + e.getMessage());
        }
    }

    @DeleteMapping("/category/{cid}")
    public void deleteCategory(@PathVariable long cid, HttpServletResponse response){
        categoryService.deleteCategory(cid);
        try {
            response.getWriter().println("Category was successfully deleted");
        } catch (IOException e) {
            System.out.println("Issue with adding category response" + e.getMessage());
        }
    }
}
