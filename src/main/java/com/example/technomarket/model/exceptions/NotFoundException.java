package com.example.technomarket.model.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String msg){
        super(msg);
    }
}
