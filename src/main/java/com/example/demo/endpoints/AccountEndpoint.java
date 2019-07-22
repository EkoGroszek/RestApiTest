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

    // TODO: 22.07.2019  zmienić nazwy na camelcase
    @GetMapping("/balance/{account_number}")
    public ResponseEntity<BigDecimal> getCurrentBalance(@PathVariable String account_number) {
        return new ResponseEntity<>(accountService.getCurrentBalance(account_number), HttpStatus.OK);
    }

    @GetMapping("/{account_id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Integer account_id) {
        return new ResponseEntity<>(accountService.findById(account_id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Account> addAccount(@RequestBody Account account) {
        return new ResponseEntity<>(accountService.save(account), HttpStatus.OK);
    }

    @PatchMapping("/{account_number}")
    public ResponseEntity<Account> updateAccountBalance(@RequestBody AccountBalanceUpdateDto accountBalanceUpdate, @PathVariable String account_number) {
        return new ResponseEntity<>(accountService.updateAccountBalance(account_number, accountBalanceUpdate), HttpStatus.OK);
    }

    @PatchMapping("/{account_number}/name")
    public ResponseEntity<Account> changeAccountName(@RequestBody String newAccountName, @PathVariable String account_number) {
        return new ResponseEntity<>(accountService.changeAccountName(account_number, newAccountName), HttpStatus.OK);
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
