package org.Service.Services;

import org.DTOs.CalculationFormulasForLoansRequest;
import org.DTOs.CalculationFormulasForLoansResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CalculationFormulasForLoansServiceTest {
    private CalculationFormulasForLoansService calculationFormulasForLoansService;

    @Mock
    private CalculationFormulasForLoansRequest calculationFormulasForLoansRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        calculationFormulasForLoansService = new CalculationFormulasForLoansService();
    }

    @Test
    void testCalculateForStudentLoan() {
        when(calculationFormulasForLoansRequest.getAmount()).thenReturn(10000);
        when(calculationFormulasForLoansRequest.getTenure()).thenReturn(36);
        when(calculationFormulasForLoansRequest.getInterestRate()).thenReturn(5.0);

        CalculationFormulasForLoansResponse response = calculationFormulasForLoansService.calculateForStudentLoan(calculationFormulasForLoansRequest);

        assertEquals(299.71, response.getInstallment(), 0.01);
        assertEquals(10789.53, response.getSumToBePaid(), 0.01);
    }

    @Test
    void testCalculateOtherTypesOfLoans() {
        when(calculationFormulasForLoansRequest.getAmount()).thenReturn(20000);
        when(calculationFormulasForLoansRequest.getTenure()).thenReturn(48);
        when(calculationFormulasForLoansRequest.getInterestRate()).thenReturn(4.5);

        CalculationFormulasForLoansResponse response = calculationFormulasForLoansService.calculateOtherTypesOfLoans(calculationFormulasForLoansRequest);

        assertEquals(456.07, response.getInstallment(), 0.01);
        assertEquals(21891.35, response.getSumToBePaid(), 0.01);
    }
}
