package com.example.technomarket.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserWithoutPasswordDTO {
    private long id;
    private String email;
    private String firstName;
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
}
