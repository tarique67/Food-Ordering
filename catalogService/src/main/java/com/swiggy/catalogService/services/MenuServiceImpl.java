package com.swiggy.catalogService.services;

import com.swiggy.catalogService.entities.Item;
import com.swiggy.catalogService.entities.Menu;
import com.swiggy.catalogService.entities.Restaurant;
import com.swiggy.catalogService.exceptions.MenuNotFoundException;
import com.swiggy.catalogService.exceptions.RestaurantMenuMismatchException;
import com.swiggy.catalogService.exceptions.RestaurantNotFoundException;
import com.swiggy.catalogService.repositories.MenuRepository;
import com.swiggy.catalogService.repositories.RestaurantRepository;
import com.swiggy.catalogService.services.interfaces.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.swiggy.catalogService.constants.ExceptionMessages.*;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Menu addItems(int restaurantId, int menuId, List<Item> items) throws RestaurantNotFoundException, MenuNotFoundException, RestaurantMenuMismatchException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(()-> new RestaurantNotFoundException(RESTAURANT_NOT_FOUND));
        Menu menu = menuRepository.findById(menuId).orElseThrow(()-> new MenuNotFoundException(MENU_NOT_FOUND));

        if(!restaurant.getMenu().equals(menu))
            throw new RestaurantMenuMismatchException(RESTAURANT_MENU_MISMATCH);

        menu.addItems(items);

        return menuRepository.save(menu);
    }

    @Override
    public Menu fetch(int restaurantId, int menuId) throws RestaurantMenuMismatchException, MenuNotFoundException, RestaurantNotFoundException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(()-> new RestaurantNotFoundException(RESTAURANT_NOT_FOUND));
        Menu menu = menuRepository.findById(menuId).orElseThrow(()-> new MenuNotFoundException(MENU_NOT_FOUND));

        if(!restaurant.getMenu().equals(menu))
            throw new RestaurantMenuMismatchException(RESTAURANT_MENU_MISMATCH);

        return menu;
    }
}
