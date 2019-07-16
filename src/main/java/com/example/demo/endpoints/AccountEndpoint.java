package com.example.demo.endpoints;

import com.example.demo.entities.Account;
import com.example.demo.entities.DTO.AccountBalanceInfoDTO;
import com.example.demo.entities.DTO.AccountBalanceUpdateDTO;
import com.example.demo.entities.DTO.AccountNameUpdateDTO;
import com.example.demo.entities.Transfer;
import com.example.demo.entities.User;
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

    //test heroku v2z
    @Autowired
    public AccountEndpoint(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Account>> getAll() {                                                                  //tak jak tu trzeba wszystko opakowywać
        return new ResponseEntity<>(accountService.findAll(), HttpStatus.OK);
    }


    @GetMapping("/balance/{account_number}")
    public BigDecimal getCurrentBalance(@PathVariable String account_number) {
        return accountService.getCurrentBalance(account_number);
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
    public Account updateAccountBalance(@RequestBody AccountBalanceUpdateDTO accountBalanceUpdate, @PathVariable String account_number) {
        BigDecimal updatedAccountBalance = accountBalanceUpdate.getBalance();
        return accountService.updateAccountBalance(account_number, updatedAccountBalance);
    }

    @PatchMapping("/{account_number}/name")
    public Account changeAccountName(@RequestBody String newAccountName, @PathVariable String account_number) {

        return accountService.changeAccountName(account_number, newAccountName);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Account> updateAccountName(@RequestBody AccountNameUpdateDTO accountName, @PathVariable Integer id) {
        return new ResponseEntity<>(accountService.updateAccountName(accountName, id), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") Integer id) {
        accountService.deleteAccount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
