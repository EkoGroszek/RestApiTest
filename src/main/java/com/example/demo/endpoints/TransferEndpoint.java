package com.example.demo.endpoints;

import com.example.demo.entities.Transfer;
import com.example.demo.services.TransferServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Transfer> getTransfersList(@PathVariable String account_number){
        return transferService.getTransfersListForAccount(account_number);
    }

    @PostMapping
    public Transfer createTransfer(@RequestBody Transfer transfer){
        return transferService.createNewTransfer(transfer);
    }
}
