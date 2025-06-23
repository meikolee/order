package com.example.demo.service.impl;

import com.example.demo.dto.ExchangeRateParams;
import com.example.demo.mapper.ExchangeRateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
        // 修改setScale方法的第二个参数为RoundingMode枚举常量
        rate = rate.setScale(2, RoundingMode.HALF_UP);
        return exchangeRateParams.getAmount().multiply(rate);
    }
}
