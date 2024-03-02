package com.swiggy.catalogService.controllers;

import com.swiggy.catalogService.entities.Item;
import com.swiggy.catalogService.enums.ItemStatus;
import com.swiggy.catalogService.exceptions.*;
import com.swiggy.catalogService.services.interfaces.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.swiggy.catalogService.constants.ExceptionMessages.SPECIFY_PRICE_STATUS_UPDATE;

@RestController
@RequestMapping("api/v1/restaurants/{restaurantId}/menus/{menuId}/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PutMapping("/{itemId}")
    public ResponseEntity<Item> updatePrice(@PathVariable("restaurantId") int restaurantId, @PathVariable("menuId") int menuId, @PathVariable("itemId") int itemId, @RequestParam(required = false) Integer price, @RequestParam(required = false) ItemStatus status) throws InvalidPriceException, MenuNotFoundException, ItemNotInMenuException, RestaurantMenuMismatchException, RestaurantNotFoundException, ItemNotFoundException {
        Item item = new Item();
        boolean updated = false;
        if(price != null){
            item = itemService.updatePrice(restaurantId, menuId, itemId, price);
            updated = true;
        }
        if(status != null){
            item = itemService.updateStatus(restaurantId,menuId,itemId,status);
            updated = true;
        }
        if(!updated)
            throw new IllegalArgumentException(SPECIFY_PRICE_STATUS_UPDATE);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }
}
