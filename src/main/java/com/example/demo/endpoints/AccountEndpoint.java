package com.example.demo.endpoints;

import com.example.demo.entities.Account;
import com.example.demo.entities.DTO.AccountBalanceInfoDTO;
import com.example.demo.entities.DTO.AccountBalanceUpdateDTO;
import com.example.demo.entities.Transfer;
import com.example.demo.services.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/accounts")
public class AccountEndpoint {
    private AccountServiceImpl accountService;

    @Autowired
    public AccountEndpoint(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/all")
    public Iterable<Account> getAll() {
        return accountService.findAll();
    }


    @GetMapping("/{account_number}")
    public BigDecimal getCurrentBalance(@PathVariable String account_number) {

        return accountService.getCurrentBalance(account_number);

    }

    @PostMapping
    public Account addAccount(@RequestBody Account account) {
        return accountService.save(account);
    }


    @PatchMapping("/{account_number}")
    public Account updateAccountBalance(@RequestBody AccountBalanceUpdateDTO accountBalanceUpdate, @PathVariable String account_number) {
        BigDecimal updatedAccountBalance = accountBalanceUpdate.getBalance();
        return accountService.updateAccountBalance(account_number, updatedAccountBalance);
    }

    @PatchMapping("/{account_number}/name")
    public Account changeAccountName(@RequestBody String newAccountName, @PathVariable String account_number) {

        return accountService.changeAccountName(account_number, newAccountName);
    }

}
