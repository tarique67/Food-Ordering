package com.swiggy.orderService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swiggy.orderService.entities.Customer;
import com.swiggy.orderService.entities.Orders;
import com.swiggy.orderService.enums.OrderStatus;
import com.swiggy.orderService.exceptions.DeliveryExecutiveNotFoundException;
import com.swiggy.orderService.exceptions.ItemNotInRestaurantException;
import com.swiggy.orderService.exceptions.OrderNotFoundException;
import com.swiggy.orderService.exceptions.UserNotFoundException;
import com.swiggy.orderService.repositories.CustomerRepository;
import com.swiggy.orderService.repositories.OrdersRepository;
import com.swiggy.orderService.requestModels.OrdersRequestModel;
import com.swiggy.orderService.responseModels.OrdersResponseModel;
import com.swiggy.orderService.services.interfaces.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.swiggy.orderService.constants.ExceptionMessages.*;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public OrdersResponseModel create(String username, OrdersRequestModel orderRequest) throws JsonProcessingException, ItemNotInRestaurantException, UserNotFoundException, DeliveryExecutiveNotFoundException {
        Customer customer = customerRepository.findByUserName(username).orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND));
        Orders order = new Orders();
        order.create(orderRequest.getRestaurantId(), orderRequest.getItems(), customer);
        Orders savedOrder = ordersRepository.save(order);

        try {
            savedOrder.assignDeliveryExecutive(orderRequest.getRestaurantId());
        } catch (DeliveryExecutiveNotFoundException exception) {
            ordersRepository.delete(savedOrder);
            throw new DeliveryExecutiveNotFoundException("No delivery executive found at requested location.");
        }
        Orders assignedOrder = ordersRepository.save(savedOrder);
        return new OrdersResponseModel(assignedOrder.getOrderId(),assignedOrder.getRestaurantId(),assignedOrder.getStatus(), assignedOrder.getTotal_price(), username, assignedOrder.getItems(), assignedOrder.getDeliveryExecutiveId());
    }

    @Override
    public OrdersResponseModel fetch(String username, int orderId) throws UserNotFoundException, OrderNotFoundException {
        Customer customer = customerRepository.findByUserName(username).orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND));
        Orders order = ordersRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException(ORDER_NOT_FOUND));

        if(!customer.getOrders().contains(order))
            throw new OrderNotFoundException(ORDER_CUSTOMER_MISMATCH);
        return new OrdersResponseModel(order.getOrderId(),order.getRestaurantId(),order.getStatus(), order.getTotal_price(), username, order.getItems(), order.getDeliveryExecutiveId());
    }

    @Override
    public OrdersResponseModel update(int orderId, OrderStatus status) throws OrderNotFoundException {
        Orders order = ordersRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException(ORDER_NOT_FOUND));
        order.setStatus(status);

        Orders updatedOrder = ordersRepository.save(order);
        return new OrdersResponseModel(updatedOrder.getOrderId(), updatedOrder.getRestaurantId(), updatedOrder.getStatus(), updatedOrder.getTotal_price(), updatedOrder.getCustomer().getUserName(), updatedOrder.getItems(), updatedOrder.getDeliveryExecutiveId());
    }
}
