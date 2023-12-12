package com.rental.agency.exception;

public class CapacityExceededException extends RuntimeException {

    public CapacityExceededException(String message){
        super(message);
    }
}
