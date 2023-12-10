package com.campusrental.exception;

public class CapacityExceededException extends RuntimeException {

    public CapacityExceededException(String message){
        super(message);
    }
}
