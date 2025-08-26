package com.bank.service;

import com.bank.model.Transaction;
import com.bank.model.TransactionStatus;
import com.bank.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
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
}
