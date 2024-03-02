package com.swiggy.catalogService.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.catalogService.entities.Item;
import com.swiggy.catalogService.entities.Menu;
import com.swiggy.catalogService.entities.Restaurant;
import com.swiggy.catalogService.enums.ItemStatus;
import com.swiggy.catalogService.services.ItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        reset(itemService);
    }

    @Test
    void testUpdatePrice_ExpectSuccessful() throws Exception {
        Item item = new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE);
        Menu menu = new Menu(1, new ArrayList<>(List.of(item)));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        Item itemReturned = new Item(1, "Wings", 200.0, ItemStatus.AVAILABLE);
        when(itemService.updatePrice(1,1, 1, 200.0)).thenReturn(itemReturned);

        mockMvc.perform(put("/api/v1/restaurants/1/menus/1/items/1?price=200").
                        contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.price").value(200.0));
        verify(itemService, times(1)).updatePrice(1,1,1 ,200.0);
    }

    @Test
    void testUpdateStatus_ExpectSuccessful() throws Exception {
        Item item = new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE);
        Menu menu = new Menu(1, new ArrayList<>(List.of(item)));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        Item itemReturned = new Item(1, "Wings", 180.0, ItemStatus.OUT_OF_STOCK);
        when(itemService.updateStatus(1,1, 1, ItemStatus.OUT_OF_STOCK)).thenReturn(itemReturned);

        mockMvc.perform(put("/api/v1/restaurants/1/menus/1/items/1?status=OUT_OF_STOCK").
                        contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.status").value("OUT_OF_STOCK"));
        verify(itemService, times(1)).updateStatus(1,1,1 ,ItemStatus.OUT_OF_STOCK);
    }

    @Test
    void testUpdateStatus_ExpectException() throws Exception {
        Item item = new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE);
        Menu menu = new Menu(1, new ArrayList<>(List.of(item)));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        Item itemReturned = new Item(1, "Wings", 200.0, ItemStatus.OUT_OF_STOCK);
        when(itemService.updateStatus(1,1, 1, ItemStatus.OUT_OF_STOCK)).thenReturn(itemReturned);

        mockMvc.perform(put("/api/v1/restaurants/1/menus/1/items/1").
                        contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());
    }
}
