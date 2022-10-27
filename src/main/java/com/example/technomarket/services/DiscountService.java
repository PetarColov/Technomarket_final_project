package com.example.technomarket.services;

import com.example.technomarket.model.dto.discounts.DiscountProductsDTO;
import com.example.technomarket.model.dto.discounts.RequestDiscountDTO;
import com.example.technomarket.model.dto.discounts.ResponseDiscountDTO;
import com.example.technomarket.model.dto.product.ProductForClientDTO;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.pojo.Discount;
import com.example.technomarket.model.pojo.Product;
import com.example.technomarket.model.repository.DiscountRepository;
import com.example.technomarket.model.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiscountService {
    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ModelMapper modelMapper;

//    public ResponseDiscountDTO addDiscount(RequestDiscountDTO requestDiscountDTO) {
//        LocalDate startAt = requestDiscountDTO.getStartedAt();
//        LocalDate endAt = requestDiscountDTO.getEndedAt();
//        int discountPercent = requestDiscountDTO.getDiscountPercent();
//        String discountDescription = requestDiscountDTO.getDiscountDescription();
//
//        if(endAt.isBefore(startAt)){
//            throw new BadRequestException("Invalid duration entered!");
//        }
//
//        if(startAt.isBefore(LocalDate.now())){
//            throw new BadRequestException("This date has already passed!");
//        }
//
//        Optional<Discount> discountOptional = discountRepository.findByDiscountDescriptionAndDiscountPercent(discountDescription,discountPercent);
//
//        if(discountOptional.isPresent()){
//            Discount discount = discountOptional.get();
//            for(Long pid : requestDiscountDTO.getProducts()){
//                Product product = getProduct(pid);
////                discount.getProducts().add(product);
//               // discountRepository.save(discount);
//                product.setDiscount(discount);
//                productRepository.save(product);
//            }
//            //discountRepository.save(discount);
//        }
//        else{
//            Discount discount = modelMapper.map(requestDiscountDTO, Discount.class);
//            for(Long pid : requestDiscountDTO.getProducts()){
//                Product product = getProduct(pid);
//                //discount.getProducts().add(product);
//               // discountRepository.save(discount);
//                product.setDiscount(discount);
//                productRepository.save(product);
//            }
//        }
//
//        Optional<Discount> discountOpt = discountRepository.findByDiscountDescriptionAndDiscountPercent(discountDescription,discountPercent);
//        Discount discountFromDB = discountOpt.get();
//        ResponseDiscountDTO responseDiscountDTO = modelMapper.map(discountFromDB,ResponseDiscountDTO.class);
//        responseDiscountDTO.setProducts(discountFromDB.getProducts().stream().map(p -> modelMapper.map(p, ProductForClientDTO.class)).collect(Collectors.toSet()));
//        return responseDiscountDTO;
//    }


    private Product getProduct(Long pid){
        Optional<Product> product = productRepository.findById(pid);
        if(product.isPresent()){
           return product.get();
        }

        throw new BadRequestException("No such product");
    }

    public ResponseDiscountDTO addDiscount(RequestDiscountDTO requestDiscountDTO) {
        int discountPercent = requestDiscountDTO.getDiscountPercent();
        String discountDescription = requestDiscountDTO.getDiscountDescription();

        Optional<Discount> discountOptional = discountRepository.findByDiscountDescriptionAndDiscountPercent(discountDescription,discountPercent);
        if(discountOptional.isEmpty()){
            Discount discount = modelMapper.map(requestDiscountDTO,Discount.class);
            discountRepository.save(discount);
            return modelMapper.map(discount,ResponseDiscountDTO.class);
        }
        else{
            throw new BadRequestException("This discount already exists!");
        }
    }

    public ResponseDiscountDTO addProductsForDiscount(DiscountProductsDTO discountProductsDTO) {
        List<Long> productIds = discountProductsDTO.getProducts();
        int discountPercent = discountProductsDTO.getDiscountPercent();
        String discountDescription = discountProductsDTO.getDiscountDescription();

        Optional<Discount> discountOptional = discountRepository.findByDiscountDescriptionAndDiscountPercent(discountDescription,discountPercent);
        if(discountOptional.isPresent()){
            Discount discount = discountOptional.get();
            for(Long pid : productIds){
                Product p = getProduct(pid);
                p.setDiscount(discount);
                productRepository.save(p);
            }

            return modelMapper.map(discountOptional.get(),ResponseDiscountDTO.class);
        }
        else{
            throw new BadRequestException("This discount does not exists");
        }
    }
}
