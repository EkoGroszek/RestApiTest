package com.example.demo.converter;

import com.example.demo.dao.AccountRepository;
import com.example.demo.entities.Account;
import com.example.demo.entities.DTO.ExternalTransferDto;
import com.example.demo.entities.ExternalTransfer;

import java.time.LocalDateTime;

public class ExternalTransferDtoToExternalTransferConverter implements Converter<ExternalTransferDto, ExternalTransfer> {

    AccountRepository accountRepository;

    public ExternalTransferDtoToExternalTransferConverter(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public ExternalTransfer convert(ExternalTransferDto from) {
        ExternalTransfer externalTransfer = new ExternalTransfer();
                externalTransfer.builder()
                        .amount(from.getAmount())
                        .dateOfSendingTransfer(LocalDateTime.now())
                        .sendingAccount(getSendingAccount(from))
                        .targetAccount(from.getToAccount())
                        .build();

        return externalTransfer;
    }

    private Account getSendingAccount(ExternalTransferDto from) {
        Account sendingAccount = accountRepository.findByAccountNumber(from.getExternalAccount());
        return sendingAccount;

    }
}
