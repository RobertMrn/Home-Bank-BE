package org.Service.Services;

import org.DTOs.CustomerBalanceDTO;
import org.Service.Entities.CustomerBalance;
import org.Service.Entities.User;
import org.Service.Repositories.CustomerBalanceRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerBalanceServiceTest {

    @Mock
    private CustomerBalanceRepo customerBalanceRepo;

    @Mock
    private UserService userService;

    @InjectMocks
    private CustomerBalanceService customerBalanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCustomerBalance() {
        // Mock data
        int userId = 1;
        BigDecimal balanceToAdd = new BigDecimal("100.00");
        User user = new User();
        user.setUserId(userId);
        CustomerBalanceDTO customerBalanceDTO = new CustomerBalanceDTO();
        customerBalanceDTO.setUserId(userId);
        customerBalanceDTO.setBalance(balanceToAdd);
        CustomerBalance oldCustomerBalance = new CustomerBalance(user, userId, new BigDecimal("500.00"));
        CustomerBalance expectedCustomerBalance = new CustomerBalance(user, userId, new BigDecimal("600.00"));

        // Mock dependencies
        when(userService.findUserById(userId)).thenReturn(user);
        when(customerBalanceRepo.findById(userId)).thenReturn(Optional.of(oldCustomerBalance));
        when(customerBalanceRepo.save(any(CustomerBalance.class))).thenReturn(expectedCustomerBalance);

        // Call the method
        CustomerBalance result = customerBalanceService.addCustomerBalance(customerBalanceDTO);

        // Verify the interactions
        verify(userService).findUserById(userId);
        verify(customerBalanceRepo).findById(userId);
        verify(customerBalanceRepo).save(any(CustomerBalance.class));

        // Verify the result
        Assertions.assertEquals(expectedCustomerBalance, result);
    }

    @Test
    void testAddCustomerBalance_WithNonexistentUser() {
        // Mock data
        int userId = 1;
        BigDecimal balanceToAdd = new BigDecimal("100.00");
        CustomerBalanceDTO customerBalanceDTO = new CustomerBalanceDTO();
        customerBalanceDTO.setUserId(userId);
        customerBalanceDTO.setBalance(balanceToAdd);

        // Mock dependencies
        when(userService.findUserById(userId)).thenThrow(new NoSuchElementException("user not found"));

        // Call the method and verify the exception
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            customerBalanceService.addCustomerBalance(customerBalanceDTO);
        });

        // Verify the interactions
        verify(userService).findUserById(userId);
        verifyNoInteractions(customerBalanceRepo);
    }

    // Add more test cases for other methods as needed

}
