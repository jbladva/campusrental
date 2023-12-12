package com.rental.agency.exception;

public class UserUnAuthorizedException extends RuntimeException{
    public  UserUnAuthorizedException(String message){super(message);}
}
