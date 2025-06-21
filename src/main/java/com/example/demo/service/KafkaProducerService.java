package com.example.demo.service;

import com.example.demo.dto.Payment;
import com.example.demo.dto.PaymentOrder;

public interface KafkaProducerService {

    void sendPayLog(Payment payment);

    void sendOrderLog(PaymentOrder paymentOrder);
}
