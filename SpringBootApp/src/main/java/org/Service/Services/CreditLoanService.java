package org.Service.Services;

import org.DTOs.CreditLoanDTO;
import org.Service.Entities.CreditLoan;
import org.Service.Entities.User;
import org.Service.Repositories.CreditLoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CreditLoanService {
    @Autowired
    private CreditLoanRepo creditLoanRepo;

    @Autowired
    private UserService userService;
    public CreditLoan addCreditLoan(CreditLoanDTO creditLoan) {
        User user = userService.findUserById(creditLoan.getUser());
        return creditLoanRepo.save(new CreditLoan(creditLoan.getContractId(), user, creditLoan.getAmount(), creditLoan.getInterestRate(),
                creditLoan.getTenure(), creditLoan.getInstallment(), creditLoan.getAmountToBePaid(), creditLoan.getCreditBureauScore(),
                creditLoan.getEsDecision(), creditLoan.getCreditType(), creditLoan.getCreationDate(), creditLoan.getLastUpdate()));
    }

    public CreditLoan findCreditLoanById(int id) {
        Optional<CreditLoan> creditLoan = creditLoanRepo.findById(id);
        if (creditLoan.isPresent()) {
            return creditLoan.get();
        } else {
            throw new NoSuchElementException("credit loan not found");
        }
    }
}
