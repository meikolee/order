package com.example.demo.service;

import com.example.demo.dto.Payment;

// [新增] 7. PaymentService 模拟业务服务
public interface PaymentService {
    String processPayment(Payment payment);

    Payment findByOrderId(String orderId);
}
