package com.example.demo.component;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class IdempotencyChecker {

    private StringRedisTemplate redis;

    public boolean checkAndMark(String key) {
        // 尝试设置键值对，设置成功则表示请求未处理
        Boolean success = redis.opsForValue().setIfAbsent(key, String.valueOf(Duration.ofMinutes(10)));
        return Boolean.TRUE.equals(success);
    }
}
