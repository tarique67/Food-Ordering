package com.swiggy.orderService.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swiggy.orderService.exceptions.ItemNotInRestaurantException;
import com.swiggy.orderService.exceptions.OrderNotFoundException;
import com.swiggy.orderService.exceptions.UserNotFoundException;
import com.swiggy.orderService.requestModels.OrdersRequestModel;
import com.swiggy.orderService.responseModels.OrdersResponseModel;
import com.swiggy.orderService.services.interfaces.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping
    public ResponseEntity<OrdersResponseModel> create(@RequestBody OrdersRequestModel ordersRequest) throws JsonProcessingException, ItemNotInRestaurantException, UserNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        OrdersResponseModel response = ordersService.create(username, ordersRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<OrdersResponseModel> fetch(@PathVariable("order_id") int orderId) throws UserNotFoundException, OrderNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        OrdersResponseModel response = ordersService.fetch(username, orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
