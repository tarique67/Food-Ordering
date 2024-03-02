package com.swiggy.catalogService.serviceTests;

import com.swiggy.catalogService.entities.Item;
import com.swiggy.catalogService.entities.Menu;
import com.swiggy.catalogService.entities.Restaurant;
import com.swiggy.catalogService.enums.ItemStatus;
import com.swiggy.catalogService.exceptions.*;
import com.swiggy.catalogService.repositories.ItemRepository;
import com.swiggy.catalogService.repositories.MenuRepository;
import com.swiggy.catalogService.repositories.RestaurantRepository;
import com.swiggy.catalogService.services.ItemServiceImpl;
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
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void testUpdatePrice_ExpectSuccessful() throws InvalidPriceException, MenuNotFoundException, ItemNotInMenuException, RestaurantMenuMismatchException, RestaurantNotFoundException, ItemNotFoundException {
        Item item = new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE);
        Menu menu = new Menu(1, new ArrayList<>(List.of(item)));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        Item returnedItem = new Item(1, "Wings", 200.0, ItemStatus.AVAILABLE);
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        itemService.updatePrice(1,1,1, 200.0);

        assertEquals(returnedItem, item);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void testUpdatePrice_ExpectRestaurantNotFoundException() throws InvalidPriceException, MenuNotFoundException, ItemNotInMenuException, RestaurantMenuMismatchException, RestaurantNotFoundException, ItemNotFoundException {
        when(restaurantRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class,()-> itemService.updatePrice(1,1,1, 200.0));
    }

    @Test
    void testUpdatePrice_ExpectMenuNotFoundException() throws InvalidPriceException, MenuNotFoundException, ItemNotInMenuException, RestaurantMenuMismatchException, RestaurantNotFoundException, ItemNotFoundException {
        Menu menu = new Menu(1, new ArrayList<>());
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(MenuNotFoundException.class,()-> itemService.updatePrice(1,1,1, 200.0));
    }

    @Test
    void testUpdatePrice_ExpectItemNotFoundException() throws InvalidPriceException, MenuNotFoundException, ItemNotInMenuException, RestaurantMenuMismatchException, RestaurantNotFoundException, ItemNotFoundException {
        Menu menu = new Menu(1, new ArrayList<>());
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));
        when(itemRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class,()-> itemService.updatePrice(1,1,1, 200.0));
    }

    @Test
    void testUpdatePrice_ExpectRestaurantMenuMismatchException() throws InvalidPriceException, MenuNotFoundException, ItemNotInMenuException, RestaurantMenuMismatchException, RestaurantNotFoundException, ItemNotFoundException {
        Item item = new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE);
        Menu menu = new Menu(1, new ArrayList<>(List.of(item)));
        Menu secondMenu = new Menu(1, new ArrayList<>());
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", secondMenu);
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        assertThrows(RestaurantMenuMismatchException.class, ()-> itemService.updatePrice(1,1,1, 200.0));
    }

    @Test
    void testUpdatePrice_ExpectItemNotInMenuException() throws InvalidPriceException, MenuNotFoundException, ItemNotInMenuException, RestaurantMenuMismatchException, RestaurantNotFoundException, ItemNotFoundException {
        Item item = new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE);
        Item secondItem =  new Item(2, "JINGS", 200.0, ItemStatus.AVAILABLE);
        Menu menu = new Menu(1, new ArrayList<>(List.of(item)));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));
        when(itemRepository.findById(2)).thenReturn(Optional.of(secondItem));

        assertThrows(ItemNotInMenuException.class, ()-> itemService.updatePrice(1,1,2, 200.0));
    }

    @Test
    void testUpdateStatus_ExpectSuccessful() throws InvalidPriceException, MenuNotFoundException, ItemNotInMenuException, RestaurantMenuMismatchException, RestaurantNotFoundException, ItemNotFoundException {
        Item item = new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE);
        Menu menu = new Menu(1, new ArrayList<>(List.of(item)));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        Item returnedItem = new Item(1, "Wings", 180.0, ItemStatus.OUT_OF_STOCK);
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        itemService.updateStatus(1,1,1, ItemStatus.OUT_OF_STOCK);

        assertEquals(returnedItem, item);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void testUpdateStatus_ExpectRestaurantNotFoundException() throws InvalidPriceException, MenuNotFoundException, ItemNotInMenuException, RestaurantMenuMismatchException, RestaurantNotFoundException, ItemNotFoundException {
        when(restaurantRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class,()-> itemService.updateStatus(1,1,1, ItemStatus.OUT_OF_STOCK));
    }

    @Test
    void testUpdateStatus_ExpectMenuNotFoundException() throws InvalidPriceException, MenuNotFoundException, ItemNotInMenuException, RestaurantMenuMismatchException, RestaurantNotFoundException, ItemNotFoundException {
        Menu menu = new Menu(1, new ArrayList<>());
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(MenuNotFoundException.class,()-> itemService.updateStatus(1,1,1, ItemStatus.OUT_OF_STOCK));
    }

    @Test
    void testUpdateStatus_ExpectItemNotFoundException() throws InvalidPriceException, MenuNotFoundException, ItemNotInMenuException, RestaurantMenuMismatchException, RestaurantNotFoundException, ItemNotFoundException {
        Menu menu = new Menu(1, new ArrayList<>());
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));
        when(itemRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class,()-> itemService.updateStatus(1,1,1, ItemStatus.OUT_OF_STOCK));
    }

    @Test
    void testUpdateStatus_ExpectRestaurantMenuMismatchException() throws InvalidPriceException, MenuNotFoundException, ItemNotInMenuException, RestaurantMenuMismatchException, RestaurantNotFoundException, ItemNotFoundException {
        Item item = new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE);
        Menu menu = new Menu(1, new ArrayList<>(List.of(item)));
        Menu secondMenu = new Menu(1, new ArrayList<>());
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", secondMenu);
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        assertThrows(RestaurantMenuMismatchException.class, ()-> itemService.updateStatus(1,1,1, ItemStatus.OUT_OF_STOCK));
    }

    @Test
    void testUpdateStatus_ExpectItemNotInMenuException() throws InvalidPriceException, MenuNotFoundException, ItemNotInMenuException, RestaurantMenuMismatchException, RestaurantNotFoundException, ItemNotFoundException {
        Item item = new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE);
        Item secondItem =  new Item(2, "JINGS", 200.0, ItemStatus.AVAILABLE);
        Menu menu = new Menu(1, new ArrayList<>(List.of(item)));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));
        when(itemRepository.findById(2)).thenReturn(Optional.of(secondItem));

        assertThrows(ItemNotInMenuException.class, ()-> itemService.updateStatus(1,1,2, ItemStatus.OUT_OF_STOCK));
    }
}
