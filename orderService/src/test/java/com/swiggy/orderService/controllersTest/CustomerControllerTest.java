package com.swiggy.orderService.controllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.orderService.entities.Customer;
import com.swiggy.orderService.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        reset(customerService);
    }

    @Test
    void testCustomerCreation_ExpectSuccessful() throws Exception {
        Customer customer = new Customer(1, "test", "test", "test", new ArrayList<>());
        Customer returnedCustomer = new Customer(1, "test", null, "test", new ArrayList<>());
        when(customerService.register(customer)).thenReturn(returnedCustomer);

        mockMvc.perform(post("/api/v1/customers").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(customer))).
                andExpect(status().isCreated());
        verify(customerService, times(1)).register(returnedCustomer);
    }

}
