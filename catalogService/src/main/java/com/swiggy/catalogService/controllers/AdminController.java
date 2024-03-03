package com.swiggy.catalogService.controllers;

import com.swiggy.catalogService.entities.Admin;
import com.swiggy.catalogService.exceptions.UsernameTakenException;
import com.swiggy.catalogService.services.interfaces.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping
    public ResponseEntity<Admin> register(@RequestBody Admin admin) throws UsernameTakenException {
        Admin savedAdmin = adminService.register(admin);
        return new ResponseEntity<>(savedAdmin, HttpStatus.CREATED);
    }
}
