package com.example.demo.services.factory;

import com.example.demo.entities.Account;
import com.example.demo.entities.Transfer;
import com.example.demo.services.AccountServiceImpl;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Setter
public class TargetAccountBalanceCalculator {

    private AccountServiceImpl accountService;
    private GetRate getRate;

    @Autowired
    public TargetAccountBalanceCalculator(AccountServiceImpl accountService, GetRate getRate) {
        this.accountService = accountService;
        this.getRate = getRate;
    }

    public void setBalanceOfTargetAccount(Transfer transfer) {
        Account sendingAccount = accountService.findByAccountNumber(transfer.getSendingAccount().getAccountNumber());
        Account targetAccount = accountService.findByAccountNumber(transfer.getTargetAccount().getAccountNumber());
        BigDecimal amount = transfer.getAmount();

        if (sendingAccount.getCurrency().equals(targetAccount.getCurrency())) {
            targetAccount.setBalance(targetAccount.getBalance().add(amount));
        } else {
            BigDecimal amountAfterCurrencyConversion = amount.multiply(getRate.execute(sendingAccount.getCurrency(), targetAccount.getCurrency()));
            targetAccount.setBalance(targetAccount.getBalance().add(amountAfterCurrencyConversion));
        }
        accountService.save(targetAccount);
    }

}
