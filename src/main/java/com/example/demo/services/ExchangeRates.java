package com.example.demo.services;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Setter
@Getter
public class ExchangeRates implements Serializable {

    private Map<String, BigDecimal> rates;

    private String base;

    private String date;

    @Override
    public String toString() {
        return "ExchangeRates{" +
                "rates='" + rates + '\'' +
                ", base='" + base + '\'' +
                ", date=" + date +
                '}';
    }
}
