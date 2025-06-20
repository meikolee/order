package com.example.demo.service;

import com.example.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundService {
    @Autowired
    private AccountRepository accountRepository;
}
