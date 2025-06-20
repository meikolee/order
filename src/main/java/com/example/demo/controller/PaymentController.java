package com.example.demo.controller;

import com.example.demo.dto.Payment;
import com.example.demo.service.PaymentPublisher;
import com.example.demo.service.PaymentRecordService;
import com.example.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// [新增] 2. PaymentController 控制器
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentPublisher paymentPublisher;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRecordService paymentRecordService; // 注入 PaymentRecordService

    /**
     * 发布支付信息
     *
     * 该方法通过 POST 请求接收一个 Payment 对象，将其发布到消息队列 RabbitMQ 中
     * 这使得支付信息能够在不同的微服务之间异步传递和处理
     *
     * @param payment 包含支付详情的 Payment 对象，通过请求体接收
     * @return 返回一个 ResponseEntity 对象，包含一个表示发布成功的消息
     */
    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody Payment payment) {
        // 发布支付信息到 RabbitMQ
        paymentPublisher.publishPayment(payment);
        return ResponseEntity.ok("Payment published successfully");
    }

    //// [新增] 模拟支付发起接口
    @PostMapping("/pay")
    public ResponseEntity<String> pay(@RequestParam String orderId) {
        // 模拟支付逻辑
        // 查找订单
        Payment payment = paymentService.findByOrderId(orderId);
        if (payment == null) {
            return ResponseEntity.badRequest().body(orderId+" : 订单号不存在");
        }
        // 如果是已支付状态，直接返回
        if ("PAID".equals(payment.getStatus())) {
            return ResponseEntity.ok("订单已支付，无需重复支付");
        }


        payment.setStatus("PAID"); // 更新支付状态为已完成
        try {
            // 调用服务处理支付
            paymentRecordService.updateStatus(orderId, payment.getStatus());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("订单处理失败 : " + e.getMessage());
        }

        // 这里可以添加实际的支付处理逻辑
        paymentPublisher.publishPayment(payment);
        return ResponseEntity.ok("订单支付成功，订单号: " + orderId);
    }

}
