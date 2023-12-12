package com.rental.agency.exception;

public class UserNotFoundException extends RuntimeException{
   public UserNotFoundException(String message)
   {super(message);}
}
