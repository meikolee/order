package com.example.demo.service;

import com.example.demo.dto.Payment;

// [新增] 7. PaymentService 模拟业务服务
public interface PaymentService {

    public void updateStatus(String orderId, String status);

    String processPayment(Payment payment);
}
