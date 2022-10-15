package com.example.technomarket.services;

import com.example.technomarket.model.dto.RegisterDto;
import com.example.technomarket.model.pojo.User;
import com.example.technomarket.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean vaidateData(RegisterDto dto) {
        Optional<User> byUsername = userRepository.findByUsername(dto.getUsername());
        if (byUsername.isPresent()){
            return false;
        }

        Optional<User> byEmail = userRepository.findByEmail(dto.getEmail());
        if (byEmail.isPresent()){
            return false;
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())){
            return false;
        }
        return true;
    }
}
