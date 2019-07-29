package com.example.demo.services;

import com.example.demo.dao.AccountRepository;
import com.example.demo.entities.Account;
import com.example.demo.entities.DTO.AccountBalanceUpdateDto;
import com.example.demo.entities.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)

public class AccountServiceImplTest {
    private User user;

    @MockBean
    private Account account;

    @MockBean
    private AccountServiceImpl accountService;

    private AccountRepository accountRepository;


    @Before
    public void init() {
        accountRepository = mock(AccountRepository.class);
        accountService = new AccountServiceImpl(accountRepository);
        user = new User();
        account = new Account("76116022020000000034364384", new BigDecimal(200), user, "PLN", "Name");
        accountRepository.save(account);
    }


    @Test
    public void accountBalanceShouldBeRightAfterUpdate() {
        //GIVEN
        String accountNumber = account.getAccountNumber();
        AccountBalanceUpdateDto accountBalanceUpdateDto = AccountBalanceUpdateDto.builder()
                .accountNumber(accountNumber)
                .balance(BigDecimal.valueOf(220))
                .build();
        //WHEN
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        accountService.updateAccountBalance(accountNumber, accountBalanceUpdateDto);
        //THEN
        Assert.assertThat(account.getBalance(), equalTo(BigDecimal.valueOf(220)));

    }


}
