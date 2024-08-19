package com.joel.best_travel.util.exceptions;

public class ForbiddenCustomerException extends RuntimeException{
    
    public ForbiddenCustomerException(){
        super("This Customer is blocked");
    }
}
