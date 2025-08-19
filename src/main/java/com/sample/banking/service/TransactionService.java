package com.sample.banking.service;

import com.sample.banking.entity.Transaction;
import com.sample.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction>getTransactions(Long accountId){
        return transactionRepository.findByAccountId(accountId);
    }
}
