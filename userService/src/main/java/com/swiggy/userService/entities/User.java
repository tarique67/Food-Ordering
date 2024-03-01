package com.swiggy.userService.entities;

import com.swiggy.userService.enums.Role;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class User {

    private String name;
    private String password;
    private Role role;

}
