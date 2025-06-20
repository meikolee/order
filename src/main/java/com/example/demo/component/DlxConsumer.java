package com.example.demo.component;

import com.example.demo.dto.Payment;
import com.example.demo.service.PaymentPublisher;
import com.example.demo.service.PaymentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DlxConsumer {

    @Autowired
    private PaymentPublisher paymentPublisher;

    @RabbitListener(queues = "payment.dlx.queue") // 监听延迟队列
    public void handleDlx(Payment payment) {
        // 处理接收到的延迟消息
        System.out.println("[DLX] 接收到延迟消息，重新发布订单: " + payment.getOrderId());

        // 重新发布到主队列
        paymentPublisher.publishPaymentWithDelay(payment);

        // 可以在这里添加更多的业务逻辑来处理延迟消息
    }
}
