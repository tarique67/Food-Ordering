package com.swiggy.catalogService.services.interfaces;

import com.swiggy.catalogService.entities.Item;
import com.swiggy.catalogService.enums.ItemStatus;
import com.swiggy.catalogService.exceptions.*;

public interface ItemService {

    Item updatePrice(int restaurantId, int menuId, int itemId, double newPrice) throws MenuNotFoundException, ItemNotFoundException, ItemNotInMenuException, RestaurantNotFoundException, RestaurantMenuMismatchException, InvalidPriceException;
    Item updateStatus(int restaurantId, int menuId, int itemId, ItemStatus itemStatus) throws ItemNotInMenuException, RestaurantMenuMismatchException, ItemNotFoundException, MenuNotFoundException, RestaurantNotFoundException;
}
