package com.example.demo.services;


import com.example.demo.dao.TransferRepository;
import com.example.demo.entities.Account;
import com.example.demo.entities.Transfer;
import com.example.demo.entities.User;
import com.example.demo.services.factory.TargetAccountBalanceCalculator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
//@SpringBootTest
public class TransferServiceTest {

    private TransferRepository transferRepository;

    @MockBean
    private TransferServiceImpl transferService;

    @MockBean
    private AccountServiceImpl accountService;
    @MockBean
    private JavaMailSender javaMailSender;

    private TargetAccountBalanceCalculator targetAccountBalanceCalculator;

    private Account sendingAccount;

    private Account targetAccount;

    private User user;

    private Transfer transfer;

    @Before
    public void init() {
        user = new User();
        sendingAccount = new Account("76116022020000000034364384", new BigDecimal(200), user, "PLN", "Name");
        targetAccount = new Account("77116022020000000034364384", new BigDecimal(100), user, "PLN", "NameSecond");
        transfer = Transfer.builder()
                .amount(BigDecimal.valueOf(10))
                .sendingAccount(sendingAccount)
                .targetAccount(targetAccount)
                .ifSendEmail(false)
                .build();

        accountService = mock(AccountServiceImpl.class);
        when(accountService.findByAccountNumber("76116022020000000034364384")).thenReturn(sendingAccount);
        when(accountService.findByAccountNumber("77116022020000000034364384")).thenReturn(targetAccount);

        when(accountService.save(sendingAccount)).thenReturn(sendingAccount);
        when(accountService.save(targetAccount)).thenReturn(targetAccount);

        transferRepository = mock(TransferRepository.class);
        when(transferRepository.save(transfer)).thenReturn(transfer);
        transferService = new TransferServiceImpl(transferRepository, accountService, javaMailSender, targetAccountBalanceCalculator);
    }

    @Test
    public void transferAfterCreatingShouldHasStatusPending() {
        //WHEN
        Transfer result = transferService.createNewTransfer(transfer);
        //THEN
        Assert.assertThat(transfer.getStatus(), equalTo("PENDING"));
    }

    @Test
    public void transferWithAddressEmailShouldCallSendEmailOnce() {
        transfer.setIfSendEmail(true);
        transferService.createNewTransfer(transfer);
        verify(javaMailSender, times(1)).send((SimpleMailMessage) any());
    }

    @Test
    public void transferWithoutAddressEmailShouldNotCallSendEmail() {
        transferService.createNewTransfer(transfer);
        verify(javaMailSender, times(0)).send((SimpleMailMessage) any());
    }

}
