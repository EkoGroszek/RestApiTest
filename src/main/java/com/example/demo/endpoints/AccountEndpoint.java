package com.example.demo.endpoints;

import com.example.demo.entities.Account;
import com.example.demo.entities.DTO.AccountBalanceUpdateDto;
import com.example.demo.entities.DTO.AccountNameUpdateDto;
import com.example.demo.services.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/accounts")
public class AccountEndpoint {
    private AccountServiceImpl accountService;

    @Autowired
    public AccountEndpoint(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Account>> getAll() {
        return new ResponseEntity<>(accountService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<BigDecimal> getCurrentBalance(@PathVariable String accountNumber) {
        return new ResponseEntity<>(accountService.getCurrentBalance(accountNumber), HttpStatus.OK);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable Integer accountId) {
        return new ResponseEntity<>(accountService.findById(accountId), HttpStatus.OK);
    }

    // TODO: 23.07.2019 dopisać nowy exception żeby rzucało jak chce się zduplikować konto
    @PostMapping
    public ResponseEntity<Account> addAccount(@RequestBody Account account) {
        return new ResponseEntity<>(accountService.save(account), HttpStatus.OK);
    }

    @PatchMapping("/{accountNumber}")
    public ResponseEntity<Account> updateAccountBalance(@RequestBody AccountBalanceUpdateDto accountBalanceUpdate, @PathVariable String accountNumber) {
        return new ResponseEntity<>(accountService.updateAccountBalance(accountNumber, accountBalanceUpdate), HttpStatus.OK);
    }

    @PatchMapping("/{accountNumber}/name")
    public ResponseEntity<Account> changeAccountName(@RequestBody String newAccountName, @PathVariable String accountNumber) {
        return new ResponseEntity<>(accountService.changeAccountName(accountNumber, newAccountName), HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Account> updateAccountName(@RequestBody AccountNameUpdateDto accountName, @PathVariable Integer id) {
        return new ResponseEntity<>(accountService.updateAccountName(accountName, id), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") Integer id) {
        accountService.deleteAccount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
