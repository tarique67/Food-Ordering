package com.swiggy.catalogService.services;

import com.swiggy.catalogService.entities.Admin;
import com.swiggy.catalogService.exceptions.UsernameTakenException;
import com.swiggy.catalogService.repositories.AdminRepository;
import com.swiggy.catalogService.services.interfaces.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.swiggy.catalogService.constants.ExceptionMessages.USERNAME_TAKEN;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Admin register(Admin customer) throws UsernameTakenException {
        if(adminRepository.findByUserName(customer.getUserName()).isPresent())
            throw new UsernameTakenException(USERNAME_TAKEN);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return adminRepository.save(customer);
    }
}
