package com.example.technomarket.services;

import com.example.technomarket.model.dto.UserRegisterDTO;
import com.example.technomarket.model.dto.UserWithoutPasswordDTO;
import com.example.technomarket.model.pojo.User;
import com.example.technomarket.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean validateData(UserRegisterDTO dto) {

        boolean isEmailValid = checkEmail(dto);
        boolean checkIfTwoPasswordsMatch = checkPassword(dto);



    }

    private boolean checkEmail(UserRegisterDTO dto){
        String userEmail = dto.getEmail();
        Optional<User> byEmail = userRepository.findByEmail(userEmail);
        String regex = "^[a-zA-Z0-9_+&*-] + (?:\\\\.[a-zA-Z0-9_+&*-]\n" +
                "+ )*@(?:[a-zA-Z0-9-]+\\\\.) + [a-zA-Z]{2,7}$ ";
        if(!byEmail.isPresent() && Pattern.compile(regex).matcher(userEmail).matches()){
            return true;
        }
        return false;
    }

    private boolean checkPassword(UserRegisterDTO dto){
        String password = dto.getPassword();
        if(password.equals(dto.getConfirmPassword()) && password.length() >= 8 ){
            return true;
        }
        return false;
    }

}
