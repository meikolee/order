package com.example.demo.component;

import com.example.demo.dto.Payment;
import com.example.demo.dto.PaymentOrder;
import com.example.demo.log.ReconciliationLog;
import com.example.demo.log.ReconciliationOrderLog;
import com.example.demo.service.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.concurrent.Executor;
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
    private CheckPaymentService checkPaymentService;

    @Autowired
    private KafkaProducerService kafkaProducerService; // 注入 KafkaProducerService

    @Autowired
    private PaymentRecordService paymentRecordService; // 注入 PaymentRecordService

    @Autowired
    private ReconciliationOrderLogService reconciliationOrderLogService; // 注入对账日志服务

    @Resource // 这个注解是为了使用 Spring 提供的线程池
    private Executor orderExecutor;

    @RabbitListener(queues = "payment.queue", concurrency = "5") // 监听 payment.queue 队列 监听支付处理消息
    public void consume(Payment payment) {
        orderExecutor.execute(() -> handlePayment(payment)); // 多线程执行
    }

    private void handlePayment(Payment payment) {
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

        paymentRecordService.updateStatus(payment.getOrderId(), "PROCESSED_MQ"); // 更新支付状态
        // 模拟：记录日志
        log.info("[业务] 已处理订单: {} 金额: {}", payment.getOrderId(), payment.getAmount());
        // [更新] 插入对账日志
        ReconciliationOrderLog logEntry = new ReconciliationOrderLog();
        String currentDate = java.time.LocalDateTime.now().toString();
        logEntry.setDate(currentDate);
        logEntry.setOrderId(payment.getOrderId());
        logEntry.setLocalAmount(payment.getAmount());
        logEntry.setRemoteAmount(payment.getAmount()); // 模拟一致
        logEntry.setReason("正常对账");
        // 保存对账日志
        reconciliationOrderLogService.saveReconciliationLog(logEntry);
        // 模拟：发起第三方清算回调（可扩展为 FeignClient 调用）
        log.info("[回调] 模拟通知清算服务处理完成: 订单{}", payment.getOrderId());
    }



    // 监听成功队列"payment.success.queue"
    @RabbitListener(queues = "payment.success.queue")
    public void consumeSuccess(String orderId) {
        // 处理接收到的成功消息
        log.info("[MQ] 接收到支付成功消息: {}", orderId);
        // 这里可以添加更多的业务逻辑来处理成功消息
        String key = "payment:success:processed:" + orderId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            log.warn("订单 {} 已处理成功，忽略重复消息", orderId);
            return;
        }
        // 标记为已处理成功
        redisTemplate.opsForValue().set(key, "1", Duration.ofHours(1)); // 设置1小时过期时间

        // 更新支付状态为成功
        paymentRecordService.updateStatus(orderId, "SUCCESS");

        // 模拟：记录日志
        log.info("[业务] 已处理成功订单: {} 金额: {}", orderId);

        // 查找最新的支付记录
        Payment payment = checkPaymentService.findByOrderId(orderId);

        // 最新的状态是SUCCESS 则入账

        kafkaProducerService.sendPayLog(payment);

        // [更新] 插入对账日志
        ReconciliationOrderLog logEntry = new ReconciliationOrderLog();
        // 对账日期
        //  当前时间
        String currentDate = java.time.LocalDateTime.now().toString();
        logEntry.setDate(currentDate);
        logEntry.setOrderId(payment.getOrderId());
        logEntry.setLocalAmount(payment.getAmount());
        logEntry.setRemoteAmount(payment.getAmount()); // 模拟一致
        logEntry.setReason("正常对账");
        // 保存对账日志
        reconciliationOrderLogService.saveReconciliationLog(logEntry);
    }

    // 监听创建订单 payment.order
    @RabbitListener(queues = "payment.order.queue")
    public void consumeOrder(PaymentOrder paymentOrder) {
        String orderId = paymentOrder.getOrderId();
        // 处理接收到的成功消息
        log.info("[MQ] 接收到创建订单消息消息: {}", paymentOrder);
        // 这里可以添加更多的业务逻辑来处理成功消息
        String key = "payment:order:processed:" + orderId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            log.warn("订单 {} 已处理成功，忽略重复消息", orderId);
            return;
        }
        // 标记为已处理成功
        redisTemplate.opsForValue().set(key, "1", Duration.ofHours(1)); // 设置1小时过期时间

        // 更新支付状态为成功
        paymentRecordService.updateOrderStatus(orderId, "MQUPDATE_CREATED_SUCCESS");

        // 模拟：记录日志
        log.info("[业务] 已处理成功订单: {} 金额: {}", orderId);

        // 查找最新的支付记录
        PaymentOrder paymentOrder1 = checkPaymentService.findOrderByOrderId(orderId);
        // 最新的状态是SUCCESS 则入账
        kafkaProducerService.sendOrderLog(paymentOrder1);
        // [更新] 插入对账日志
        ReconciliationOrderLog logEntry = new ReconciliationOrderLog();
        // 对账日期
        //  当前时间
        String currentDate = java.time.LocalDateTime.now().toString();
        logEntry.setDate(currentDate);
        logEntry.setOrderId(paymentOrder.getOrderId());
        logEntry.setLocalAmount(paymentOrder.getAmount());
        logEntry.setRemoteAmount(paymentOrder.getAmount()); // 模拟一致
        logEntry.setReason("创建订单");
        // 保存对账日志
        reconciliationOrderLogService.saveReconciliationLog(logEntry);
    }
}
