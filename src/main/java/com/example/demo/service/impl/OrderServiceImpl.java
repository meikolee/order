package com.example.demo.service.impl;

import com.example.demo.dto.Payment;
import com.example.demo.dto.PaymentOrder;
import com.example.demo.service.CheckPaymentService;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentPublisher;
import com.example.demo.service.PaymentRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private PaymentRecordService paymentRecordService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CheckPaymentService checkPaymentService;

    @Autowired
    private PaymentPublisher paymentPublisher;

    @Override
    @Transactional
    public String creatOrder(PaymentOrder paymentOrder) {
        // 设置初始状态、生成订单号
        String orderId = UUID.randomUUID().toString().replaceAll("-", "");
        paymentOrder.setOrderId(orderId);
        // 初始状态为 INIT
        log.info("[DB] 1 创建订单 ：orderId={}, amount={}, currency={}, userId={}",
                paymentOrder.getOrderId(), paymentOrder.getAmount(), paymentOrder.getCurrency(), paymentOrder.getUserId());
        try {
            createPaymentRecord(paymentOrder); // 保存支付记录到数据库 事务不能通过this调用自身方法，
            return "订单创建成功 : order: " + orderId;
        } catch (Exception e) {
            log.error("[DB] 支付处理失败：orderId={}, 错误信息={}", orderId, e.getMessage());
            throw new RuntimeException("订单创建失败 : order: " + orderId, e);
        }
    }


    @Override
    public void createPaymentRecord(PaymentOrder paymentOrder) {
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

            // 查找订单 如果存在才发送消息
            PaymentOrder order = checkPaymentService.findOrderByOrderId(paymentOrder.getOrderId());

            if (order != null) {
                log.info("[DB] 订单创建成功：orderId={}, amount={}, currency={}, userId={}, status={}",
                        order.getOrderId(), order.getAmount(), order.getCurrency(), order.getUserId(), order.getStatus());
                // 确保成功才发布消息
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
