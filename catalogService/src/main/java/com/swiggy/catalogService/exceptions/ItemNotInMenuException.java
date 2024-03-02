package com.swiggy.catalogService.exceptions;

public class ItemNotInMenuException extends Exception{

    public ItemNotInMenuException(String message) {
        super(message);
    }
}
