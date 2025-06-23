package com.example.demo.service;

import com.example.demo.entity.Payment;
import com.example.demo.entity.PaymentOrder;

public interface KafkaProducerService {

    void sendPayLog(Payment payment);

    void sendOrderLog(PaymentOrder paymentOrder);
}
