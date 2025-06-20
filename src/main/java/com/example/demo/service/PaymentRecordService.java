package com.example.demo.service;

import com.example.demo.dto.Payment;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentRecordService {
    void createPaymentRecord(Payment payment);

    @Transactional
    void updateStatus(String orderId, String status);
}
