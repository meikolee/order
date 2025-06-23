package com.example.demo.service;

import com.example.demo.dto.ExchangeRequest;
import java.math.BigDecimal;

public interface ExchangeService {
    BigDecimal convertCurrency(ExchangeRequest request);
}
