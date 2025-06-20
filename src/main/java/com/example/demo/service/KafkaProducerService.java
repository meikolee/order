package com.example.demo.service;

import com.example.demo.dto.Payment;

public interface KafkaProducerService {

    void sendPayLog(Payment payment);
}
