package org.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalculationFormulasForLoansRequest {
    private int amount;
    private int tenure;
    private double interestRate;
    private String creditType;
}
