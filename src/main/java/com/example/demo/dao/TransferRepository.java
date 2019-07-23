package com.example.demo.dao;

import com.example.demo.entities.Account;
import com.example.demo.entities.Transfer;
import com.example.demo.entities.TransferStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends CrudRepository<Transfer, Integer> {
    List<Transfer> findAllBySendingAccountId(Integer id);
    List<Transfer> findAllByTargetAccountId(Integer id);
    List<Transfer> findAllByStatus(String status);
}
