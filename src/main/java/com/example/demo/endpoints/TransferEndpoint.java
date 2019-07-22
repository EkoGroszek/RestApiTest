package com.example.demo.endpoints;

import com.example.demo.entities.Transfer;
import com.example.demo.services.TransferServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/transfers")
public class TransferEndpoint {
    private TransferServiceImpl transferService;

    @Autowired
    public TransferEndpoint(TransferServiceImpl transferService) {
        this.transferService = transferService;
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<Transfer>> getTransfersListByUserName(@PathVariable String accountNumber) {
        return new ResponseEntity<>(transferService.getTransfersListForAccountByUserName(accountNumber), HttpStatus.OK);
    }

    @GetMapping("/sended/{accountId}")
    public ResponseEntity<List<Transfer>> getSentTransfersListById(@PathVariable Integer accountId) {
        return new ResponseEntity<>(transferService.getSendedTransfersListForAccountById(accountId), HttpStatus.OK);
    }

    @GetMapping("/received/{accountId}")
    public ResponseEntity<List<Transfer>> getReceivedTransfersListById(@PathVariable Integer accountId) {
        return new ResponseEntity<>(transferService.getRecievedTransfersListForAccountById(accountId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Transfer> createTransfer(@RequestBody Transfer transfer) {
        return new ResponseEntity<>(transferService.createNewTransfer(transfer), HttpStatus.OK);
    }
}
