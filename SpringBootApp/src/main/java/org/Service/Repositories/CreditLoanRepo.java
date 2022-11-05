package org.Service.Repositories;

import org.Service.Entities.CreditLoan;
import org.springframework.data.repository.CrudRepository;

public interface CreditLoanRepo extends CrudRepository<CreditLoan, Integer> {
}
