package com.swiggy.catalogService.controllers;

import com.swiggy.catalogService.entities.Restaurant;
import com.swiggy.catalogService.exceptions.RestaurantNotFoundException;
import com.swiggy.catalogService.services.interfaces.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantsController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant){
        Restaurant savedRestaurant = restaurantService.create(restaurant);
        return new ResponseEntity<>(savedRestaurant, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> fetchAll(){
        List<Restaurant> restaurants = restaurantService.fetchAll();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping("/{restaurant-id}")
    public ResponseEntity<Restaurant> fetchById(@PathVariable("restaurant-id") int restaurantId) throws RestaurantNotFoundException {
        Restaurant restaurant = restaurantService.fetchById(restaurantId);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

}
