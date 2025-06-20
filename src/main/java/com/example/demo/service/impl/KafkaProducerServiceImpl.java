package com.example.demo.service.impl;

import com.example.demo.dto.Payment;
import com.example.demo.service.KafkaProducerService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {
    // 这里可以添加 Kafka 生产者的相关逻辑
    // 例如发送消息到 Kafka 主题等操作

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate; // 假设有一个 KafkaTemplate 用于发送消息

    // 示例方法
    public void sendMessage(String topic, String message) {
        // 发送消息到指定的 Kafka 主题
        // 实际实现需要使用 Kafka 的客户端库来完成
        System.out.println("Sending message to topic " + topic + ": " + message);
    }

    @Override
    public void sendPayLog(Payment payment) {
        // 发送支付日志到 Kafka
        String topic = "pay-log"; // 假设有一个 Kafka 主题用于支付日志
        String json = new Gson().toJson(payment); // 将 Payment 对象转换为 JSON 字符串
        kafkaTemplate.send(topic, json);
        log.info("发送日志到 :  Kafka topic {}: {}", topic, json);
    }
}
