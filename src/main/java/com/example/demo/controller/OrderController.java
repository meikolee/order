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

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody Payment payment) {
        // Logic to create an order
        // For demonstration, we will just call the payment service
        payment.setStatus("CREATED"); // Set initial status to CREATED
        String paymentResponse = paymentService.processPayment(payment);

        return ResponseEntity.ok("Order created successfully. " + paymentResponse);
    }
}
