package com.swiggy.orderService.exceptions;

public class ItemNotInRestaurantException extends Exception{

    public ItemNotInRestaurantException(String message) {
        super(message);
    }
}
