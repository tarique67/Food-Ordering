package com.swiggy.catalogService.exceptions;

public class UsernameTakenException extends Exception{

    public UsernameTakenException(String message) {
        super(message);
    }
}
