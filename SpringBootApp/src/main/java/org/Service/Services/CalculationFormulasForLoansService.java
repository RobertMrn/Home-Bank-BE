package org.Service.Services;


import org.DTOs.CalculationFormulasForLoansRequest;
import org.DTOs.CalculationFormulasForLoansResponse;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CalculationFormulasForLoansService {
    public CalculationFormulasForLoansResponse redirectToTheSpecificTypeOfLoan(CalculationFormulasForLoansRequest calculationFormulasForLoansRequest){
        if(calculationFormulasForLoansRequest.getCreditType().equals("Student loan")){
            return calculateForStudentLoan(calculationFormulasForLoansRequest);
        }else if(calculationFormulasForLoansRequest.getCreditType().equals("New home") || calculationFormulasForLoansRequest.getCreditType().equals("Personal needs") ||
                calculationFormulasForLoansRequest.getCreditType().equals("Car loan")){
            return calculateOtherTypesOfLoans(calculationFormulasForLoansRequest);
        }
        throw new NoSuchElementException("No type of credit was selected");
    }

    public CalculationFormulasForLoansResponse calculateForStudentLoan(CalculationFormulasForLoansRequest calculationFormulasForLoansRequest) {
        final int AMOUNT = calculationFormulasForLoansRequest.getAmount();
        final int TENURE = calculationFormulasForLoansRequest.getTenure();
        final int PERCENT = 100;
        final int NUMBER_OF_MONTHS = 12;
        final double INTEREST_RATE_PER_MONTH = calculationFormulasForLoansRequest.getInterestRate() / PERCENT / NUMBER_OF_MONTHS;
        double installment;
        double sumToBePaid;

        installment = (AMOUNT * INTEREST_RATE_PER_MONTH) / (1 - Math.pow(1 + INTEREST_RATE_PER_MONTH, -TENURE));
        sumToBePaid = installment * TENURE;
        installment = Math.ceil(installment * 100) / 100;
        sumToBePaid = Math.ceil(sumToBePaid * 100) / 100;

        return new CalculationFormulasForLoansResponse(installment, sumToBePaid);
    }

    public CalculationFormulasForLoansResponse calculateOtherTypesOfLoans(CalculationFormulasForLoansRequest calculationFormulasForLoansRequest) {
        final int AMOUNT = calculationFormulasForLoansRequest.getAmount();
        final int TENURE = calculationFormulasForLoansRequest.getTenure();
        final int PERCENT = 100;
        final int NUMBER_OF_MONTHS = 12;
        final double INTEREST_RATE_PER_MONTH = calculationFormulasForLoansRequest.getInterestRate() / PERCENT / NUMBER_OF_MONTHS;
        double installment;
        double sumToBePaid;

        installment = ((AMOUNT * INTEREST_RATE_PER_MONTH * Math.pow(1 + INTEREST_RATE_PER_MONTH, TENURE)) / ((Math.pow(1 + INTEREST_RATE_PER_MONTH, TENURE)) - 1));
        sumToBePaid = installment * TENURE;
        installment = Math.ceil(installment * 100) / 100;
        sumToBePaid = Math.ceil(sumToBePaid * 100) / 100;

        return new CalculationFormulasForLoansResponse(installment, sumToBePaid);
    }


}
