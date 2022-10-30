package com.example.technomarket.contoller;

import com.example.technomarket.model.dto.brand.AddBrandDTO;
import com.example.technomarket.model.pojo.Brand;
import com.example.technomarket.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @PostMapping("/add")
    public Brand addBrand(@RequestBody AddBrandDTO brandDTO){
        return brandService.addBrand(brandDTO);
    }

}
