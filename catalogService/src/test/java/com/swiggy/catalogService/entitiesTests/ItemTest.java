package com.swiggy.catalogService.entitiesTests;

import com.swiggy.catalogService.entities.Item;
import com.swiggy.catalogService.enums.ItemStatus;
import com.swiggy.catalogService.exceptions.InvalidPriceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ItemTest {

    @Test
    void testUpdatePrice_ExpectSuccessful() throws InvalidPriceException {
        Item item = new Item(1,"wings", 180.0, ItemStatus.AVAILABLE);
        Item expected = new Item(1, "wings", 200.0, ItemStatus.AVAILABLE);

        item.updatePrice(200.0);

        assertEquals(expected, item);
    }

    @Test
    void testUpdatePrice_ExpectInvalidPriceException() throws InvalidPriceException {
        Item item = new Item(1,"wings", 180.0, ItemStatus.AVAILABLE);

        assertThrows(InvalidPriceException.class,()-> item.updatePrice(-200.0));
    }

    @Test
    void testUpdateStatus_ExpectSuccessful() {
        Item item = new Item(1,"wings", 180.0, ItemStatus.AVAILABLE);
        Item expected = new Item(1, "wings", 180.0, ItemStatus.OUT_OF_STOCK);

        item.updateStatus(ItemStatus.OUT_OF_STOCK);

        assertEquals(expected, item);
    }
}
