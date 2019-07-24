package com.example.demo.services;

import com.example.demo.entities.DTO.ExternalTransferDto;
import com.example.demo.entities.ExternalTransfer;
import com.example.demo.entities.Transfer;
import org.springframework.stereotype.Service;

@Service
public class ExternalTransferServiceImpl {


    public ExternalTransfer createNewTransfer(ExternalTransferDto transfer) {
        ExternalTransfer externalTransfer = new ExternalTransfer();

        return externalTransfer;

    }
}
