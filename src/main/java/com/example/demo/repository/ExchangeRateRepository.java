package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.math.BigDecimal;

@Mapper
public interface ExchangeRateRepository {
    @Select("SELECT rate FROM exchange_rate WHERE from_currency = #{from} AND to_currency = #{to}")
    BigDecimal findRate(@Param("from") String from, @Param("to") String to);
}
