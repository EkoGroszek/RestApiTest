package com.example.demo.services;

import com.example.demo.converter.ExternalTransferDtoToExternalTransferConverter;
import com.example.demo.dao.AccountRepository;
import com.example.demo.dao.ExternalTransferRepository;
import com.example.demo.entities.Account;
import com.example.demo.entities.DTO.ExternalTransferDto;
import com.example.demo.entities.ExternalTransfer;
import com.example.demo.entities.Transfer;
import com.example.demo.exceptions.NoEnoughMoneyException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class ExternalTransferServiceImpl {

    private ExternalTransferDtoToExternalTransferConverter converter;
    private ExternalTransferRepository externalTransferRepository;

    @Autowired
    public ExternalTransferServiceImpl(ExternalTransferDtoToExternalTransferConverter converter, ExternalTransferRepository externalTransferRepository) {
        this.converter = converter;
        this.externalTransferRepository = externalTransferRepository;
    }

    // TODO: 24.07.2019
    public ExternalTransfer createNewTransfer(ExternalTransferDto transfer) throws NoEnoughMoneyException {
        ExternalTransfer externalTransfer = converter.convert(transfer);
        BigDecimal amount = externalTransfer.getAmount();
        Account sendingAccount = externalTransfer.getSendingAccount();
        transfer.setCurrency(sendingAccount.getCurrency());
        transfer.setBankName("Wiktor77");
        if (!(transfer.getAmount().compareTo(sendingAccount.getBalance()) == 1)) {
            subtractAmountFromSendingAccount(sendingAccount, amount);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Object> objectResponseEntity = null;
                objectResponseEntity = restTemplate.postForEntity("https://comarch.herokuapp.com/transfer/external-transfer", transfer, null);


        } else {
            throw new NoEnoughMoneyException("Zbyt mało pieniędzy ka koncie");
        }

        return externalTransferRepository.save(externalTransfer);
    }

    private void subtractAmountFromSendingAccount(Account sendingAccount, BigDecimal amount) {
        sendingAccount.setBalance(sendingAccount.getBalance().subtract(amount));
    }
}
