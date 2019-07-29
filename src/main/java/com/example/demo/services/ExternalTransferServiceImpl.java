package com.example.demo.services;

import com.example.demo.converter.ExternalTransferDtoToExternalTransferConverter;
import com.example.demo.dao.AccountRepository;
import com.example.demo.dao.ExternalTransferRepository;
import com.example.demo.entities.Account;
import com.example.demo.entities.DTO.ExternalTransferDto;
import com.example.demo.entities.ExternalTransfer;
import com.example.demo.entities.Transfer;
import com.example.demo.exceptions.NoEnoughMoneyException;
import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class ExternalTransferServiceImpl {

    private ExternalTransferDtoToExternalTransferConverter converter;

    private ExternalTransferRepository externalTransferRepository;

    private JavaMailSender javaMailSender;

    @Autowired
    public ExternalTransferServiceImpl(ExternalTransferDtoToExternalTransferConverter converter, ExternalTransferRepository externalTransferRepository,  JavaMailSender javaMailSender) {
        this.converter = converter;
        this.externalTransferRepository = externalTransferRepository;
        this.javaMailSender = javaMailSender;
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

        if (transfer.getIfSendEmail()) {
            sendEmail(transfer);
        }

        return externalTransferRepository.save(externalTransfer);
    }

    private void sendEmail(ExternalTransferDto transfer) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(transfer.getEmailAddress());

        msg.setSubject("Potwierdzenie przelewu zewnętrznego ");
        msg.setText("Pomyślnie przelano kwote : " + transfer.getAmount() + transfer.getCurrency() +
                " z konta " + transfer.getExternalAccount() +
                " na konto " + transfer.getToAccount());

        javaMailSender.send(msg);

    }

    private void subtractAmountFromSendingAccount(Account sendingAccount, BigDecimal amount) {
        sendingAccount.setBalance(sendingAccount.getBalance().subtract(amount));
    }
}
