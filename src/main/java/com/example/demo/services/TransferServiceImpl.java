package com.example.demo.services;

import com.example.demo.dao.TransferRepository;
import com.example.demo.entities.Account;
import com.example.demo.entities.Transfer;
import com.example.demo.entities.TransferStatus;
import com.example.demo.services.factory.TargetAccountBalanceCalculator;
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
        List<Transfer> transfers = transferRepository.findAllByStatus(TransferStatus.PENDING.getValue());

        for (Transfer transfer : transfers) {
            setTransferPostingDate(transfer);
            addAmountToTargetAccount(transfer);
            changeTransferStatusToCompleted(transfer);
            // TODO: 22.07.2019 dodać try catch żeby w razie błędu podczas updateu salda reszta kont zostła zupdateowana (transakcje)
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

    // TODO: 22.07.2019 test do tego | wyciągnąc do osobnej klasy(DONE) | stworzyć interfejs (dwie implementacje z api i bez) "TargetAccountBalanceCalculator" - fabryka
    private void addAmountToTargetAccount(Transfer transfer) {
        TargetAccountBalanceCalculator targetAccountBalanceCalculator = new TargetAccountBalanceCalculator();
        targetAccountBalanceCalculator.setBalanceOfTargetAccount(transfer, accountService);

    }

    // TODO: 23.07.2019 refactor zrobić jakąś metode wyciągnąć ze srodka czy coś bo za duże to bydle
    public Transfer createNewTransfer(Transfer transfer) {
        Account sendingAccount = accountService.findByAccountNumber(transfer.getSendingAccount().getAccountNumber());
        Account targetAccount = accountService.findByAccountNumber(transfer.getTargetAccount().getAccountNumber());

        BigDecimal amount = transfer.getAmount();
        subtractAmountFromSendingAccount(sendingAccount, amount);
        Account updatedSendingAccount = accountService.save(sendingAccount);
        transfer.setSendingAccount(updatedSendingAccount);
        transfer.setTargetAccount(targetAccount);

        transfer.setDateOfSendingTransfer(LocalDateTime.now());
        transfer.setStatus(TransferStatus.PENDING.getValue());

        return transferRepository.save(transfer);
    }

    private void subtractAmountFromSendingAccount(Account sendingAccount, BigDecimal amount) {
        sendingAccount.setBalance(sendingAccount.getBalance().subtract(amount));
    }


    // TODO: 22.07.2019 błąd w nazwie

    public List<Transfer> getSendedTransfersListForAccountById(Integer accountId) {
        return transferRepository.findAllBySendingAccountId(accountId);
    }

    public List<Transfer> getRecievedTransfersListForAccountById(Integer accountId) {
        return transferRepository.findAllByTargetAccountId(accountId);
    }
}
