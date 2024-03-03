package com.swiggy.orderService.responseModels;

import com.swiggy.orderService.entities.Item;
import com.swiggy.orderService.entities.Customer;
import com.swiggy.orderService.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersResponseModel {

    private Integer orderId;
    private Integer restaurantId;
    private OrderStatus status;
    private Double total_price;
    private String customer;
    private List<Item> items;
}
