package com.swiggy.catalogService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorDetails> restaurantNotFoundExceptionHandler(RestaurantNotFoundException exception, WebRequest re){
        ErrorDetails err = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), re.getDescription(false));

        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MenuNotFoundException.class)
    public ResponseEntity<ErrorDetails> menuNotFoundExceptionHandler(MenuNotFoundException exception, WebRequest re){
        ErrorDetails err = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), re.getDescription(false));

        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RestaurantMenuMismatchException.class)
    public ResponseEntity<ErrorDetails> restaurantMenuMismatchExceptionHandler(RestaurantMenuMismatchException exception, WebRequest re){
        ErrorDetails err = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), re.getDescription(false));

        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPriceException.class)
    public ResponseEntity<ErrorDetails> invalidPriceExceptionHandler(InvalidPriceException exception, WebRequest re){
        ErrorDetails err = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), re.getDescription(false));

        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorDetails> itemNotFoundExceptionHandler(ItemNotFoundException exception, WebRequest re){
        ErrorDetails err = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), re.getDescription(false));

        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> illegalArgumentExceptionHandler(IllegalArgumentException exception, WebRequest re){
        ErrorDetails err = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), re.getDescription(false));

        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }
}
