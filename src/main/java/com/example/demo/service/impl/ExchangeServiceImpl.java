package com.example.demo.service.impl;

import com.example.demo.dto.ExchangeRequest;
import com.example.demo.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ExchangeServiceImpl implements com.example.demo.service.ExchangeService {

    /**
     * @param request
     * @return
     */
    @Override
    public BigDecimal convertCurrency(ExchangeRequest request) {
        return null;
    }
}
