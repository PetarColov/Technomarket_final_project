package com.example.technomarket.model.dto.errors;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ErrorDTO {
    private String message;
    private int status;
    private LocalDate time;
}
