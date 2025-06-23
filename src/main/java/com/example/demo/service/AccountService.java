package com.example.demo.service;

import java.math.BigDecimal;

public interface AccountService {

    // 充值
    void recharge(String userId, BigDecimal amount);

    // 提现
    void withdraw(String userId, BigDecimal amount);

    // 冻结余额
    void freeze(String userId, BigDecimal amount);
}
