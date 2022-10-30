package com.example.technomarket.services;

import com.example.technomarket.model.dto.orders.FinishOrderDTO;
import com.example.technomarket.model.dto.orders.OrderCredentialsDTO;
import com.example.technomarket.model.dto.product.ProductFinishOrderDTO;
import com.example.technomarket.model.dto.user.UserWithoutPasswordDTO;
import com.example.technomarket.model.pojo.Cart;
import com.example.technomarket.model.pojo.Product;
import com.example.technomarket.model.pojo.User;
import com.example.technomarket.model.repository.CartRepository;
import com.example.technomarket.model.repository.UserRepository;
import com.example.technomarket.util.CurrentUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CurrentUser currentUser;
    @Autowired
    private ModelMapper mapper;


    @Transactional
    public FinishOrderDTO finishOrder(OrderCredentialsDTO credentialsDTO) {
        User user = userRepository.findById(currentUser.getId()).get();

        user.setAddress(credentialsDTO.getAddress());

        List<Cart> allByUserId = cartRepository.findAllByUserId(currentUser.getId());

        FinishOrderDTO finishOrderDTO = new FinishOrderDTO();

        double sum = 0;

        for (Cart cart : allByUserId) {
            int quantity = cart.getQuantity();
            double price = cart.getProduct().getPrice().doubleValue();
            sum += (quantity*price);
        }

//        finishOrderDTO.setProducts(allByUserId.stream().map(c -> mapper.map(c.getProduct(), ProductWithNameDTO.class)).toList());

        List<ProductFinishOrderDTO> list = new ArrayList<>();

        for (Cart cart : allByUserId) {
            Product product = cart.getProduct();
            int quantity = cart.getQuantity();

            list.add(new ProductFinishOrderDTO(product.getName(), product.getPrice(), quantity));

        }

        finishOrderDTO.setProducts(list);

        finishOrderDTO.setUserWithoutPasswordDTO(mapper.map(user, UserWithoutPasswordDTO.class));

        BigDecimal totalPrice = new BigDecimal(sum);

        finishOrderDTO.setPriceSum(totalPrice);

        user.getCartUser().clear();

        cartRepository.removeCartByUserId(user);

        userRepository.save(user);

        return finishOrderDTO;
    }
}
