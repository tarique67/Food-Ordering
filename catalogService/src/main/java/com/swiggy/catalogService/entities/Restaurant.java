package com.swiggy.catalogService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer restaurantId;

    private String restaurantName;
    private String address;

    @OneToOne(cascade = CascadeType.ALL)
    private Menu menu;

    public Restaurant(String restaurantName, String address, Menu menu) {
        this.restaurantName = restaurantName;
        this.address = address;
        this.menu = menu;
    }
}
