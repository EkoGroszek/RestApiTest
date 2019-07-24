package com.example.demo.converter;

import com.example.demo.dao.AccountRepository;
import com.example.demo.entities.Account;
import com.example.demo.entities.DTO.ExternalTransferDto;
import com.example.demo.entities.ExternalTransfer;
import com.example.demo.exceptions.AccountDoesNotExistException;
import com.example.demo.services.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExternalTransferDtoToExternalTransferConverter implements Converter<ExternalTransferDto, ExternalTransfer> {

    AccountServiceImpl accountService;

    @Autowired
    public ExternalTransferDtoToExternalTransferConverter(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }


    @Override
    public ExternalTransfer convert(ExternalTransferDto from) {
        ExternalTransfer externalTransfer = ExternalTransfer.builder()
                .amount(from.getAmount())
                .dateOfSendingTransfer(LocalDateTime.now())
                .sendingAccount(getSendingAccount(from))
                .targetAccount(from.getToAccount())
                .build();

        return externalTransfer;
    }

    private Account getSendingAccount(ExternalTransferDto from) {
        Account sendingAccount = accountService.findByAccountNumber(from.getExternalAccount());
        return sendingAccount;

    }
}
