package com.example.demo.service;

import com.example.demo.dto.ExchangeRequest;
import com.example.demo.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface ExchangeService {

    @Autowired
    ExchangeRateRepository rateRepo = null;

    default BigDecimal convertCurrency(ExchangeRequest request) {
        BigDecimal rate = rateRepo.findRate(request.getFromCurrency(), request.getToCurrency());
        return request.getAmount().multiply(rate).setScale(2, RoundingMode.HALF_UP);// 四舍五入保留两位小数
    }
}
