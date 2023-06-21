package org.Service.Controllers;


import com.lowagie.text.DocumentException;
import org.DTOs.*;
import org.Service.Entities.CreditLoan;
import org.Service.Entities.CreditLoanConsumerData;
import org.Service.Entities.User;
import org.Service.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
public class CreditLoanController {
    @Autowired
    CreditLoanService creditLoanService;

    @Autowired
    CalculationFormulasForLoansService calculationFormulasForLoansService;

    @Autowired
    CreditDecisionService creditDecisionService;

    @Autowired
    UserService userService;

    @Autowired
    CreditLoanConsumerDataService creditLoanConsumerDataService;

    @PostMapping("/addCreditLoan")
    public CreditLoan addCreditLoan(@RequestBody CreditLoanDTO creditLoan) {
        List<CreditLoansForUser> creditLoansForUsers = creditLoanService.findCreditLoansByUserId(creditLoan.getUser());
        BigDecimal totalAmountToBeReturned = creditLoan.getAmountToBePaid();
        for(CreditLoansForUser creditLoansForUser: creditLoansForUsers){
            totalAmountToBeReturned = totalAmountToBeReturned.add(creditLoansForUser.getAmountToBePaid());
        }
        creditLoan.setCreditBureauScore(creditDecisionService.getCreditBureauScore(totalAmountToBeReturned));
        return creditLoanService.addCreditLoan(creditLoan);
    }

    @PostMapping("/addConsumerData")
    public CreditLoanConsumerData addConsumerData(@RequestBody ConsumerDataDTO consumerDataDTO) {
        return creditLoanConsumerDataService.addConsumerData(consumerDataDTO);
    }

    @GetMapping("/findCreditLoanById")
    public CreditLoan findCreditLoanById(@RequestParam int id) {
        return creditLoanService.findCreditLoanById(id);
    }

    @GetMapping("/findCreditLoansByUserId")
    public List<CreditLoansForUser> findCreditLoansByUserId(@RequestParam int userId) {
        return creditLoanService.findCreditLoansByUserId(userId);
    }
    @GetMapping("/findAllLoans")
    public List<CreditLoanDTO> findAllLoans() {
        return creditLoanService.findAllLoans();
    }

    @PostMapping("/calculateCredit")
    public CalculationFormulasForLoansResponse calculateCredit(@RequestBody CalculationFormulasForLoansRequest calculationFormulasForLoansRequest){
        return calculationFormulasForLoansService.redirectToTheSpecificTypeOfLoan(calculationFormulasForLoansRequest);
    }

    @PostMapping("/getDecision")
    public SystemExpertResponse getDecision(@RequestParam int userId, @RequestBody SystemExpertRequest systemExpertRequest){
        User user = userService.findUserById(userId);
        List<CreditLoansForUser> creditLoansForUsers = creditLoanService.findCreditLoansByUserId(userId);
        BigDecimal totalAmountToBeReturned = systemExpertRequest.getAmount();
        for(CreditLoansForUser creditLoansForUser: creditLoansForUsers){
            totalAmountToBeReturned = totalAmountToBeReturned.add(creditLoansForUser.getAmountToBePaid());
        }
        systemExpertRequest.setScore(creditDecisionService.getCreditBureauScore(totalAmountToBeReturned));
        return creditDecisionService.callForCreditDecision(user.getBirthDate(), systemExpertRequest);
    }

    @PutMapping("/updateCreditLoanById")
    public CreditLoan updateCreditLoanById(@RequestBody CreditLoanDTO creditLoanDTO){
        return creditLoanService.updateCreditLoanById(creditLoanDTO);
    }

    @GetMapping("/generateContractPDF")
    public void exportPDF(@RequestParam int contractId, HttpServletResponse response) throws DocumentException, IOException {
        CreditLoan creditLoan = creditLoanService.findCreditLoanById(contractId);
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        ContractGeneration contractGeneration = new ContractGeneration();
        contractGeneration.exportPDF(response, creditLoan.getUser(), creditLoan);
    }
}
