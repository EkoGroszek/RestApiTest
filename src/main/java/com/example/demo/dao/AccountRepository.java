package com.example.demo.dao;

import com.example.demo.entities.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {
    Account findByAccountNumber(String accountNumber);
    Optional<Account> findById(Integer iD);
}
