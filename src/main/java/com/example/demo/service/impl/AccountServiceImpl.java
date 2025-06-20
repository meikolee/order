package com.example.demo.service.impl;

import com.example.demo.mapper.AccountMapper;
import com.example.demo.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    /**
     * @param userId
     * @param amount
     */
    @Override
    @Transactional
    public void recharge(String userId, BigDecimal amount) {
        // 1 查找账户
        log.info("开始充值：用户id{}, ", userId);
        log.info("[DB] 查找账户：{}", userId);
        BigDecimal balance = accountMapper.findBalance(userId);
        // 最后写入金额
        BigDecimal newBalance = balance.add(amount);
        log.info("[DB] 账户余额：{}", balance);
        if (balance == null) {
            log.info("[DB] 账户不存在：{}", userId);
            throw new RuntimeException("账户不存在");
        } else {
            // 2 更新账户
            log.info("[DB] 更新账户：{}", userId);
            accountMapper.updateBalance(userId, newBalance);
            log.info("[DB] 充值成功：{}", userId);
        }

    }

    /**
     * @param userId
     * @param amount
     */
    @Override
    @Transactional
    public void withdraw(String userId, BigDecimal amount) {
        // 1 查找账户
        log.info("开始提现：用户id{}, ", userId);
        log.info("[DB] 查找账户：{}", userId);
        BigDecimal balance = accountMapper.findBalance(userId);
        // 最后写入金额
        BigDecimal newBalance = balance.subtract(amount);
        log.info("[DB] 账户余额：{}", balance);
        if (balance == null) {
            log.info("[DB] 账户不存在：{}", userId);
            throw new RuntimeException("账户不存在");
        } else {
            // 2 验证余额
            log.info("[DB] 验证余额：{}", userId);
            if (balance.compareTo(amount) < 0) { //  余额跟减后的金额比较
                log.info("[DB] 余额不足：{}", userId);
                throw new RuntimeException("余额不足");
            } else {
                // 3 提现
                log.info("[DB] 提现：{}", userId);
                accountMapper.updateBalance(userId, newBalance);
            }
        }

    }
}
