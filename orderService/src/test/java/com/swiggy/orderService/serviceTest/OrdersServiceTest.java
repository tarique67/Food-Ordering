package com.swiggy.orderService.serviceTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swiggy.orderService.clients.catalogServiceClients.responseModels.Menu;
import com.swiggy.orderService.clients.catalogServiceClients.responseModels.Restaurant;
import com.swiggy.orderService.entities.Customer;
import com.swiggy.orderService.entities.Item;
import com.swiggy.orderService.entities.Orders;
import com.swiggy.orderService.enums.OrderStatus;
import com.swiggy.orderService.exceptions.ItemNotInRestaurantException;
import com.swiggy.orderService.exceptions.OrderNotFoundException;
import com.swiggy.orderService.exceptions.UserNotFoundException;
import com.swiggy.orderService.repositories.CustomerRepository;
import com.swiggy.orderService.repositories.OrdersRepository;
import com.swiggy.orderService.requestModels.OrdersRequestModel;
import com.swiggy.orderService.responseModels.OrdersResponseModel;
import com.swiggy.orderService.services.OrdersServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrdersServiceTest {

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private OrdersServiceImpl ordersService;

    @Test
    void testCreateOrder_ExpectSuccessful() throws UserNotFoundException, JsonProcessingException, ItemNotInRestaurantException {
        Customer customer = new Customer(1, "test", "test", "test", new ArrayList<>());
        List<Item> items = new ArrayList<>(List.of(new Item(1, "wings", 180.0), new Item(2, "rings", 180.0)));
        Orders order = new Orders(1, 1, OrderStatus.ACCEPTED, 360.0, customer, items);
        OrdersRequestModel request = new OrdersRequestModel(1, List.of("wings", "rings"));
        when(customerRepository.findByUserName("test")).thenReturn(Optional.of(customer));
        when(authentication.getName()).thenReturn("test");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(ordersRepository.save(any())).thenReturn(order);
        OrdersResponseModel expected = new OrdersResponseModel(1,1,OrderStatus.ACCEPTED,360.0,"test", items);

        OrdersResponseModel actual = ordersService.create("test", request);

        assertEquals(expected,actual);
        verify(customerRepository, times(1)).findByUserName("test");
        verify(ordersRepository, times(1)).save(any());
    }


    @Test
    void testFetchOrder_ExpectSuccessful() throws UserNotFoundException, JsonProcessingException, ItemNotInRestaurantException, OrderNotFoundException {
        List<Item> items = new ArrayList<>(List.of(new Item(1, "wings", 180.0), new Item(2, "rings", 180.0)));
        Customer customer = new Customer(1, "test", "test", "test", new ArrayList<>());
        Orders order = new Orders(1, 1, OrderStatus.ACCEPTED, 360.0, customer, items);
        customer.getOrders().add(order);
        when(customerRepository.findByUserName("test")).thenReturn(Optional.of(customer));
        when(authentication.getName()).thenReturn("test");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(ordersRepository.findById(1)).thenReturn(Optional.of(order));
        OrdersResponseModel expected = new OrdersResponseModel(1,1,OrderStatus.ACCEPTED,360.0,"test", items);

        OrdersResponseModel actual = ordersService.fetch("test", 1);

        assertEquals(expected,actual);
        verify(customerRepository, times(1)).findByUserName("test");
        verify(ordersRepository, times(1)).findById(1);
    }

    @Test
    void testFetchOrder_ExpectSOrderNotFoundException() throws UserNotFoundException, JsonProcessingException, ItemNotInRestaurantException, OrderNotFoundException {
        Customer customer = new Customer(1, "test", "test", "test", new ArrayList<>());
        when(customerRepository.findByUserName("test")).thenReturn(Optional.of(customer));
        when(authentication.getName()).thenReturn("test");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(ordersRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,()-> ordersService.fetch("test", 1));
    }

    @Test
    void testFetchOrder_ExpectSOrderNotFoundExceptionOrderCustomerMismatch() throws UserNotFoundException, JsonProcessingException, ItemNotInRestaurantException, OrderNotFoundException {
        List<Item> items = new ArrayList<>(List.of(new Item(1, "wings", 180.0), new Item(2, "rings", 180.0)));
        Customer customer = new Customer(1, "test", "test", "test", new ArrayList<>());
        Orders order = new Orders(1, 1, OrderStatus.ACCEPTED, 360.0, new Customer(), items);
        when(customerRepository.findByUserName("test")).thenReturn(Optional.of(customer));
        when(authentication.getName()).thenReturn("test");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(ordersRepository.findById(1)).thenReturn(Optional.of(order));

        assertThrows(OrderNotFoundException.class,()-> ordersService.fetch("test", 1));
    }
}
