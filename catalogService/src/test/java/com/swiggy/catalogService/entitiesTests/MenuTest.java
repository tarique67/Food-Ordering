package com.swiggy.catalogService.entitiesTests;

import com.swiggy.catalogService.entities.Item;
import com.swiggy.catalogService.entities.Menu;
import com.swiggy.catalogService.enums.ItemStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuTest {

    @Test
    void testAddItem_ExpectItemAdded() {
        Menu menu = new Menu(1, new ArrayList<>(Arrays.asList(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE))));
        Menu expected = new Menu(1, Arrays.asList(new Item(1,"Wings", 180.0, ItemStatus.AVAILABLE), new Item(2,"Popcorn", 180.0, ItemStatus.AVAILABLE)));

        menu.addItems(new ArrayList<>(Arrays.asList(new Item(2,"Popcorn", 180.0, ItemStatus.AVAILABLE))));

        assertEquals(expected, menu);
    }

    @Test
    void testAddItem_ExpectItemNotAdded() {
        Menu menu = new Menu(1, new ArrayList<>(Arrays.asList(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE))));
        Menu expected = new Menu(1, Arrays.asList(new Item(1,"Wings", 180.0, ItemStatus.AVAILABLE)));

        menu.addItems(new ArrayList<>(Arrays.asList(new Item(1,"Wings", 180.0, ItemStatus.AVAILABLE))));

        assertEquals(expected, menu);
    }

    @Test
    void testAddItem_ExpectExistingItemPriceUpdated() {
        Menu menu = new Menu(1, new ArrayList<>(Arrays.asList(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE))));
        Menu expected = new Menu(1, Arrays.asList(new Item(1,"Wings", 200.0, ItemStatus.AVAILABLE)));

        menu.addItems(new ArrayList<>(Arrays.asList(new Item(1,"Wings", 200.0, ItemStatus.AVAILABLE))));

        assertEquals(expected, menu);
    }
}
