package com.rental.agency.exception;

public class TenantNotFoundException extends RuntimeException{

    public TenantNotFoundException(String message){
        super(message);
    }
}
