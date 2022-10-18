package com.example.technomarket.contoller;


import com.example.technomarket.model.dto.UserRegisterDTO;
import com.example.technomarket.model.dto.UserWithoutPasswordDTO;
import com.example.technomarket.model.pojo.User;
import com.example.technomarket.model.repository.UserRepository;
import com.example.technomarket.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends AbstractController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper mapper;


    @PostMapping("/register")
    public UserWithoutPasswordDTO registerUser(@RequestBody UserRegisterDTO dto){



        return null;
    }

}
