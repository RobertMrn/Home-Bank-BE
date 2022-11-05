package org.Service.Controllers;


import org.DTOs.CreditLoanDTO;
import org.Service.Entities.CreditLoan;
import org.Service.Services.CreditLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CreditLoanController {
    @Autowired
    CreditLoanService creditLoanService;

    @PostMapping("/addCreditLoan")
    public CreditLoan addCreditLoan(@RequestBody CreditLoanDTO creditLoan){
        return creditLoanService.addCreditLoan(creditLoan);
    }

    @GetMapping("/findCreditLoanById")
    public CreditLoan findCreditLoanById(@RequestParam int id){
        return creditLoanService.findCreditLoanById(id);
    }
}
