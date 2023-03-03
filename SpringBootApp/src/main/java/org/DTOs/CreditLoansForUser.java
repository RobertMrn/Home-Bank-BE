package org.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditLoansForUser {
    private int contractId;
    private double amount;
    private BigDecimal amountToBePaid;
    private double interestRate;
    private int tenure;
    private String creditType;
    private String esDecision;
}
