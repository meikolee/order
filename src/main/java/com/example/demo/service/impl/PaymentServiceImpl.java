package com.example.demo.service.impl;
import com.example.demo.dto.Payment;
import com.example.demo.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService{
    // 模拟支付状态更新

    @Autowired
    private JdbcTemplate jdbcTemplate;// 使用 JdbcTemplate 进行数据库操作

    @Autowired
    private PaymentRecordServiceImpl paymentRecordService; // 注入 PaymentRecordService

    /**
     * @return 
     */
    @Override
    public String processPayment(Payment payment) {
        // 设置初始状态、生成订单号
        String orderId = UUID.randomUUID().toString().replaceAll("-", "");
        payment.setOrderId(orderId);
         // 初始状态为 INIT
        log.info("[DB] 开始处理支付：orderId={}, amount={}, currency={}, userId={}",
                payment.getOrderId(), payment.getAmount(), payment.getCurrency(), payment.getUserId());
        try {
            // 模拟支付处理逻辑
            // 使用注入的服务调用事务方法
            paymentRecordService.createPaymentRecord(payment); // 保存支付记录到数据库 事务不能通过this调用自身方法，
            // 否则事务无法生效
            // 发布支付信息到 RabbitMQ
            // 更新订单状态为 COMPLETED

            log.info("[DB] 订单创建成功：orderId={}, status={}", orderId, payment.getStatus());
            return "Payment processed successfully for order: " + orderId;
        } catch (Exception e) {
            log.error("[DB] 支付处理失败：orderId={}, 错误信息={}", orderId, e.getMessage());
            throw new RuntimeException("Payment processing failed for order: " + orderId, e);
        }

    }

    /**
     * @param orderId 
     * @return
     */
    @Override
    public Payment findByOrderId(String orderId) {
        // 模拟从数据库中查找订单
        log.info("[DB] 查找订单：orderId={}", orderId);
        Payment payment = jdbcTemplate.queryForObject(
                "SELECT * FROM payment WHERE order_id = ?",
                new Object[]{orderId},
                (rs, rowNum) -> {
                    Payment p = new Payment();
                    p.setOrderId(rs.getString("order_id"));
                    p.setAmount(rs.getBigDecimal("amount"));
                    p.setCurrency(rs.getString("currency"));
                    p.setUserId(rs.getString("user_id"));
                    p.setStatus(rs.getString("status"));
                    return p;
                }
        );
        if (payment != null) {
            log.info("[DB] 找到订单：{}", payment);
        } else {
            log.warn("[DB] 未找到订单：orderId={}", orderId);
        }
        return payment;
    }
}
