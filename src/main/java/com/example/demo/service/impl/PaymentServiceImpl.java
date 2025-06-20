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

    @Override
    @Transactional
    public void updateStatus(String orderId, String status) {
        try {
            int updated = jdbcTemplate.update(
                    "UPDATE payment_order SET status = ? WHERE order_id = ?",
                    status, orderId
            );
            if (updated > 0) {
                log.info("[DB] 成功更新订单状态：orderId={}, status={}", orderId, status);
            } else {
                log.warn("[DB] 未找到订单：orderId={}, 期望更新为 status={}", orderId, status);
            }
        } catch (Exception e) {
            log.error("[DB] 更新订单状态失败：orderId={}, status={}, 错误信息={}", orderId, status, e.getMessage());
            throw e; // 抛出异常以触发事务回滚
        }


    }

    /**
     * @return 
     */
    @Override
    public String processPayment(Payment payment) {
        // 设置初始状态、生成订单号
        String orderId = UUID.randomUUID().toString().replaceAll("-", "");
        payment.setOrderId(orderId);
        payment.setStatus("INIT"); // 初始状态为 INIT
        log.info("[DB] 开始处理支付：orderId={}, amount={}, currency={}, userId={}",
                payment.getOrderId(), payment.getAmount(), payment.getCurrency(), payment.getUserId());
        try {
            // 模拟支付处理逻辑
            // 这里可以添加实际的支付处理代码，比如调用支付网关等
            // 假设支付成功，更新状态为 SUCCESS
            payment.setStatus("SUCCESS");
            createPaymentRecord(payment); // 保存支付记录到数据库
            log.info("[DB] 支付处理成功：orderId={}, status={}", orderId, payment.getStatus());
            return "Payment processed successfully for order: " + orderId;
        } catch (Exception e) {
            log.error("[DB] 支付处理失败：orderId={}, 错误信息={}", orderId, e.getMessage());
            throw new RuntimeException("Payment processing failed for order: " + orderId, e);
        }

    }

    @Transactional
    public void createPaymentRecord(Payment payment) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO payment (order_id, amount, currency, user_id, status) VALUES (?, ?, ?, ?, ?)",
                     payment.getOrderId(), payment.getAmount(), payment.getCurrency(),
                    payment.getUserId(), payment.getStatus()
            );
            log.info("[DB] 成功创建支付记录：orderId={}, amount={}, currency={}, userId={}, status={}",
                    payment.getOrderId(), payment.getAmount(), payment.getCurrency(), payment.getUserId(), payment.getStatus());
        } catch (Exception e) {
            log.error("[DB] 创建支付记录失败：orderId={}, 错误信息={}", payment.getOrderId(), e.getMessage());
            throw e; // 抛出异常以触发事务回滚
        }
    }
}
