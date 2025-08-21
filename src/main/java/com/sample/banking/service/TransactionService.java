package com.sample.banking.service;

import com.sample.banking.entity.Account;
import com.sample.banking.entity.Transaction;
import com.sample.banking.entity.User;
import com.sample.banking.repository.TransactionRepository;
import com.sample.banking.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public List<Transaction>getTransactions(Long accountId){
        return transactionRepository.findByAccountId(accountId);
    }

  public void deleteTransactionsById(Long accountId){
        transactionRepository.deleteAccountById(accountId);
  }

  public Map<String,List<Transaction>>getTransactionsByUserId(Long userId){
        User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("User Doesn't exists "));
        List<Long>AccountIds=user.getAccounts().stream().map(Account::getId).toList();

        Map<String,List<Transaction>>transactionsMap = new HashMap<>();
        for (Account account:user.getAccounts()){
            List<Transaction>transactions=new ArrayList<>();
            transactions.addAll(transactionRepository.findByAccountId(account.getId()));
            transactionsMap.put(account.getAccountHolderName(), transactions);
        }
        return  transactionsMap;
  }

  public Map<Long,List<Transaction>>getTransactionsByUserAndAccountIds(Long UserId,Long id){
        User user = userRepository.findById(UserId).orElseThrow(() -> new RuntimeException("User Doesn't exists"));
        Account account=user.getAccounts().stream().filter(acc ->acc.getId().equals(id)).findFirst().orElseThrow(() -> new RuntimeException("Accoount Doesn't exists"));

        List<Transaction>transactions=transactionRepository.findByAccountId(id);

        Map<Long,List<Transaction>> result=new HashMap<>();

        result.put(id, transactions);

        return result;
  }
}
