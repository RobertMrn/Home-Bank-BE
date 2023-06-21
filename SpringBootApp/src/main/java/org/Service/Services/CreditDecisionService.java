package org.Service.Services;


import org.DTOs.CreditBureauResponse;
import org.DTOs.SystemExpertRequest;
import org.DTOs.SystemExpertResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@Service
public class CreditDecisionService {
    WebClient httpCreditBureau = WebClient.builder().build();
    WebClient httpSystemExpert = WebClient.builder().build();

    public BigDecimal getCreditBureauScore(BigDecimal amount) {
        try {
            return Objects.requireNonNull(httpCreditBureau
                            .get().uri("http://localhost:8095/getCreditBureauScore?amount=" + amount)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .retrieve()
                            .bodyToMono(CreditBureauResponse.class)
                            .block())
                    .getScore();
        } catch (Exception e) {
            return null;
        }
    }

    public SystemExpertResponse getSystemExpertDecision(SystemExpertRequest systemExpertRequest) {
        try {
            return httpSystemExpert
                    .post().uri("http://localhost:8090/calculateDecision")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(systemExpertRequest), SystemExpertRequest.class)
                    .retrieve()
                    .bodyToMono(SystemExpertResponse.class)
                    .block();
        } catch (Exception e) {
            return null;
        }
    }

    public int calculateCustomerAge(Date birthdate){
        Instant instant = Instant.ofEpochMilli(birthdate.getTime());
        LocalDate currentDate = LocalDate.now();
        LocalDate convertedBirthDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        int age = Period.between(convertedBirthDate,currentDate).getYears();
        return age;
    }

    public SystemExpertResponse callForCreditDecision(Date birthdate,SystemExpertRequest systemExpertRequest){
        systemExpertRequest.setCustomerAge(calculateCustomerAge(birthdate));
        return getSystemExpertDecision(systemExpertRequest);
    }

}
