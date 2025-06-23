package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

// [新增] 4. Payment 消息实体类
@Data
@Setter
@Getter
@Table(name = "recharge_order")
@JsonIgnoreProperties(ignoreUnknown = true) // 忽略未知属性
public class RechargeOrder implements Serializable {
    private static final long serialVersionUID = 1L;// 序列化版本号

    private String id; // 支付ID
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String userId;
    private String status="INIT"; // 支付状态
}
