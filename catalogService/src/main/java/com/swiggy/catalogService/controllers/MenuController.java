package com.swiggy.catalogService.controllers;

import com.swiggy.catalogService.entities.Item;
import com.swiggy.catalogService.entities.Menu;
import com.swiggy.catalogService.exceptions.MenuNotFoundException;
import com.swiggy.catalogService.exceptions.RestaurantMenuMismatchException;
import com.swiggy.catalogService.exceptions.RestaurantNotFoundException;
import com.swiggy.catalogService.services.interfaces.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants/{restaurant_id}/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping("{menu_id}/items")
    public ResponseEntity<Menu> addItems(@PathVariable("restaurant_id") int restaurantId, @PathVariable("menu_id") int menuId, @RequestBody List<Item> items) throws MenuNotFoundException, RestaurantMenuMismatchException, RestaurantNotFoundException {
        Menu menu = menuService.addItems(restaurantId, menuId, items);
        return new ResponseEntity<>(menu, HttpStatus.CREATED);
    }
}
