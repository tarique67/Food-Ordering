package com.swiggy.orderService.services;

import com.swiggy.orderService.entities.Customer;
import com.swiggy.orderService.exceptions.UsernameTakenException;
import com.swiggy.orderService.repositories.CustomerRepository;
import com.swiggy.orderService.services.interfaces.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.swiggy.orderService.constants.ExceptionMessages.USERNAME_TAKEN;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Customer register(Customer customer) throws UsernameTakenException {
        if(customerRepository.findByUserName(customer.getUserName()).isPresent())
            throw new UsernameTakenException(USERNAME_TAKEN);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }
}
