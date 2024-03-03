package com.swiggy.orderService.controllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.orderService.entities.Item;
import com.swiggy.orderService.enums.OrderStatus;
import com.swiggy.orderService.requestModels.OrdersRequestModel;
import com.swiggy.orderService.responseModels.OrdersResponseModel;
import com.swiggy.orderService.services.OrdersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrdersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrdersServiceImpl ordersService;

    @BeforeEach
    void setUp() {
        reset(ordersService);
    }

    @Test
    @WithMockUser(username = "testUser")
    void testCreateOrder_ExpectSuccessful() throws Exception {
        OrdersRequestModel request = new OrdersRequestModel(1, List.of("wings","rings"));
        List<Item> items = new ArrayList<>(List.of(new Item(1, "wings", 180.0), new Item(2, "rings", 180.0)));
        OrdersResponseModel response = new OrdersResponseModel(1,1, OrderStatus.ACCEPTED,360.0,"testUser",items);
        when(ordersService.create("testUser", request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/orders").
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(request))).
                andExpect(status().isCreated()).
                andExpect(jsonPath("$.orderId").value(1)).
                andExpect(jsonPath("$.customer").value("testUser"));
        verify(ordersService, times(1)).create("testUser", request);
    }

    @Test
    @WithMockUser(username = "testUser")
    void testFetchOrder_ExpectSuccessful() throws Exception {
        OrdersRequestModel request = new OrdersRequestModel(1, List.of("wings","rings"));
        List<Item> items = new ArrayList<>(List.of(new Item(1, "wings", 180.0), new Item(2, "rings", 180.0)));
        OrdersResponseModel response = new OrdersResponseModel(1,1, OrderStatus.ACCEPTED,360.0,"testUser",items);
        when(ordersService.fetch("testUser", 1)).thenReturn(response);

        mockMvc.perform(get("/api/v1/orders/1").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request))).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.orderId").value(1)).
                andExpect(jsonPath("$.customer").value("testUser"));
        verify(ordersService, times(1)).fetch("testUser", 1);
    }
}
