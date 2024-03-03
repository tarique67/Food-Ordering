package com.swiggy.orderService.serviceTest;

import com.swiggy.orderService.entities.Customer;
import com.swiggy.orderService.exceptions.UsernameTakenException;
import com.swiggy.orderService.repositories.CustomerRepository;
import com.swiggy.orderService.services.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void testCustomerRegister_ExpectSuccessful() throws UsernameTakenException {
        Customer customer = new Customer(1, "test", "test", "test", new ArrayList<>());
        Customer expected = new Customer(1, "test", "encoded", "test", new ArrayList<>());
        when(passwordEncoder.encode("test")).thenReturn("encoded");
        when(customerRepository.save(customer)).thenReturn(expected);

        Customer actual = customerService.register(customer);

        assertEquals(expected,actual);
        verify(customerRepository,times(1)).save(expected);
        verify(passwordEncoder, times(1)).encode("test");
    }

    @Test
    void testCustomerRegister_ExpectUsernameTakenException() throws UsernameTakenException {
        Customer customer = new Customer(1, "test", "test", "test", new ArrayList<>());
        Customer expected = new Customer(1, "test", "encoded", "test", new ArrayList<>());
        when(passwordEncoder.encode("test")).thenReturn("encoded");
        when(customerRepository.save(customer)).thenReturn(expected);
        when(customerRepository.findByUserName("test")).thenReturn(Optional.of(expected));

        assertThrows(UsernameTakenException.class,()-> customerService.register(customer));
    }
}
