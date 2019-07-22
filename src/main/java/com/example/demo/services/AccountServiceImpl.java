package com.example.demo.services;

import com.example.demo.dao.AccountRepository;
import com.example.demo.entities.Account;
import com.example.demo.entities.DTO.AccountBalanceUpdateDto;
import com.example.demo.entities.DTO.AccountNameUpdateDto;
import com.example.demo.exceptions.AccountDoesNotExistException;
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

    // TODO: 22.07.2019  wynieść ifa do osobnej metody
    public Account updateAccountBalance(String accountNumber, AccountBalanceUpdateDto accountBalanceUpdate) {
        BigDecimal updatedAccountBalance = accountBalanceUpdate.getBalance();
        Optional<Account> accountToUpdate = Optional.ofNullable(accountRepository.findByAccountNumber(accountNumber));
        if (!accountToUpdate.isPresent()) {
            throw new AccountDoesNotExistException("Nie znaleziono konta o numerze " + accountToUpdate);
        }
        accountToUpdate.get().setBalance(updatedAccountBalance);
        return accountRepository.save(accountToUpdate.get());
    }

    public BigDecimal getCurrentBalance(String account_number) {
        Optional<Account> account = Optional.ofNullable(accountRepository.findByAccountNumber(account_number));
        if (!account.isPresent()) {
            throw new AccountDoesNotExistException("Nie znaleziono konta o numerze " + account_number);
        }
        return account.get().getBalance();
    }

    public Account changeAccountName(String accountNumber, String newAccountName) {
        Optional<Account> accountToChangeName = Optional.ofNullable(accountRepository.findByAccountNumber(accountNumber));
        if (!accountToChangeName.isPresent()) {
            throw new AccountDoesNotExistException("Nie znaleziono konta o numerze " + accountNumber);
        }
        accountToChangeName.get().setName(newAccountName);
        return accountRepository.save(accountToChangeName.get());
    }

    public Account findByAccountNumber(String accountNumber) {
        Optional<Account> account = Optional.ofNullable(accountRepository.findByAccountNumber(accountNumber));
        if (!account.isPresent()) {
            throw new AccountDoesNotExistException("Nie znaleziono konta o numerze " + accountNumber);
        }
        return account.get();
    }

    public Account findById(Integer account_id) {
        Optional<Account> account = accountRepository.findById(account_id);
        if (!account.isPresent()) {
            throw new AccountDoesNotExistException("Nie znaleziono konta o id " + account_id);
        }
        return account.get();
    }

    public Account updateAccountName(AccountNameUpdateDto accountName, Integer id) {
        Optional<Account> accountToChangeName = accountRepository.findById(id);
        if (!accountToChangeName.isPresent()) {
            throw new AccountDoesNotExistException("Nie znaleziono konta o id " + id);
        }
        accountToChangeName.get().setName(accountName.getAccountName());
        return accountRepository.save(accountToChangeName.get());
    }

    public void deleteAccount(Integer accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (!account.isPresent()) {
            throw new AccountDoesNotExistException("Nie znaleziono konta o id " + accountId);
        }
        accountRepository.deleteById(accountId);
    }

}
