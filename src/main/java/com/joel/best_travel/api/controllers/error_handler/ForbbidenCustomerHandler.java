package com.joel.best_travel.api.controllers.error_handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.joel.best_travel.api.models.response.BaseErrorResponse;
import com.joel.best_travel.api.models.response.ErrorResponse;
import com.joel.best_travel.util.exceptions.ForbiddenCustomerException;

@RestControllerAdvice
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbbidenCustomerHandler {
    
    @ExceptionHandler(ForbiddenCustomerException.class)
    public BaseErrorResponse handleIdNotFound(ForbiddenCustomerException exception){
        return ErrorResponse.builder()
            .message(exception.getMessage())
            .status(HttpStatus.FORBIDDEN.name())
            .code(HttpStatus.FORBIDDEN.value())
            .build();
    }
}
