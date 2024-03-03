package com.swiggy.orderService.clients.catalogServiceClients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.orderService.clients.catalogServiceClients.responseModels.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class CatalogServiceClient {

    private static String BASE_URL = "http://localhost:8091/api/v1/restaurants";

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    public Restaurant fetchRestaurantFromCatalogService(int restaurantId) throws JsonProcessingException {
        String fetchMenuURL = BASE_URL+ "/" + restaurantId;
        String response = restTemplate.getForObject(fetchMenuURL, String.class);
        Restaurant restaurant = objectMapper.readValue(response, Restaurant.class);
        return restaurant;
    }
}
