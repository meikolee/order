package com.example.demo.controller;

import com.example.demo.dto.Payment;
import com.example.demo.service.PaymentPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// [新增] 2. PaymentController 控制器
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentPublisher paymentPublisher;

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody Payment payment) {
        // 发布支付信息到 RabbitMQ
        paymentPublisher.publishPayment(payment);
        return ResponseEntity.ok("Payment published successfully");
    }

}
