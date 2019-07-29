package com.example.demo.services;


import com.example.demo.dao.AccountRepository;
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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringRunner.class)
//@SpringBootTest
public class TransferServiceTest {

    @MockBean
    private TransferRepository transferRepository;

    @MockBean
    private TransferServiceImpl transferService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountServiceImpl accountService;

    @MockBean
    private JavaMailSender javaMailSender;

    private TargetAccountBalanceCalculator targetAccountBalanceCalculator;

    private Account sendingAccount;

    private Account targetAccount;

    private User user;

    @Before
    public void init() {
        user = new User();
        sendingAccount = new Account("76116022020000000034364384", new BigDecimal(200), user, "PLN", "Name");
        targetAccount = new Account("77116022020000000034364384", new BigDecimal(100), user, "PLN", "NameSecond");
        accountRepository.save(sendingAccount);
        accountRepository.save(targetAccount);
        transferService = new TransferServiceImpl(transferRepository, accountService, javaMailSender, targetAccountBalanceCalculator);
    }

    @Test
    public void transferBeforePostShouldHasStatusPending() {
        //GIVEN
        Transfer transfer = new Transfer();
        Transfer transfer2 = new Transfer();
        BigDecimal amount = new BigDecimal(20);
        transfer.setAmount(amount);
        transfer.setSendingAccount(sendingAccount);
        transfer.setTargetAccount(targetAccount);
        transfer.setStatus("PENDING");

        //WHEN
        transfer2 = transferService.changeTransferStatusToCompleted(transfer);

        //THEN
        Assert.assertThat(transfer2.getStatus(), equalTo("COMPLETED"));
    }

    @Test
    public void transferBetweenTwoAccountsInTheSameCurrencyShouldAddRightAmountOfMoneyToTargetAccount() {
//        //GIVEN
//        Transfer transfer = Transfer.builder()
//                .amount(new BigDecimal(10))
//                .sendingAccount(sendingAccount)
//                .targetAccount(targetAccount)
//                .build();
//        transferService.se
//
//        //WHEN
//        //THEN
//        Assert.as
    }



    //GIVEN
    //WHEN
    //THEN

}
