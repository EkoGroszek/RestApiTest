package com.example.demo.services.factory;

import com.example.demo.services.ExchangeRates;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class GetRateFromExternalApi implements GetRate {

    public BigDecimal execute(String sentCurrency, String targetCurrency) {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "https://api.exchangeratesapi.io/latest?base=";

        ExchangeRates exchangeRates = restTemplate
                .getForObject(fooResourceUrl + sentCurrency, ExchangeRates.class);
        return exchangeRates.getRates().get(targetCurrency);
    }


}
