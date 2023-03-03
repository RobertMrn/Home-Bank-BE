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
public class UserDataAndConsumerData {
    private int userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String gender;
    private String address;
    private String nationality;
    private Date birthDate;
    private String iban;
    private String familySituation;
    private String occupation;

    @Temporal(value = TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Bucharest")
    private Date employmentStartDate;

    private BigDecimal monthlyIncome;
}
