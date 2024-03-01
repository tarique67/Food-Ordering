package com.swiggy.catalogService.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.catalogService.entities.Item;
import com.swiggy.catalogService.entities.Menu;
import com.swiggy.catalogService.enums.ItemStatus;
import com.swiggy.catalogService.services.MenuServiceImpl;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuServiceImpl menuService;

    @BeforeEach
    void setUp() {
        reset(menuService);
    }

    @Test
    void testItemsAddedToMenu_ExpectSuccessful() throws Exception {
        Menu menu = new Menu(1, new ArrayList<>(List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE))));
        List<Item> itemsToAdd = new ArrayList<>(List.of(new Item(2, "Jings", 180.0, ItemStatus.AVAILABLE)));
        Menu menuReturned = new Menu(1, new ArrayList<>(List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE), new Item(2, "Jings", 180.0, ItemStatus.AVAILABLE))));
        when(menuService.addItems(1,1, itemsToAdd)).thenReturn(menuReturned);

        mockMvc.perform(post("/api/v1/restaurants/1/menus/1/items").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(itemsToAdd))).
                andExpect(status().isCreated()).
                andExpect(jsonPath("$.items[0].itemName").value("Wings")).
                andExpect(jsonPath("$.items[1].itemName").value("Jings"));
        verify(menuService, times(1)).addItems(1,1,itemsToAdd);

    }
}
