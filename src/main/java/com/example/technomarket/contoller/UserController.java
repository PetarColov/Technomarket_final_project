package com.example.technomarket.contoller;


import com.example.technomarket.model.dto.user.*;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.repository.UserRepository;
import com.example.technomarket.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
        UserWithoutPasswordDTO userWithoutPass = userService.validateData(dto);
        if(userWithoutPass != null){
            return userWithoutPass;
        }
        else{
            throw new BadRequestException("Wrong credentials!");
        }
    }

    @PostMapping("/login")
    public UserWithoutPasswordDTO loginUser(@RequestBody @Valid LoginDTO loginDTO, BindingResult bindingResult){

        if (!bindingResult.hasErrors()){
            return userService.login(loginDTO);
        }
        throw new BadRequestException("Enter valid credentials!");
    }

    @PostMapping("/logout")
    public void logoutUser(){
        userService.logout();
    }


    @PostMapping("/edit_profile")
    public UserWithoutPasswordDTO editUser(@RequestBody EditUserDTO editUserDTO){
        return userService.editUser(editUserDTO);
    }

    @PostMapping("/change_password")
    public UserWithoutPasswordDTO changePassword(@RequestBody ChangePasswordDTO changePasswordDTO){
        return userService.changePassword(changePasswordDTO);
    }

}
