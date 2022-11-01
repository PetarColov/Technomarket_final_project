package com.example.technomarket.services;

import com.example.technomarket.model.dto.discounts.DiscountProductsDTO;
import com.example.technomarket.model.dto.discounts.RequestDiscountDTO;
import com.example.technomarket.model.dto.discounts.ResponseDiscountDTO;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.exceptions.UnauthorizedException;
import com.example.technomarket.model.pojo.Discount;
import com.example.technomarket.model.pojo.Product;
import com.example.technomarket.model.pojo.User;
import com.example.technomarket.model.repository.DiscountRepository;
import com.example.technomarket.model.repository.ProductRepository;
import com.example.technomarket.model.repository.UserRepository;
import com.example.technomarket.util.CurrentUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {
    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private CurrentUser currentUser;
    @Autowired
    private JavaMailSender mailSender;

    private Product getProduct(Long pid){
        Optional<Product> product = productRepository.findById(pid);
        if(product.isPresent()){
           return product.get();
        }

        throw new BadRequestException("No such product");
    }

    public ResponseDiscountDTO addDiscount(RequestDiscountDTO requestDiscountDTO) {

        if(!currentUser.isAdmin()){
            throw new UnauthorizedException("You don`t have permission for this operation!");
        }

        LocalDate startAt = requestDiscountDTO.getStartedAt();
        LocalDate endAt = requestDiscountDTO.getEndedAt();
        int discountPercent = requestDiscountDTO.getDiscountPercent();
        String discountDescription = requestDiscountDTO.getDiscountDescription();

        if(endAt.isBefore(startAt)){
            throw new BadRequestException("Invalid duration entered!");
        }

        if(startAt.isBefore(LocalDate.now())){
            throw new BadRequestException("This date has already passed!");
        }

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
        if(!currentUser.isAdmin()){
            throw new UnauthorizedException("You don`t have permission for this operation!");
        }

        List<Long> productIds = discountProductsDTO.getProducts();
        Long discountId = discountProductsDTO.getDiscountId();

        Optional<Discount> discountOptional = discountRepository.findByDiscountId(discountId);
        if(discountOptional.isEmpty()) {
            throw new BadRequestException("This discount does not exists");
        }

        Discount discount = discountOptional.get();
        //check if all products are present and save them in a list
        List<Product> products = new ArrayList<>();
        for(Long pid : productIds){
            Product product = productRepository.findById(pid)
                    .orElseThrow(() -> new BadRequestException("Some of the products you are trying to add does not exists!"));
            products.add(product);
        }

        //set the discount for every product
        for(Product product : products){
            product.setDiscount(discount);
            productRepository.save(product);
        }

        //notify users
        for (Product product : products) {
            for (User user : product.getUsersSubscribed()) {
                sendEmail(user, product, discount.getDiscountPercent());
            }
        }

        return modelMapper.map(discount,ResponseDiscountDTO.class);
    }

    private void sendEmail(User user, Product product, int discountPercent) {
        new Thread(() -> {
            String toAddress = user.getEmail();
            String fromAddress = "technomarketfinalproject@gmail.com";
            String subject = "Product on sale";
            String content = "Dear " + user.getFirstName() + " " + user.getLastName() + ", one of your favourite products is now on sale. " +
                    product.getName() + " is now " + discountPercent + "% down. Hurry up and get one!";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(toAddress);
            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);
        }).start();
    }
}
