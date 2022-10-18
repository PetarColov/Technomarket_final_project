package com.example.technomarket.model.dto;


import lombok.Data;

@Data
public class UserRegisterDTO {

    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
}
