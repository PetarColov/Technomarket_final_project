package com.example.technomarket.services;

import com.example.technomarket.model.dto.LoginDTO;
import com.example.technomarket.model.dto.UserRegisterDTO;
import com.example.technomarket.model.dto.UserWithoutPasswordDTO;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.exceptions.UnauthorizedException;
import com.example.technomarket.model.pojo.User;
import com.example.technomarket.model.repository.UserRepository;
import com.example.technomarket.util.CurrentUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private CurrentUser currentUser;

    public UserWithoutPasswordDTO validateData(UserRegisterDTO dto) {

         if(!checkEmail(dto)){
             throw new BadRequestException("Incorrect email!");
         }

         if(!checkPassword(dto)){
             throw new BadRequestException("Password mismatch");
         }

         if(!checkAge(dto)){
             throw new BadRequestException("Invalid age!");
         }

         if (!checkPasswordLength(dto)){
             throw new BadRequestException("Invalid password length");
         }

         dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
         User user = mapper.map(dto, User.class);
         userRepository.save(user);
         return mapper.map(user, UserWithoutPasswordDTO.class);
    }

    private boolean checkPasswordLength(UserRegisterDTO dto) {
        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();
        if (password.length() < 8 || confirmPassword.length() < 8){
            return false;
        }
        return true;
    }

    private boolean checkEmail(UserRegisterDTO dto){
        String userEmail = dto.getEmail();
        Optional<User> byEmail = userRepository.findByEmail(userEmail);
        String regex = "^(.+)@(.+)$";
        if(!byEmail.isPresent() && Pattern.compile(regex).matcher(userEmail).matches()){
            return true;
        }
        return false;
    }

    private boolean checkPassword(UserRegisterDTO dto){
        String password = dto.getPassword();
        if(password.equals(dto.getConfirmPassword())){
            return true;
        }
        return false;
    }

    private boolean checkAge(UserRegisterDTO dto){
        if(LocalDate.now().getYear() - dto.getDateOfBirth().getYear() >= 18){
            return true;
        }
        return false;
    }

    public UserWithoutPasswordDTO login(LoginDTO loginDTO) {
        Optional<User> user = userRepository
                .findByEmail(loginDTO.getEmail());

        if (user.isPresent()){
            if (bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.get().getPassword())){
                currentUser.login(user.get());
                return mapper.map(user, UserWithoutPasswordDTO.class);
            }
        }
        throw new BadRequestException("User not found!");
    }

    public void logout() {
        currentUser.logout();
    }
}
