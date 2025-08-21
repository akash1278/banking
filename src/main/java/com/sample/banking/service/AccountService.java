package com.sample.banking.service;

import com.sample.banking.dto.AccountDTO;
import com.sample.banking.entity.Account;
import com.sample.banking.entity.Transaction;


import java.util.List;
import java.util.Map;


public interface AccountService {

    AccountDTO createAccount(AccountDTO accountDTO);

    AccountDTO getAccountById(Long id);

    AccountDTO deposit(Long id, double amount);

    AccountDTO withdraw(Long id, double amount);

    double getBalance(Long id);

    List<AccountDTO> getAllAccounts();

    void deleteAccount(Long id);

    Map<String, List<Account>> getAllAccountsByName();



}
