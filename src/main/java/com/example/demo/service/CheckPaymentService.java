package com.example.demo.service;

import com.example.demo.dto.Payment;
import com.example.demo.dto.PaymentOrder;

// [新增] 7. PaymentService 模拟业务服务
public interface CheckPaymentService {
    // 稽查订单
    Payment findByOrderId(String orderId);

    PaymentOrder findOrderByOrderId(String orderId);
}
