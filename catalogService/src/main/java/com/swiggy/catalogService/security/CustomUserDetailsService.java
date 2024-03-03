package com.swiggy.catalogService.security;

import com.swiggy.catalogService.entities.Admin;
import com.swiggy.catalogService.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository customerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin user = customerRepository.findByUserName(username).orElseThrow(()-> new UsernameNotFoundException("No user found with name "+ username));
        return new CustomUserDetails(user);
    }
}
