package com.example.demo;


import com.example.demo.dao.AccountRepository;
import com.example.demo.dao.TransferRepository;
import com.example.demo.entities.Account;
import com.example.demo.entities.Transfer;
import com.example.demo.entities.User;
import com.example.demo.services.AccountServiceImpl;
import com.example.demo.services.TransferServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringRunner.class)
//@SpringBootTest
public class TransferServiceTest {

    @MockBean
    TransferRepository transferRepository;

    @MockBean
    TransferServiceImpl transferService;

    @MockBean
    AccountRepository accountRepository;

    @MockBean
    AccountServiceImpl accountService;

    User user;

    @Before
    public void init() {
        user = new User();
        Account account = new Account("76116022020000000034364384", new BigDecimal(200), user, "PLN", "Name");
        Account account2 = new Account("77116022020000000034364384", new BigDecimal(100), user, "PLN", "NameSecond");
        accountRepository.save(account);
        accountRepository.save(account2);

//       accountService = new AccountServiceImpl(accountRepository);
//
        transferService = new TransferServiceImpl();
    }


    @Test
    public void transferBeforePostShouldHasStatusPending() {
        Transfer transfer = new Transfer();
        Transfer transfer2 = new Transfer();
        BigDecimal amount = new BigDecimal(20);
        transfer.setAmount(amount);
        transfer.setSendingAccountNumber("76116022020000000034364384");
        transfer.setTargetAccountNumber("77116022020000000034364384");
        transfer.setStatus("PENDING");
        transfer2 = transferService.changeTransferStatusToCompleted(transfer);

        Assert.assertThat(transfer2.getStatus(), equalTo("COMPLETED"));

    }

}
