package com.sample.banking.service.impl;

import com.sample.banking.controller.AccountMapper;
import com.sample.banking.dto.AccountDTO;
import com.sample.banking.entity.Account;
import com.sample.banking.entity.Transaction;
import com.sample.banking.entity.User;
import com.sample.banking.repository.AccountRepository;
import com.sample.banking.repository.TransactionRepository;
import com.sample.banking.repository.UserRepository;
import com.sample.banking.service.AccountService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService  {

   private final AccountRepository accountRepository;
   private final TransactionRepository transactionRepository;
   private final UserRepository userRepository;


    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }


    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {

        User user = userRepository.findById(accountDTO.getUserId()).orElseThrow(()->new RuntimeException("user Not Found"));

        Account account = AccountMapper.addToAccount(accountDTO,user);
        Account savedAccount=accountRepository.save(account);

        return AccountMapper.addToAccountDto(savedAccount);
    }

    @Override
    public AccountDTO getAccountById(Long id) {

        Account account = accountRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException("Account Doesn't Exists."));
        return AccountMapper.addToAccountDto(account);
    }

    @Override
    public AccountDTO deposit(Long id, double amount) {

        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account Doesn't Exists."));
        if(amount<=0){
            throw new RuntimeException("Amount you want to deposit must be greater than 0");
        }else{
            double total= account.getBalance() + amount;
            account.setBalance(total);

            Transaction transaction;
            transaction = new Transaction();
            transaction.setType("Deposit");
            transaction.setAmount(amount);
            transaction.setLocalDateTime(LocalDateTime.now());
            transaction.setAccount(account);
            transaction.setAccountHolderName(account.getAccountHolderName());
            transactionRepository.save(transaction);

            Account savedAccount=accountRepository.save(account);
            return  AccountMapper.addToAccountDto(savedAccount);

        }

    }

    @Override
    public AccountDTO withdraw(Long id, double amount) {

        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account doesn't exist"));
        if(amount > account.getBalance()){
            throw new RuntimeException("The Requested Amount is greater than your Account Balance . Balance: "+account.getBalance());
        }else{
            double total= account.getBalance() - amount;
            account.setBalance(total);
            Account savedAccount=accountRepository.save(account);

            Transaction transaction = new Transaction();
            transaction.setType("Withdraw");
            transaction.setAmount(amount);
            transaction.setLocalDateTime(LocalDateTime.now());
            transaction.setAccount(account);
            transaction.setAccountHolderName(account.getAccountHolderName());
            transactionRepository.save(transaction);

            return AccountMapper.addToAccountDto(savedAccount);
        }

    }


    @Override
    public double getBalance(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(()->new RuntimeException("Account doesn't exists"));
        return account.getBalance();
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts =  accountRepository.findAll();
        return  accounts.stream().map(AccountMapper::addToAccountDto).collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(Long id) {

        Account account=accountRepository.findById(id).orElseThrow(()->new RuntimeException("Account doesn't exists."));
        accountRepository.deleteById(id);
    }

    @Override
    public Map<String, List<Account>> getAllAccountsByName() {
        List<Account>accounts=accountRepository.findAll();

        Map<String, List<Account>> accountMap= new HashMap<>();
        for(Account account:accounts){
            accountMap.computeIfAbsent(account.getAccountHolderName(), k -> new ArrayList<>()).add(account);

        }
        return accountMap;
    }


}
