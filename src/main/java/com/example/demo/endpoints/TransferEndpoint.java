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

    @GetMapping("/{account_number}")
    public ResponseEntity<List<Transfer>> getTransfersListByUserName(@PathVariable String account_number) {
        return new ResponseEntity<>(transferService.getTransfersListForAccountByUserName(account_number), HttpStatus.OK);
    }

    @GetMapping("/sended/{account_id}")
    public ResponseEntity<List<Transfer>> getSendedTransfersListById(@PathVariable Integer account_id) {
        return new ResponseEntity<>(transferService.getSendedTransfersListForAccountById(account_id), HttpStatus.OK);
    }

    @GetMapping("/received/{account_id}")
    public ResponseEntity<List<Transfer>> getRecievedTransfersListById(@PathVariable Integer account_id) {
        return new ResponseEntity<>(transferService.getRecievedTransfersListForAccountById(account_id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Transfer> createTransfer(@RequestBody Transfer transfer) {
        return new ResponseEntity<>(transferService.createNewTransfer(transfer), HttpStatus.OK);
    }
}
