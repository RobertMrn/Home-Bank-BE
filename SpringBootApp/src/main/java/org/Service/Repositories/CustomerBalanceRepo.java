package org.Service.Repositories;

import org.Service.Entities.CustomerBalance;
import org.Service.Entities.User;
import org.springframework.data.repository.CrudRepository;

public interface CustomerBalanceRepo extends CrudRepository<CustomerBalance, Integer> {
    CustomerBalance findCustomerBalanceByUser(User user);

}
