package com.example.technomarket.contoller;

import com.example.technomarket.model.dto.brand.AddBrandDTO;
import com.example.technomarket.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @PostMapping("/add")
    public void addBrand(@RequestBody AddBrandDTO brandDTO, HttpServletResponse response){
        brandService.addBrand(brandDTO);
        try {
            response.getWriter().println("Brand was successfully added");
        } catch (IOException e) {
            System.out.println("Issue with adding brand " + e.getMessage());
        }
    }

}
