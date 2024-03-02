package com.swiggy.catalogService.services.interfaces;

import com.swiggy.catalogService.entities.Item;
import com.swiggy.catalogService.entities.Menu;
import com.swiggy.catalogService.exceptions.MenuNotFoundException;
import com.swiggy.catalogService.exceptions.RestaurantMenuMismatchException;
import com.swiggy.catalogService.exceptions.RestaurantNotFoundException;

import java.util.List;

public interface MenuService {
    Menu addItems(int restaurantId, int menuId, List<Item> items) throws RestaurantNotFoundException, MenuNotFoundException, RestaurantMenuMismatchException;

    Menu fetch(int restaurantId, int menuId) throws RestaurantMenuMismatchException, MenuNotFoundException, RestaurantNotFoundException;
}
