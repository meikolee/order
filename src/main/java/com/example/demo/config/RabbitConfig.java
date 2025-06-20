package com.example.demo.config;

import ch.qos.logback.classic.pattern.MessageConverter;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

import java.util.HashMap;
import java.util.Map;

// [新增] 6. RabbitMQ 配置类（确保绑定）
@Slf4j
@Configuration
public class RabbitConfig {


    @Bean
    public Queue paymentQueue() {
        return new Queue("payment.queue", true);
    }

    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange("payment.exchange");
    }

    @Bean
    public Binding binding(Queue paymentQueue, DirectExchange paymentExchange) {
        return BindingBuilder.bind(paymentQueue).to(paymentExchange).with("payment.process");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        log.info("[MQ] RabbitTemplate 初始化");
        return new RabbitTemplate(connectionFactory);
    }

    // 延迟队列配置
    @Bean
    public Queue delayedQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "payment.dlx.exchange");
        args.put("x-dead-letter-routing-key", "payment.dlx.routing");
        args.put("x-message-ttl", 10000); // 消息过期时间，单位为毫秒，这里设置为 10 秒
        return new Queue("payment.delay.queue", true, false, false, args);
    }

    /*
     * 死信交换机和路由键配置
     * 当延迟队列中的消息过期后，将会被发送到死信交换机
     */
    @Bean
    public DirectExchange delayedExchange() {
        return new DirectExchange("payment.dlx.exchange");
    }

    @Bean
    public Queue dlxQueue() {
        return new Queue("payment.dlx.queue", true);
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue()).to(delayedExchange()).with("payment.dlx.routing");
    }
}
