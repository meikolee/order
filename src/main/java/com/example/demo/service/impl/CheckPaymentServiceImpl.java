package com.example.demo.service.impl;
import com.example.demo.entity.Payment;
import com.example.demo.entity.PaymentOrder;
import com.example.demo.service.CheckPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CheckPaymentServiceImpl implements CheckPaymentService {
    // 模拟支付状态更新

    @Autowired
    private JdbcTemplate jdbcTemplate;// 使用 JdbcTemplate 进行数据库操作

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


    /**
     * @param orderId
     * @return
     */
    @Override
    public PaymentOrder findOrderByOrderId(String orderId) {
        // 模拟从数据库中查找订单
        log.info("[DB] 查找订单：orderId={}", orderId);
        PaymentOrder paymentOrder = jdbcTemplate.queryForObject(
                "SELECT * FROM payment_order WHERE order_id = ?",
                new Object[]{orderId},
                (rs, rowNum) -> {
                    PaymentOrder p = new PaymentOrder();
                    p.setOrderId(rs.getString("order_id"));
                    p.setAmount(rs.getBigDecimal("amount"));
                    p.setCurrency(rs.getString("currency"));
                    p.setUserId(rs.getString("user_id"));
                    p.setStatus(rs.getString("status"));
                    return p;
                }
        );
        if (paymentOrder != null) {
            log.info("[DB] 找到订单：{}", paymentOrder);
        } else {
            log.warn("[DB] 未找到订单：orderId={}", orderId);
        }
        return paymentOrder;
    }
}
