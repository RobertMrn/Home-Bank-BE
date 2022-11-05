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
@Table(name = "CREDIT_LOAN")
public class CreditLoan {

    @Id
    @Column(name = "contract_id", nullable = false)
    @GeneratedValue
    private int contractId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "interest_rate", nullable = false)
    private double interestRate;

    @Column(name = "tenure", nullable = false)
    private int tenure;

    @Column(name = "installment", nullable = false)
    private double installment;

    @Column(name = "amount_to_be_paid", nullable = false)
    private BigDecimal amountToBePaid;

    @Column(name = "credit_bureau_score", nullable = false)
    private BigDecimal creditBureauScore;

    @Column(name = "es_decision", nullable = false)
    private String esDecision;

    @Column(name = "credit_type", nullable = false)
    private String creditType;

    @Column(name = "creation_date", nullable = false)
    @Temporal(value = TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Bucharest")
    private Date creationDate;

    @Column(name = "last_update", nullable = false)
    @Temporal(value = TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Bucharest")
    private Date lastUpdate;



}
