package com.sample.banking.controller;

import com.sample.banking.dto.AccountDTO;
import com.sample.banking.dto.UserDTO;
import com.sample.banking.entity.Account;
import com.sample.banking.entity.Transaction;
import com.sample.banking.service.AccountService;
import com.sample.banking.service.TransactionService;
import com.sample.banking.service.UserService;
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
    private final UserService userService;

    public AccountController(AccountService accountService, TransactionService transactionService, UserService userService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.userService = userService;
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

    @DeleteMapping("/transaction/{id}")
    public ResponseEntity<String> deleteTransactions(@PathVariable Long id){
        transactionService.deleteTransactionsById(id);
        return  ResponseEntity.ok("Transactions Deleted Successfully.");
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

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> users=userService.getAllusers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/user")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO){
        return new ResponseEntity<>(userService.createUser(userDTO),HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDTO>getUserById(@PathVariable Long userId){
        UserDTO userDTO=userService.getUserById(userId);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/user/{userId}/transaction")
    public ResponseEntity<Map<String,List<Transaction>>> transactionsByUserId(@PathVariable Long userId){
        Map<String,List<Transaction>> result =transactionService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}/transaction/{id}")
    public  ResponseEntity<Map<Long,List<Transaction>>> transactionsByUserIdAndAccouunt(@PathVariable Long userId,@PathVariable Long id){
        Map<Long,List<Transaction>> result=transactionService.getTransactionsByUserAndAccountIds(userId,id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String>deleteuser(@PathVariable Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok("User Deleted Successfully.");
    }

    @PutMapping("/{fromAccountId}/transfer/{toAccountId}")
    public ResponseEntity<Map<String,AccountDTO>> transfer(@PathVariable  Long fromAccountId,@PathVariable Long toAccountId,@RequestBody Map<String, Double> request){

        double amount=request.get("amount");
        Map<String,AccountDTO> result=accountService.transferAmount(fromAccountId,toAccountId,amount);
        return ResponseEntity.ok(result);
    }
}
