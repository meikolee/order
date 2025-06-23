package com.example.demo.service;

import com.example.demo.dto.ExchangeRateParams;

import java.math.BigDecimal;

public interface ExchangeService {
    BigDecimal convertCurrency(ExchangeRateParams exchangeRateParams);
}
