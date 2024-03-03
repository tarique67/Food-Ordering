package com.swiggy.orderService.services.interfaces;

import com.swiggy.orderService.entities.Customer;
import com.swiggy.orderService.exceptions.UsernameTakenException;

public interface CustomerService {

    Customer register(Customer customer) throws UsernameTakenException;
}
