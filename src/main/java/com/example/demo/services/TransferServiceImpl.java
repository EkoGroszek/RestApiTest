package com.example.demo.services;

import com.example.demo.dao.TransferRepository;
import com.example.demo.entities.Account;
import com.example.demo.entities.Transfer;
import com.example.demo.entities.TransferStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransferServiceImpl {

    private TransferRepository transferRepository;

    private AccountServiceImpl accountService;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository, AccountServiceImpl accountService) {
        this.transferRepository = transferRepository;
        this.accountService = accountService;
    }

    @Scheduled(fixedRate = 15000)
    public void completeTransfer() {
        // TODO: 22.07.2019  odfiltrować przelewy "panding" z bazy
//        List<Transfer> transfers = (List<Transfer>) transferRepository.findAll();
        List<Transfer> transfers = (List<Transfer>) transferRepository.findAll();

        // TODO: 22.07.2019  nie uzależniać sie od nazyw enuma (getValue() w enumie)
        for (Transfer transfer : transfers) {
            if (TransferStatus.PENDING.getValue().equals(transfer.getStatus())) {
                setTransferPostingDate(transfer);
                addAmountToTargetAccount(transfer);
                changeTransferStatusToCompleted(transfer);
            }
            // TODO: 22.07.2019 dodać try catch żeby w razie błędu podczas updateu salda reszta kont zostła zupdateowana  
            transferRepository.save(transfer);
        }

    }

    public Transfer changeTransferStatusToCompleted(Transfer transfer) {
        transfer.setStatus(TransferStatus.COMPLETED.getValue());
        return transfer;

    }

    private void setTransferPostingDate(Transfer transfer) {
        transfer.setDateOfPostingTransfer(LocalDateTime.now());
    }

    // TODO: 22.07.2019 test do tego | wyciągnąc do osobnej klasy | stworzyć interfejs (dwie implementacje z api i bez) "targetAccountBalanceCalculator" - fabryka
    private void addAmountToTargetAccount(Transfer transfer) {
        Account sendingAccount = accountService.findByAccountNumber(transfer.getSendingAccountNumber());
        Account targetAccount = accountService.findByAccountNumber(transfer.getTargetAccountNumber());
        BigDecimal amount = transfer.getAmount();

        if (sendingAccount.getCurrency().equals(targetAccount.getCurrency())) {
            targetAccount.setBalance(targetAccount.getBalance().add(amount));
        } else {
            BigDecimal amountAfterCurrencyConversion = amount.multiply(BigDecimal.valueOf(getRate(sendingAccount.getCurrency(), targetAccount.getCurrency())));
            targetAccount.setBalance(targetAccount.getBalance().add(amountAfterCurrencyConversion));
        }
        accountService.save(targetAccount);

    }

    public Transfer createNewTransfer(Transfer transfer) {
        Account sendingAccount = accountService.findByAccountNumber(transfer.getSendingAccountNumber());
        Account targetAccount = accountService.findByAccountNumber(transfer.getTargetAccountNumber());

        BigDecimal amount = transfer.getAmount();
        subtractAmountFromSendingAccount(sendingAccount, amount);
        accountService.save(sendingAccount);
        accountService.save(targetAccount);


        transfer.setDateOfSendingTransfer(LocalDateTime.now());
        transfer.setStatus(TransferStatus.PENDING.getValue());


        return transferRepository.save(transfer);
    }

    private void subtractAmountFromSendingAccount(Account sendingAccount, BigDecimal amount) {
        sendingAccount.setBalance(sendingAccount.getBalance().subtract(amount));
    }

    //// TODO: 22.07.2019  zmienić double na BigDecimal
    //// TODO: 22.07.2019  zabezpieczyć przed Nullpointerem
    private double getRate(String sendedCurrency, String targetCurrency) {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "https://api.exchangeratesapi.io/latest?base=";

        ExchangeRates exchangeRates = restTemplate
                .getForObject(fooResourceUrl + sendedCurrency, ExchangeRates.class);
        return exchangeRates.getRates().get(targetCurrency);
    }

    // TODO: 22.07.2019 błąd w nazwie
    public List<Transfer> getTransfersListForAccountByUserName(String account_number) {
        return transferRepository.findAllBySendingAccountNumber(account_number);
    }

    public List<Transfer> getSendedTransfersListForAccountById(Integer account_id) {
        Account account = accountService.findById(account_id);
        return transferRepository.findAllBySendingAccountNumber(account.getAccountNumber());
    }

    public List<Transfer> getRecievedTransfersListForAccountById(Integer account_id) {
        Account account = accountService.findById(account_id);
        return transferRepository.findAllByTargetAccountNumber(account.getAccountNumber());
    }
}
