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

    /**
     * 创建一个支付队列的Bean
     *
     * @return Queue实例，用于处理支付相关的消息队列
     */
    @Bean
    public Queue paymentQueue() {
        return new Queue("payment.queue", true);
    }

    /**
     * 创建一个支付订单队列
     *
     * @return Queue实例，用于处理支付订单的消息
     */
    @Bean
    public Queue paymentOrderQueue() {
        return new Queue("payment.order.queue", true);// true表示持久化消息
    }

    /**
     * 创建一个名为payment.success.queue的队列
     * 该队列用于接收支付成功的消息
     * 队列的第二个参数表示是否持久化消息如果支付成功的消息需要在RabbitMQ重启后仍然保留那么可以选择将该值设置为true
     *
     * @return 新创建的支付成功队列
     */
    @Bean
    public Queue paymentSuccessQueue() {
        return new Queue("payment.success.queue", true);
    }

    @Bean
    public Binding paymentOrderBinding(Queue paymentOrderQueue, DirectExchange paymentExchange) {
        return BindingBuilder.bind(paymentOrderQueue).to(paymentExchange).with("payment.order");
    }

    /**
     * 创建一个绑定，用于将支付成功的消息队列与支付交换机进行连接
     * 此绑定使得支付成功的消息能够被正确地路由到支付成功队列中
     *
     * @param paymentSuccessQueue 支付成功队列，用于接收支付成功的消息
     * @param paymentExchange 支付交换机，用于路由支付相关的消息
     * @return 返回一个绑定对象，它定义了队列与交换机之间的路由关系
     */
    @Bean
    public Binding successBinding(Queue paymentSuccessQueue, DirectExchange paymentExchange) {
        return BindingBuilder.bind(paymentSuccessQueue).to(paymentExchange).with("payment.success.process");
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
