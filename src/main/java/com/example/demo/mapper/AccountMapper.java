package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

@Mapper
public interface AccountMapper {
    BigDecimal findBalance(String userId);

    void updateBalance(String userId, BigDecimal amount);
}
