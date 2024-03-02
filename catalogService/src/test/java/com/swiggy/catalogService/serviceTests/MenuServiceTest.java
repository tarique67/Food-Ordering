package com.swiggy.catalogService.serviceTests;

import com.swiggy.catalogService.entities.Item;
import com.swiggy.catalogService.entities.Menu;
import com.swiggy.catalogService.entities.Restaurant;
import com.swiggy.catalogService.enums.ItemStatus;
import com.swiggy.catalogService.exceptions.MenuNotFoundException;
import com.swiggy.catalogService.exceptions.RestaurantMenuMismatchException;
import com.swiggy.catalogService.exceptions.RestaurantNotFoundException;
import com.swiggy.catalogService.repositories.MenuRepository;
import com.swiggy.catalogService.repositories.RestaurantRepository;
import com.swiggy.catalogService.services.MenuServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuServiceImpl menuService;

    @Test
    void testAddItemsToMenu_ExpectSuccessful() throws MenuNotFoundException, RestaurantMenuMismatchException, RestaurantNotFoundException {
        Menu menu = new Menu(1, new ArrayList<>(List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE))));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        List<Item> itemsToAdd = new ArrayList<>(List.of(new Item(2, "Jings", 180.0, ItemStatus.AVAILABLE)));
        Menu menuReturned = new Menu(1, new ArrayList<>(List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE), new Item(2, "Jings", 180.0, ItemStatus.AVAILABLE))));
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));

        menuService.addItems(1,1,itemsToAdd);

        assertEquals(menu, menuReturned);
        verify(menuRepository, times(1)).save(menuReturned);
    }

    @Test
    void testAddItemsToMenu_ExpectRestaurantNotFoundException() throws MenuNotFoundException, RestaurantMenuMismatchException, RestaurantNotFoundException {
        Menu menu = new Menu(1, new ArrayList<>(List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE))));
        List<Item> itemsToAdd = new ArrayList<>(List.of(new Item(2, "Jings", 180.0, ItemStatus.AVAILABLE)));
        when(restaurantRepository.findById(1)).thenReturn(Optional.empty());
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));

        assertThrows(RestaurantNotFoundException.class,()-> menuService.addItems(1,1,itemsToAdd));
    }

    @Test
    void testAddItemsToMenu_ExpectMenuNotFoundException() throws MenuNotFoundException, RestaurantMenuMismatchException, RestaurantNotFoundException {
        Menu menu = new Menu(1, new ArrayList<>(List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE))));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        List<Item> itemsToAdd = new ArrayList<>(List.of(new Item(2, "Jings", 180.0, ItemStatus.AVAILABLE)));
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(MenuNotFoundException.class,()-> menuService.addItems(1,2,itemsToAdd));
    }

    @Test
    void testAddItemsToMenu_ExpectRestaurantMenuMismatchException() throws MenuNotFoundException, RestaurantMenuMismatchException, RestaurantNotFoundException {
        Menu firstMenu = new Menu(1, new ArrayList<>(List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE))));
        Menu secondMenu = new Menu(2, new ArrayList<>(List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE))));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", firstMenu);
        List<Item> itemsToAdd = new ArrayList<>(List.of(new Item(2, "Jings", 180.0, ItemStatus.AVAILABLE)));
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(2)).thenReturn(Optional.of(secondMenu));

        assertThrows(RestaurantMenuMismatchException.class,()-> menuService.addItems(1,2,itemsToAdd));
    }

    @Test
    void testFetchMenu_ExpectSuccessful() throws MenuNotFoundException, RestaurantMenuMismatchException, RestaurantNotFoundException {
        Menu menu = new Menu(1, new ArrayList<>(List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE))));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));

        menuService.fetch(1,1);

        verify(menuRepository, times(1)).findById(1);
        verify(restaurantRepository, times(1)).findById(1);
    }

    @Test
    void testFetchMenu_ExpectRestaurantNotFoundException() throws MenuNotFoundException, RestaurantMenuMismatchException, RestaurantNotFoundException {
        Menu menu = new Menu(1, new ArrayList<>(List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE))));
        when(restaurantRepository.findById(1)).thenReturn(Optional.empty());
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));

        assertThrows(RestaurantNotFoundException.class,()-> menuService.fetch(1,1));
    }

    @Test
    void testFetchMenu_ExpectMenuNotFoundException() throws MenuNotFoundException, RestaurantMenuMismatchException, RestaurantNotFoundException {
        Menu menu = new Menu(2, new ArrayList<>(List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE))));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(MenuNotFoundException.class,()-> menuService.fetch(1,1));
    }

    @Test
    void testFetchMenu_ExpectRestaurantMenuMismatchException() throws MenuNotFoundException, RestaurantMenuMismatchException, RestaurantNotFoundException {
        Menu menu = new Menu(1, new ArrayList<>(List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE))));
        Menu secondMenu = new Menu(2, new ArrayList<>(List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE))));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(2)).thenReturn(Optional.of(secondMenu));

        assertThrows(RestaurantMenuMismatchException.class,()-> menuService.fetch(1,2));
    }
}
