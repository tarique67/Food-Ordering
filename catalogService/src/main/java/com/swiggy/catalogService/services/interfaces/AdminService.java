package com.swiggy.catalogService.services.interfaces;

import com.swiggy.catalogService.entities.Admin;
import com.swiggy.catalogService.exceptions.UsernameTakenException;

public interface AdminService {
    Admin register(Admin customer) throws UsernameTakenException;
}
