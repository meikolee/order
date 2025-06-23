package com.example.demo.service.impl;
import com.example.demo.entity.Payment;
import com.example.demo.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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
        log.info("[DB] 1 创建订单 ：orderId={}, amount={}, currency={}, userId={}",
                payment.getOrderId(), payment.getAmount(), payment.getCurrency(), payment.getUserId());
        try {
            paymentRecordService.createPaymentRecord(payment); // 保存支付记录到数据库 事务不能通过this调用自身方法，
            return "订单创建成功 : order: " + orderId;
        } catch (Exception e) {
            log.error("[DB] 支付处理失败：orderId={}, 错误信息={}", orderId, e.getMessage());
            throw new RuntimeException("订单创建失败 : order: " + orderId, e);
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
