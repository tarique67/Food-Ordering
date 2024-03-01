package com.swiggy.userService.entities;

import com.swiggy.userService.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Admin extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer adminId;

    public Admin(String name, String password, Integer adminId) {
        super(name, password, Role.ADMIN);
        this.adminId = adminId;
    }
}
