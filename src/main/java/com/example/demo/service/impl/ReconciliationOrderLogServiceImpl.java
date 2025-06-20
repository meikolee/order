package com.example.demo.service.impl;

import com.example.demo.log.ReconciliationOrderLog;
import com.example.demo.service.ReconciliationOrderLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ReconciliationOrderLogServiceImpl implements ReconciliationOrderLogService {

    @Autowired
    private JdbcTemplate jdbcTemplate; // 使用 JdbcTemplate 进行数据库操作
    /**
     * @param logEntry
     */
    @Override
    @Transactional
    public void saveReconciliationLog(ReconciliationOrderLog logEntry) {
        // 这里可以添加保存对账日志到数据库的逻辑
        // 例如使用 JdbcTemplate 或 JPA Repository
        // 目前仅作为示例，具体实现需要根据实际情况来完成
        log.info("[DB] 保存对账日志：{}", logEntry);
        // 模拟保存操作 //	id	date 对账日期	system_total 系统汇总金额	bank_total 银行汇总金额	difference 差额	status 对账状态 MATCHED / MISMATCHED	order_id 支付订单ID	local_amount 本地金额	remote_amount 远程金额	reason 账变原因
        try {
            jdbcTemplate.update(
                    "INSERT INTO reconciliation_order_log (order_id, date, system_total, bank_total, difference, status, local_amount, remote_amount, reason) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    logEntry.getOrderId(), logEntry.getDate(), logEntry.getSystemTotal(), logEntry.getBankTotal(),
                    logEntry.getDifference(), logEntry.getStatus(), logEntry.getLocalAmount(),
                    logEntry.getRemoteAmount(), logEntry.getReason());
            log.info("[DB] 对账日志保存成功：orderId={}, date={}, status={}", logEntry.getOrderId(), logEntry.getDate(), logEntry.getStatus());
        } catch (Exception e) {
            log.error("[DB] 保存对账日志失败：orderId={}, 错误信息={}", logEntry.getOrderId(), e.getMessage());
            throw e; // 抛出异常以触发事务回滚
        }
    }
}
