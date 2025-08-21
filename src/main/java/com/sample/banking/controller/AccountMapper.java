package com.sample.banking.controller;

import com.sample.banking.dto.AccountDTO;
import com.sample.banking.entity.Account;
import com.sample.banking.entity.User;


public class AccountMapper {

    public static Account addToAccount(AccountDTO accountDTO,User user){

        Account account = new Account();
        account.setId(accountDTO.getId());
        account.setAccountHolderName(accountDTO.getAccountHolderName());
        account.setBalance(accountDTO.getBalance());
        account.setUser(user);
        return account;
    }

    public static AccountDTO addToAccountDto(Account account){
        return new AccountDTO(account.getId(),account.getAccountHolderName(),account.getBalance(),
                account.getUser()!= null ? account.getUser().getUserId() : null);
    }
}
