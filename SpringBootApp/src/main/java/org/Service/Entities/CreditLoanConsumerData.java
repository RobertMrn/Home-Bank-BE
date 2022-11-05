package org.Service.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CREDIT_LOAN_CONSUMER_DATA")
public class CreditLoanConsumerData {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "contract_id")
    @MapsId
    private CreditLoan contractId;

    @Id
    private int consumerDataId;

    @Column(name = "IBAN", nullable = false)
    private String iban;

    @Column(name = "number_of_children", nullable = false)
    private String numberOfChildren;

    @Column(name = "living_situation", nullable = false)
    private String livingSituation;

    @Column(name = "family_situation", nullable = false)
    private String familySituation;

    @Column(name = "occupation", nullable = false)
    private String occupation;

    @Column(name = "employment_start_date", nullable = false)
    @Temporal(value = TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Bucharest")
    private Date employmentStartDate;

    @Column(name = "monthly_income", nullable = false)
    private BigDecimal monthlyIncome;
}
