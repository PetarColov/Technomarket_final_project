package com.example.technomarket.contoller;

import com.example.technomarket.model.dto.subcategoryDTOs.SubcategoryWithNewName;
import com.example.technomarket.model.dto.subcategoryDTOs.SubcategoryWithNameOnly;
import com.example.technomarket.model.repository.SubcategoryRepository;
import com.example.technomarket.services.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class SubcategoryController extends AbstractController{

    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private SubcategoryService subcategoryService;

    @GetMapping("/{cid}/")
    public List<SubcategoryWithNameOnly> getAllSubcategoriesPerCategory(@PathVariable long cid){
        return subcategoryService.showSubcategory(cid);
    }

    //TODO: change URI
    @PostMapping("/{cid}/")
    public void addSubcategory(@PathVariable long cid, @RequestBody SubcategoryWithNameOnly subcategory, HttpServletResponse response){
        subcategoryService.addSubcategory(cid,subcategory);
        try {
            response.getWriter().println("Subcategory was successfully added");
        } catch (IOException e) {
            System.out.println("Issue with subcategory response" + e.getMessage());
        }
    }

    @PutMapping("/{cid}/")
    public void editSubcategory(@PathVariable long cid, @RequestBody SubcategoryWithNewName subcategory, HttpServletResponse response){
        subcategoryService.editSubcategory(cid, subcategory);
        try {
            response.getWriter().println("Subcategory was successfully edited");
        } catch (IOException e) {
            System.out.println("Issue with subcategory response" + e.getMessage());
        }
    }

    @DeleteMapping("/{cid}/")
    public void deleteSubcategory(@PathVariable long cid, @RequestBody SubcategoryWithNameOnly subcategory, HttpServletResponse response){
        subcategoryService.deleteSubcategory(cid,subcategory);
        try {
            response.getWriter().println("Subcategory was successfully deleted");
        } catch (IOException e) {
            System.out.println("Issue with subcategory response" + e.getMessage());
        }
    }
}
