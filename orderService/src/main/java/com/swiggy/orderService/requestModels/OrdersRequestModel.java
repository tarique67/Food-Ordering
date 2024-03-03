package com.swiggy.orderService.requestModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersRequestModel {
    private Integer restaurantId;
    private List<String> items;
}
