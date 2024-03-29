package com.swiggy.orderService.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swiggy.orderService.enums.OrderStatus;
import com.swiggy.orderService.exceptions.DeliveryExecutiveNotFoundException;
import com.swiggy.orderService.exceptions.ItemNotInRestaurantException;
import com.swiggy.orderService.exceptions.OrderNotFoundException;
import com.swiggy.orderService.exceptions.UserNotFoundException;
import com.swiggy.orderService.requestModels.OrdersRequestModel;
import com.swiggy.orderService.responseModels.OrdersResponseModel;

public interface OrdersService {

    OrdersResponseModel create(String username, OrdersRequestModel orderRequest) throws JsonProcessingException, ItemNotInRestaurantException, UserNotFoundException, DeliveryExecutiveNotFoundException;

    OrdersResponseModel fetch(String username, int orderId) throws UserNotFoundException, OrderNotFoundException;

    OrdersResponseModel update(int orderId, OrderStatus status) throws OrderNotFoundException;
}
