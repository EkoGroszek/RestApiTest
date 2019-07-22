package com.example.demo.dao;

import com.example.demo.entities.Transfer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends CrudRepository <Transfer, Integer>{
    List<Transfer> findAllBySendingAccountNumber(String accountNumber);
    List<Transfer> findAllByTargetAccountNumber(String accountNumber);
//    List<Transfer> findAllBySendingAccountId(Integer accountId);

    List<Transfer> findAllById(Integer accountId);
}
