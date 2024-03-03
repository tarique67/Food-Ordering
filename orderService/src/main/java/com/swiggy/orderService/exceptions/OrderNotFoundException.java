package com.swiggy.orderService.exceptions;

public class OrderNotFoundException extends Exception{

    public OrderNotFoundException(String message) {
        super(message);
    }
}
