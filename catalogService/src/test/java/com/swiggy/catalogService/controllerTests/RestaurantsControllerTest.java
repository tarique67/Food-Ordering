package com.swiggy.catalogService.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.catalogService.entities.Item;
import com.swiggy.catalogService.entities.Menu;
import com.swiggy.catalogService.entities.Restaurant;
import com.swiggy.catalogService.enums.ItemStatus;
import com.swiggy.catalogService.services.RestaurantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestaurantServiceImpl restaurantService;

    @BeforeEach
    void setUp() {
        reset(restaurantService);
    }

    @Test
    void testRestaurantCreation_ExpectSuccessful() throws Exception {
        Menu menu = new Menu(1, List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE)));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", menu);
        when(restaurantService.create(restaurant)).thenReturn(restaurant);

        mockMvc.perform(post("/api/v1/restaurants").
               contentType(MediaType.APPLICATION_JSON).
               content(objectMapper.writeValueAsString(restaurant))).
               andExpect(status().isCreated()).
               andExpect(jsonPath("$.restaurantName").value("JFC")).
               andExpect(jsonPath("$.menu.items[0].itemName").value("Wings"));
        verify(restaurantService, times(1)).create(restaurant);
    }

    @Test
    void testGetAllRestaurants_Expect2Restaurants() throws Exception {
        Menu firstMenu = new Menu(1, List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE)));
        Restaurant firstRestaurant = new Restaurant(1, "JFC", "Ranchi", firstMenu);
        Menu secondMenu = new Menu(1, List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE), new Item(2, "Krushers", 280.0, ItemStatus.AVAILABLE)));
        Restaurant secondRestaurant = new Restaurant(1, "KFC", "Ranchi", secondMenu);
        when(restaurantService.fetchAll()).thenReturn(List.of(firstRestaurant, secondRestaurant));

        mockMvc.perform(get("/api/v1/restaurants").
                        contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$[0].restaurantName").value("JFC")).
                andExpect(jsonPath("$[1].restaurantName").value("KFC"));
        verify(restaurantService, times(1)).fetchAll();
    }

    @Test
    void testGetRestaurantById_ExpectJFCRestaurant() throws Exception {
        Menu firstMenu = new Menu(1, List.of(new Item(1, "Wings", 180.0, ItemStatus.AVAILABLE)));
        Restaurant restaurant = new Restaurant(1, "JFC", "Ranchi", firstMenu);
        when(restaurantService.fetchById(1)).thenReturn(restaurant);

        mockMvc.perform(get("/api/v1/restaurants/1").
                        contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.restaurantName").value("JFC"));
        verify(restaurantService, times(1)).fetchById(1);
    }
}
