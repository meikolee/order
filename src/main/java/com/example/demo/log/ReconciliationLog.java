package com.example.demo.log;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

// 9. 对账日志模块（每日记录资金流与差异）
@Entity
@Table(name = "reconciliation_log")
@Setter
@Getter
public class ReconciliationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;
    private BigDecimal systemTotal;
    private BigDecimal bankTotal;
    private BigDecimal difference;

    private String status; // MATCHED / MISMATCHED
}
