package com.example.technomarket.contoller;

import com.example.technomarket.model.dto.errors.ErrorDTO;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.exceptions.NotFoundException;
import com.example.technomarket.model.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

public abstract class AbstractController {
    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorDTO notFoundHandler(Exception exception){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setStatus(HttpStatus.NOT_FOUND.value());
        errorDTO.setMessage(exception.getMessage());
        errorDTO.setTime(LocalDate.now());
        return errorDTO;
    }

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorDTO badRequestHandler(Exception exception){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDTO.setMessage(exception.getMessage());
        errorDTO.setTime(LocalDate.now());
        return errorDTO;
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ErrorDTO unauthorizedExceptionHandler(Exception exception){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorDTO.setMessage(exception.getMessage());
        errorDTO.setTime(LocalDate.now());
        return errorDTO;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO allOthersExceptionHandler(Exception exception){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDTO.setMessage(exception.getMessage());
        errorDTO.setTime(LocalDate.now());
        return errorDTO;
    }

}
