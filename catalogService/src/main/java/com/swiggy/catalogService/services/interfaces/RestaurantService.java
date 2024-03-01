package com.swiggy.catalogService.services.interfaces;

import com.swiggy.catalogService.entities.Restaurant;
import com.swiggy.catalogService.exceptions.RestaurantNotFoundException;

import java.util.List;

public interface RestaurantService {

    Restaurant create(Restaurant restaurant);

    List<Restaurant> fetchAll();

    Restaurant fetchById(int restaurantId) throws RestaurantNotFoundException;
}
