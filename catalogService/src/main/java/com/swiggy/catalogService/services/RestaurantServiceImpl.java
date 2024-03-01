package com.swiggy.catalogService.services;

import com.swiggy.catalogService.entities.Restaurant;
import com.swiggy.catalogService.exceptions.RestaurantNotFoundException;
import com.swiggy.catalogService.repositories.RestaurantRepository;
import com.swiggy.catalogService.services.interfaces.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.swiggy.catalogService.constants.ExceptionMessages.RESTAURANT_NOT_FOUND;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Restaurant create(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public List<Restaurant> fetchAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant fetchById(int restaurantId) throws RestaurantNotFoundException {
        return restaurantRepository.findById(restaurantId).orElseThrow(()-> new RestaurantNotFoundException(RESTAURANT_NOT_FOUND));
    }
}
