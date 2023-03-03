package org.Service.Repositories;

import org.Service.Entities.CreditLoan;
import org.Service.Entities.CreditLoanConsumerData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public interface CreditLoanConsumerDataRepo extends CrudRepository<CreditLoanConsumerData, Integer> {
    CreditLoanConsumerData findCreditLoanConsumerDataByContractId(CreditLoan creditLoan);
}