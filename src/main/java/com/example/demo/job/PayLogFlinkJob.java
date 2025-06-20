package com.example.demo.job;

import com.example.demo.dto.Payment;
import com.google.gson.Gson;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.math.BigDecimal;
import java.util.Properties;

public class PayLogFlinkJob {

    public static void main(String[] args) throws Exception {
        //StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 创建本地执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        // 读取kafka支付日志
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "192.168.137.135:9092");
        props.setProperty("group.id", "pay-log-group");

        // 创建Flink Kafka消费者并添加为数据源
        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>("pay-log", new SimpleStringSchema(), props);
        DataStream<String> stream = env.addSource(consumer);

        // 解析为payment 对象
        DataStream<Payment> paymentStream = stream
                .map(json -> new Gson().fromJson(json, Payment.class))
                .returns(Payment.class);

        // 实时统计金额
        paymentStream.map(payment -> payment.getAmount())
                .returns(BigDecimal.class)
                .windowAll(TumblingProcessingTimeWindows.of(Time.minutes(1)))
                .reduce(BigDecimal::add)
                .map(amount -> "实时统计金额：" + amount)
                .print();


        // 执行Flink作业
        env.execute("FxPay Flink Payment Job");



    }

}
