package com.sample.banking.controller;

import com.sample.banking.dto.AccountDTO;
import com.sample.banking.entity.Account;

public class AccountMapper {
    public static Account addToAccount(AccountDTO accountDTO){

        return new Account(accountDTO.getId(), accountDTO.getAccountHolderName(), accountDTO.getBalance());
    }

    public static AccountDTO addToAccountDto(Account account){
        return new AccountDTO(account.getId(),account.getAccountHolderName(),account.getBalance());
    }
}
