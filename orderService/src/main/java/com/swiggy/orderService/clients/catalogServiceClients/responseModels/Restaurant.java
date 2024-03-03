package com.swiggy.orderService.clients.catalogServiceClients.responseModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant {

    private Integer restaurantId;
    private String restaurantName;
    private String address;
    private Menu menu;
}
