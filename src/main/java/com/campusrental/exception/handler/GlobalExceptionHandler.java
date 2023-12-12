package com.campusrental.exception.handler;

import com.campusrental.dto.ErrorResponseDTO;
import com.campusrental.exception.CapacityExceededException;
import com.campusrental.exception.PropertyNotFoundException;
import com.campusrental.exception.PropertyValidationException;
import com.campusrental.exception.TenantNotFoundException;
import graphql.GraphQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(PropertyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO getPropertyNotFoundException(PropertyNotFoundException exception){
      log.error("getPropertyNotFoundException {}", exception.getMessage());
      return ErrorResponseDTO.builder()
              .status(HttpStatus.NOT_FOUND.value())
              .message(exception.getMessage())
              .build();
    }

    @ExceptionHandler(TenantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO getTenantNotFoundException(TenantNotFoundException exception){
        log.error("getTenantNotFoundException {}",exception.getMessage());
        return ErrorResponseDTO.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(CapacityExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO getCapacityExceededException(CapacityExceededException exception){
        log.error("getCapacityExceededException {}",exception.getMessage());
        return ErrorResponseDTO.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(PropertyValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  ErrorResponseDTO getPropertyValidationException(PropertyValidationException  exception)
    {
        log.error("getPropertyValidationException {}",exception.getMessage());
        return ErrorResponseDTO.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
    }

}
