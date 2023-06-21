package org.Service.Repositories;

import org.Service.Entities.CreditLoan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CreditLoanRepo extends CrudRepository<CreditLoan, Integer> {

    @Query("select count(*) from CreditLoan c where c.esDecision = 'Yellow'")
    int findNumberOfYellowDecisionContracts();
}
