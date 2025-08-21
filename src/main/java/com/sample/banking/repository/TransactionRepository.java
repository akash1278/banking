package com.sample.banking.repository;


import com.sample.banking.entity.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {



    List<Transaction> findByAccountId(Long accountId);

    @Transactional
    void deleteAccountById(Long accountId);


}
