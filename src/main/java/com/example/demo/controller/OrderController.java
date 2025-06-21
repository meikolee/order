package com.example.demo.controller;

import com.example.demo.dto.Payment;
import com.example.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    /**
     * 订单服务
     */
    @Autowired
    private PaymentService paymentService;

    /**
     * 创建订单
     *
     * @param payment
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody Payment payment) {
        payment.setStatus("CREATED");
        String paymentResponse = paymentService.processPayment(payment);
        return ResponseEntity.ok("订单创建成功 : " + paymentResponse);
    }
}
