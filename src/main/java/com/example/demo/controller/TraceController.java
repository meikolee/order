package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 8. 链路追踪（Spring Cloud Sleuth 示例）
@RestController
@RequestMapping("/api/trace")
public class TraceController {
    private static final Logger log = LoggerFactory.getLogger(TraceController.class);

    @GetMapping("/ping")
    public String ping() {
        log.info("ping received");
        return "pong";
    }
}
