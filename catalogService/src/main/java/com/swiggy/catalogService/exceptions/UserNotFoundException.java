package com.swiggy.catalogService.exceptions;

public class UserNotFoundException extends  Exception{

    public UserNotFoundException(String message) {
        super(message);
    }
}
