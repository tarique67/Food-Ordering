package com.swiggy.orderService.controllers;

import com.swiggy.orderService.entities.Customer;
import com.swiggy.orderService.exceptions.UsernameTakenException;
import com.swiggy.orderService.services.interfaces.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomersController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> register(@RequestBody Customer customer) throws UsernameTakenException {
        Customer savedCustomer = customerService.register(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }
}
