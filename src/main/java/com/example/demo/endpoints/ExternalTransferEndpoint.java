package com.example.demo.endpoints;

import com.example.demo.entities.DTO.ExternalTransferDto;
import com.example.demo.entities.ExternalTransfer;
import com.example.demo.entities.Transfer;
import com.example.demo.services.ExternalTransferServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/externalTransfers")
public class ExternalTransferEndpoint {
    private ExternalTransferServiceImpl externalTransferService;

    @Autowired
    public ExternalTransferEndpoint(ExternalTransferServiceImpl externalTransferService) {
        this.externalTransferService = externalTransferService;
    }

    @PostMapping
    public ResponseEntity<ExternalTransfer> createExternalTransfer(@RequestBody ExternalTransferDto externalTransferDto) {
        return new ResponseEntity<>(externalTransferService.createNewTransfer(externalTransferDto), HttpStatus.OK);
    }
}
