package com.example.technomarket.model.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegisterDTO {

    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;
}
