package com.example.technomarket.services;

import com.example.technomarket.model.dto.brand.AddBrandDTO;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.exceptions.UnauthorizedException;
import com.example.technomarket.model.pojo.Brand;
import com.example.technomarket.model.repository.BrandRepository;
import com.example.technomarket.util.CurrentUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BrandService {

    @Autowired
    private CurrentUser currentUser;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ModelMapper mapper;

    public Brand addBrand(AddBrandDTO brandDTO) {
        if (!currentUser.checkAdmin()) {
            throw new UnauthorizedException("You don`t have permission for this operation!");
        }

        Optional<Brand> byName = brandRepository.findByBrandName(brandDTO.getBrandName());
        if (byName.isEmpty()){
            Brand brand = mapper.map(brandDTO, Brand.class);
            brandRepository.save(brand);
            return brand;
        }else {
            throw new BadRequestException("Brand already exist");
        }

    }
}
