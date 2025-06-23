package com.example.demo.controller;

import com.example.demo.dto.ExchangeRequest;
import com.example.demo.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

// 2. 汇率计算 Controller
@RestController
@RequestMapping("/api/exchange")
public class ExchangeController {

    @Autowired
    private ExchangeService exchangeService;

    @PostMapping("/convert")
    public ResponseEntity<BigDecimal> convert(@RequestBody ExchangeRequest request) {

        BigDecimal result = exchangeService.convertCurrency(request);
        return ResponseEntity.ok(result);

    }

}
