package com.swiggy.catalogService.serviceTests;

import com.swiggy.catalogService.entities.Item;
import com.swiggy.catalogService.entities.Menu;
import com.swiggy.catalogService.entities.Restaurant;
import com.swiggy.catalogService.enums.ItemStatus;
import com.swiggy.catalogService.exceptions.RestaurantNotFoundException;
import com.swiggy.catalogService.repositories.RestaurantRepository;
import com.swiggy.catalogService.services.RestaurantServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Test
    void testRestaurantCreation_ExpectSuccessful() {
        Menu menu = new Menu(1, List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE)));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        restaurantService.create(restaurant);

        verify(restaurantRepository, times(1)).save(restaurant);
    }

    @Test
    void testGetAllRestaurants_Expect1Restaurant() {
        Menu menu = new Menu(1, List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE)));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));

        restaurantService.fetchAll();

        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    void testGetAllRestaurants_Expect2Restaurants() {
        Menu firstMenu = new Menu(1, List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE)));
        Restaurant firstRestaurant = new Restaurant(1, "JFC", "Ranchi", firstMenu);
        Menu secondMenu = new Menu(1, List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE), new Item(2, "Krushers", 280.0, ItemStatus.AVAILABLE)));
        Restaurant secondRestaurant = new Restaurant(1, "KFC", "Ranchi", secondMenu);
        when(restaurantRepository.findAll()).thenReturn(List.of(firstRestaurant,secondRestaurant));

        List<Restaurant> restaurants = restaurantService.fetchAll();

        verify(restaurantRepository, times(1)).findAll();
        assertEquals(2, restaurants.size());
    }

    @Test
    void testGetRestaurantById_ExpectSuccessful() throws RestaurantNotFoundException {
        Menu firstMenu = new Menu(1, List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE)));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", firstMenu);
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));

        restaurantService.fetchById(1);

        verify(restaurantRepository, times(1)).findById(1);
    }

    @Test
    void testGetRestaurantById_ExpectRestaurantNotFoundException() throws RestaurantNotFoundException {
        when(restaurantRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class,()-> restaurantService.fetchById(1));
    }
}
