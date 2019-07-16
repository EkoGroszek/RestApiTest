package com.example.demo.services;

import com.example.demo.dao.AccountRepository;
import com.example.demo.entities.Account;
import com.example.demo.entities.DTO.AccountNameUpdateDTO;
import com.example.demo.exceptions.AccountDoesNotExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountServiceImpl {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Iterable<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Account updateAccountBalance(String accountNumber, BigDecimal balance) {
        Account accountToUpdate = accountRepository.findByAccountNumber(accountNumber);
        accountToUpdate.setBalance(balance);

        return accountRepository.save(accountToUpdate);
    }

    public BigDecimal getCurrentBalance(String account_number) {
        return accountRepository.findByAccountNumber(account_number).getBalance();
    }

    public Account changeAccountName(String accountNumber, String newAccountName) {
        Account accountToChangeName = accountRepository.findByAccountNumber(accountNumber);
        accountToChangeName.setName(newAccountName);

        return accountRepository.save(accountToChangeName);

    }

    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public Account findById(Integer account_id) {
        Account account = accountRepository.findById(account_id).get();
        return account;
    }

    public Account updateAccountName(AccountNameUpdateDTO accountName, Integer id) {
        Account accountToChangeName = accountRepository.findById(id).get();
        accountToChangeName.setName(accountName.getAccountName());

        return accountRepository.save(accountToChangeName);

    }

    public void deleteAccount(Integer accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (!account.isPresent()) {
            throw new AccountDoesNotExist("Nie znaleziono uzytkownika o id " + accountId);
        }
        accountRepository.deleteById(accountId);
    }

}
