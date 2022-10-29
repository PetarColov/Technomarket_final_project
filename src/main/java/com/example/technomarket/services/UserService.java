package com.example.technomarket.services;

import com.example.technomarket.model.dto.user.*;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.exceptions.UnauthorizedException;
import com.example.technomarket.model.pojo.Cart;
import com.example.technomarket.model.pojo.User;
import com.example.technomarket.model.repository.CartRepository;
import com.example.technomarket.model.repository.UserRepository;
import com.example.technomarket.util.CurrentUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private JavaMailSender mailSender;

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
         if(isFirstUser()){
            user.setAdmin(true);
         }
         else{
             user.setAdmin(false);
         }
         userRepository.save(user);

        try {
            sendEmail(user);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new BadRequestException("Something with sending email went wrong!");
        }

        return mapper.map(user, UserWithoutPasswordDTO.class);
    }

    private void sendEmail(User user) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "technomarketfinalproject@gmail.com";
        String subject = "Registration";
        String content = "Dear " + user.getFirstName() + " " + user.getLastName() + ", you have completed your registration!";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toAddress);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }

    private boolean isFirstUser() {
        return userRepository.count() == 0;
    }

    private boolean checkPasswordLength(UserRegisterDTO dto) {
        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();
        return ((password.length() > 8 && password.length() < 20 )|| (confirmPassword.length() > 8 && confirmPassword.length() < 20));
    }

    private boolean checkEmail(UserRegisterDTO dto){
        String userEmail = dto.getEmail();
        Optional<User> byEmail = userRepository.findByEmail(userEmail);
        String regex = "^(.+)@(.+)$";
        return (!byEmail.isPresent() && Pattern.compile(regex).matcher(userEmail).matches());
    }

    private boolean checkPassword(UserRegisterDTO dto){
        String password = dto.getPassword();
        return (password.equals(dto.getConfirmPassword()));
    }

    private boolean checkAge(UserRegisterDTO dto){
        return (LocalDate.now().getYear() - dto.getDateOfBirth().getYear() >= 18);
    }

    public UserWithoutPasswordDTO login(LoginDTO loginDTO) {
        Optional<User> user = userRepository
                .findByEmail(loginDTO.getEmail());

        if (user.isPresent()){
            if (bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.get().getPassword())){
                currentUser.login(user.get());

                List<Cart> cartByUserId = cartRepository.findAllByUserId(currentUser.getId());

                Set<Cart> collect = new HashSet<>(cartByUserId);

                currentUser.setCartUser(collect);
                return mapper.map(user, UserWithoutPasswordDTO.class);
            }
        }
        throw new BadRequestException("User not found!");
    }

    public void logout() {
        currentUser.logout();
    }

    public UserWithoutPasswordDTO editUser(EditUserDTO editUserDTO) {

        if (currentUser.getId() == null){
            throw new UnauthorizedException("User not logged in!");
        }

        User byId = userRepository.findById(currentUser.getId()).get();

        byId.setEmail(editUserDTO.getEmail());
        byId.setFirstName(editUserDTO.getFirstName());
        byId.setLastName(editUserDTO.getLastName());

        userRepository.save(byId);

        return mapper.map(byId, UserWithoutPasswordDTO.class);
    }

    public UserWithoutPasswordDTO changePassword(ChangePasswordDTO changePasswordDTO) {
        if (currentUser.getId() == null){
            throw new UnauthorizedException("User not logged in!");
        }

        User byId = userRepository.findById(currentUser.getId()).get();

        if (!bCryptPasswordEncoder.matches(changePasswordDTO.getOldPassword(), byId.getPassword())){
            throw new BadRequestException("Old password is wrong!");
        }

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())){
            throw new BadRequestException("Passwords does not match!");
        }

        byId.setPassword(bCryptPasswordEncoder.encode(changePasswordDTO.getNewPassword()));

        userRepository.save(byId);

        return mapper.map(byId, UserWithoutPasswordDTO.class);

    }
}
