package com.example.demo.service;

import com.example.demo.dto.Payment;
import com.example.demo.log.ReconciliationOrderLog;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentRecordService {
    void createPaymentRecord(Payment payment);

    @Transactional
    void updateStatus(String orderId, String status);

    @Transactional
    void updateOrderStatus(String orderId, String status);
}
