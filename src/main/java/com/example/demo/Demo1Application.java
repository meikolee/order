package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/*
 * 高级Java开发岗位技术 DEMO：换汇支付系统
 * 技术点：Spring Boot、MyBatis、Redis、RabbitMQ、分布式事务、幂等设计、链路追踪、JVM优化点预留
 * 新增内容：
 * ✅ 完整接口控制器（支付、转账、对账、幂等、MQ 发布）
 * ✅ MQ 消费者（RabbitListener）
 * ✅ 幂等校验 + 落库 + 回调模拟逻辑
 * ✅ Swagger 接口文档配置
 * ✅ 所有新增部分已标注 [新增] 注释
 */
@SpringBootApplication
@EnableTransactionManagement
public class Demo1Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo1Application.class, args);
    }

}
