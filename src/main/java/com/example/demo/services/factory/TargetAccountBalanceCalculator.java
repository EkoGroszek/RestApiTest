package com.example.demo.services.factory;

import com.example.demo.entities.Account;
import com.example.demo.entities.Transfer;
import com.example.demo.services.AccountServiceImpl;
import com.example.demo.services.ExchangeRates;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TargetAccountBalanceCalculator {
    public void setBalanceOfTargetAccount(Transfer transfer, AccountServiceImpl accountService) {

        Account sendingAccount = accountService.findByAccountNumber(transfer.getSendingAccount().getAccountNumber());
        Account targetAccount = accountService.findByAccountNumber(transfer.getTargetAccount().getAccountNumber());
        BigDecimal amount = transfer.getAmount();

        if (sendingAccount.getCurrency().equals(targetAccount.getCurrency())) {
            targetAccount.setBalance(targetAccount.getBalance().add(amount));
        } else {
            BigDecimal amountAfterCurrencyConversion = amount.multiply(getRate(sendingAccount.getCurrency(), targetAccount.getCurrency()));
            targetAccount.setBalance(targetAccount.getBalance().add(amountAfterCurrencyConversion));
        }
        accountService.save(targetAccount);
    }

    //// TODO: 22.07.2019  zabezpieczyć przed Nullpointerem
    private BigDecimal getRate(String sendedCurrency, String targetCurrency) {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "https://api.exchangeratesapi.io/latest?base=";

        ExchangeRates exchangeRates = restTemplate
                .getForObject(fooResourceUrl + sendedCurrency, ExchangeRates.class);
        return exchangeRates.getRates().get(targetCurrency);
    }


}
