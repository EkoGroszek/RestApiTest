package com.example.demo.services.factory;

import com.example.demo.dao.AccountRepository;
import com.example.demo.entities.Account;
import com.example.demo.entities.Transfer;
import com.example.demo.entities.User;
import com.example.demo.services.AccountServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TargetAccountBalanceCalculatorTest {

    private TargetAccountBalanceCalculator targetAccountBalanceCalculator;

    private AccountServiceImpl accountService;

    @MockBean
    private AccountRepository accountRepository;

    private Account sendingAccount;

    private Account targetAccount;

    private Account targetAccountInEuro;

    private User user;

    private AccountServiceImpl accountServiceMock;

    private GetRate getRate;

    private GetRateFromExternalApi getRateFromExternalApi;

    @Before
    public void init() {
        user = new User();
        sendingAccount = new Account("76116022020000000034364384", new BigDecimal(200), user, "PLN", "Name");
        targetAccount = new Account("77116022020000000034364384", new BigDecimal(100), user, "PLN", "NameSecond");
        targetAccountInEuro = new Account("77006022020000000034364384", new BigDecimal(100), user, "EUR", "EurName");
        accountRepository.save(sendingAccount);
        accountRepository.save(targetAccount);
        accountRepository.save(targetAccountInEuro);
        accountService = new AccountServiceImpl(accountRepository);

        accountServiceMock = mock(AccountServiceImpl.class);
        when(accountServiceMock.findByAccountNumber(sendingAccount.getAccountNumber())).thenReturn(sendingAccount);
        when(accountServiceMock.findByAccountNumber(targetAccount.getAccountNumber())).thenReturn(targetAccount);
        when(accountServiceMock.findByAccountNumber(targetAccountInEuro.getAccountNumber())).thenReturn(targetAccountInEuro);

        getRate = mock(GetRate.class);
        when(getRate.execute(anyString(), anyString())).thenReturn(BigDecimal.valueOf(2));
        targetAccountBalanceCalculator = new TargetAccountBalanceCalculator(accountServiceMock, getRate);

    }

    @Test
    public void transferBetweenTwoAccountsInTheSameCurrencyShouldAddRightAmountOfMoneyToTargetAccount() {
        //GIVEN
        Transfer transfer = Transfer.builder()
                .amount(new BigDecimal(10))
                .sendingAccount(sendingAccount)
                .targetAccount(targetAccount)
                .build();
        BigDecimal balanceAtTargetAccountBeforeTransfer = targetAccount.getBalance();
        BigDecimal expectedBalanceAtTargetAccountAfterTransfer = balanceAtTargetAccountBeforeTransfer.add(BigDecimal.valueOf(10));
        //WHEN
        targetAccountBalanceCalculator.setBalanceOfTargetAccount(transfer);
        BigDecimal result = targetAccount.getBalance();
        //THEN
        Assert.assertThat(result, equalTo(expectedBalanceAtTargetAccountAfterTransfer));
    }

    @Test
    public void transferBetweenTwoAccounts_InTheDifferentCurrency_ShouldAddRightAmountOfMoney_ToTargetAccount() {
        //GIVEN
        BigDecimal amount = BigDecimal.valueOf(10);
        Transfer transfer = Transfer.builder()
                .amount(amount)
                .sendingAccount(sendingAccount)
                .targetAccount(targetAccountInEuro)
                .build();
        String sendingAccountNumber = sendingAccount.getAccountNumber();
        String targetAccountNumber = targetAccountInEuro.getAccountNumber();
        BigDecimal rate = getRate.execute(sendingAccountNumber, targetAccountNumber);

        BigDecimal balanceAtTargetAccountBeforeTransfer = targetAccountInEuro.getBalance();
        BigDecimal expectedBalanceAtTargetAccountAfterTransfer = balanceAtTargetAccountBeforeTransfer.add(amount.multiply(rate));
        //WHEN
        targetAccountBalanceCalculator.setBalanceOfTargetAccount(transfer);
        BigDecimal result = targetAccountInEuro.getBalance();
        //THEN
        Assert.assertThat(result, equalTo(expectedBalanceAtTargetAccountAfterTransfer));
    }
}
