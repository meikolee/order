package com.example.demo.service.impl;

import com.example.demo.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    /**
     * @param userId
     * @param amount
     */
    @Override
    @Transactional
    public void recharge(String userId, BigDecimal amount) {

    }

    /**
     * @param userId
     * @param amount
     */
    @Override
    @Transactional
    public void withdraw(String userId, BigDecimal amount) {

    }
}
