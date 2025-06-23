package com.example.demo.controller;

import com.example.demo.dto.Payment;
import com.example.demo.dto.PaymentOrder;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
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

        String paymentResponse = orderService.creatOrder(paymentOrder);

        //        List<String> paymentResponse = new ArrayList<>();
        //        for (int i = 0; i < 10; i++) {
        //            paymentOrder.setStatus("CREATING");
        //            try {
        //                String orderId =orderService.creatOrder(paymentOrder);
        //                paymentResponse.add(orderId);
        //            }catch (Exception e) {
        //                log.error("[DB] 订单创建失败：orderId={}, 错误信息={}", paymentOrder.getOrderId(), e.getMessage());
        //            }
        //        }
        return ResponseEntity.ok(paymentResponse);
    }
}
