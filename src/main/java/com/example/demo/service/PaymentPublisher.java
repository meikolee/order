package com.example.demo.service;

import com.example.demo.dto.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// [新增] 3. PaymentPublisher 服务类
@Slf4j
@Service
public class PaymentPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishPayment(Payment payment) {
        // 将支付信息发送到 RabbitMQ 队列
        log.info("[MQ] 发布支付信息到 RabbitMQ 队列: {}", payment);
        rabbitTemplate.convertAndSend("payment.exchange","payment.process",payment);
    }

    /**
     * 发布支付信息到 RabbitMQ 队列，使用延迟队列
     * @param payment 支付信息
     */
    public void publishPaymentWithDelay(Payment payment) {
        // 将支付信息发送到 RabbitMQ 队列
        log.warn("[MQ] 发布支付信息到延迟队列: {}", payment);
        rabbitTemplate.convertAndSend("payment.exchange","payment.process",payment);// 发送到主队列
        rabbitTemplate.convertAndSend("payment.delay.queue","payment.dlx",payment);
    }
}
