package com.swiggy.catalogService.services;

import com.swiggy.catalogService.entities.Item;
import com.swiggy.catalogService.entities.Menu;
import com.swiggy.catalogService.entities.Restaurant;
import com.swiggy.catalogService.enums.ItemStatus;
import com.swiggy.catalogService.exceptions.*;
import com.swiggy.catalogService.repositories.ItemRepository;
import com.swiggy.catalogService.repositories.MenuRepository;
import com.swiggy.catalogService.repositories.RestaurantRepository;
import com.swiggy.catalogService.services.interfaces.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.swiggy.catalogService.constants.ExceptionMessages.*;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Item updatePrice(int restaurantId, int menuId, int itemId, double newPrice) throws MenuNotFoundException, ItemNotFoundException, ItemNotInMenuException, RestaurantNotFoundException, RestaurantMenuMismatchException, InvalidPriceException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(()-> new RestaurantNotFoundException(RESTAURANT_NOT_FOUND));
        Menu menu = menuRepository.findById(menuId).orElseThrow(()-> new MenuNotFoundException(MENU_NOT_FOUND));
        Item item = itemRepository.findById(itemId).orElseThrow(()-> new ItemNotFoundException(ITEM_NOT_FOUND));

        if(!menu.equals(restaurant.getMenu()))
            throw new RestaurantMenuMismatchException(RESTAURANT_MENU_MISMATCH);

        if(!menu.hasItem(item))
            throw new ItemNotInMenuException(MENU_ITEM_MISMATCH);

        item.updatePrice(newPrice);

        return itemRepository.save(item);
    }

    @Override
    public Item updateStatus(int restaurantId,int menuId, int itemId, ItemStatus itemStatus) throws ItemNotInMenuException, RestaurantMenuMismatchException, ItemNotFoundException, MenuNotFoundException, RestaurantNotFoundException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(()-> new RestaurantNotFoundException(RESTAURANT_NOT_FOUND));
        Menu menu = menuRepository.findById(menuId).orElseThrow(()-> new MenuNotFoundException(MENU_NOT_FOUND));
        Item item = itemRepository.findById(itemId).orElseThrow(()-> new ItemNotFoundException(ITEM_NOT_FOUND));

        if(!menu.equals(restaurant.getMenu()))
            throw new RestaurantMenuMismatchException(RESTAURANT_MENU_MISMATCH);

        if(!menu.hasItem(item))
            throw new ItemNotInMenuException(MENU_ITEM_MISMATCH);

        item.updateStatus(itemStatus);

        return itemRepository.save(item);
    }
}
