package com.example.technomarket.services;

import com.example.technomarket.model.dto.discounts.DiscountProductsDTO;
import com.example.technomarket.model.dto.discounts.RequestDiscountDTO;
import com.example.technomarket.model.dto.discounts.ResponseDiscountDTO;
import com.example.technomarket.model.dto.notifications.SetNotificationDTO;
import com.example.technomarket.model.dto.product.ProductForClientDTO;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.exceptions.UnauthorizedException;
import com.example.technomarket.model.pojo.Discount;
import com.example.technomarket.model.pojo.Notification;
import com.example.technomarket.model.pojo.Product;
import com.example.technomarket.model.pojo.User;
import com.example.technomarket.model.repository.DiscountRepository;
import com.example.technomarket.model.repository.NotificationRepository;
import com.example.technomarket.model.repository.ProductRepository;
import com.example.technomarket.model.repository.UserRepository;
import com.example.technomarket.util.CurrentUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private CurrentUser currentUser;
    @Autowired
    private NotificationRepository notificationRepository;
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

        if(!currentUser.isAdmin()){
            throw new UnauthorizedException("You don`t have permission for this operation!");
        }

        List<Long> productIds = discountProductsDTO.getProducts();
        int discountPercent = discountProductsDTO.getDiscountPercent();
        String discountDescription = discountProductsDTO.getDiscountDescription();

        Optional<Discount> discountOptional = discountRepository.findByDiscountDescriptionAndDiscountPercent(discountDescription,discountPercent);
        if(discountOptional.isPresent()){
            Discount discount = discountOptional.get();
            for(Long pid : productIds){
                Product p = getProduct(pid);
                List<User> usersSubscribed = p.getUsersSubscribed();
                for (User user : usersSubscribed) {
                    Notification map = modelMapper.map(new SetNotificationDTO(setMessageToNotify(p, discount), LocalDateTime.now()), Notification.class);
                    map.getNotifiedUser().add(user);
                    user.getNotifications().add(map);
                    notificationRepository.save(map);
                    userRepository.save(user);
                }
                p.setDiscount(discount);
                productRepository.save(p);
            }

            for (Long productId : discountProductsDTO.getProducts()) {
                Product product = productRepository.findById(productId).get();
                for (User user : product.getUsersSubscribed()) {
                    sendEmail(user, product, discountPercent);
                }
            }

            return modelMapper.map(discountOptional.get(),ResponseDiscountDTO.class);
        }
        else{
            throw new BadRequestException("This discount does not exists");
        }
    }

    private void sendEmail(User user, Product product, int discountPercent) {
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
    }

    private String setMessageToNotify(Product p, Discount discount) {
        return p.getName() + " is on sale until " + discount.getEndedAt().toString();
    }
}
