package com.bank.service;

import com.bank.model.Transaction;
import com.bank.model.TransactionStatus;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    List<Transaction> findAll();
    List<Transaction> findByStatus(TransactionStatus status);
    void flagTransaction(Long id, String reason);
    void rollbackTransaction(Long id);
    void approveTransaction(Long id);
    void transferMoney(String senderAccountNumber, String receiverAccountNumber, BigDecimal amount);
}
