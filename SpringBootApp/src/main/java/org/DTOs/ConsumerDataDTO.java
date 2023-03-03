package org.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerDataDTO {
    private int contractId;
    private int consumerDataId;
    private String iban;
    private String familySituation;
    private String livingSituation;
    private int numberOfChildren;
    private String occupation;

    @Column(name = "employment_start_date", nullable = false)
    @Temporal(value = TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Bucharest")
    private Date employmentStartDate;

    private BigDecimal monthlyIncome;
}
