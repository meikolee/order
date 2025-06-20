package com.example.demo.log;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

// 9. 对账日志模块（每日记录资金流与差异）
@Entity
@Table(name = "reconciliation_order_log")
@Setter
@Getter
public class ReconciliationOrderLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;
    private String date;
    private BigDecimal systemTotal;
    private BigDecimal bankTotal;
    private BigDecimal difference;
    private BigDecimal localAmount; // 本地系统金额
    private BigDecimal remoteAmount; // 远程系统金额
    private String reason; // 差异原因

    private String status; // MATCHED / MISMATCHED
}
