package com.swiggy.orderService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorDetails> httpClientExceptionHandler(HttpClientErrorException exception, WebRequest re){
        ErrorDetails err = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), re.getDescription(false));

        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemNotInRestaurantException.class)
    public ResponseEntity<ErrorDetails> itemNotInRestaurantExceptionHandler(ItemNotInRestaurantException exception, WebRequest re){
        ErrorDetails err = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), re.getDescription(false));

        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameTakenException.class)
    public ResponseEntity<ErrorDetails> usernameTakenExceptionHandler(UsernameTakenException exception, WebRequest re){
        ErrorDetails err = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), re.getDescription(false));

        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetails> userNotFoundExceptionHandler(UserNotFoundException exception, WebRequest re){
        ErrorDetails err = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), re.getDescription(false));

        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDetails> orderNotFoundExceptionHandler(OrderNotFoundException exception, WebRequest re){
        ErrorDetails err = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), re.getDescription(false));

        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeliveryExecutiveNotFoundException.class)
    public ResponseEntity<ErrorDetails> deliveryExecutiveNotFoundException(DeliveryExecutiveNotFoundException exception, WebRequest re){
        ErrorDetails err = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), re.getDescription(false));

        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }

}
