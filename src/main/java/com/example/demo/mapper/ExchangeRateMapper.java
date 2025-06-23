package com.example.demo.mapper;

import com.example.demo.dto.ExchangeRateParams;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

@Mapper
public interface ExchangeRateMapper{


    BigDecimal findRate(ExchangeRateParams exchangeRateParams);
}
