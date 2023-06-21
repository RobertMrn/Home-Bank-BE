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
public class StockMarketDTO {
    private int stockId;
    private int userId;
    private String ticker;
    private int quantity;
    private BigDecimal totallyBought;
}
