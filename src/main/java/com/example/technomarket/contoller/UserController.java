package com.example.technomarket.contoller;


import com.example.technomarket.model.dto.RegisterDto;
import com.example.technomarket.model.pojo.User;
import com.example.technomarket.model.repository.UserRepository;
import com.example.technomarket.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper mapper;


    @PostMapping("/register")
    public User registerUser(@RequestBody RegisterDto dto){

        boolean isValid = userService.vaidateData(dto);

        User user = mapper.map(dto, User.class);

        if (isValid){
            userRepository.save(user);
            return user;
        }

        return null;
    }

}
