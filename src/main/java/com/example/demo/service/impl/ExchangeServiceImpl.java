package com.example.demo.service.impl;

import com.example.demo.dto.ExchangeRateParams;
import com.example.demo.mapper.ExchangeRateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ExchangeServiceImpl implements com.example.demo.service.ExchangeService {

    @Autowired
    private ExchangeRateMapper exchangeRateMapper;

    /**
     * @param exchangeRateParams
     * @return
     */
    @Override
    public BigDecimal convertCurrency(ExchangeRateParams exchangeRateParams) {
        BigDecimal rate = exchangeRateMapper.findRate(exchangeRateParams);
        return exchangeRateParams.getAmount().multiply(rate);
    }
}
