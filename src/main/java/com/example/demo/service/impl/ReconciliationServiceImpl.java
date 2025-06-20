package com.example.demo.service.impl;

import com.example.demo.log.ReconciliationLog;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ExchangeRateRepository;
import com.example.demo.repository.ReconciliationRepository;
import com.example.demo.service.ReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ReconciliationServiceImpl implements ReconciliationService {
    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private ReconciliationRepository reconciliationRepo;

    public void reconcile(String date, BigDecimal bankTotal) {
        // 模拟从账户中聚合总金额
        BigDecimal systemTotal = accountRepo.sumAllBalance();// 假设这个方法返回系统中所有账户的总余额
        BigDecimal difference = systemTotal.subtract(bankTotal); // 计算差异
        ReconciliationLog log = new ReconciliationLog();// 创建对账日志对象 为什么不能自动注入 因为这个类不是spring管理的
        log.setDate(date);
        log.setSystemTotal(systemTotal);
        log.setBankTotal(bankTotal);
        log.setDifference(difference);
        log.setStatus(difference.abs().compareTo(new BigDecimal("0.01")) < 0 ? "MATCHED" : "MISMATCHED"); // 设置状态
    }

}
