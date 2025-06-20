package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRequest {
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal amount;
}
