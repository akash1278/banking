package com.sample.banking.service.impl;

import com.sample.banking.controller.AccountMapper;
import com.sample.banking.dto.AccountDTO;
import com.sample.banking.entity.Account;
import com.sample.banking.entity.Transaction;
import com.sample.banking.repository.AccountRepository;
import com.sample.banking.repository.TransactionRepository;
import com.sample.banking.service.AccountService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

   private final AccountRepository accountRepository;
   private final TransactionRepository transactionRepository;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }



    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {

       Account account = AccountMapper.addToAccount(accountDTO);
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

    @Override
    public Map<String, List<Account>> multipleAccounts() {
        List<Account>accounts=accountRepository.findAll();
        Map<String,List<Account>> accountMap=new HashMap<>();
        for(Account account:accounts){
            accountMap.computeIfAbsent(account.getAccountHolderName(), k ->new ArrayList<>()).add(account);
        }

        Map<String,List<Account>> multipleAccount =new HashMap<>();
        for(Map.Entry<String,List<Account>> entry:accountMap.entrySet()){
            if(entry.getValue().size() >1){
                multipleAccount.put(entry.getKey(), entry.getValue());
            }
        }
        return multipleAccount;
    }

    @Override
    public Map<String, List<Account>> singleAccounts() {
        List<Account>accounts = accountRepository.findAll();
        Map<String,List<Account>> accountMap=new HashMap<>();
        for(Account account:accounts){
            accountMap.computeIfAbsent(account.getAccountHolderName(), k ->new ArrayList<>()).add(account);
        }

        Map<String,List<Account>> singleAccount=new HashMap<>();
        for(Map.Entry<String,List<Account>> entry:accountMap.entrySet()){
            if(entry.getValue().size()<=1){
                singleAccount.put(entry.getKey(), entry.getValue());
            }
        }
        return singleAccount;
    }

    @Override
    public Map<String, List<Transaction>> getTransactionsForMultipleAccountsCombined() {
        List<Account>accounts = accountRepository.findAll();
        Map<String,List<Account>> accouontMap = new HashMap<>();
        for(Account account : accounts ){
            accouontMap.computeIfAbsent(account.getAccountHolderName(), k -> new ArrayList<>()).add(account);
        }
        Map<String,List<Transaction>> result = new HashMap<>();
        for(Map.Entry<String,List<Account>> entry: accouontMap.entrySet()){
            if(entry.getValue().size()>1){
                List<Transaction> transactions = new ArrayList<>();
                for(Account account: entry.getValue()){
                    transactions.addAll(transactionRepository.findByAccountId(account.getId()));
                }
                result.put(entry.getKey(), transactions);
            }
        }
        return  result;
    }

    @Override
    public Map<String, Map<Long, List<Transaction>>> getTransactionsForMultipleAccountsSeperately() {
        List<Account> accounts = accountRepository.findAll();
        Map<String,List<Account>> accountMap = new HashMap<>();
        for (Account account : accounts){
            accountMap.computeIfAbsent(account.getAccountHolderName(), k -> new ArrayList<>()).add(account);
        }
        Map<String, Map<Long, List<Transaction>>> result = new HashMap<>();
        for(Map.Entry<String,List<Account>> entry : accountMap.entrySet()){
            if(entry.getValue().size() > 1){
                Map<Long,List<Transaction>> accountTransaction = new HashMap<>();
                for(Account account: entry.getValue()){
                    List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());
                    accountTransaction.put(account.getId(),transactions);
                }
                result.put(entry.getKey(), accountTransaction);
            }

        }
        return  result;
    }

    @Override
    public List<Transaction> getTransactionsByNameCombined(String name) {
        List<Account> accounts =accountRepository.findAll()
                .stream()
                .filter(acc -> acc.getAccountHolderName().replaceAll("\\s+","").equalsIgnoreCase(name))
                .toList();
        if(accounts.size()<=1){
            throw new RuntimeException("No multiple accounts found for this name: " + name);
        }
        List<Transaction> transactions = new ArrayList<>();
        for(Account account:accounts){
            transactions.addAll(transactionRepository.findByAccountId(account.getId()));
        }
        return transactions;
    }

    @Override
    public Map<String, List<Transaction>> getTransactionsByNameSeperately(String name) {
        List<Account> accounts = accountRepository.findAll()
                .stream()
                .filter( acc -> acc.getAccountHolderName().replaceAll("\\s+","").equalsIgnoreCase(name))
                .toList();
        if(accounts.size() <= 1){
            throw  new RuntimeException("No multiple accounts found for this name: "+ name);
        }
        Map<String,List<Transaction>> result = new HashMap<>();
        for(Account account : accounts){
            List<Transaction> transactions = new ArrayList<>();
            transactions.addAll(transactionRepository.findByAccountId(account.getId()));
            result.put(account.getAccountHolderName(),transactions);
        }
        return  result;
    }


}
