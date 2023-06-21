package org.Service.Services;

import org.DTOs.CustomerBalanceDTO;
import org.Service.Entities.CustomerBalance;
import org.Service.Entities.User;
import org.Service.Repositories.CustomerBalanceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CustomerBalanceService {
    @Autowired
    private CustomerBalanceRepo customerBalanceRepo;

    @Autowired
    private UserService userService;

    public CustomerBalance addCustomerBalance(CustomerBalanceDTO customerBalanceDTO) {
        User user = userService.findUserById(customerBalanceDTO.getUserId());
        CustomerBalance oldCustomerBalance = findCustomerBalance(customerBalanceDTO.getUserId());
        BigDecimal newBalance = oldCustomerBalance.getBalance().add(customerBalanceDTO.getBalance());
        CustomerBalance customerBalance = new CustomerBalance(user, customerBalanceDTO.getUserId(), newBalance);
        return customerBalanceRepo.save(customerBalance);
    }

    public CustomerBalance findCustomerBalance(int id) {
        Optional<CustomerBalance> customerBalance = customerBalanceRepo.findById(id);
        if (customerBalance.isPresent()) {
            return customerBalance.get();
        } else {
            throw new NoSuchElementException("customer balance not found");
        }
    }

    public CustomerBalance updateCustomerBalanceById(CustomerBalanceDTO customerBalanceDTO) {
        CustomerBalance customerBalance = findCustomerBalance(customerBalanceDTO.getUserId());
        User user = userService.findUserById(customerBalance.getUserId());
        CustomerBalance updatedBalance = new CustomerBalance(user, customerBalanceDTO.getUserId(), customerBalanceDTO.getBalance());
        return customerBalanceRepo.save(updatedBalance);
    }

}
