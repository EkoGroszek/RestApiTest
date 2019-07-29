package com.example.demo.services.factory;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

public interface GetRate {
     BigDecimal execute(String sentCurrency, String targetCurrency);
}
