package com.swiggy.orderService.security;

import com.swiggy.orderService.entities.Customer;
import com.swiggy.orderService.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Customer user = customerRepository.findByUserName(username).orElseThrow(()-> new UsernameNotFoundException("No user found with name "+ username));
        return new CustomUserDetails(user);
    }
}
