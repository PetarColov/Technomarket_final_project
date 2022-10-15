package com.example.technomarket.model.dto;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class RegisterDto {

    private String username;

    private String password;

    private String confirmPassword;

    private String email;

    private String firstName;

    private String lastName;

    private String address;

    private String birthDate;
}
