package com.swiggy.catalogService.exceptions;

public class RestaurantNotFoundException extends Exception{

    public RestaurantNotFoundException(String message) {
        super(message);
    }
}
