package com.example.demo.service.impl;

import com.example.demo.dto.Payment;
import com.example.demo.log.ReconciliationOrderLog;
import com.example.demo.service.CheckPaymentService;
import com.example.demo.service.PaymentPublisher;
import com.example.demo.service.PaymentRecordService;
import com.example.demo.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PaymentRecordServiceImpl implements PaymentRecordService {
    @Autowired
    private JdbcTemplate jdbcTemplate;// 使用 JdbcTemplate 进行数据库操作

    @Autowired
    private PaymentPublisher paymentPublisher; // 注入 PaymentPublisher

    @Autowired
    private CheckPaymentService checkPaymentService;

    @Override
    @Transactional
    public void createPaymentRecord(Payment payment) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO payment (order_id, amount, currency, user_id, status) VALUES (?, ?, ?, ?, ?)",
                    payment.getOrderId(), payment.getAmount(), payment.getCurrency(),
                    payment.getUserId(), payment.getStatus()
            );

            // 查找订单 如果存在才发送消息
            Payment order = checkPaymentService.findByOrderId(payment.getOrderId());

            if (order != null) { //  TODO 验证事务是否已经自动提交commit 已验证已自动提交
                log.info("[DB] 订单创建成功：orderId={}, amount={}, currency={}, userId={}, status={}",
                        payment.getOrderId(), payment.getAmount(), payment.getCurrency(), payment.getUserId(), payment.getStatus());
                // 确保成功才发布消息
                paymentPublisher.publishPayment(payment);
            } else {
                log.warn("[DB] 未找到订单：orderId={}", payment.getOrderId());
                // 抛出异常
                throw new RuntimeException("订单创建失败");
            }


        } catch (Exception e) {
            log.error("[DB] 创建支付记录失败：orderId={}, 错误信息={}", payment.getOrderId(), e.getMessage());
            throw e; // 抛出异常以触发事务回滚
        }
    }
        // 这里可以添加保存支付记录到数据库的逻辑
        // 例如使用 JdbcTemplate 或 JPA Repository
        // 目前仅作为示例，具体实现需要根据实际情况来完成

    @Override
    @Transactional
    public void updateStatus(String orderId, String status) {
        try {
            int updated = jdbcTemplate.update(
                    "UPDATE payment SET status = ? WHERE order_id = ?",
                    status, orderId
            );
            if (updated > 0) {
                // 查找订单 如果存在才发送消息
                Payment order = checkPaymentService.findByOrderId(orderId);
                // 如果状态是 PAID 则发布到成功队列
                if (order.getStatus().equals("PAID")) {
                    log.info("[DB] 成功更新订单状态：orderId={}, status={}", orderId, status);
                    paymentPublisher.publishPaymentSuccess(orderId);
                }
            } else {
                log.warn("[DB] 未找到订单：orderId={}, 期望更新为 status={}", orderId, status);
            }
        } catch (Exception e) {
            log.error("[DB] 更新订单状态失败：orderId={}, status={}, 错误信息={}", orderId, status, e.getMessage());
            throw e; // 抛出异常以触发事务回滚
        }


    }


}
