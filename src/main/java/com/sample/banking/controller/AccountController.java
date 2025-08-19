package com.sample.banking.controller;

import com.sample.banking.dto.AccountDTO;
import com.sample.banking.entity.Account;
import com.sample.banking.entity.Transaction;
import com.sample.banking.service.AccountService;
import com.sample.banking.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bank/accounts")
public class AccountController {
    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<AccountDTO> AddAccount(@RequestBody AccountDTO accountDTO){
        return new ResponseEntity<>(accountService.createAccount(accountDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id){

        AccountDTO accountDTO = accountService.getAccountById(id);
        return ResponseEntity.ok(accountDTO);
    }

    @PutMapping("/{id}/deposit")
    public ResponseEntity<AccountDTO> deposit(@PathVariable Long id,@RequestBody Map<String , Double> request){

        double amount=request.get("amount");
        AccountDTO accountDTO = accountService.deposit(id,amount);
        return  ResponseEntity.ok(accountDTO);
    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<AccountDTO> withdraw(@PathVariable Long id, @RequestBody Map<String, Double> request){

        double amount=request.get("amount");
        AccountDTO accountDTO=accountService.withdraw(id,amount);
        return  ResponseEntity.ok(accountDTO);
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts(){
        List<AccountDTO> accounts= accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}/getbalance")
    public double getBalance(@PathVariable Long id){
        return accountService.getBalance(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){

        accountService.deleteAccount(id);
        return   ResponseEntity.ok("Account Deleted Successfully.");
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Transaction>>getTransactions(@PathVariable Long id){
        List<Transaction>transaction = transactionService.getTransactions(id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/accountsByName")
    public Map<String,List<Account>>getAllAccountsByName(){
        return accountService.getAllAccountsByName();
    }
    @GetMapping("/multipleaccounts")
    public Map<String,List<Account>>multipleAccounts(){
        return accountService.multipleAccounts();
    }

    @GetMapping("/singleaccount")
    public Map<String,List<Account>>singleAccounts(){
        return  accountService.singleAccounts();
    }

    @GetMapping("/multipleaccounts/transactions_Combined")
    public Map<String,List<Transaction>> getTransactionsForMutlipleAccountsCombined(){
        return  accountService.getTransactionsForMultipleAccountsCombined();
    }

    @GetMapping("/multipleaccounts/transactions_seperated")
    public Map<String,Map<Long,List<Transaction>>> getTransactionsForMutlipleAccountsSeperately(){
        return  accountService.getTransactionsForMultipleAccountsSeperately();
    }

    @GetMapping("/multipleaccounts/{name}/transactionscombined")
    public ResponseEntity< List<Transaction>> getTransactionsByNamecombined(@PathVariable String name){
        return ResponseEntity.ok( accountService.getTransactionsByNameCombined(name));
    }

    @GetMapping("/multipleaccounts/{name}/transactionsseperately")
    public ResponseEntity<Map<String,List<Transaction>>> getTransactionsByNameSeperately(@PathVariable String name){
        return  ResponseEntity.ok( accountService.getTransactionsByNameSeperately(name));
    }
}
