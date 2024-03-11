package com.swiggy.orderService.entitiesTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swiggy.orderService.clients.catalogServiceClients.CatalogServiceClient;
import com.swiggy.orderService.clients.catalogServiceClients.responseModels.Menu;
import com.swiggy.orderService.clients.catalogServiceClients.responseModels.Restaurant;
import com.swiggy.orderService.entities.Customer;
import com.swiggy.orderService.entities.Item;
import com.swiggy.orderService.entities.Orders;
import com.swiggy.orderService.enums.OrderStatus;
import com.swiggy.orderService.exceptions.DeliveryExecutiveNotFoundException;
import com.swiggy.orderService.exceptions.ItemNotInRestaurantException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrdersTest {

    @Mock
    private CatalogServiceClient catalogServiceClient;

    @InjectMocks
    private Orders order;

    @Test
    void testCreateOrder_ExpectSuccessful() throws JsonProcessingException, ItemNotInRestaurantException, DeliveryExecutiveNotFoundException {
        Customer customer = new Customer(1, "test", "test", "test", new ArrayList<>());
        List<Item> items = new ArrayList<>(List.of(new Item(null,1, "wings", 180.0), new Item(null,2, "rings", 180.0)));
        Menu menu = new Menu(1, items);
        Restaurant restaurant = new Restaurant(1, "KFC", "Ranchi", menu);
        Orders expected = new Orders(null, 1, OrderStatus.ACCEPTED, 360.0, customer, items, null);
        Orders actual = new Orders();
        when(catalogServiceClient.fetchRestaurantFromCatalogService(1)).thenReturn(restaurant);

        actual.create(1, List.of("wings", "rings"), customer);

        assertEquals(expected, actual);
    }

    @Test
    void testCreateOrder_ExpectItemNotInRestaurantException() throws JsonProcessingException, ItemNotInRestaurantException {
        Customer customer = new Customer(1, "test", "test", "test", new ArrayList<>());
        List<Item> items = new ArrayList<>(List.of(new Item(1,1, "wings", 180.0), new Item(2,2, "rings", 180.0)));
        Menu menu = new Menu(1, items);
        Restaurant restaurant = new Restaurant(1, "KFC", "Ranchi", menu);
        Orders actual = new Orders();
        when(catalogServiceClient.fetchRestaurantFromCatalogService(1)).thenReturn(restaurant);

        assertThrows(ItemNotInRestaurantException.class, ()-> actual.create(1, List.of("wings", "rings", "samosa"), customer));
    }
}
