package com.swiggy.catalogService.responseModels;

import com.swiggy.catalogService.entities.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantResponseModel {

    private String restaurantName;
    private String address;
    private Menu menu;
}
