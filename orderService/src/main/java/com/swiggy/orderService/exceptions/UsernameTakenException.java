package com.swiggy.orderService.exceptions;

public class UsernameTakenException extends Exception{

    public UsernameTakenException(String message) {
        super(message);
    }
}
