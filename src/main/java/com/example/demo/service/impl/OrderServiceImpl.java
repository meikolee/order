package com.example.demo.service.impl;

import com.example.demo.dto.PaymentOrder;
import com.example.demo.service.CheckPaymentService;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CheckPaymentService checkPaymentService;

    @Autowired
    private PaymentPublisher paymentPublisher;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public String creatOrder(PaymentOrder paymentOrder) {
        // 1 生成唯一订单号
        // 设置初始状态、生成订单号
        // 用uuid的好处是，不会重复，且长度固定,因为UUID.randomUUID().toString()返回的是字符串，所以用replaceAll("-", "")去掉-
        String orderId = "ORD" + UUID.randomUUID().toString().replaceAll("-", "");

        // 2 幂等控制 防止用户重复提交
        String key = "order:idempotent:"+orderId;
        redisTemplate.opsForValue().set(key, "1", Duration.ofMinutes(30)); // 设置30分钟的有效期

        try {
            // 3 创建订单到数据库
            paymentOrder.setOrderId(orderId);
            paymentOrder.setStatus("INIT");
            createOrderRecord(paymentOrder);
            return "订单创建成功 : order: " + orderId;
        } catch (Exception e) {
            log.error("[DB] 支付处理失败：orderId={}, 错误信息={}", orderId, e.getMessage());
            throw new RuntimeException("订单创建失败 : order: " + orderId, e);
        }
    }

    @Override
    public void createOrderRecord(PaymentOrder paymentOrder) {
        try {
            String orderId = paymentOrder.getOrderId();
            BigDecimal amount = paymentOrder.getAmount();
            String currency = paymentOrder.getCurrency();
            String userId = paymentOrder.getUserId();
            String status = paymentOrder.getStatus();

            jdbcTemplate.update(
                    "INSERT INTO payment_order (order_id, amount, currency, user_id, status) VALUES (?, ?, ?, ?, ?)",
                    orderId, amount, currency, userId, status
            );


            PaymentOrder order = checkPaymentService.findOrderByOrderId(paymentOrder.getOrderId());

            if (order != null) {
                log.info("[DB] 订单创建成功：orderId={}, amount={}, currency={}, userId={}, status={}",
                        order.getOrderId(), order.getAmount(), order.getCurrency(), order.getUserId(), order.getStatus());
                // 4. 发送支付消息 用户获取到订单创建成功信息
                paymentPublisher.publishNewOrder(paymentOrder);
            } else {
                log.warn("[DB] 未找到订单：orderId={}", orderId);
                throw new RuntimeException("订单创建失败");
            }

        } catch (Exception e) {
            log.error("[DB] 创建支付记录失败：orderId={}, 错误信息={}", paymentOrder.getOrderId(), e.getMessage());
            throw e; // 抛出异常以触发事务回滚
        }
    }

}
