package com.example.demo.service;

import com.example.demo.dto.Payment;
import com.example.demo.dto.PaymentOrder;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService {
    String creatOrder(PaymentOrder paymentOrder);

    void createPaymentRecord(PaymentOrder paymentOrder);
}
