package com.example.demo.services;

import com.example.demo.dao.TransferRepository;
import com.example.demo.entities.Account;
import com.example.demo.entities.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransferServiceImpl {
    private TransferRepository transferRepository;
//    private AccountRepository accountRepository;
    private AccountServiceImpl accountService;

    private int elops;

    public TransferServiceImpl(){

    }


    public TransferServiceImpl(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    List<Transfer> transfers = new ArrayList<>();

    @Scheduled(fixedRate = 15000)
    public void completeTransfer() {
        transfers = (List<Transfer>) transferRepository.findAll();
        Account sendingAccount = null;
        Account targetAccount = null;
        BigDecimal amount;

        for (Transfer transfer : transfers) {                                                                           //iteruje po tabeli przelewów
            if (transfer.getStatus().equals(String.valueOf(Transfer.status.PENDING))) {
                setTransferPostingDate(transfer);
                addAmountToTargetAccount(transfer);                                        //dodaje kwote przelewu do konta docelowego
                changeTransferStatusToCompleted(transfer);
            }
            transferRepository.save(transfer);                                                                          //zapis obiektu "transfer" do bazy danych
        }

    }

    public Transfer changeTransferStatusToCompleted(Transfer transfer) {
        transfer.setStatus(String.valueOf(Transfer.status.COMPLETED));
        return transfer;

    }

    private void setTransferPostingDate(Transfer transfer) {
        LocalDateTime dateOfPostingTransfer = LocalDateTime.now();                                              //tworzę date zaksięgowania przelewu
        transfer.setDateOfPostingTransfer(dateOfPostingTransfer);                                               //zapisuje date do obiektu "Transfer"
    }

    private void addAmountToTargetAccount(Transfer transfer) {
        Account sendingAccount = accountService.findByAccountNumber(transfer.getSendingAccountNumber());                //konto  z którego wysyłamy przelew
        Account targetAccount = accountService.findByAccountNumber(transfer.getTargetAccountNumber());               //konto  na które wysyłamy przelew
        BigDecimal amount = transfer.getAmount();

        if (sendingAccount.getCurrency().equals(targetAccount.getCurrency())) {                                 //
            targetAccount.setBalance(targetAccount.getBalance().add(amount));
        } else {
            BigDecimal amountAfterCurrencyConversion = amount.multiply(BigDecimal.valueOf(getRate(sendingAccount.getCurrency(), targetAccount.getCurrency())));
            targetAccount.setBalance(targetAccount.getBalance().add(amountAfterCurrencyConversion));
        }

        accountService.save(targetAccount);

    }

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository1, AccountServiceImpl accountService) {
        this.transferRepository = transferRepository1;
        this.accountService = accountService;
    }

    public Transfer createNewTransfer(Transfer transfer) {
        Account sendingAccount = accountService.findByAccountNumber(transfer.getSendingAccountNumber());
        Account targetAccount = accountService.findByAccountNumber(transfer.getTargetAccountNumber());
        BigDecimal amount = transfer.getAmount();
        substractAmountFromSendingAccount(sendingAccount, targetAccount, amount);
        accountService.save(sendingAccount);
        accountService.save(targetAccount);

        LocalDateTime dateOfSendingTransfer = LocalDateTime.now();

        transfer.setDateOfSendingTransfer(dateOfSendingTransfer);
        transfer.setStatus(String.valueOf(Transfer.status.PENDING));


        return transferRepository.save(transfer);
    }

    private void substractAmountFromSendingAccount(Account sendingAccount, Account targetAccount, BigDecimal amount) {
        if (sendingAccount.getCurrency().equals(targetAccount.getCurrency())) {
            sendingAccount.setBalance(sendingAccount.getBalance().subtract(amount));
        } else {
            sendingAccount.setBalance(sendingAccount.getBalance().subtract(amount));
        }
    }

    public List<Transfer> getTransfersListForAccount(String account_number) {
        return transferRepository.findAllBySendingAccountNumber(account_number);
    }

    public double getRate(String sendedCurrency, String targetCurrency) {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "https://api.exchangeratesapi.io/latest?base=";

        ExchangeRates exchangeRates = restTemplate
                .getForObject(fooResourceUrl + sendedCurrency, ExchangeRates.class);
        return exchangeRates.getRates().get(targetCurrency);
    }
}
