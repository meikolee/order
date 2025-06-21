package com.example.demo.controller;

import com.example.demo.dto.Payment;
import com.example.demo.dto.PaymentOrder;
import com.example.demo.service.OrderService;
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
    private OrderService orderService;

    /**
     * 创建订单
     *
     * @param paymentOrder
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody PaymentOrder paymentOrder) {
        paymentOrder.setStatus("CREATING");
        String paymentResponse = orderService.creatOrder(paymentOrder);
        return ResponseEntity.ok("订单创建成功 : " + paymentResponse);
    }
}
