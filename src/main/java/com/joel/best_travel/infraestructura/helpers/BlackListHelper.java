package com.joel.best_travel.infraestructura.helpers;

import org.springframework.stereotype.Component;

import com.joel.best_travel.util.exceptions.ForbiddenCustomerException;

@Component
public class BlackListHelper {
    
    public void isInBlackListCustomer(String customerId){
        if(customerId.equals("GOTW771012HMRGR087")){
            throw new ForbiddenCustomerException();
        }
    }
}
