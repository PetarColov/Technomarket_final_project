package com.example.technomarket.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class LoginDTO {

    @Email
    private String email;

    @Size(min = 8, max = 20)
    private String password;
}
