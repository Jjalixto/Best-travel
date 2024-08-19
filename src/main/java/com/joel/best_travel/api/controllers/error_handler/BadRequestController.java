package com.joel.best_travel.api.controllers.error_handler;

import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.joel.best_travel.api.models.response.BaseErrorResponse;
import com.joel.best_travel.api.models.response.ErrorResponse;
import com.joel.best_travel.api.models.response.ErrorsResponse;
import com.joel.best_travel.util.exceptions.IdNotFoundException;

import java.util.ArrayList;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestController {
    
    @ExceptionHandler(IdNotFoundException.class)
    public BaseErrorResponse handleIdNotFound(IdNotFoundException exception){
        return ErrorResponse.builder()
            .message(exception.getMessage())
            .status(HttpStatus.BAD_REQUEST.name())
            .code(HttpStatus.BAD_REQUEST.value())
            .build();
            
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseErrorResponse handleIdNotFound(MethodArgumentNotValidException exception){
        var errors = new ArrayList<String>();
        exception.getAllErrors()
            .forEach(error -> errors.add(error.getDefaultMessage()));
        return ErrorsResponse.builder()
                .errors(errors)
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }
}
