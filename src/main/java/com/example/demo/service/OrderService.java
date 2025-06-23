package com.example.demo.service;

import com.example.demo.entity.PaymentOrder;

public interface OrderService {
    String creatOrder(PaymentOrder paymentOrder);

    void createOrderRecord(PaymentOrder paymentOrder);
}
