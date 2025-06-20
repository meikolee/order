package com.example.demo.component;

import com.example.demo.dto.Payment;
import com.example.demo.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.logging.Logger;

// // [新增] 5. MQ 消费者逻辑
@Component
@Slf4j
public class PaymentConsumer {
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public PaymentConsumer(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    // 这里可以添加方法来处理接收到的消息
    @Autowired
    private PaymentService paymentService;

    @RabbitListener(queues = "payment.queue") // 监听 payment.queue 队列 监听支付处理消息
    public void consume(Payment payment) {
        // 处理接收到的消息
        log.info("[MQ] 接收到支付处理消息: {}", payment);
        // 这里可以添加更多的业务逻辑来处理消息

        String key = "payment:processed:" + payment.getOrderId();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            log.warn("订单 {} 已处理，忽略重复消息", payment.getOrderId());
            return;
        }
        // 标记为已处理
        redisTemplate.opsForValue().set(key, "1", Duration.ofHours(1)); // 设置1小时过期时间

        // 更新支付状态
        paymentService.updateStatus(payment.getOrderId(), "PROCESSED");

        // 模拟：记录日志
        log.info("[业务] 已处理订单: {} 金额: {}", payment.getOrderId(), payment.getAmount());

        // 模拟：发起第三方清算回调（可扩展为 FeignClient 调用）
        log.info("[回调] 模拟通知清算服务处理完成: 订单{}", payment.getOrderId());

    }
}
