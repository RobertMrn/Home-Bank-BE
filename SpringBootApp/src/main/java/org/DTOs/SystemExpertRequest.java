package org.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemExpertRequest {
    private String iban;
    private String familySituation;
    private String occupation;
    private Date employmentStartDate;
    private BigDecimal monthlyIncome;
    private BigDecimal score;
    private int CustomerAge;
    private double loanDuration;
    private double monthlyRate;
    private BigDecimal amount;

}
