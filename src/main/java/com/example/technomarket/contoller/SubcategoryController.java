package com.example.technomarket.contoller;

import com.example.technomarket.model.dto.subcategoryDTOs.ResponseSubcategoryDTO;
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

    @PostMapping("/add/{cid}/")
    public ResponseSubcategoryDTO addSubcategory(@PathVariable long cid, @RequestBody SubcategoryWithNameOnly subcategory){
       return subcategoryService.addSubcategory(cid,subcategory);
    }

    @PutMapping("/edit/{cid}/")
    public ResponseSubcategoryDTO editSubcategory(@PathVariable long cid, @RequestBody SubcategoryWithNewName subcategory){
       return subcategoryService.editSubcategory(cid, subcategory);
    }

    @DeleteMapping("/delete/{cid}/")
    public ResponseSubcategoryDTO deleteSubcategory(@PathVariable long cid, @RequestBody SubcategoryWithNameOnly subcategory){
        return subcategoryService.deleteSubcategory(cid,subcategory);
    }
}
