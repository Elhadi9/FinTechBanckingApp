package com.bank.service;

import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.TransactionStatus;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;


    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> findByStatus(TransactionStatus status) {
        return transactionRepository.findByStatus(status);
    }

    @Override
    public void flagTransaction(Long id, String reason) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        transaction.setStatus(TransactionStatus.FLAGGED);
        transaction.setDescription(reason);
        transactionRepository.save(transaction);
    }

    @Override
    public void rollbackTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        transaction.setStatus(TransactionStatus.ROLLED_BACK);
        transactionRepository.save(transaction);
    }

    @Override
    public void approveTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        transaction.setStatus(TransactionStatus.APPROVED);
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void transferMoney(String senderAccountNumber, String receiverAccountNumber, BigDecimal amount) {
        // fetch sender and receiver by accountNumber
        Account sender = accountRepository.findByAccountNumber(senderAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));

        Account receiver = accountRepository.findByAccountNumber(receiverAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds in sender account");
        }

        // update balances
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        // save transaction
        Transaction transaction = new Transaction();
        transaction.setSenderAccount(sender);
        transaction.setReceiverAccount(receiver);
        transaction.setAmount(amount);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setDescription("Transfer from " + sender.getAccountNumber() +
                " to " + receiver.getAccountNumber());

        transactionRepository.save(transaction);
    }

}
